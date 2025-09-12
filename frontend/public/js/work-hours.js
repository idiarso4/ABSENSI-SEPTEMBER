// Work Hours Management JavaScript functions
document.addEventListener('DOMContentLoaded', function() {
    const workHoursManager = new WorkHoursManager();
});

// Work Hours Manager Class
class WorkHoursManager {
    constructor() {
        this.shifts = [];
        this.overtimeRequests = [];
        this.settings = {
            defaultStartTime: '08:00',
            defaultEndTime: '17:00',
            breakDuration: 60,
            overtimeRate: 15000
        };
        this.currentUser = null;
        this.canManageOvertime = false;

        this.init();
    }

    init() {
        this.bindEvents();
        this.loadData();
        this.loadSettings();
    }

    bindEvents() {
        // Logout functionality
        document.getElementById('logoutBtn').addEventListener('click', (e) => {
            e.preventDefault();
            this.logout();
        });

        // Save shift
        document.getElementById('saveShiftBtn').addEventListener('click', () => {
            this.saveShift();
        });

        // Submit overtime
        document.getElementById('submitOvertimeBtn').addEventListener('click', () => {
            this.submitOvertimeRequest();
        });

        // Populate employees when overtime modal opens
        const overtimeModal = document.getElementById('overtimeModal');
        if (overtimeModal) {
            overtimeModal.addEventListener('show.bs.modal', () => {
                this.populateOvertimeEmployeeSelect();
            });
        }

        // Save work settings
        document.getElementById('workSettingsForm').addEventListener('submit', (e) => {
            e.preventDefault();
            this.saveSettings();
        });

        // Load calendar
        document.getElementById('loadCalendarBtn').addEventListener('click', () => {
            this.loadWorkCalendar();
        });

        // Tab changes
        document.querySelectorAll('#workHoursTabs .nav-link').forEach(tab => {
            tab.addEventListener('shown.bs.tab', (e) => {
                const targetId = e.target.getAttribute('data-bs-target');
                if (targetId === '#shifts') {
                    this.loadShifts();
                } else if (targetId === '#overtime') {
                    this.loadOvertimeRequests();
                }
            });
        });
    }

    async loadData() {
        try {
            const [meRes, empRes, attRes] = await Promise.all([
                fetch('/api/auth/me', { headers: { 'Authorization': 'Bearer ' + this.getToken() } }),
                fetch('/api/employees', { headers: { 'Authorization': 'Bearer ' + this.getToken() } }),
                fetch('/api/attendance', { headers: { 'Authorization': 'Bearer ' + this.getToken() } })
            ]);
            if (empRes.status === 401) return window.location.href = '/login';
            this.currentUser = meRes.ok ? await meRes.json() : null;
            const utype = (this.currentUser?.userType || '').toUpperCase();
            this.canManageOvertime = utype === 'ADMIN' || utype === 'HR_MANAGER';
            this.employees = empRes.ok ? await empRes.json() : [];
            this.attendance = attRes.ok ? await attRes.json() : [];
        } catch (e) {
            console.warn('Gagal memuat data dasar jam kerja', e);
        } finally {
            this.updateStatistics();
            this.loadShifts();
            this.loadOvertimeRequests();
        }
    }

    updateSettingsForm(settings) {
        this.settings = { ...this.settings, ...settings };
        document.getElementById('defaultStartTime').value = this.settings.defaultStartTime;
        document.getElementById('defaultEndTime').value = this.settings.defaultEndTime;
        document.getElementById('breakDuration').value = this.settings.breakDuration;
        document.getElementById('overtimeRate').value = this.settings.overtimeRate;
    }

    async loadShifts() {
        try {
            const res = await fetch('/api/work-hours/shifts', {
                headers: { 'Authorization': 'Bearer ' + this.getToken() }
            });
            if (res.status === 401) return (window.location.href = '/login');
            this.shifts = res.ok ? await res.json() : [];
        } catch (e) {
            console.warn('Gagal memuat shift', e);
            this.shifts = [];
        } finally {
            this.renderShiftsTable();
        }
    }

    renderShiftsTable() {
        const tbody = document.getElementById('shiftsTableBody');

        if (this.shifts.length === 0) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="8" class="text-center">
                        Belum ada konfigurasi shift. <button class="btn btn-sm btn-primary ms-2" onclick="document.querySelector('[data-bs-target=\'#shiftModal\']').click()">Buat Shift Baru</button>
                    </td>
                </tr>
            `;
            return;
        }

        const rows = this.shifts.map(shift => `
            <tr>
                <td>${shift.name}</td>
                <td>${shift.startTime}</td>
                <td>${shift.endTime}</td>
                <td>${shift.breakDuration} menit</td>
                <td>${this.calculateTotalHours(shift.startTime, shift.endTime, shift.breakDuration)} jam</td>
                <td>${shift.employeeCount || 0} karyawan</td>
                <td><span class="badge bg-${shift.isActive ? 'success' : 'secondary'}">${shift.isActive ? 'Aktif' : 'Nonaktif'}</span></td>
                <td>
                    <button class="btn btn-sm btn-outline-primary me-1" onclick="workHoursManager.editShift(${shift.id})" title="Edit">
                        <i class="bi bi-pencil"></i>
                    </button>
                    <button class="btn btn-sm btn-outline-info me-1" onclick="workHoursManager.viewShift(${shift.id})" title="Detail">
                        <i class="bi bi-eye"></i>
                    </button>
                    <button class="btn btn-sm btn-outline-danger" onclick="workHoursManager.deleteShift(${shift.id})" title="Hapus">
                        <i class="bi bi-trash"></i>
                    </button>
                </td>
            </tr>
        `).join('');

        tbody.innerHTML = rows;
    }

    async loadWorkCalendar() {
        const month = document.getElementById('calendarMonth').value;
        const year = document.getElementById('calendarYear').value;

        try {
            this.renderCalendar(null, parseInt(month,10), parseInt(year,10));
        } catch (error) {
            console.error('Error loading calendar:', error);
            this.renderCalendar(null, parseInt(month,10), parseInt(year,10));
        }
    }

    renderCalendar(calendarData, month, year) {
        const container = document.getElementById('calendarContainer');
        const daysInMonth = new Date(year, month + 1, 0).getDate();

        let html = '<div class="col-12"><div class="row gx-1">';

        // Days of week header
        const daysOfWeek = ['Minggu', 'Senin', 'Selasa', 'Rabu', 'Kamis', 'Jumat', 'Sabtu'];
        daysOfWeek.forEach(day => {
            html += `<div class="col p-2 bg-light text-center border">${day}</div>`;
        });

        // Empty cells for days before first day of month
        const firstDay = new Date(year, month, 1).getDay();
        for (let i = 0; i < firstDay; i++) {
            html += '<div class="col p-2 border bg-secondary"></div>';
        }

        // Days in month
        for (let day = 1; day <= daysInMonth; day++) {
            const isWeekend = [0, 6].includes(new Date(year, month, day).getDay());
            const isToday = new Date().toDateString() === new Date(year, month, day).toDateString();

            let cellClass = 'col p-2 border';
            cellClass += isWeekend ? ' bg-light' : ' bg-white';
            cellClass += isToday ? ' border-warning border-3' : '';

            html += `<div class="${cellClass}">${day}</div>`;
        }

        html += '</div></div>';
        container.innerHTML = html;
    }

    async loadOvertimeRequests() {
        try {
            const endpoint = this.canManageOvertime ? '/api/work-hours/overtime' : '/api/work-hours/overtime/mine';
            const res = await fetch(endpoint, {
                headers: { 'Authorization': 'Bearer ' + this.getToken() }
            });
            if (res.status === 401) return (window.location.href = '/login');
            this.overtimeRequests = res.ok ? await res.json() : [];
        } catch (e) {
            this.overtimeRequests = [];
        } finally {
            if (typeof this.renderOvertimeTable === 'function') this.renderOvertimeTable();
        }
    }

    renderOvertimeTable() {
        const tbody = document.getElementById('overtimeTableBody');

        if (this.overtimeRequests.length === 0) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="8" class="text-center text-muted">
                        Belum ada pengajuan lembur
                    </td>
                </tr>
            `;
            return;
        }

        const rows = this.overtimeRequests.map(request => {
            const empName = request.employee ? `${request.employee.firstName || ''} ${request.employee.lastName || ''}`.trim() : (request.employeeName || '-');
            const total = request.totalHours != null ? request.totalHours : this.calculateHours(request.startTime, request.endTime);
            return `
            <tr>
                <td>${empName}</td>
                <td>${this.formatDate(request.date)}</td>
                <td>${request.startTime}</td>
                <td>${request.endTime}</td>
                <td>${total}</td>
                <td>${request.taskDescription}</td>
                <td>
                    <span class="badge bg-${this.getStatusColor(request.status)}">${this.formatStatus(request.status)}</span>
                    ${request.status === 'APPROVED' && request.approvedAt ? `<div class="small text-muted">${this.formatDate(request.approvedAt)}${request.approvedBy?.name ? ' • ' + request.approvedBy.name : ''}</div>` : ''}
                    ${request.status === 'REJECTED' && request.rejectedAt ? `<div class="small text-muted">${this.formatDate(request.rejectedAt)}${request.approvalNotes ? ' • ' + request.approvalNotes : ''}</div>` : ''}
                </td>
                <td>
                    ${this.canManageOvertime && request.status === 'PENDING' ?
                        `<button class="btn btn-sm btn-outline-success me-1" onclick="workHoursManager.approveOvertime(${request.id})">
                            <i class="bi bi-check-circle"></i>
                        </button>
                        <button class="btn btn-sm btn-outline-danger" onclick="workHoursManager.rejectOvertime(${request.id})">
                            <i class="bi bi-x-circle"></i>
                        </button>` :
                        request.status === 'APPROVED' ? '<span class="text-success">Disetujui</span>' : request.status === 'REJECTED' ? '<span class="text-danger">Ditolak</span>' : ''
                    }
                </td>
            </tr>
        `;}).join('');

        tbody.innerHTML = rows;
    }

    async saveSettings() {
        const settings = {
            defaultStartTime: document.getElementById('defaultStartTime').value,
            defaultEndTime: document.getElementById('defaultEndTime').value,
            breakDuration: parseInt(document.getElementById('breakDuration').value),
            overtimeRate: parseInt(document.getElementById('overtimeRate').value)
        };

        try {
            const response = await fetch('/api/work-hours/settings', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + this.getToken()
                },
                body: JSON.stringify(settings)
            });

            if (response.ok) {
                this.settings = settings;
                this.showAlert('Konfigurasi berhasil disimpan', 'success');
            } else {
                this.showAlert('Gagal menyimpan konfigurasi', 'danger');
            }
        } catch (error) {
            console.error('Error saving settings:', error);
            this.showAlert('Error menyimpan konfigurasi: ' + error.message, 'danger');
        }
    }

    async submitOvertimeRequest() {
        const employeeId = document.getElementById('overtimeEmployee').value;
        const date = document.getElementById('overtimeDate').value;
        const startTime = document.getElementById('overtimeStartTime').value;
        const endTime = document.getElementById('overtimeEndTime').value;
        const taskDescription = document.getElementById('overtimeTask').value.trim();

        if (!employeeId || !date || !startTime || !endTime) {
            return this.showAlert('Lengkapi semua field lembur', 'warning');
        }

        const payload = {
            employee: { id: parseInt(employeeId, 10) },
            date, startTime, endTime, taskDescription
        };

        try {
            const res = await fetch('/api/work-hours/overtime', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + this.getToken()
                },
                body: JSON.stringify(payload)
            });
            if (res.ok) {
                this.showAlert('Pengajuan lembur terkirim', 'success');
                this.loadOvertimeRequests();
                // close modal
                const modalEl = document.getElementById('overtimeModal');
                if (modalEl && window.bootstrap) {
                    const modal = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl);
                    modal.hide();
                }
            } else {
                const err = await res.text();
                this.showAlert('Gagal mengajukan lembur: ' + err, 'danger');
            }
        } catch (e) {
            this.showAlert('Error mengajukan lembur', 'danger');
        }
    }

    async approveOvertime(id) {
        try {
            const notes = prompt('Catatan persetujuan (opsional):', '') || undefined;
            const res = await fetch(`/api/work-hours/overtime/${id}/approve`, {
                method: 'PUT',
                headers: { 'Authorization': 'Bearer ' + this.getToken(), 'Content-Type': 'application/json' },
                body: JSON.stringify(notes ? { notes } : {})
            });
            if (res.ok) { this.showAlert('Lembur disetujui', 'success'); this.loadOvertimeRequests(); }
            else this.showAlert('Gagal menyetujui', 'danger');
        } catch (e) { this.showAlert('Error menyetujui lembur', 'danger'); }
    }

    async rejectOvertime(id) {
        try {
            const notes = prompt('Alasan penolakan (opsional):', '') || undefined;
            const res = await fetch(`/api/work-hours/overtime/${id}/reject`, {
                method: 'PUT',
                headers: { 'Authorization': 'Bearer ' + this.getToken(), 'Content-Type': 'application/json' },
                body: JSON.stringify(notes ? { notes } : {})
            });
            if (res.ok) { this.showAlert('Lembur ditolak', 'success'); this.loadOvertimeRequests(); }
            else this.showAlert('Gagal menolak lembur', 'danger');
        } catch (e) { this.showAlert('Error menolak lembur', 'danger'); }
    }

    populateOvertimeEmployeeSelect() {
        const sel = document.getElementById('overtimeEmployee');
        const group = document.getElementById('overtimeEmployeeGroup');
        if (!sel) return;
        sel.innerHTML = '<option value="">Pilih karyawan</option>';
        (this.employees || []).forEach(emp => {
            const opt = document.createElement('option');
            opt.value = emp.id;
            opt.textContent = `${emp.firstName || ''} ${emp.lastName || ''}`.trim();
            sel.appendChild(opt);
        });

        // If current user is EMPLOYEE/USER, preselect their employee record and lock selection
        const utype = (this.currentUser?.userType || '').toUpperCase();
        if (utype === 'EMPLOYEE' || utype === 'USER') {
            const emp = (this.employees || []).find(e => e.user && e.user.id === this.currentUser.id);
            if (emp) {
                sel.value = emp.id;
                sel.disabled = true;
                if (group) group.style.display = 'none';
            } else {
                sel.disabled = false;
                if (group) group.style.display = '';
            }
        } else {
            sel.disabled = false;
            if (group) group.style.display = '';
        }
    }

    calculateHours(start, end) {
        if (!start || !end) return '-';
        try {
            const s = new Date(`2000-01-01T${start}`);
            const e = new Date(`2000-01-01T${end}`);
            const diff = (e - s) / 3600000;
            return Math.max(0, diff).toFixed(1);
        } catch (_) { return '-'; }
    }

    editShift(shiftId) {
        const shift = this.shifts.find(s => s.id === shiftId);
        if (!shift) return;

        // For now, just show a message that this feature is not implemented
        this.showAlert('Fitur edit shift belum tersedia', 'info');
    }

    async deleteShift(shiftId) {
        if (!confirm('Apakah Anda yakin ingin menghapus shift ini?')) return;

        try {
            const response = await fetch(`/api/work-hours/shifts/${shiftId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': 'Bearer ' + this.getToken()
                }
            });

            if (response.ok) {
                this.showAlert('Shift berhasil dihapus', 'success');
                this.loadShifts();
                this.updateStatistics();
            } else {
                this.showAlert('Gagal menghapus shift', 'danger');
            }
        } catch (error) {
            console.error('Error deleting shift:', error);
            this.showAlert('Error menghapus shift: ' + error.message, 'danger');
        }
    }

    calculateTotalHours(startTime, endTime, breakMinutes) {
        const start = new Date(`2000-01-01T${startTime}`);
        const end = new Date(`2000-01-01T${endTime}`);
        const diffMs = end - start;
        const diffHours = diffMs / (1000 * 60 * 60);
        const breakHours = breakMinutes / 60;
        return Math.max(0, diffHours - breakHours).toFixed(1);
    }

    updateStatistics() {
        document.getElementById('totalShifts').textContent = this.employees.length;
        const now = new Date();
        const ym = `${now.getFullYear()}-${String(now.getMonth()+1).padStart(2,'0')}`;
        const dailyByEmp = {};
        (this.attendance || []).forEach(a => {
            if (!a.clockInTime) return;
            const ci = new Date(a.clockInTime);
            const keyMonth = `${ci.getFullYear()}-${String(ci.getMonth()+1).padStart(2,'0')}`;
            if (keyMonth !== ym) return;
            const id = a.employee?.id || 'unknown';
            const dayKey = ci.toISOString().slice(0,10);
            const co = a.clockOutTime ? new Date(a.clockOutTime) : null;
            const hours = co ? Math.max(0, (co-ci)/3600000) : 0;
            if (!dailyByEmp[id]) dailyByEmp[id] = {};
            dailyByEmp[id][dayKey] = Math.max(dailyByEmp[id][dayKey]||0, hours);
        });
        let overtime = 0;
        Object.values(dailyByEmp).forEach(days => {
            Object.values(days).forEach(h => { overtime += Math.max(0, h - 8); });
        });
        document.getElementById('monthlyOvertime').textContent = overtime.toFixed(1);

        const today = new Date(); today.setHours(0,0,0,0);
        const presentIds = new Set((this.attendance||[]).filter(a => a.status==='present' && a.clockInTime && new Date(a.clockInTime).setHours(0,0,0,0)===today.getTime()).map(a => a.employee?.id));
        const empCount = this.employees.length || 1;
        const compliance = Math.round((presentIds.size/empCount)*100);
        document.getElementById('complianceRate').textContent = `${isFinite(compliance)?compliance:0}%`;
    }

    async loadSettings() {
        try {
            const res = await fetch('/api/work-hours/settings', {
                headers: { 'Authorization': 'Bearer ' + this.getToken() }
            });
            if (res.status === 401) return (window.location.href = '/login');
            if (res.ok) {
                const data = await res.json();
                this.updateSettingsForm(data);
            }
        } catch (e) {
            // keep defaults
        }
    }

    async saveShift() {
        // Prevent double submission
        const saveBtn = document.getElementById('saveShiftBtn');
        if (saveBtn && saveBtn.disabled) return; // Prevent double click

        // Expecting inputs with IDs: shiftName, shiftStartTime, shiftEndTime, shiftBreak
        const nameEl = document.getElementById('shiftName');
        const startEl = document.getElementById('shiftStartTime');
        const endEl = document.getElementById('shiftEndTime');
        const breakEl = document.getElementById('shiftBreak');
        if (!nameEl || !startEl || !endEl || !breakEl) {
            return this.showAlert('Form shift belum tersedia di halaman', 'warning');
        }

        const payload = {
            name: nameEl.value.trim(),
            startTime: startEl.value,
            endTime: endEl.value,
            breakDuration: parseInt(breakEl.value || '0', 10),
            isActive: true
        };

        if (!payload.name || !payload.startTime || !payload.endTime) {
            return this.showAlert('Lengkapi data shift', 'warning');
        }

        // Disable button to prevent double submission
        if (saveBtn) {
            saveBtn.disabled = true;
            saveBtn.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Menyimpan...';
        }

        try {
            const res = await fetch('/api/work-hours/shifts', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + this.getToken()
                },
                body: JSON.stringify(payload)
            });
            if (res.ok) {
                this.showAlert('Shift berhasil disimpan', 'success');
                this.loadShifts();

                // Clear form fields
                const nameEl = document.getElementById('shiftName');
                const startEl = document.getElementById('shiftStartTime');
                const endEl = document.getElementById('shiftEndTime');
                const breakEl = document.getElementById('shiftBreak');
                if (nameEl) nameEl.value = '';
                if (startEl) startEl.value = '08:00';
                if (endEl) endEl.value = '17:00';
                if (breakEl) breakEl.value = '60';

                // close modal if present
                const modalEl = document.getElementById('shiftModal');
                if (modalEl && window.bootstrap) {
                    const modal = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl);
                    modal.hide();
                }
            } else {
                this.showAlert('Gagal menyimpan shift', 'danger');
            }
        } catch (e) {
            this.showAlert('Error menyimpan shift', 'danger');
        } finally {
            // Re-enable button
            if (saveBtn) {
                saveBtn.disabled = false;
                saveBtn.innerHTML = 'Simpan';
            }
        }
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

    formatDate(dateString) {
        if (!dateString) return '-';
        try {
            return new Date(dateString).toLocaleDateString('id-ID');
        } catch (e) {
            return dateString;
        }
    }

    formatStatus(status) {
        const statusMap = {
            'PENDING': 'Menunggu',
            'APPROVED': 'Disetujui',
            'REJECTED': 'Ditolak'
        };
        return statusMap[status] || status;
    }

    getStatusColor(status) {
        const colorMap = {
            'PENDING': 'warning',
            'APPROVED': 'success',
            'REJECTED': 'danger'
        };
        return colorMap[status] || 'secondary';
    }

    logout() {
        localStorage.removeItem('token');
        window.location.href = '/login';
    }

    getToken() {
        return localStorage.getItem('token') || '';
    }
}

// Initialize the work hours manager when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    window.workHoursManager = new WorkHoursManager();
});
