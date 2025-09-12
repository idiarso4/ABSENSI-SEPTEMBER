// Designation management JavaScript functions
document.addEventListener('DOMContentLoaded', function() {
    window.designationManager = new DesignationManager();
});

// Designation Manager Class
class DesignationManager {
    constructor() {
        this.designations = [];
        this.departments = [];
        this.currentPage = 0;
        this.pageSize = 10;
        this.totalPages = 0;
        this.searchTerm = '';

        this.init();
    }

    init() {
        this.loadDesignations();
        this.populateDepartmentSelect();
        this.bindEvents();
    }

    bindEvents() {
        // Search functionality
        document.getElementById('searchBtn').addEventListener('click', () => {
            this.searchTerm = document.getElementById('searchInput').value.trim();
            this.loadDesignations();
        });

        document.getElementById('searchInput').addEventListener('keyup', (e) => {
            if (e.key === 'Enter') {
                document.getElementById('searchBtn').click();
            }
        });

        // Save designation
        document.getElementById('saveDesignationBtn').addEventListener('click', () => {
            this.saveDesignation();
        });

        // Action buttons for edit, view, delete (using event delegation)
        document.addEventListener('click', (e) => {
            const target = e.target.closest('.edit-btn, .view-btn, .delete-btn');
            if (!target) return;

            const designationId = parseInt(target.dataset.id);
            if (target.classList.contains('edit-btn')) {
                this.editDesignation(designationId);
            } else if (target.classList.contains('view-btn')) {
                this.viewDesignation(designationId);
            } else if (target.classList.contains('delete-btn')) {
                this.deleteDesignation(designationId);
            }
        });

        // Pagination events will be added dynamically
        document.addEventListener('click', (e) => {
            const paginationLink = e.target.closest('.page-link[data-page]');
            if (paginationLink) {
                e.preventDefault();
                const page = parseInt(paginationLink.dataset.page);
                this.goToPage(page);
            }
        });
    }

    async populateDepartmentSelect() {
        try {
            const response = await fetch('/api/v1/departments', {
                headers: {
                    'Authorization': 'Bearer ' + this.getToken()
                }
            });

            const select = document.getElementById('departmentSelect');
            if (!select) return;

            select.innerHTML = '<option value="">Pilih Departemen</option>';

            if (response.ok) {
                const departments = await response.json();
                const deptList = Array.isArray(departments) ? departments : (Array.isArray(departments?.content) ? departments.content : []);

                deptList.forEach(dept => {
                    const option = document.createElement('option');
                    option.value = dept.id;
                    option.textContent = dept.name;
                    select.appendChild(option);
                });
            } else {
                console.warn('Failed to load departments for designation form');
                select.innerHTML += '<option value="">Departemen tidak dapat dimuat</option>';
            }
        } catch (error) {
            console.error('Error loading departments:', error);
            const select = document.getElementById('departmentSelect');
            if (select) {
                select.innerHTML += '<option value="">Error memuat departemen</option>';
            }
        }
    }

    async loadDesignations() {
        try {
            const response = await fetch('/api/v1/designations', {
                headers: {
                    'Authorization': 'Bearer ' + this.getToken()
                }
            });

            if (response.ok) {
                const data = await response.json();
                this.designations = Array.isArray(data) ? data : (Array.isArray(data?.content) ? data.content : []);
                this.totalPages = 1;
                this.renderDesignationsTable();
                this.updatePagination();
                this.updateStatistics();
            } else if (response.status === 401) {
                window.location.href = '/login';
            } else {
                const errText = await response.text().catch(()=> '');
                this.showAlert(`Gagal memuat data jabatan${errText?': '+errText:''}`, 'danger');
            }
        } catch (error) {
            console.error('Error loading designations:', error);
            this.showAlert('Gagal memuat data jabatan', 'danger');
        }
    }

    renderDesignationsTable() {
        const tbody = document.getElementById('designationTableBody');
        if (!tbody) return;

        const list = Array.isArray(this.designations) ? this.designations : [];
        if (list.length === 0) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="5" class="text-center">
                        ${this.searchTerm ? 'Tidak ada jabatan yang sesuai dengan pencarian.' : 'Belum ada jabatan yang dibuat.'}
                    </td>
                </tr>
            `;
            return;
        }

        const rows = list.map(designation => `
            <tr>
                <td>${designation.id}</td>
                <td>${designation.name || 'N/A'}</td>
                <td>${designation.description || '-'}</td>
                <td>-</td>
                <td>-</td>
                <td><span class="badge bg-primary">Aktif</span></td>
                <td>
                    <button class="btn btn-sm btn-outline-primary me-1 edit-btn" data-id="${designation.id}" title="Edit">
                                    <i class="bi bi-pencil"></i>
                                </button>
                                <button class="btn btn-sm btn-outline-info me-1 view-btn" data-id="${designation.id}" title="Detail">
                                    <i class="bi bi-eye"></i>
                                </button>
                                <button class="btn btn-sm btn-outline-danger delete-btn" data-id="${designation.id}" title="Hapus">
                                    <i class="bi bi-trash"></i>
                                </button>
                </td>
            </tr>
        `).join('');

        tbody.innerHTML = rows;
    }

    updatePagination() {
        const pagination = document.getElementById('designationPagination');
        pagination.innerHTML = '';

        if (this.totalPages <= 1) return;

        // Previous button
        const prevBtn = document.createElement('li');
        prevBtn.className = `page-item ${this.currentPage === 0 ? 'disabled' : ''}`;
        prevBtn.innerHTML = `<a class="page-link" href="#" data-page="${this.currentPage - 1}">Sebelumnya</a>`;
        pagination.appendChild(prevBtn);

        // Page numbers
        const startPage = Math.max(0, this.currentPage - 2);
        const endPage = Math.min(this.totalPages - 1, this.currentPage + 2);

        for (let i = startPage; i <= endPage; i++) {
            const pageBtn = document.createElement('li');
            pageBtn.className = `page-item ${i === this.currentPage ? 'active' : ''}`;
            pageBtn.innerHTML = `<a class="page-link" href="#" data-page="${i}">${i + 1}</a>`;
            pagination.appendChild(pageBtn);
        }

        // Next button
        const nextBtn = document.createElement('li');
        nextBtn.className = `page-item ${this.currentPage >= this.totalPages - 1 ? 'disabled' : ''}`;
        nextBtn.innerHTML = `<a class="page-link" href="#" data-page="${this.currentPage + 1}">Selanjutnya</a>`;
        pagination.appendChild(nextBtn);
    }

    updateStatistics() {
        const total = Array.isArray(this.designations) ? this.designations.length : 0;
        const totalEl = document.getElementById('totalDesignations');
        if (totalEl) totalEl.textContent = total;

        // Active not tracked; assume all active
        const activeCount = total;
        const activeEl = document.getElementById('activeDesignations');
        if (activeEl) activeEl.textContent = activeCount;
        const avgEl = document.getElementById('avgSalary');
        if (avgEl) avgEl.textContent = '-';
    }

    async saveDesignation() {
        const form = {
            name: document.getElementById('designationName').value.trim(),
            description: document.getElementById('designationDescription').value.trim()
        };

        if (!form.name) {
            this.showAlert('Nama jabatan wajib diisi', 'warning');
            return;
        }

        // No department linkage in backend model

        try {
            const designationId = document.getElementById('designationId').value;
            const isUpdate = designationId !== '';

            const url = isUpdate ? `/api/v1/designations/${designationId}` : '/api/v1/designations';
            const method = isUpdate ? 'PUT' : 'POST';

            const response = await fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + this.getToken()
                },
                body: JSON.stringify(form)
            });

            if (response.ok) {
                this.showAlert(`Jabatan ${isUpdate ? 'diperbarui' : 'ditambahkan'} dengan berhasil`, 'success');
                document.getElementById('designationModal').querySelector('[data-bs-dismiss="modal"]').click();
                this.resetForm();
                this.loadDesignations();

                // Dispatch global event for cross-page synchronization
                window.dispatchEvent(new CustomEvent('designationsUpdated'));
            } else if (response.status === 401) {
                window.location.href = '/login';
            } else {
                let msg = 'Gagal menyimpan jabatan';
                try { const err = await response.json(); msg = err.message || err.error || msg; } catch(_) { try { msg = await response.text(); } catch(__) {} }
                this.showAlert(msg, 'danger');
            }
        } catch (error) {
            console.error('Error saving designation:', error);
            this.showAlert('Terjadi kesalahan saat menyimpan jabatan', 'danger');
        }
    }

    editDesignation(id) {
        const designation = this.designations.find(d => d.id === id);
        if (!designation) return;

        document.getElementById('designationId').value = designation.id;
        document.getElementById('designationName').value = designation.name || '';
        document.getElementById('designationDescription').value = designation.description || '';
        const bs = document.getElementById('basicSalary'); if (bs) bs.value = '';
        const al = document.getElementById('allowance'); if (al) al.value = '';
        const ds = document.getElementById('departmentSelect'); if (ds) ds.value = '';

        document.getElementById('designationModalLabel').textContent = 'Edit Jabatan';
        document.getElementById('saveDesignationBtn').textContent = 'Perbarui Jabatan';

        const modal = new bootstrap.Modal(document.getElementById('designationModal'));
        modal.show();
    }

    viewDesignation(id) {
        const designation = this.designations.find(d => d.id === id);
        if (!designation) return;

        document.getElementById('detailsName').textContent = designation.name || 'N/A';
        document.getElementById('detailsDepartment').textContent = '-';
        document.getElementById('detailsSalary').textContent = '-';
        document.getElementById('detailsAllowance').textContent = '-';
        document.getElementById('detailsTotalSalary').textContent = '-';
        document.getElementById('detailsEmployeeCount').textContent = designation.employees ? designation.employees.length : '0';
        document.getElementById('detailsStatus').textContent = 'Aktif';
        document.getElementById('detailsStatus').className = `badge bg-success`;

        document.getElementById('detailsDescription').textContent = designation.description || 'Tidak ada deskripsi';

        const employeeList = document.getElementById('employeeList');
        employeeList.innerHTML = '';
        const emps = designation.employees || [];
        if (emps.length === 0) {
            employeeList.innerHTML = '<li class="list-group-item text-center text-muted">Belum ada karyawan</li>';
        } else {
            emps.forEach(e => {
                const li = document.createElement('li');
                li.className = 'list-group-item';
                li.textContent = `${e.firstName || ''} ${e.lastName || ''}`.trim();
                employeeList.appendChild(li);
            });
        }

        const modal = new bootstrap.Modal(document.getElementById('designationDetailsModal'));
        modal.show();
    }

    async deleteDesignation(id) {
        if (!confirm('Hapus jabatan ini?')) return;
        try {
            const res = await fetch(`/api/v1/designations/${id}`, { method: 'DELETE', headers: { 'Authorization': 'Bearer ' + this.getToken() } });
            if (res.ok) { this.showAlert('Jabatan dihapus', 'success'); this.loadDesignations(); }
            else this.showAlert('Gagal menghapus jabatan', 'danger');
        } catch (e) { this.showAlert('Error menghapus jabatan', 'danger'); }
    }

    goToPage(page) {
        if (page >= 0 && page < this.totalPages) {
            this.currentPage = page;
            this.loadDesignations();
        }
    }

    resetForm() {
        document.getElementById('designationForm').reset();
        document.getElementById('designationId').value = '';
        document.getElementById('designationModalLabel').textContent = 'Tambah Jabatan';
        document.getElementById('saveDesignationBtn').textContent = 'Simpan Jabatan';
        // Reset department selection to first option
        const deptSelect = document.getElementById('departmentSelect');
        if (deptSelect) deptSelect.value = '';
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

    getToken() {
        return localStorage.getItem('token') || '';
    }
}
