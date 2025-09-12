// Attendance Management JavaScript
document.addEventListener('DOMContentLoaded', function() {
    initializeAttendanceManagement();
    loadAttendanceRecords();
    setupAttendanceEventListeners();
});

function initializeAttendanceManagement() {
    console.log('Attendance management initialized');
}

function setupAttendanceEventListeners() {
    // Date filter
    const dateFilter = document.getElementById('dateFilter');
    if (dateFilter) {
        dateFilter.addEventListener('change', function() {
            filterAttendance();
        });
    }

    // Class filter
    const classFilter = document.getElementById('classFilter');
    if (classFilter) {
        classFilter.addEventListener('change', function() {
            filterAttendance();
        });
    }

    // Subject filter
    const subjectFilter = document.getElementById('subjectFilter');
    if (subjectFilter) {
        subjectFilter.addEventListener('change', function() {
            filterAttendance();
        });
    }

    // Status filter
    const statusFilter = document.getElementById('statusFilter');
    if (statusFilter) {
        statusFilter.addEventListener('change', function() {
            filterAttendance();
        });
    }

    // Reset filters
    const resetBtn = document.getElementById('resetFilters');
    if (resetBtn) {
        resetBtn.addEventListener('click', function() {
            resetFilters();
        });
    }

    // Mark attendance button
    const markBtn = document.getElementById('markAttendanceBtn');
    if (markBtn) {
        markBtn.addEventListener('click', function() {
            markAttendance();
        });
    }

    // Export attendance button
    const exportBtn = document.getElementById('exportAttendanceBtn');
    if (exportBtn) {
        exportBtn.addEventListener('click', function(e) {
            e.preventDefault();
            exportAttendance();
        });
    }
}

function loadAttendanceRecords() {
    const date = document.getElementById('dateFilter')?.value || new Date().toISOString().split('T')[0];
    const classId = document.getElementById('classFilter')?.value;
    const subjectId = document.getElementById('subjectFilter')?.value;

    let url = '/api/v1/attendance';
    const params = new URLSearchParams();

    if (date) params.append('date', date);
    if (classId) params.append('classId', classId);
    if (subjectId) params.append('subjectId', subjectId);

    if (params.toString()) {
        url += '?' + params.toString();
    }

    fetch(url, {
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(data => {
        displayAttendanceRecords(data.content || data || []);
    })
    .catch(error => {
        console.error('Error loading attendance records:', error);
        showError('Gagal memuat data kehadiran');
    });
}

function displayAttendanceRecords(records) {
    const tbody = document.getElementById('attendanceTableBody');

    if (!records || records.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="7" class="text-center">
                    <div class="alert alert-info">
                        <i class="fas fa-info-circle"></i> Tidak ada data kehadiran ditemukan
                    </div>
                </td>
            </tr>
        `;
        return;
    }

    tbody.innerHTML = records.map(record => `
        <tr>
            <td>${formatDate(record.attendanceDate || record.date)}</td>
            <td>${record.studentName || record.student?.fullName || '-'}</td>
            <td>${record.className || record.class?.name || '-'}</td>
            <td>${record.subjectName || record.subject?.name || '-'}</td>
            <td>${record.attendanceType || record.type || '-'}</td>
            <td>
                <span class="badge ${getStatusBadgeClass(record.status)}">
                    ${getStatusText(record.status)}
                </span>
            </td>
            <td>
                <div class="btn-group" role="group">
                    <button class="btn btn-sm btn-outline-primary" onclick="viewAttendanceDetail(${record.id})" title="Lihat Detail">
                        <i class="fas fa-eye"></i>
                    </button>
                    <button class="btn btn-sm btn-outline-warning" onclick="editAttendance(${record.id})" title="Edit">
                        <i class="fas fa-edit"></i>
                    </button>
                </div>
            </td>
        </tr>
    `).join('');
}

function getStatusBadgeClass(status) {
    switch (status?.toLowerCase()) {
        case 'present':
        case 'hadir':
            return 'bg-success';
        case 'absent':
        case 'tidak hadir':
            return 'bg-danger';
        case 'late':
        case 'terlambat':
            return 'bg-warning';
        case 'permission':
        case 'izin':
            return 'bg-info';
        default:
            return 'bg-secondary';
    }
}

function getStatusText(status) {
    switch (status?.toLowerCase()) {
        case 'present':
            return 'Hadir';
        case 'absent':
            return 'Tidak Hadir';
        case 'late':
            return 'Terlambat';
        case 'permission':
            return 'Izin';
        default:
            return status || '-';
    }
}

function filterAttendance() {
    loadAttendanceRecords();
}

function resetFilters() {
    const dateFilter = document.getElementById('dateFilter');
    const classFilter = document.getElementById('classFilter');
    const subjectFilter = document.getElementById('subjectFilter');
    const statusFilter = document.getElementById('statusFilter');

    if (dateFilter) dateFilter.value = '';
    if (classFilter) classFilter.value = '';
    if (subjectFilter) subjectFilter.value = '';
    if (statusFilter) statusFilter.value = '';

    loadAttendanceRecords();
}

function markAttendance() {
    const form = document.getElementById('markAttendanceForm');
    if (!form) return;

    const formData = new FormData(form);

    const attendanceData = {
        studentId: parseInt(formData.get('studentId')),
        subjectId: parseInt(formData.get('subjectId')),
        attendanceDate: formData.get('attendanceDate'),
        attendanceType: formData.get('attendanceType'),
        status: formData.get('status'),
        notes: formData.get('notes')
    };

    fetch('/api/v1/attendance', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(attendanceData)
    })
    .then(response => response.json())
    .then(data => {
        if (data.id) {
            showSuccess('Kehadiran berhasil dicatat');
            loadAttendanceRecords();
            const modal = bootstrap.Modal.getInstance(document.getElementById('markAttendanceModal'));
            if (modal) modal.hide();
            form.reset();
        } else {
            showError('Gagal mencatat kehadiran');
        }
    })
    .catch(error => {
        console.error('Error marking attendance:', error);
        showError('Gagal mencatat kehadiran');
    });
}

function viewAttendanceDetail(attendanceId) {
    fetch(`/api/v1/attendance/${attendanceId}`, {
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(attendance => {
        displayAttendanceDetails(attendance);
        const modal = new bootstrap.Modal(document.getElementById('viewAttendanceModal'));
        modal.show();
    })
    .catch(error => {
        console.error('Error loading attendance details:', error);
        showError('Gagal memuat detail kehadiran');
    });
}

function displayAttendanceDetails(attendance) {
    const detailsContainer = document.getElementById('attendanceDetails');
    if (!detailsContainer) return;

    detailsContainer.innerHTML = `
        <div class="row">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header">
                        <h6 class="mb-0">Detail Kehadiran</h6>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6">
                                <p><strong>Siswa:</strong> ${attendance.studentName || attendance.student?.fullName || '-'}</p>
                                <p><strong>Kelas:</strong> ${attendance.className || attendance.class?.name || '-'}</p>
                                <p><strong>Mata Pelajaran:</strong> ${attendance.subjectName || attendance.subject?.name || '-'}</p>
                                <p><strong>Tanggal:</strong> ${formatDate(attendance.attendanceDate || attendance.date)}</p>
                            </div>
                            <div class="col-md-6">
                                <p><strong>Tipe Kehadiran:</strong> ${attendance.attendanceType || attendance.type || '-'}</p>
                                <p><strong>Status:</strong>
                                    <span class="badge ${getStatusBadgeClass(attendance.status)}">
                                        ${getStatusText(attendance.status)}
                                    </span>
                                </p>
                                <p><strong>Catatan:</strong> ${attendance.notes || '-'}</p>
                                <p><strong>Dicatat pada:</strong> ${formatDateTime(attendance.createdAt)}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `;
}

function editAttendance(attendanceId) {
    fetch(`/api/v1/attendance/${attendanceId}`, {
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(attendance => {
        populateEditAttendanceForm(attendance);
        const modal = new bootstrap.Modal(document.getElementById('editAttendanceModal'));
        modal.show();
    })
    .catch(error => {
        console.error('Error loading attendance for edit:', error);
        showError('Gagal memuat data kehadiran untuk diedit');
    });
}

function populateEditAttendanceForm(attendance) {
    document.getElementById('editAttendanceId').value = attendance.id;
    document.getElementById('editStudentId').value = attendance.studentId || attendance.student?.id || '';
    document.getElementById('editSubjectId').value = attendance.subjectId || attendance.subject?.id || '';
    document.getElementById('editAttendanceDate').value = formatDateForInput(attendance.attendanceDate || attendance.date);
    document.getElementById('editAttendanceType').value = attendance.attendanceType || attendance.type || '';
    document.getElementById('editStatus').value = attendance.status || '';
    document.getElementById('editNotes').value = attendance.notes || '';
}

function updateAttendance() {
    const form = document.getElementById('editAttendanceForm');
    const formData = new FormData(form);
    const attendanceId = document.getElementById('editAttendanceId').value;

    const attendanceData = {
        studentId: parseInt(formData.get('studentId')),
        subjectId: parseInt(formData.get('subjectId')),
        attendanceDate: formData.get('attendanceDate'),
        attendanceType: formData.get('attendanceType'),
        status: formData.get('status'),
        notes: formData.get('notes')
    };

    fetch(`/api/v1/attendance/${attendanceId}`, {
        method: 'PUT',
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(attendanceData)
    })
    .then(response => response.json())
    .then(data => {
        if (data.id) {
            showSuccess('Kehadiran berhasil diperbarui');
            loadAttendanceRecords();
            const modal = bootstrap.Modal.getInstance(document.getElementById('editAttendanceModal'));
            if (modal) modal.hide();
        } else {
            showError('Gagal memperbarui kehadiran');
        }
    })
    .catch(error => {
        console.error('Error updating attendance:', error);
        showError('Gagal memperbarui kehadiran');
    });
}

function exportAttendance() {
    const date = document.getElementById('dateFilter')?.value;
    const classId = document.getElementById('classFilter')?.value;
    const subjectId = document.getElementById('subjectFilter')?.value;

    let url = '/api/v1/attendance/export';
    const params = new URLSearchParams();

    if (date) params.append('date', date);
    if (classId) params.append('classId', classId);
    if (subjectId) params.append('subjectId', subjectId);

    if (params.toString()) {
        url += '?' + params.toString();
    }

    fetch(url, {
        headers: {
            'Authorization': `Bearer ${getToken()}`
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Failed to export attendance data');
        }
        return response.blob();
    })
    .then(blob => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'attendance_export.xlsx';
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
        document.body.removeChild(a);
        showSuccess('Data kehadiran berhasil diekspor');
    })
    .catch(error => {
        console.error('Error exporting attendance:', error);
        showError('Gagal mengekspor data kehadiran');
    });
}

function formatDate(dateString) {
    if (!dateString) return '-';
    const date = new Date(dateString);
    return date.toLocaleDateString('id-ID', {
        day: 'numeric',
        month: 'long',
        year: 'numeric'
    });
}

function formatDateTime(dateString) {
    if (!dateString) return '-';
    const date = new Date(dateString);
    return date.toLocaleDateString('id-ID', {
        day: 'numeric',
        month: 'long',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
}

function formatDateForInput(dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toISOString().split('T')[0];
}

function getToken() {
    return localStorage.getItem('token') || '';
}

function showSuccess(message) {
    // Simple success notification
    alert(message);
}

function showError(message) {
    // Simple error notification
    alert('Error: ' + message);
}