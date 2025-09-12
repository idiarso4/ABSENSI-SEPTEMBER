// Benefits & Deductions Management JavaScript

class BenefitsManager {
    constructor() {
        this.selectedEmployeeId = null;
        this.benefitsData = [];
        this.init();
    }

    init() {
        this.loadEmployees();
        this.bindEvents();
    }

    bindEvents() {
        // Logout functionality
        document.getElementById('logoutBtn').addEventListener('click', (e) => {
            e.preventDefault();
            this.logout();
        });

        // Save benefit button
        document.getElementById('saveBenefitBtn').addEventListener('click', () => {
            this.saveBenefitDeduction();
        });
    }

    async loadEmployees() {
        try {
            const response = await fetch('/api/employees', {
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('token')
                }
            });

            if (response.ok) {
                const employees = await response.json();
                this.populateEmployeeSelect(employees);
            } else if (response.status === 401) {
                window.location.href = '/login';
            } else {
                console.error('Failed to load employees');
                this.showAlert('Gagal memuat daftar karyawan', 'danger');
            }
        } catch (error) {
            console.error('Error loading employees:', error);
            this.showAlert('Error memuat karyawan: ' + error.message, 'danger');
        }
    }

    populateEmployeeSelect(employees) {
        const select = document.getElementById('employeeSelect');
        select.innerHTML = '<option value="">Pilih Karyawan</option>';

        employees.forEach(employee => {
            const option = document.createElement('option');
            option.value = employee.id;
            option.textContent = `${employee.firstName} ${employee.lastName} (${employee.employeeId})`;
            select.appendChild(option);
        });

        // Add change event listener
        select.addEventListener('change', (e) => {
            this.onEmployeeSelected(e.target.value);
        });
    }

    async onEmployeeSelected(employeeId) {
        this.selectedEmployeeId = employeeId;
        document.getElementById('addBtn').disabled = !employeeId;

        if (employeeId) {
            await Promise.all([
                this.loadEmployeeBenefits(employeeId),
                this.loadEmployeeSummary(employeeId)
            ]);
            document.getElementById('employeeSummary').style.display = 'block';
            document.getElementById('benefitsCard').style.display = 'block';
        } else {
            // Hide sections when no employee is selected
            document.getElementById('employeeSummary').style.display = 'none';
            document.getElementById('benefitsCard').style.display = 'none';
        }
    }

    async loadEmployeeSummary(employeeId) {
        try {
            const [benefitsResponse, deductionsResponse, impactResponse] = await Promise.all([
                fetch(`/api/benefit-deductions/employee/${employeeId}/total-benefits`, {
                    headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') }
                }),
                fetch(`/api/benefit-deductions/employee/${employeeId}/total-deductions`, {
                    headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') }
                }),
                fetch(`/api/benefit-deductions/employee/${employeeId}/net-impact`, {
                    headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') }
                })
            ]);

            if (benefitsResponse.ok) {
                const totalBenefits = await benefitsResponse.json();
                document.getElementById('totalBenefits').textContent = this.formatCurrency(totalBenefits);
            }

            if (deductionsResponse.ok) {
                const totalDeductions = await deductionsResponse.json();
                document.getElementById('totalDeductions').textContent = this.formatCurrency(totalDeductions);
            }

            if (impactResponse.ok) {
                const netImpact = await impactResponse.json();
                document.getElementById('netImpact').textContent = this.formatCurrency(netImpact);
                document.getElementById('netImpact').style.color = netImpact >= 0 ? '#28a745' : '#dc3545';
            }

            // Load base salary for reference
            const salaryResponse = await fetch(`/api/employees/${employeeId}`, {
                headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') }
            });

            if (salaryResponse.ok) {
                const employee = await salaryResponse.json();
                document.getElementById('baseSalary').textContent = this.formatCurrency(employee.basicSalary || 0);
            }

        } catch (error) {
            console.error('Error loading employee summary:', error);
        }
    }

    async loadEmployeeBenefits(employeeId) {
        try {
            const response = await fetch(`/api/benefit-deductions/employee/${employeeId}`, {
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('token')
                }
            });

            if (response.ok) {
                this.benefitsData = await response.json();
                this.renderBenefitsTable();
            } else if (response.status === 401) {
                window.location.href = '/login';
            } else {
                this.showAlert('Gagal memuat tunjangan karyawan', 'danger');
            }
        } catch (error) {
            console.error('Error loading benefits:', error);
            this.showAlert('Error memuat tunjangan: ' + error.message, 'danger');
        }
    }

    renderBenefitsTable() {
        const tbody = document.getElementById('benefitsTableBody');
        tbody.innerHTML = '';

        if (this.benefitsData.length === 0) {
            const row = document.createElement('tr');
            row.innerHTML = '<td colspan="10" class="text-center text-muted">Belum ada tunjangan/potongan untuk karyawan ini</td>';
            tbody.appendChild(row);
            return;
        }

        this.benefitsData.forEach(benefit => {
            const row = document.createElement('tr');
            const actionBadge = benefit.type === 'add' ? 'badge bg-success' : 'badge bg-warning';
            const statusBadge = benefit.isActive ? 'badge bg-primary' : 'badge bg-secondary';

            row.innerHTML = `
                <td>${benefit.name}</td>
                <td><span class="${actionBadge}">${benefit.type === 'add' ? 'Tambah' : 'Potong'}</span></td>
                <td>${benefit.amountType === 'percent' ? benefit.amount + '%' : this.formatCurrency(benefit.amount)}</td>
                <td>${benefit.amountType === 'fixed' ? 'Tetap' : 'Persentase'}</td>
                <td>${this.formatDate(benefit.effectiveFrom) || '-'}</td>
                <td>${this.formatDate(benefit.effectiveUntil) || '-'}</td>
                <td>${this.getFrequencyLabel(benefit.frequency)}</td>
                <td>${this.formatCurrency(benefit.calculatedAmount)}</td>
                <td><span class="${statusBadge}">${benefit.isActive ? 'Aktif' : 'Nonaktif'}</span></td>
                <td>
                    <button class="btn btn-sm btn-outline-primary me-1" onclick="benefitsManager.editBenefit(${benefit.id})">
                        <i class="bi bi-pencil"></i>
                    </button>
                    ${benefit.isActive ?
                        `<button class="btn btn-sm btn-outline-danger" onclick="benefitsManager.deactivateBenefit(${benefit.id})">
                            <i class="bi bi-x-circle"></i>
                        </button>` :
                        `<button class="btn btn-sm btn-outline-success" onclick="benefitsManager.activateBenefit(${benefit.id})">
                            <i class="bi bi-check-circle"></i>
                        </button>`
                    }
                </td>
            `;
            tbody.appendChild(row);
        });
    }

    addNewBenefit() {
        if (!this.selectedEmployeeId) {
            this.showAlert('Pilih karyawan terlebih dahulu', 'warning');
            return;
        }

        this.currentBenefitId = null;
        document.getElementById('benefitModalLabel').textContent = 'Tambah Tunjangan/Potongan';
        document.getElementById('benefitForm').reset();
        this.benefitModal = new bootstrap.Modal(document.getElementById('benefitModal'));
        this.benefitModal.show();
    }

    editBenefit(benefitId) {
        const benefit = this.benefitsData.find(b => b.id === benefitId);
        if (!benefit) return;

        this.currentBenefitId = benefitId;
        document.getElementById('benefitModalLabel').textContent = 'Edit Tunjangan/Potongan';
        document.getElementById('benefitName').value = benefit.name;
        document.getElementById('benefitType').value = benefit.type;
        document.getElementById('amount').value = benefit.amount;
        document.getElementById('amountType').value = benefit.amountType;
        document.getElementById('effectiveFrom').value = benefit.effectiveFrom ? this.formatDateForInput(benefit.effectiveFrom) : '';
        document.getElementById('effectiveUntil').value = benefit.effectiveUntil ? this.formatDateForInput(benefit.effectiveUntil) : '';
        document.getElementById('frequency').value = benefit.frequency;
        document.getElementById('description').value = benefit.description || '';
        document.getElementById('remarks').value = benefit.remarks || '';

        this.benefitModal = new bootstrap.Modal(document.getElementById('benefitModal'));
        this.benefitModal.show();
    }

    async saveBenefitDeduction() {
        if (!this.selectedEmployeeId) return;

        const formData = {
            employee: { id: this.selectedEmployeeId },
            name: document.getElementById('benefitName').value,
            type: document.getElementById('benefitType').value,
            amount: parseFloat(document.getElementById('amount').value),
            amountType: document.getElementById('amountType').value,
            effectiveFrom: document.getElementById('effectiveFrom').value || null,
            effectiveUntil: document.getElementById('effectiveUntil').value || null,
            frequency: document.getElementById('frequency').value,
            description: document.getElementById('description').value || null,
            remarks: document.getElementById('remarks').value || null,
            isActive: true
        };

        const url = this.currentBenefitId
            ? `/api/benefit-deductions/${this.currentBenefitId}`
            : '/api/benefit-deductions';

        const method = this.currentBenefitId ? 'PUT' : 'POST';

        try {
            const response = await fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + localStorage.getItem('token')
                },
                body: JSON.stringify(formData)
            });

            if (response.ok) {
                this.showAlert(this.currentBenefitId ? 'Tunjangan berhasil diperbarui' : 'Tunjangan berhasil ditambahkan', 'success');
                this.benefitModal.hide();
                await this.loadEmployeeBenefits(this.selectedEmployeeId);
                await this.loadEmployeeSummary(this.selectedEmployeeId);
            } else {
                const error = await response.text();
                this.showAlert('Gagal menyimpan tunjangan: ' + error, 'danger');
            }
        } catch (error) {
            console.error('Error saving benefit:', error);
            this.showAlert('Error menyimpan tunjangan: ' + error.message, 'danger');
        }
    }

    async deactivateBenefit(benefitId) {
        if (!confirm('Apakah Anda yakin ingin menonaktifkan tunjangan ini?')) return;

        try {
            const response = await fetch(`/api/benefit-deductions/${benefitId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('token')
                }
            });

            if (response.ok) {
                this.showAlert('Tunjangan berhasil dinonaktifkan', 'success');
                await this.loadEmployeeBenefits(this.selectedEmployeeId);
                await this.loadEmployeeSummary(this.selectedEmployeeId);
            } else {
                this.showAlert('Gagal menonaktifkan tunjangan', 'danger');
            }
        } catch (error) {
            console.error('Error deactivating benefit:', error);
            this.showAlert('Error menonaktifkan tunjangan: ' + error.message, 'danger');
        }
    }

    async activateBenefit(benefitId) {
        // Note: This would require additional backend endpoint for reactivation
        this.showAlert('Fitur aktivasi ulang belum tersedia', 'info');
    }

    formatCurrency(amount) {
        return new Intl.NumberFormat('id-ID', {
            style: 'currency',
            currency: 'IDR',
            minimumFractionDigits: 0
        }).format(amount || 0);
    }

    formatDate(dateString) {
        if (!dateString) return '';
        return new Date(dateString).toLocaleDateString('id-ID');
    }

    formatDateForInput(dateString) {
        if (!dateString) return '';
        return new Date(dateString).toISOString().split('T')[0];
    }

    getFrequencyLabel(frequency) {
        const labels = {
            'monthly': 'Bulanan',
            'yearly': 'Tahunan',
            'quarterly': 'Triwulanan',
            'once': 'Sekali'
        };
        return labels[frequency] || frequency;
    }

    showAlert(message, type = 'info') {
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
            alertDiv.remove();
        }, 5000);
    }

    logout() {
        localStorage.removeItem('token');
        window.location.href = '/login';
    }
}

// Initialize the benefits manager when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    window.benefitsManager = new BenefitsManager();
});