// Employee management JavaScript functions

class EmployeeManager {
    constructor() {
        this.employees = [];
        this.departments = [];
        this.designations = [];
        this.selectedEmployees = new Set();
        this.currentFilter = 'active';
        this.currentSearch = '';
        // Track whether we're editing an existing employee (stores numeric DB id)
        this.currentEditId = null;
        this.init();
    }

    init() {
        this.loadDepartments();
        this.loadDesignations();
        this.loadEmployees();
        this.bindEvents();
    }

    bindEvents() {
        // Add employee form submission
        document.getElementById('saveEmployeeBtn').addEventListener('click', (e) => {
            e.preventDefault();
            this.saveEmployee();
        });

        // Logout button
        document.getElementById('logoutBtn').addEventListener('click', (e) => {
            e.preventDefault();
            this.logout();
        });

        // Filter events
        document.getElementById('statusFilter').addEventListener('change', (e) => {
            this.currentFilter = e.target.value;
            this.loadEmployees();
        });

        // Search events
        document.getElementById('searchInput').addEventListener('input', (e) => {
            this.currentSearch = e.target.value.toLowerCase();
            this.filterEmployees();
        });

        // Refresh button
        document.getElementById('refreshBtn').addEventListener('click', () => {
            this.loadEmployees();
        });

        // Bulk delete
        document.getElementById('bulkDeleteBtn').addEventListener('click', () => {
            this.bulkDeleteSelected();
        });

        // Export button
        document.getElementById('exportExcelBtn').addEventListener('click', () => {
            this.exportEmployees();
        });

        // Import form
        document.getElementById('importForm').addEventListener('submit', (e) => {
            e.preventDefault();
            this.importEmployees();
        });

        // Download template
        document.getElementById('downloadTemplateBtn').addEventListener('click', () => {
            this.downloadTemplate();
        });

        // When the Add Employee modal is opened via the "Tambah Karyawan" button,
        // ensure we're in create mode and form is reset properly
        const addModalEl = document.getElementById('addEmployeeModal');
        if (addModalEl) {
            addModalEl.addEventListener('show.bs.modal', () => {
                // If not explicitly editing, default to create mode
                if (this.currentEditId === null) {
                    this.resetFormForCreate();
                }
            });
            addModalEl.addEventListener('hidden.bs.modal', () => {
                // Clear edit state when modal is closed
                this.currentEditId = null;
                this.resetFormForCreate();
            });
        }
    }

    async loadDepartments() {
        try {
            const response = await fetch('/api/v1/departments', {
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('token')
                }
            });
            if (response.ok) {
                const data = await response.json();
                // Normalize to array
                this.departments = Array.isArray(data) ? data : (Array.isArray(data?.content) ? data.content : []);
                this.populateDepartments();
            } else if (response.status === 401) {
                window.location.href = '/login';
            } else {
                throw new Error(`HTTP ${response.status}: ${response.statusText}`);
            }
        } catch (error) {
            console.error('Error loading departments:', error);
            this.showAlert('Error loading departments', 'danger');
        }
    }

    async loadDesignations() {
        try {
            const response = await fetch('/api/v1/designations', {
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('token')
                }
            });
            if (response.ok) {
                const data = await response.json();
                this.designations = Array.isArray(data) ? data : (Array.isArray(data?.content) ? data.content : []);
                this.populateDesignations();
            } else if (response.status === 401) {
                window.location.href = '/login';
            } else {
                throw new Error(`HTTP ${response.status}: ${response.statusText}`);
            }
        } catch (error) {
            console.error('Error loading designations:', error);
            this.showAlert('Error loading designations', 'danger');
        }
    }

    populateDepartments() {
        const departmentSelect = document.getElementById('department');
        if (!departmentSelect) return;
        departmentSelect.innerHTML = '<option value="">Select Department</option>';
        
        (Array.isArray(this.departments) ? this.departments : []).forEach(dept => {
            const option = document.createElement('option');
            option.value = dept.id;
            option.textContent = dept.name;
            departmentSelect.appendChild(option);
        });
    }

    populateDesignations() {
        const designationSelect = document.getElementById('designation');
        if (!designationSelect) return;
        designationSelect.innerHTML = '<option value="">Select Designation</option>';
        
        (Array.isArray(this.designations) ? this.designations : []).forEach(designation => {
            const option = document.createElement('option');
            option.value = designation.id;
            option.textContent = designation.name;
            designationSelect.appendChild(option);
        });
    }

    async loadEmployees() {
        try {
            const url = `/api/v1/users${this.currentFilter !== 'active' ? `?status=${this.currentFilter}` : ''}`;
            const response = await fetch(url, {
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('token')
                }
            });
            if (response.ok) {
                const data = await response.json();
                this.employees = Array.isArray(data) ? data : (Array.isArray(data?.content) ? data.content : []);
                this.renderEmployeeTable();
                this.updateStats();
            } else if (response.status === 401) {
                window.location.href = '/login';
            } else {
                throw new Error(`HTTP ${response.status}: ${response.statusText}`);
            }
        } catch (error) {
            console.error('Error loading employees:', error);
            this.showAlert('Error loading employees: ' + error.message, 'danger');
        }
    }

    renderEmployeeTable() {
        const tableBody = document.getElementById('employeeTableBody');
        if (!tableBody) return;
        tableBody.innerHTML = '';

        (Array.isArray(this.employees) ? this.employees : []).forEach(employee => {
            const fullAddress = [employee.address, employee.city, employee.state, employee.zip]
                .filter(part => part && part.trim() !== '')
                .join(', ');

            // Determine active status
            const isActive = employee.isActive !== false; // Default to true if null
            const activeClass = isActive ? 'badge bg-success' : 'badge bg-danger';
            const activeText = isActive ? 'Aktif' : 'Tidak Aktif';

            // Create action buttons based on status
            let actionButtons = `
                <button class="btn btn-sm btn-outline-info me-1 view-employee" data-id="${employee.id}" title="View Details">
                    <i class="bi bi-eye"></i>
                </button>
                <button class="btn btn-sm btn-outline-primary me-1 edit-employee" data-id="${employee.id}" title="Edit">
                    <i class="bi bi-pencil"></i>
                </button>
                <button class="btn btn-sm btn-outline-secondary me-1 print-employee" data-id="${employee.id}" title="Print Profile">
                    <i class="bi bi-printer"></i>
                </button>`;

            if (isActive) {
                actionButtons += `
                    <button class="btn btn-sm btn-outline-warning me-1 deactivate-employee" data-id="${employee.id}" title="Nonaktifkan Karyawan">
                        <i class="bi bi-person-dash"></i>
                    </button>`;
            } else {
                actionButtons += `
                    <button class="btn btn-sm btn-outline-success me-1 reactivate-employee" data-id="${employee.id}" title="Aktifkan Kembali">
                        <i class="bi bi-person-plus"></i>
                    </button>`;
            }

            const employeeTypeText = employee.employeeType || 'PERMANENT';
            const employeeTypeLabel = {
                'PERMANENT': 'Tetap',
                'CONTRACT': 'Kontrak',
                'DAILY_CASUAL': 'Harian Lepas'
            }[employeeTypeText] || employeeTypeText;

            const row = document.createElement('tr');
            row.innerHTML = `
                <td><input type="checkbox" class="employee-checkbox" data-id="${employee.id}"></td>
                <td>${employee.employeeId || 'N/A'}</td>
                <td>${employee.firstName || ''} ${employee.lastName || ''}</td>
                <td>${employee.designation ? employee.designation.name : 'N/A'}</td>
                <td>${employee.department ? employee.department.name : 'N/A'}</td>
                <td>${fullAddress || 'N/A'}</td>
                <td>${employee.email || 'N/A'}</td>
                <td>${employee.phone || 'N/A'}</td>
                <td>${employeeTypeLabel}</td>
                <td><span class="badge ${activeClass}">${activeText}</span></td>
                <td>
                    <div class="btn-group" role="group">
                        ${actionButtons}
                    </div>
                </td>
            `;
            tableBody.appendChild(row);
        });

        // Bind all action events
        document.querySelectorAll('.view-employee').forEach(btn => {
            btn.addEventListener('click', (e) => {
                const employeeId = e.target.closest('button').dataset.id;
                this.viewEmployee(employeeId);
            });
        });

        document.querySelectorAll('.edit-employee').forEach(btn => {
            btn.addEventListener('click', (e) => {
                const employeeId = e.target.closest('button').dataset.id;
                this.editEmployee(employeeId);
            });
        });

        document.querySelectorAll('.print-employee').forEach(btn => {
            btn.addEventListener('click', (e) => {
                const employeeId = e.target.closest('button').dataset.id;
                this.printEmployee(employeeId);
            });
        });

        document.querySelectorAll('.delete-employee').forEach(btn => {
            btn.addEventListener('click', (e) => {
                const employeeId = e.target.closest('button').dataset.id;
                this.deleteEmployee(employeeId);
            });
        });

        // Bind checkbox events
        document.querySelectorAll('.employee-checkbox').forEach(checkbox => {
            checkbox.addEventListener('change', (e) => {
                this.toggleEmployeeSelection(parseInt(e.target.dataset.id));
            });
        });

        // Bind select all checkbox
        const selectAll = document.getElementById('selectAll');
        if (selectAll) {
            selectAll.addEventListener('change', () => {
                this.toggleSelectAll();
            });
        }

        // Bind new event handlers for deactivate and reactivate
        document.querySelectorAll('.deactivate-employee').forEach(btn => {
            btn.addEventListener('click', (e) => {
                const employeeId = e.target.closest('button').dataset.id;
                this.deactivateEmployee(employeeId);
            });
        });

        document.querySelectorAll('.reactivate-employee').forEach(btn => {
            btn.addEventListener('click', (e) => {
                const employeeId = e.target.closest('button').dataset.id;
                this.reactivateEmployee(employeeId);
            });
        });
    }

    viewEmployee(employeeId) {
        // Find the employee
        const employee = this.employees.find(emp => emp.id == employeeId);
        if (!employee) {
            this.showAlert('Employee not found', 'danger');
            return;
        }

        // Create view modal content
        const fullAddress = [employee.address, employee.city, employee.state, employee.zip]
            .filter(part => part && part.trim() !== '')
            .join(', ');

        const viewModal = `
            <div class="modal fade" id="viewEmployeeModal" tabindex="-1">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Detail Karyawan</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <div class="row">
                                <div class="col-md-4 text-center">
                                    ${employee.photo ? `<img src="${employee.photo}" class="img-fluid rounded mb-3" style="max-width: 150px;" alt="Foto Karyawan">` : '<div class="bg-light rounded p-4 mb-3"><i class="bi bi-person-fill fs-1 text-secondary"></i></div>'}
                                </div>
                                <div class="col-md-8">
                                    <h4>${employee.firstName || ''} ${employee.lastName || ''}</h4>
                                    <p class="text-muted mb-3">ID: ${employee.employeeId || 'N/A'}</p>
                                    <hr>
                                    <div class="row">
                                        <div class="col-sm-6"><strong>Jabatan:</strong> ${employee.designation ? employee.designation.name : 'N/A'}</div>
                                        <div class="col-sm-6"><strong>Departemen:</strong> ${employee.department ? employee.department.name : 'N/A'}</div>
                                        <div class="col-sm-6"><strong>Email:</strong> ${employee.email || 'N/A'}</div>
                                        <div class="col-sm-6"><strong>Telepon:</strong> ${employee.phone || 'N/A'}</div>
                                        <div class="col-sm-6"><strong>Tanggal Bergabung:</strong> ${employee.joiningDate ? new Date(employee.joiningDate).toLocaleDateString('id-ID') : 'N/A'}</div>
                                        <div class="col-sm-6"><strong>Gaji Pokok:</strong> ${employee.basicSalary ? 'Rp ' + employee.basicSalary.toLocaleString('id-ID') : 'N/A'}</div>
                                    </div>
                                    <hr>
                                    <h6>Alamat Lengkap:</h6>
                                    <p>${fullAddress || 'N/A'}</p>
                                    ${employee.emergencyContact ? `<p><strong>Kontak Darurat:</strong> ${employee.emergencyContact}</p>` : ''}
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Tutup</button>
                            <button type="button" class="btn btn-primary" onclick="window.employeeManager.printEmployee(${employee.id})">
                                <i class="bi bi-printer"></i> Print Profile
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        `;

        // Remove existing modal if any
        const existingModal = document.getElementById('viewEmployeeModal');
        if (existingModal) {
            existingModal.remove();
        }

        // Add modal to body and show
        document.body.insertAdjacentHTML('beforeend', viewModal);
        const modal = new bootstrap.Modal(document.getElementById('viewEmployeeModal'));
        modal.show();

        // Clean up modal after hiding
        document.getElementById('viewEmployeeModal').addEventListener('hidden.bs.modal', () => {
            document.getElementById('viewEmployeeModal').remove();
        });
    }

    printEmployee(employeeId) {
        const employee = this.employees.find(emp => emp.id == employeeId);
        if (!employee) {
            this.showAlert('Employee not found', 'danger');
            return;
        }

        // Open print window
        const printWindow = window.open('', '_blank', 'width=800,height=600');
        const fullAddress = [employee.address, employee.city, employee.state, employee.zip]
            .filter(part => part && part.trim() !== '')
            .join(', ');

        printWindow.document.write(`
            <html>
            <head>
                <title>Profile Karyawan - ${employee.firstName} ${employee.lastName}</title>
                <style>
                    body { font-family: Arial, sans-serif; margin: 20px; }
                    .header { text-align: center; border-bottom: 2px solid #333; padding-bottom: 20px; margin-bottom: 20px; }
                    .info { margin-bottom: 15px; }
                    .label { font-weight: bold; display: inline-block; width: 150px; }
                    table { width: 100%; border-collapse: collapse; margin-top: 20px; }
                    th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
                    th { background-color: #f2f2f2; }
                    @media print { body { margin: 0; } }
                </style>
            </head>
            <body>
                <div class="header">
                    <h1>PT. COCOK GAN</h1>
                    <h2>Profile Karyawan</h2>
                </div>

                <div class="info">
                    <div class="label">ID Karyawan:</div> ${employee.employeeId || 'N/A'}<br>
                    <div class="label">Nama:</div> ${employee.firstName || ''} ${employee.lastName || ''}<br>
                    <div class="label">Jabatan:</div> ${employee.designation ? employee.designation.name : 'N/A'}<br>
                    <div class="label">Departemen:</div> ${employee.department ? employee.department.name : 'N/A'}<br>
                    <div class="label">Email:</div> ${employee.email || 'N/A'}<br>
                    <div class="label">Telepon:</div> ${employee.phone || 'N/A'}<br>
                    <div class="label">Tanggal Bergabung:</div> ${employee.joiningDate ? new Date(employee.joiningDate).toLocaleDateString('id-ID') : 'N/A'}<br>
                    <div class="label">Gaji Pokok:</div> ${employee.basicSalary ? 'Rp ' + employee.basicSalary.toLocaleString('id-ID') : 'N/A'}<br>
                    <div class="label">Alamat:</div> ${fullAddress || 'N/A'}<br>
                    ${employee.emergencyContact ? '<div class="label">Kontak Darurat:</div> ' + employee.emergencyContact : ''}
                </div>

                <table>
                    <thead>
                        <tr>
                            <th>Field</th>
                            <th>Value</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr><td>Foto Profil</td><td>${employee.photo ? 'Ada' : 'Tidak ada'}</td></tr>
                        <tr><td>Gaji Pokok</td><td>${employee.basicSalary ? 'Rp ' + employee.basicSalary.toLocaleString('id-ID') : 'N/A'}</td></tr>
                        <tr><td>Tipe Gaji</td><td>${employee.salaryType || 'fixed'}</td></tr>
                        <tr><td>Batas Cuti</td><td>${employee.yearlyLeaveLimit || 12} hari</td></tr>
                    </tbody>
                </table>

                <div style="margin-top: 30px; font-size: 12px; color: #666;">
                    Dicetak pada: ${new Date().toLocaleString('id-ID')}
                </div>
            </body>
            </html>
        `);

        printWindow.document.close();
        printWindow.focus();
        setTimeout(() => {
            printWindow.print();
        }, 250);
    }

    getEmployeeFormData() {
        const data = {
            firstName: document.getElementById('firstName').value,
            lastName: document.getElementById('lastName').value,
            email: document.getElementById('email').value,
            phone: document.getElementById('phone').value,
            department: {
                id: document.getElementById('department').value
            },
            designation: {
                id: document.getElementById('designation').value
            },
            joiningDate: document.getElementById('joiningDate').value,
            basicSalary: parseFloat(document.getElementById('basicSalary').value) || 0.0,
            address: document.getElementById('address')?.value || '',
            city: document.getElementById('city')?.value || '',
            state: document.getElementById('state')?.value || '',
            zip: document.getElementById('zip')?.value || '',
            country: document.getElementById('country')?.value || ''
        };

        // Only include employeeId when editing (enabled) so create relies on backend auto-generation
        if (!document.getElementById('employeeId').disabled) {
            const employeeId = document.getElementById('employeeId').value;
            if (employeeId && employeeId.trim() !== '') {
                data.employeeId = employeeId;
            }
        }

        // Add employee type
        const employeeTypeValue = document.getElementById('employeeType').value;
        if (employeeTypeValue && employeeTypeValue.trim() !== '') {
            data.employeeType = employeeTypeValue;
        }

        // Add end date
        const endDateValue = document.getElementById('endDate').value;
        if (endDateValue && endDateValue.trim() !== '') {
            data.endDate = endDateValue;
        }

        return data;
    }

    validateEmployeeForm(data) {
        const errors = {};

        if (!data.firstName || data.firstName.trim() === '') {
            errors.firstName = 'First name is required';
        }

        if (!data.lastName || data.lastName.trim() === '') {
            errors.lastName = 'Last name is required';
        }

        if (!data.email || data.email.trim() === '') {
            errors.email = 'Email is required';
        } else if (!Validation.validateEmail(data.email)) {
            errors.email = 'Please enter a valid email';
        }

        // Employee ID required only when updating existing employee
        if (this.currentEditId !== null) {
            if (!data.employeeId || data.employeeId.trim() === '') {
                errors.employeeId = 'Employee ID is required for updates';
            }
        }

        if (!data.basicSalary || data.basicSalary <= 0) {
            errors.basicSalary = 'Basic salary is required and must be greater than 0';
        }

        if (!data.joiningDate || String(data.joiningDate).trim() === '') {
            errors.joiningDate = 'Joining date is required';
        }

        if (!data.department || !data.department.id) {
            errors.department = 'Department is required';
        }

        if (!data.designation || !data.designation.id) {
            errors.designation = 'Designation is required';
        }

        return errors;
    }

    async saveEmployee() {
        const formData = this.getEmployeeFormData();
        const errors = this.validateEmployeeForm(formData);

        if (Object.keys(errors).length > 0) {
            Validation.displayErrors(errors);
            return;
        }

        try {
            // Choose method and URL based on whether we're editing or creating
            const isEdit = this.currentEditId !== null;
            const url = isEdit ? `/api/v1/users/${this.currentEditId}` : '/api/v1/users';
            const method = isEdit ? 'PUT' : 'POST';

            const response = await fetch(url, {
                method,
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + localStorage.getItem('token')
                },
                body: JSON.stringify(formData)
            });

            if (response.ok) {
                this.showAlert(isEdit ? 'Employee updated successfully!' : 'Employee created successfully!', 'success');
                // Close the modal
                const addEmployeeModal = bootstrap.Modal.getInstance(document.getElementById('addEmployeeModal'));
                addEmployeeModal.hide();
                // Reset form
                document.getElementById('addEmployeeForm').reset();
                document.getElementById('basicSalary').value = '';
                document.getElementById('employeeId').value = '';
                // Clear address fields
                if (document.getElementById('address')) document.getElementById('address').value = '';
                if (document.getElementById('city')) document.getElementById('city').value = '';
                if (document.getElementById('state')) document.getElementById('state').value = '';
                if (document.getElementById('zip')) document.getElementById('zip').value = '';
                if (document.getElementById('country')) document.getElementById('country').value = '';

                // Clear employee type and end date
                if (document.getElementById('employeeType')) document.getElementById('employeeType').value = 'PERMANENT';
                if (document.getElementById('endDate')) document.getElementById('endDate').value = '';
                // Disable employeeId for new employees
                const employeeIdField = document.getElementById('employeeId');
                employeeIdField.value = '';
                employeeIdField.disabled = true;
                // Clear edit state
                this.currentEditId = null;
                // Reload employees
                this.loadEmployees();
            } else {
                const errorData = await response.json();
                throw new Error(errorData.error || errorData.message || 'Failed to save employee');
            }
        } catch (error) {
            console.error('Error saving employee:', error);
            this.showAlert('Error saving employee: ' + error.message, 'danger');
        }
    }

    editEmployee(employeeId) {
        // Find the employee
        const employee = this.employees.find(emp => emp.id == employeeId);
        if (!employee) {
            this.showAlert('Employee not found', 'danger');
            return;
        }

        // Set edit state id for PUT endpoint
        this.currentEditId = employee.id;

        // Populate the form with employee data
        document.getElementById('employeeId').value = employee.employeeId || '';
        document.getElementById('firstName').value = employee.firstName || '';
        document.getElementById('lastName').value = employee.lastName || '';
        document.getElementById('email').value = employee.email || '';
        document.getElementById('phone').value = employee.phone || '';
        document.getElementById('department').value = employee.department ? employee.department.id : '';
        document.getElementById('designation').value = employee.designation ? employee.designation.id : '';
        // Normalize date value for input[type="date"]
        if (employee.joiningDate) {
            try {
                const d = new Date(employee.joiningDate);
                document.getElementById('joiningDate').value = isNaN(d.getTime()) ? '' : d.toISOString().slice(0, 10);
            } catch (_) {
                document.getElementById('joiningDate').value = '';
            }
        } else {
            document.getElementById('joiningDate').value = '';
        }
        document.getElementById('basicSalary').value = employee.basicSalary || '';
        // Populate address fields
        if (document.getElementById('address')) document.getElementById('address').value = employee.address || '';
        if (document.getElementById('city')) document.getElementById('city').value = employee.city || '';
        if (document.getElementById('state')) document.getElementById('state').value = employee.state || '';
        if (document.getElementById('zip')) document.getElementById('zip').value = employee.zip || '';
        if (document.getElementById('country')) document.getElementById('country').value = employee.country || '';

        // Populate employee type
        document.getElementById('employeeType').value = employee.employeeType || 'PERMANENT';

        // Populate end date
        if (employee.endDate) {
            try {
                const d = new Date(employee.endDate);
                document.getElementById('endDate').value = isNaN(d.getTime()) ? '' : d.toISOString().slice(0, 10);
            } catch (_) {
                document.getElementById('endDate').value = '';
            }
        } else {
            document.getElementById('endDate').value = '';
        }

        document.getElementById('employeeId').disabled = false;

        // Change modal title and button text
        document.getElementById('addEmployeeModalLabel').textContent = 'Edit Employee';
        document.getElementById('saveEmployeeBtn').textContent = 'Update Employee';

        // Show the modal
        const addEmployeeModal = new bootstrap.Modal(document.getElementById('addEmployeeModal'));
        addEmployeeModal.show();
    }

    resetFormForCreate() {
        // Reset form fields and switch to create mode UI
        const form = document.getElementById('addEmployeeForm');
        if (form) form.reset();

        const employeeIdField = document.getElementById('employeeId');
        if (employeeIdField) {
            employeeIdField.disabled = true; // backend will auto-generate
            employeeIdField.value = '';
        }

        const titleEl = document.getElementById('addEmployeeModalLabel');
        if (titleEl) titleEl.textContent = 'Tambah Karyawan Baru';

        const btn = document.getElementById('saveEmployeeBtn');
        if (btn) btn.textContent = 'Simpan Karyawan';
    }

    filterEmployees() {
        const tableBody = document.getElementById('employeeTableBody');
        const rows = tableBody.querySelectorAll('tr');
        let visibleCount = 0;

        rows.forEach(row => {
            const text = row.textContent.toLowerCase();
            const isVisible = !this.currentSearch || text.includes(this.currentSearch);
            row.style.display = isVisible ? '' : 'none';
            if (isVisible) visibleCount++;
        });

        this.updateStats(visibleCount);
    }

    updateStats(count) {
        const totalEl = document.getElementById('totalEmployees');
        if (totalEl) {
            totalEl.textContent = count || this.employees.length;
        }
    }

    toggleEmployeeSelection(employeeId) {
        const wasSelected = this.selectedEmployees.has(employeeId);
        if (wasSelected) {
            this.selectedEmployees.delete(employeeId);
        } else {
            this.selectedEmployees.add(employeeId);
        }

        this.updateBulkActions();
        this.updateSelectAllCheckbox();
    }

    toggleSelectAll() {
        const selectAll = document.getElementById('selectAll');
        const checkboxes = document.querySelectorAll('.employee-checkbox');

        if (selectAll.checked) {
            this.selectedEmployees.clear();
            checkboxes.forEach(checkbox => {
                const employeeId = parseInt(checkbox.dataset.id);
                this.selectedEmployees.add(employeeId);
                checkbox.checked = true;
            });
        } else {
            this.selectedEmployees.clear();
            checkboxes.forEach(checkbox => {
                checkbox.checked = false;
            });
        }

        this.updateBulkActions();
    }

    updateBulkActions() {
        const bulkDeleteBtn = document.getElementById('bulkDeleteBtn');
        const selectedCount = this.selectedEmployees.size;

        if (bulkDeleteBtn) {
            bulkDeleteBtn.disabled = selectedCount === 0;
        }

        const selectedCountEl = document.getElementById('selectedCount');
        if (selectedCountEl) {
            selectedCountEl.textContent = selectedCount;
        }
    }

    updateSelectAllCheckbox() {
        const selectAll = document.getElementById('selectAll');
        const checkboxes = document.querySelectorAll('.employee-checkbox');
        const totalCheckboxes = checkboxes.length;
        const checkedCheckboxes = document.querySelectorAll('.employee-checkbox:checked').length;

        if (selectAll) {
            selectAll.indeterminate = checkedCheckboxes > 0 && checkedCheckboxes < totalCheckboxes;
            selectAll.checked = checkedCheckboxes === totalCheckboxes && totalCheckboxes > 0;
        }
    }

    async bulkDeleteSelected() {
        if (this.selectedEmployees.size === 0) return;

        if (!confirm(`Apakah Anda yakin ingin menonaktifkan ${this.selectedEmployees.size} karyawan yang dipilih?`)) {
            return;
        }

        try {
            const response = await fetch('/api/v1/users/bulk/deactivate', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + localStorage.getItem('token')
                },
                body: JSON.stringify([...this.selectedEmployees])
            });

            if (response.ok) {
                this.showAlert(`${this.selectedEmployees.size} karyawan berhasil dinonaktifkan!`, 'success');
                this.selectedEmployees.clear();
                this.loadEmployees();
            } else {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Failed to bulk delete employees');
            }
        } catch (error) {
            console.error('Error bulk deleting employees:', error);
            this.showAlert('Error menonaktifkan karyawan: ' + error.message, 'danger');
        }
    }

    async exportEmployees() {
        try {
            const url = `/api/v1/users${this.currentFilter !== 'active' ? `?status=${this.currentFilter}` : ''}`;
            const response = await fetch(url, {
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('token')
                }
            });

            if (response.ok) {
                const employees = await response.json();
                this.downloadExcel(employees);
                this.showAlert('Data karyawan berhasil diexport!', 'success');
            } else {
                throw new Error('Failed to fetch employees for export');
            }
        } catch (error) {
            console.error('Error exporting employees:', error);
            this.showAlert('Error export data: ' + error.message, 'danger');
        }
    }

    downloadExcel(employees) {
        // Simple CSV download - in production you'd use a library like xlsx or sheetjs
        const headers = ['ID', 'EmployeeID', 'FirstName', 'LastName', 'Email', 'Phone', 'Department', 'Designation', 'JoinDate', 'Status'];
        const csvContent = [
            headers.join(','),
            ...employees.map(emp => [
                emp.id,
                emp.employeeId || '',
                emp.firstName || '',
                emp.lastName || '',
                emp.email || '',
                emp.phone || '',
                emp.department?.name || '',
                emp.designation?.name || '',
                emp.joiningDate || '',
                emp.isActive ? 'Active' : 'Inactive'
            ].join(','))
        ].join('\n');

        const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
        const link = document.createElement('a');
        const url = URL.createObjectURL(blob);
        link.setAttribute('href', url);
        link.setAttribute('download', `employees_${new Date().toISOString().split('T')[0]}.csv`);
        link.style.visibility = 'hidden';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    }

    async importEmployees() {
        const fileInput = document.getElementById('importFile');
        const file = fileInput.files[0];

        if (!file) {
            this.showAlert('Pilih file Excel untuk diupload', 'warning');
            return;
        }

        const formData = new FormData();
        formData.append('file', file);

        try {
            const response = await fetch('/api/v1/users/import', {
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('token')
                },
                body: formData
            });

            if (response.ok) {
                this.showAlert('Data karyawan berhasil diimpor!', 'success');
                this.loadEmployees();
                bootstrap.Modal.getInstance(document.getElementById('importModal')).hide();
            } else {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Failed to import employees');
            }
        } catch (error) {
            console.error('Error importing employees:', error);
            this.showAlert('Error import data: ' + error.message, 'danger');
        }
    }

    async downloadTemplate() {
        try {
            const response = await fetch('/api/reports/template/employee-import', {
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('token')
                }
            });

            if (response.ok) {
                const blob = await response.blob();
                const url = window.URL.createObjectURL(blob);
                const link = document.createElement('a');
                link.href = url;
                link.download = 'Template_Import_Karyawan.xlsx';
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
                window.URL.revokeObjectURL(url);
                this.showAlert('Template berhasil didownload!', 'success');
            } else {
                throw new Error('Failed to download template');
            }
        } catch (error) {
            console.error('Error downloading template:', error);
            this.showAlert('Error download template: ' + error.message, 'danger');
        }
    }

    async deleteEmployee(employeeId) {
        if (!confirm('Are you sure you want to delete this employee?')) {
            return;
        }

        try {
            const response = await fetch(`/api/v1/users/${employeeId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('token')
                }
            });

            if (response.ok) {
                this.showAlert('Employee deleted successfully!', 'success');
                this.loadEmployees();
            } else {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Failed to delete employee');
            }
        } catch (error) {
            console.error('Error deleting employee:', error);
            this.showAlert('Error deleting employee: ' + error.message, 'danger');
        }
    }

    async deactivateEmployee(employeeId) {
        if (!confirm('Apakah Anda yakin ingin menonaktifkan karyawan ini? Karyawan dapat diaktifkan kembali nanti.')) {
            return;
        }

        try {
            const response = await fetch(`/api/v1/users/${employeeId}/deactivate`, {
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('token')
                }
            });

            if (response.ok) {
                this.showAlert('Karyawan berhasil dinonaktifkan!', 'success');
                this.loadEmployees();
            } else {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Failed to deactivate employee');
            }
        } catch (error) {
            console.error('Error deactivating employee:', error);
            this.showAlert('Error menonaktifkan karyawan: ' + error.message, 'danger');
        }
    }

    async reactivateEmployee(employeeId) {
        if (!confirm('Apakah Anda yakin ingin mengaktifkan kembali karyawan ini?')) {
            return;
        }

        try {
            const response = await fetch(`/api/v1/users/${employeeId}/activate`, {
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('token')
                }
            });

            if (response.ok) {
                this.showAlert('Karyawan berhasil diaktifkan!', 'success');
                this.loadEmployees();
            } else {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Failed to reactivate employee');
            }
        } catch (error) {
            console.error('Error reactivating employee:', error);
            this.showAlert('Error mengaktifkan karyawan: ' + error.message, 'danger');
        }
    }

    logout() {
        localStorage.removeItem('token');
        window.location.href = '/login';
    }

    showAlert(message, type = 'info') {
        // Remove any existing alerts
        const existingAlert = document.querySelector('.alert');
        if (existingAlert) {
            existingAlert.remove();
        }

        // Create new alert
        const alertDiv = document.createElement('div');
        alertDiv.className = `alert alert-${type} alert-dismissible fade show position-fixed`;
        alertDiv.style.top = '20px';
        alertDiv.style.right = '20px';
        alertDiv.style.zIndex = '9999';
        alertDiv.role = 'alert';
        alertDiv.innerHTML = `
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        `;

        document.body.appendChild(alertDiv);

        // Auto dismiss after 5 seconds
        setTimeout(() => {
            if (alertDiv.parentNode) {
                alertDiv.parentNode.removeChild(alertDiv);
            }
        }, 5000);
    }
}

// Initialize the employee manager when the DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    window.employeeManager = new EmployeeManager();
// Global event listener for department updates
window.addEventListener('departmentsUpdated', function() {
    console.log('Departments updated, reloading department dropdown');
    if (window.employeeManager) {
        window.employeeManager.loadDepartments();
    }
});

// Global event listener for designation updates
window.addEventListener('designationsUpdated', function() {
    console.log('Designations updated, reloading designation dropdown');
    if (window.employeeManager) {
        window.employeeManager.loadDesignations();
    }
});
});
