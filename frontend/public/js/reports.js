// Reports page functionality
let currentReportData = null;

// Initialize reports page
function initializeReportsPage() {
    console.log('Initializing reports page...');
    loadSystemStatistics();
}

// Load system statistics
async function loadSystemStatistics() {
    try {
        const [studentsRes, teachersRes, subjectsRes, classroomsRes, paymentsRes, attendanceRes] = await Promise.all([
            fetch('/api/v1/students/statistics', { headers: getAuthHeaders() }),
            fetch('/api/v1/teachers/statistics', { headers: getAuthHeaders() }),
            fetch('/api/v1/subjects/statistics', { headers: getAuthHeaders() }),
            fetch('/api/v1/classrooms/statistics', { headers: getAuthHeaders() }),
            fetch('/api/v1/payments/statistics', { headers: getAuthHeaders() }),
            fetch('/api/v1/attendance/statistics', { headers: getAuthHeaders() })
        ]);

        const students = studentsRes.ok ? await studentsRes.json() : { totalCount: 0 };
        const teachers = teachersRes.ok ? await teachersRes.json() : { totalCount: 0 };
        const subjects = subjectsRes.ok ? await subjectsRes.json() : { totalCount: 0 };
        const classrooms = classroomsRes.ok ? await classroomsRes.json() : { totalCount: 0 };
        const payments = paymentsRes.ok ? await paymentsRes.json() : { totalCount: 0 };
        const attendance = attendanceRes.ok ? await attendanceRes.json() : { totalCount: 0 };

        document.getElementById('totalStudents').textContent = students.totalCount || 0;
        document.getElementById('totalTeachers').textContent = teachers.totalCount || 0;
        document.getElementById('totalSubjects').textContent = subjects.totalCount || 0;
        document.getElementById('totalClassrooms').textContent = classrooms.totalCount || 0;
        document.getElementById('totalPayments').textContent = payments.totalCount || 0;
        document.getElementById('totalAttendance').textContent = attendance.totalCount || 0;

    } catch (error) {
        console.error('Error loading system statistics:', error);
        // Set default values
        document.getElementById('totalStudents').textContent = '0';
        document.getElementById('totalTeachers').textContent = '0';
        document.getElementById('totalSubjects').textContent = '0';
        document.getElementById('totalClassrooms').textContent = '0';
        document.getElementById('totalPayments').textContent = '0';
        document.getElementById('totalAttendance').textContent = '0';
    }
}

// Generate student reports
async function generateStudentReport(type) {
    try {
        showLoading('Generating student report...');
        let endpoint = '/api/v1/students';

        switch(type) {
            case 'all':
                endpoint += '?page=0&size=1000';
                break;
            case 'by_class':
                endpoint += '/by-class';
                break;
            case 'by_major':
                endpoint += '/by-major';
                break;
        }

        const response = await fetch(endpoint, { headers: getAuthHeaders() });
        if (!response.ok) throw new Error('Failed to fetch student data');

        const data = await response.json();
        currentReportData = { type: 'student', subtype: type, data: data };

        displayReportResults(data, `Laporan Siswa - ${type.replace('_', ' ').toUpperCase()}`);
        hideLoading();

    } catch (error) {
        hideLoading();
        showError('Error generating student report: ' + error.message);
    }
}

// Generate teacher reports
async function generateTeacherReport(type) {
    try {
        showLoading('Generating teacher report...');
        let endpoint = '/api/v1/teachers';

        switch(type) {
            case 'all':
                endpoint += '?page=0&size=1000';
                break;
            case 'by_subject':
                endpoint += '/by-subject';
                break;
            case 'performance':
                endpoint += '/performance';
                break;
        }

        const response = await fetch(endpoint, { headers: getAuthHeaders() });
        if (!response.ok) throw new Error('Failed to fetch teacher data');

        const data = await response.json();
        currentReportData = { type: 'teacher', subtype: type, data: data };

        displayReportResults(data, `Laporan Guru - ${type.replace('_', ' ').toUpperCase()}`);
        hideLoading();

    } catch (error) {
        hideLoading();
        showError('Error generating teacher report: ' + error.message);
    }
}

// Generate attendance reports
async function generateAttendanceReport(type) {
    try {
        showLoading('Generating attendance report...');
        let endpoint = '/api/v1/attendance';

        switch(type) {
            case 'daily':
                endpoint += '/daily';
                break;
            case 'monthly':
                endpoint += '/monthly';
                break;
            case 'summary':
                endpoint += '/summary';
                break;
        }

        const response = await fetch(endpoint, { headers: getAuthHeaders() });
        if (!response.ok) throw new Error('Failed to fetch attendance data');

        const data = await response.json();
        currentReportData = { type: 'attendance', subtype: type, data: data };

        displayReportResults(data, `Laporan Kehadiran - ${type.replace('_', ' ').toUpperCase()}`);
        hideLoading();

    } catch (error) {
        hideLoading();
        showError('Error generating attendance report: ' + error.message);
    }
}

// Generate grade reports
async function generateGradeReport(type) {
    try {
        showLoading('Generating grade report...');
        let endpoint = '/api/v1/grades';

        switch(type) {
            case 'by_subject':
                endpoint += '/by-subject';
                break;
            case 'by_student':
                endpoint += '/by-student';
                break;
            case 'class_rank':
                endpoint += '/class-rank';
                break;
        }

        const response = await fetch(endpoint, { headers: getAuthHeaders() });
        if (!response.ok) throw new Error('Failed to fetch grade data');

        const data = await response.json();
        currentReportData = { type: 'grade', subtype: type, data: data };

        displayReportResults(data, `Laporan Nilai - ${type.replace('_', ' ').toUpperCase()}`);
        hideLoading();

    } catch (error) {
        hideLoading();
        showError('Error generating grade report: ' + error.message);
    }
}

// Generate payment reports
async function generatePaymentReport(type) {
    try {
        showLoading('Generating payment report...');
        let endpoint = '/api/v1/payments';

        switch(type) {
            case 'paid':
                endpoint += '/paid';
                break;
            case 'overdue':
                endpoint += '/overdue';
                break;
            case 'summary':
                endpoint += '/summary';
                break;
        }

        const response = await fetch(endpoint, { headers: getAuthHeaders() });
        if (!response.ok) throw new Error('Failed to fetch payment data');

        const data = await response.json();
        currentReportData = { type: 'payment', subtype: type, data: data };

        displayReportResults(data, `Laporan Pembayaran - ${type.replace('_', ' ').toUpperCase()}`);
        hideLoading();

    } catch (error) {
        hideLoading();
        showError('Error generating payment report: ' + error.message);
    }
}

// Generate subject reports
async function generateSubjectReport(type) {
    try {
        showLoading('Generating subject report...');
        let endpoint = '/api/v1/subjects';

        switch(type) {
            case 'all':
                endpoint += '?page=0&size=1000';
                break;
            case 'by_type':
                endpoint += '/by-type';
                break;
            case 'teacher_distribution':
                endpoint += '/teacher-distribution';
                break;
        }

        const response = await fetch(endpoint, { headers: getAuthHeaders() });
        if (!response.ok) throw new Error('Failed to fetch subject data');

        const data = await response.json();
        currentReportData = { type: 'subject', subtype: type, data: data };

        displayReportResults(data, `Laporan Mata Pelajaran - ${type.replace('_', ' ').toUpperCase()}`);
        hideLoading();

    } catch (error) {
        hideLoading();
        showError('Error generating subject report: ' + error.message);
    }
}

// Generate classroom reports
async function generateClassroomReport(type) {
    try {
        showLoading('Generating classroom report...');
        let endpoint = '/api/v1/classrooms';

        switch(type) {
            case 'all':
                endpoint += '?page=0&size=1000';
                break;
            case 'utilization':
                endpoint += '/utilization';
                break;
            case 'capacity':
                endpoint += '/capacity';
                break;
        }

        const response = await fetch(endpoint, { headers: getAuthHeaders() });
        if (!response.ok) throw new Error('Failed to fetch classroom data');

        const data = await response.json();
        currentReportData = { type: 'classroom', subtype: type, data: data };

        displayReportResults(data, `Laporan Ruang Kelas - ${type.replace('_', ' ').toUpperCase()}`);
        hideLoading();

    } catch (error) {
        hideLoading();
        showError('Error generating classroom report: ' + error.message);
    }
}

// Generate custom report based on form
function generateCustomReport() {
    const form = document.getElementById('reportForm');
    const formData = new FormData(form);
    const reportType = formData.get('reportType');
    const startDate = formData.get('startDate');
    const endDate = formData.get('endDate');
    const format = formData.get('format');

    if (!reportType) {
        showError('Pilih tipe laporan terlebih dahulu');
        return;
    }

    // Generate report based on type
    switch(reportType) {
        case 'student':
            generateStudentReport('all');
            break;
        case 'teacher':
            generateTeacherReport('all');
            break;
        case 'attendance':
            generateAttendanceReport('summary');
            break;
        case 'grade':
            generateGradeReport('by_subject');
            break;
        case 'payment':
            generatePaymentReport('summary');
            break;
        case 'subject':
            generateSubjectReport('all');
            break;
        case 'classroom':
            generateClassroomReport('all');
            break;
        default:
            showError('Tipe laporan tidak valid');
    }
}

// Export all reports
async function exportAllReports() {
    try {
        showLoading('Exporting all data...');

        const endpoints = [
            '/api/v1/students/excel/export',
            '/api/v1/teachers/excel/export',
            '/api/v1/subjects/excel/export',
            '/api/v1/classrooms/excel/export'
        ];

        for (const endpoint of endpoints) {
            try {
                const response = await fetch(endpoint, {
                    method: 'POST',
                    headers: getAuthHeaders()
                });

                if (response.ok) {
                    const blob = await response.blob();
                    const url = window.URL.createObjectURL(blob);
                    const a = document.createElement('a');
                    a.href = url;
                    a.download = endpoint.split('/')[2] + '_export.xlsx';
                    document.body.appendChild(a);
                    a.click();
                    window.URL.revokeObjectURL(url);
                    document.body.removeChild(a);
                }
            } catch (error) {
                console.error(`Error exporting ${endpoint}:`, error);
            }
        }

        hideLoading();
        showSuccess('Export completed');

    } catch (error) {
        hideLoading();
        showError('Error exporting data: ' + error.message);
    }
}

// Show report statistics
function showReportStatistics() {
    // This would show a modal with detailed statistics
    alert('Fitur statistik detail akan segera hadir!');
}

// Display report results
function displayReportResults(data, title) {
    const resultsDiv = document.getElementById('reportResults');
    const contentDiv = document.getElementById('reportContent');

    let html = `<h4>${title}</h4>`;

    if (Array.isArray(data)) {
        html += `<p>Total records: ${data.length}</p>`;
        html += '<div class="table-responsive">';
        html += '<table class="table table-striped">';

        // Generate table headers based on first item
        if (data.length > 0) {
            html += '<thead><tr>';
            Object.keys(data[0]).forEach(key => {
                if (key !== 'password' && key !== 'passwordResetToken') {
                    html += `<th>${key.replace(/([A-Z])/g, ' $1').replace(/^./, str => str.toUpperCase())}</th>`;
                }
            });
            html += '</tr></thead>';

            // Generate table body
            html += '<tbody>';
            data.slice(0, 100).forEach(item => { // Limit to first 100 rows
                html += '<tr>';
                Object.keys(item).forEach(key => {
                    if (key !== 'password' && key !== 'passwordResetToken') {
                        const value = item[key];
                        html += `<td>${value || '-'}</td>`;
                    }
                });
                html += '</tr>';
            });
            html += '</tbody>';
        }

        html += '</table>';
        html += '</div>';

        if (data.length > 100) {
            html += `<p class="text-muted">Showing first 100 of ${data.length} records</p>`;
        }
    } else {
        html += '<pre>' + JSON.stringify(data, null, 2) + '</pre>';
    }

    contentDiv.innerHTML = html;
    resultsDiv.style.display = 'block';
    resultsDiv.scrollIntoView({ behavior: 'smooth' });
}

// Print report
function printReport() {
    if (!currentReportData) {
        showError('Tidak ada data laporan untuk dicetak');
        return;
    }

    const printWindow = window.open('', '_blank');
    printWindow.document.write(`
        <html>
        <head>
            <title>Laporan - SIM Sekolah</title>
            <style>
                body { font-family: Arial, sans-serif; margin: 20px; }
                table { width: 100%; border-collapse: collapse; margin-top: 20px; }
                th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
                th { background-color: #f2f2f2; }
                h1 { color: #333; }
                .header { text-align: center; margin-bottom: 30px; }
            </style>
        </head>
        <body>
            <div class="header">
                <h1>SIM Sekolah Management System</h1>
                <h2>Laporan ${currentReportData.type.toUpperCase()}</h2>
                <p>Tanggal: ${new Date().toLocaleDateString('id-ID')}</p>
            </div>
            ${document.getElementById('reportContent').innerHTML}
        </body>
        </html>
    `);
    printWindow.document.close();
    printWindow.print();
}

// Download report
function downloadReport() {
    if (!currentReportData) {
        showError('Tidak ada data laporan untuk didownload');
        return;
    }

    const dataStr = JSON.stringify(currentReportData.data, null, 2);
    const dataUri = 'data:application/json;charset=utf-8,'+ encodeURIComponent(dataStr);

    const exportFileDefaultName = `laporan_${currentReportData.type}_${new Date().toISOString().split('T')[0]}.json`;

    const linkElement = document.createElement('a');
    linkElement.setAttribute('href', dataUri);
    linkElement.setAttribute('download', exportFileDefaultName);
    linkElement.click();
}

// Utility functions
function getAuthHeaders() {
    const token = localStorage.getItem('token');
    return {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
    };
}

function showLoading(message = 'Loading...') {
    // Create loading overlay
    const overlay = document.createElement('div');
    overlay.id = 'loadingOverlay';
    overlay.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0,0,0,0.5);
        display: flex;
        justify-content: center;
        align-items: center;
        z-index: 9999;
    `;

    overlay.innerHTML = `
        <div class="bg-white p-4 rounded">
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
            <div class="mt-2">${message}</div>
        </div>
    `;

    document.body.appendChild(overlay);
}

function hideLoading() {
    const overlay = document.getElementById('loadingOverlay');
    if (overlay) {
        overlay.remove();
    }
}

function showError(message) {
    // Simple alert for now - could be enhanced with toast notifications
    alert('Error: ' + message);
}

function showSuccess(message) {
    // Simple alert for now - could be enhanced with toast notifications
    alert('Success: ' + message);
}