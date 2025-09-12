// Task management JavaScript functions
document.addEventListener('DOMContentLoaded', function() {
    const taskManager = new TaskManager();
});

// Task Manager Class
class TaskManager {
    constructor() {
        this.tasks = [];
        this.employees = [];
        this.currentPage = 0;
        this.pageSize = 10;
        this.totalPages = 0;
        this.statusFilter = '';
        this.priorityFilter = '';
        this.dueDateFilter = '';

        this.init();
    }

    init() {
        this.loadEmployees();
        this.loadTasks();
        this.bindEvents();
    }

    bindEvents() {
        // Save task
        document.getElementById('saveTaskBtn').addEventListener('click', () => {
            this.saveTask();
        });

        // Filter events
        document.getElementById('filterBtn').addEventListener('click', () => {
            this.statusFilter = document.getElementById('statusFilter').value;
            this.priorityFilter = document.getElementById('priorityFilter').value;
            this.dueDateFilter = document.getElementById('dueDateFilter').value;
            this.loadTasks();
        });

        // Clear filters
        document.getElementById('clearFiltersBtn').addEventListener('click', () => {
            this.clearFilters();
        });

        // Update progress in details modal
        document.getElementById('updateProgressBtn').addEventListener('click', () => {
            this.updateTaskProgress();
        });

        // Status change auto-progress update
        document.getElementById('taskStatus').addEventListener('change', () => {
            this.autoUpdateProgress();
        });

        // Progress change completion date update
        document.getElementById('progress').addEventListener('change', () => {
            this.autoUpdateCompletionDate();
        });

        // Enter key for filters
        const filters = ['statusFilter', 'priorityFilter'];
        filters.forEach(id => {
            document.getElementById(id).addEventListener('change', () => {
                this.applyFilters();
            });
        });

        document.getElementById('dueDateFilter').addEventListener('change', () => {
            this.applyFilters();
        });
    }

    applyFilters() {
        this.statusFilter = document.getElementById('statusFilter').value;
        this.priorityFilter = document.getElementById('priorityFilter').value;
        this.dueDateFilter = document.getElementById('dueDateFilter').value;
        this.loadTasks();
    }

    clearFilters() {
        document.getElementById('statusFilter').value = '';
        document.getElementById('priorityFilter').value = '';
        document.getElementById('dueDateFilter').value = '';
        this.statusFilter = '';
        this.priorityFilter = '';
        this.dueDateFilter = '';
        this.loadTasks();
    }

    async loadEmployees() {
        try {
            const response = await fetch('/api/employees', {
                headers: {
                    'Authorization': 'Bearer ' + this.getToken()
                }
            });

            if (response.ok) {
                this.employees = await response.json();
                this.populateEmployeeSelect();
            } else if (response.status === 401) {
                window.location.href = '/login';
            }
        } catch (error) {
            console.error('Error loading employees:', error);
        }
    }

    populateEmployeeSelect() {
        const select = document.getElementById('assignedTo');
        if (!select) return;

        select.innerHTML = '<option value="">Pilih Karyawan</option>';

        this.employees.forEach(employee => {
            const option = document.createElement('option');
            option.value = employee.id;
            option.textContent = `${employee.firstName || ''} ${employee.lastName || ''}`.trim();
            select.appendChild(option);
        });
    }

    async loadTasks() {
        try {
            let url = '/api/tasks';
            const params = new URLSearchParams();

            if (this.statusFilter) params.append('status', this.statusFilter);
            if (this.priorityFilter) params.append('priority', this.priorityFilter);
            if (this.dueDateFilter) params.append('dueDate', this.dueDateFilter);

            if (params.toString()) {
                url += '?' + params.toString();
            }

            const response = await fetch(url, {
                headers: {
                    'Authorization': 'Bearer ' + this.getToken()
                }
            });

            if (response.ok) {
                const data = await response.json();
                this.tasks = data.content || data; // Handle both paginated and non-paginated responses
                this.renderTasksTable();
                this.updateStatistics();
            } else if (response.status === 401) {
                window.location.href = '/login';
            } else {
                throw new Error('Failed to load tasks');
            }
        } catch (error) {
            console.error('Error loading tasks:', error);
            this.showAlert('Gagal memuat data tugas', 'danger');
        }
    }

    renderTasksTable() {
        const tbody = document.getElementById('tasksTableBody');

        if (!this.tasks || this.tasks.length === 0) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="8" class="text-center">
                        ${this.statusFilter || this.priorityFilter || this.dueDateFilter ?
                          'Tidak ada tugas yang sesuai dengan filter.' :
                          'Belum ada tugas yang dibuat.'}
                    </td>
                </tr>
            `;
            return;
        }

        const rows = this.tasks.map(task => {
            const employeeName = this.getEmployeeName(task.assignedTo);
            const progressPercent = task.progress || 0;
            const isOverdue = task.dueDate && new Date(task.dueDate) < new Date() && task.status !== 'COMPLETED';
            const daysRemaining = task.dueDate ? this.getDaysRemaining(task.dueDate) : null;

            return `
                <tr>
                    <td>${task.id}</td>
                    <td>${task.title || 'N/A'}</td>
                    <td>${employeeName}</td>
                    <td>
                        <span class="badge ${
                            task.priority === 'URGENT' ? 'bg-danger' :
                            task.priority === 'HIGH' ? 'bg-warning text-dark' :
                            task.priority === 'MEDIUM' ? 'bg-info' : 'bg-secondary'
                        }">
                            ${this.formatPriority(task.priority)}
                        </span>
                    </td>
                    <td>
                        <span class="badge ${
                            task.status === 'COMPLETED' ? 'bg-success' :
                            task.status === 'IN_PROGRESS' ? 'bg-primary' :
                            isOverdue ? 'bg-danger' : 'bg-secondary'
                        }">
                            ${this.formatStatus(task.status)}${isOverdue ? ' (Terlambat)' : ''}
                        </span>
                    </td>
                    <td class="${daysRemaining !== null && daysRemaining < 0 && task.status !== 'COMPLETED' ? 'text-danger' : ''}">
                        ${task.dueDate ? this.formatDate(task.dueDate) : '-'}
                        ${daysRemaining !== null && daysRemaining > 0 && task.status !== 'COMPLETED' ?
                          ` (${daysRemaining} hari)` : ''}
                    </td>
                    <td>
                        <div class="progress" style="width: 100px;">
                            <div class="progress-bar" role="progressbar"
                                 style="width: ${progressPercent}%; background-color: ${
                                     progressPercent === 100 ? '#28a745' :
                                     progressPercent >= 75 ? '#17a2b8' :
                                     progressPercent >= 50 ? '#ffc107' : '#dc3545'
                                 };" aria-valuenow="${progressPercent}" aria-valuemin="0" aria-valuemax="100">
                                ${progressPercent}%
                            </div>
                        </div>
                    </td>
                    <td>
                        <button class="btn btn-sm btn-outline-primary me-1" onclick="taskManager.editTask(${task.id})" title="Edit">
                            <i class="bi bi-pencil"></i>
                        </button>
                        <button class="btn btn-sm btn-outline-info me-1" onclick="taskManager.viewTask(${task.id})" title="Detail">
                            <i class="bi bi-eye"></i>
                        </button>
                        <button class="btn btn-sm btn-outline-danger" onclick="taskManager.deleteTask(${task.id})" title="Hapus">
                            <i class="bi bi-trash"></i>
                        </button>
                    </td>
                </tr>
            `;
        }).join('');

        tbody.innerHTML = rows;
    }

    updateStatistics() {
        const totalTasks = this.tasks.length;
        const inProgressTasks = this.tasks.filter(task => task.status === 'IN_PROGRESS').length;
        const completedTasks = this.tasks.filter(task => task.status === 'COMPLETED').length;
        const overdueTasks = this.tasks.filter(task => {
            return task.dueDate && new Date(task.dueDate) < new Date() && task.status !== 'COMPLETED';
        }).length;

        document.getElementById('totalTasks').textContent = totalTasks;
        document.getElementById('inProgressTasks').textContent = inProgressTasks;
        document.getElementById('completedTasks').textContent = completedTasks;
        document.getElementById('overdueTasks').textContent = overdueTasks;
    }

    async saveTask() {
        const formData = {
            title: document.getElementById('taskName').value.trim(),
            description: document.getElementById('taskDescription').value.trim(),
            priority: document.getElementById('taskPriority').value,
            status: document.getElementById('taskStatus').value,
            assignedTo: document.getElementById('assignedTo').value,
            startDate: document.getElementById('startDate').value,
            dueDate: document.getElementById('dueDate').value,
            progress: parseInt(document.getElementById('progress').value) || 0,
            completionDate: document.getElementById('completionDate').value
        };

        if (!formData.title) {
            this.showAlert('Nama tugas wajib diisi', 'warning');
            return;
        }

        if (!formData.assignedTo) {
            this.showAlert('Penanggung jawab wajib dipilih', 'warning');
            return;
        }

        try {
            const taskId = document.getElementById('taskId').value;
            const isUpdate = taskId !== '';

            const url = isUpdate ? `/api/tasks/${taskId}` : '/api/tasks';
            const method = isUpdate ? 'PUT' : 'POST';

            const response = await fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + this.getToken()
                },
                body: JSON.stringify(formData)
            });

            if (response.ok) {
                this.showAlert(`Tugas ${isUpdate ? 'diperbarui' : 'ditambahkan'} dengan berhasil`, 'success');
                document.getElementById('taskModal').querySelector('[data-bs-dismiss="modal"]').click();
                this.resetForm();
                this.loadTasks();
            } else if (response.status === 401) {
                window.location.href = '/login';
            } else {
                const error = await response.text();
                this.showAlert(`Gagal menyimpan tugas: ${error}`, 'danger');
            }
        } catch (error) {
            console.error('Error saving task:', error);
            this.showAlert('Terjadi kesalahan saat menyimpan tugas', 'danger');
        }
    }

    editTask(id) {
        const task = this.tasks.find(t => t.id === id);
        if (!task) return;

        document.getElementById('taskId').value = task.id;
        document.getElementById('taskName').value = task.title || '';
        document.getElementById('taskDescription').value = task.description || '';
        document.getElementById('taskPriority').value = task.priority || 'MEDIUM';
        document.getElementById('taskStatus').value = task.status || 'TODO';
        document.getElementById('assignedTo').value = task.assignedTo?.id || task.assignedTo || '';
        document.getElementById('startDate').value = task.startDate ? task.startDate.split('T')[0] : '';
        document.getElementById('dueDate').value = task.dueDate ? task.dueDate.split('T')[0] : '';
        document.getElementById('progress').value = task.progress || 0;

        // Set completion date if task is completed
        const completionDateInput = document.getElementById('completionDate');
        if (task.status === 'COMPLETED' || task.progress === 100) {
            // If task is completed but no completion date, set it to today
            // If task has a completion date from backend, use that
            if (task.completionDate) {
                completionDateInput.value = task.completionDate.split('T')[0];
            } else {
                // Fallback: if task is completed but no completion date, use today's date
                completionDateInput.value = this.formatDateForInput(new Date());
            }
        } else {
            // Clear completion date for incomplete tasks
            completionDateInput.value = '';
        }

        document.getElementById('taskModalLabel').textContent = 'Edit Tugas';
        document.getElementById('saveTaskBtn').textContent = 'Perbarui Tugas';

        const modal = new bootstrap.Modal(document.getElementById('taskModal'));
        modal.show();
    }

    viewTask(id) {
        const task = this.tasks.find(t => t.id === id);
        if (!task) return;

        const employeeName = this.getEmployeeName(task.assignedTo);
        const progressPercent = task.progress || 0;

        document.getElementById('detailsTitle').textContent = task.title || 'N/A';
        document.getElementById('detailsAssignee').textContent = employeeName;
        document.getElementById('detailsPriority').textContent = this.formatPriority(task.priority);
        document.getElementById('detailsStatus').textContent = this.formatStatus(task.status);
        document.getElementById('detailsStartDate').textContent = task.startDate ? this.formatDate(task.startDate) : '-';
        document.getElementById('detailsDueDate').textContent = task.dueDate ? this.formatDate(task.dueDate) : '-';

        // Display completion date if task is completed
        if (task.completionDate) {
            // Add completion date to the details
            document.getElementById('detailsProgress').textContent = `${progressPercent}%`;
            // You might want to add a new element in the HTML for completion date
            // For now, we'll append it to progress
            if (task.status === 'COMPLETED') {
                document.getElementById('detailsProgress').textContent += ` (Diselesaikan: ${this.formatDate(task.completionDate)})`;
            }
        } else {
            document.getElementById('detailsProgress').textContent = `${progressPercent}%`;
        }

        document.getElementById('detailsDescription').textContent = task.description || 'Tidak ada deskripsi';
        document.getElementById('updateProgress').value = progressPercent;

        // Update progress bar
        const progressBar = document.getElementById('progressBar');
        progressBar.style.width = `${progressPercent}%`;
        progressBar.textContent = `${progressPercent}%`;

        // Time remaining
        const timeRemaining = document.getElementById('timeRemaining');
        if (task.dueDate) {
            const days = this.getDaysRemaining(task.dueDate);
            if (days < 0) {
                timeRemaining.textContent = `Terlambat ${Math.abs(days)} hari`;
                timeRemaining.className = 'text-danger';
            } else {
                timeRemaining.textContent = `${days} hari tersisa`;
                timeRemaining.className = days < 3 ? 'text-warning' : 'text-success';
            }
        } else {
            timeRemaining.textContent = '-';
        }

        const modal = new bootstrap.Modal(document.getElementById('taskDetailsModal'));
        modal.show();
    }

    async deleteTask(id) {
        if (!confirm('Apakah Anda yakin ingin menghapus tugas ini?')) {
            return;
        }

        try {
            const response = await fetch(`/api/tasks/${id}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': 'Bearer ' + this.getToken()
                }
            });

            if (response.ok) {
                this.showAlert('Tugas berhasil dihapus', 'success');
                this.loadTasks();
            } else if (response.status === 401) {
                window.location.href = '/login';
            } else {
                this.showAlert('Gagal menghapus tugas', 'danger');
            }
        } catch (error) {
            console.error('Error deleting task:', error);
            this.showAlert('Terjadi kesalahan saat menghapus tugas', 'danger');
        }
    }

    async updateTaskProgress() {
        const progress = parseInt(document.getElementById('updateProgress').value) || 0;
        const taskId = document.getElementById('taskId').value;

        if (!taskId) return;

        try {
            const response = await fetch(`/api/tasks/${taskId}/progress`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + this.getToken()
                },
                body: JSON.stringify({ progress })
            });

            if (response.ok) {
                this.showAlert('Progress berhasil diperbarui', 'success');
                this.loadTasks();
                document.getElementById('taskDetailsModal').querySelector('[data-bs-dismiss="modal"]').click();
            } else {
                this.showAlert('Gagal memperbarui progress', 'danger');
            }
        } catch (error) {
            console.error('Error updating progress:', error);
            this.showAlert('Terjadi kesalahan saat memperbarui progress', 'danger');
        }
    }

    autoUpdateProgress() {
        const status = document.getElementById('taskStatus').value;
        const progressInput = document.getElementById('progress');
        const startDateInput = document.getElementById('startDate');
        const completionDateInput = document.getElementById('completionDate');

        if (status === 'COMPLETED' && progressInput.value !== '100') {
            progressInput.value = '100';
            // Set completion date to today when task is completed
            if (!completionDateInput.value) {
                completionDateInput.value = this.formatDateForInput(new Date());
            }
        } else if (status === 'TODO' && progressInput.value !== '0') {
            progressInput.value = '0';
        }

        // Auto-complete when progress reaches 100%
        if (parseInt(progressInput.value) === 100 && status !== 'COMPLETED') {
            document.getElementById('taskStatus').value = 'COMPLETED';
            if (!completionDateInput.value) {
                completionDateInput.value = this.formatDateForInput(new Date());
            }
        }
    }

    resetForm() {
        document.getElementById('taskForm').reset();
        document.getElementById('taskId').value = '';
        document.getElementById('taskModalLabel').textContent = 'Tambah Tugas';
        document.getElementById('saveTaskBtn').textContent = 'Simpan Tugas';
        document.getElementById('progress').value = '0';
    }

    showAlert(message, type = 'info') {
        // Remove existing alerts
        const existingAlerts = document.querySelectorAll('.alert');
        existingAlerts.forEach(alert => alert.remove());

        const alertDiv = document.createElement('div');
        alertDiv.className = `alert alert-${type} alert-dismissible fade show position-fixed`;
        alertDiv.style.top = '20px';
        alertDiv.style.right = '20px';
        alertDiv.style.zIndex = '9999';
        alertDiv.innerHTML = `
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        `;

        document.body.appendChild(alertDiv);

        setTimeout(() => {
            if (alertDiv.parentNode) {
                alertDiv.parentNode.removeChild(alertDiv);
            }
        }, 5000);
    }

    getEmployeeName(employeeIdOrObject) {
        if (!employeeIdOrObject) return 'N/A';

        if (typeof employeeIdOrObject === 'object' && employeeIdOrObject.firstName) {
            return `${employeeIdOrObject.firstName} ${employeeIdOrObject.lastName || ''}`.trim();
        }

        const employeeId = typeof employeeIdOrObject === 'object' ? employeeIdOrObject.id : employeeIdOrObject;
        const employee = this.employees.find(e => e.id == employeeId);

        if (employee) {
            return `${employee.firstName} ${employee.lastName || ''}`.trim();
        }

        return 'N/A';
    }

    formatPriority(priority) {
        const priorityMap = {
            'LOW': 'Rendah',
            'MEDIUM': 'Sedang',
            'HIGH': 'Tinggi',
            'URGENT': 'Mendesak'
        };
        return priorityMap[priority] || priority || 'Sedang';
    }

    formatStatus(status) {
        const statusMap = {
            'TODO': 'Belum Dimulai',
            'IN_PROGRESS': 'Sedang Berjalan',
            'COMPLETED': 'Selesai',
            'OVERDUE': 'Terlambat'
        };
        return statusMap[status] || status || 'Belum Dimulai';
    }

    formatDate(dateStr) {
        if (!dateStr) return '-';
        try {
            return new Date(dateStr).toLocaleDateString('id-ID');
        } catch (e) {
            return dateStr;
        }
    }

    getDaysRemaining(dueDate) {
        const now = new Date();
        const due = new Date(dueDate);
        const diffTime = due.getTime() - now.getTime();
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
        return diffDays;
    }

    autoUpdateCompletionDate() {
        const progress = parseInt(document.getElementById('progress').value) || 0;
        const status = document.getElementById('taskStatus').value;
        const completionDateInput = document.getElementById('completionDate');

        // Set completion date when task is completed or progress reaches 100%
        if ((status === 'COMPLETED' || progress === 100) && !completionDateInput.value) {
            completionDateInput.value = this.formatDateForInput(new Date());
        }

        // Clear completion date if task status is not completed and progress < 100%
        if (status !== 'COMPLETED' && progress < 100 && completionDateInput.value) {
            completionDateInput.value = '';
        }
    }

    formatDateForInput(date) {
        // Format date for HTML input type="date" (yyyy-MM-dd)
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
    }

    getToken() {
        return localStorage.getItem('token') || '';
    }
}