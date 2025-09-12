// Classroom Management JavaScript
document.addEventListener('DOMContentLoaded', function() {
    initializeClassroomManagement();
    loadClassrooms();
    setupEventListeners();
});

function initializeClassroomManagement() {
    console.log('Classroom management initialized');
    loadMajors();
}

function setupEventListeners() {
    // Search input
    document.getElementById('searchInput').addEventListener('input', function() {
        filterClassrooms();
    });

    // Type filter
    document.getElementById('typeFilter').addEventListener('change', function() {
        filterClassrooms();
    });

    // Status filter
    document.getElementById('statusFilter').addEventListener('change', function() {
        filterClassrooms();
    });

    // Reset filters
    document.getElementById('resetFilters').addEventListener('click', function() {
        resetFilters();
    });

    // Save classroom button
    document.getElementById('saveClassroomBtn').addEventListener('click', function() {
        saveClassroom();
    });

    // Update classroom button
    const updateBtn = document.getElementById('updateClassroomBtn');
    if (updateBtn) {
        updateBtn.addEventListener('click', function() {
            updateClassroom();
        });
    }

    // Print classroom button
    const printBtn = document.getElementById('printClassroomBtn');
    if (printBtn) {
        printBtn.addEventListener('click', function() {
            printClassroom();
        });
    }

    // Excel import button
    const importBtn = document.getElementById('importClassroomExcelBtn');
    if (importBtn) {
        importBtn.addEventListener('click', function() {
            importClassroomExcel();
        });
    }

    // Download template button
    const templateBtn = document.getElementById('downloadClassroomTemplateBtn');
    if (templateBtn) {
        templateBtn.addEventListener('click', function(e) {
            e.preventDefault();
            downloadClassroomTemplate();
        });
    }

    // Export Excel button
    const exportBtn = document.getElementById('exportClassroomExcelBtn');
    if (exportBtn) {
        exportBtn.addEventListener('click', function(e) {
            e.preventDefault();
            exportClassroomExcel();
        });
    }
}

function loadMajors() {
    fetch('/api/v1/majors', {
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(data => {
        populateMajorDropdowns(data.content || []);
    })
    .catch(error => {
        console.error('Error loading majors:', error);
        showError('Gagal memuat data jurusan');
    });
}

function populateMajorDropdowns(majors) {
    const majorSelect = document.getElementById('majorId');
    const editMajorSelect = document.getElementById('editMajorId');

    // Clear existing options except the first one
    majorSelect.innerHTML = '<option value="">Pilih Jurusan</option>';
    editMajorSelect.innerHTML = '<option value="">Pilih Jurusan</option>';

    majors.forEach(major => {
        const option = document.createElement('option');
        option.value = major.id;
        option.textContent = `${major.code} - ${major.name}`;

        majorSelect.appendChild(option.cloneNode(true));
        editMajorSelect.appendChild(option);
    });
}

function loadClassrooms() {
    fetch('/api/v1/classrooms', {
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(data => {
        displayClassrooms(data.content || []);
    })
    .catch(error => {
        console.error('Error loading classrooms:', error);
        showError('Gagal memuat data ruang kelas');
    });
}

function displayClassrooms(classrooms) {
    const tbody = document.getElementById('classroomsTableBody');

    if (classrooms.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="6" class="text-center">
                    <div class="alert alert-info">
                        <i class="fas fa-info-circle"></i> Tidak ada data ruang kelas ditemukan
                    </div>
                </td>
            </tr>
        `;
        return;
    }

    tbody.innerHTML = classrooms.map(classroom => `
        <tr>
            <td>${classroom.name || '-'}</td>
            <td>${classroom.capacity || '-'}</td>
            <td>
                <span class="badge bg-primary">
                    Kelas ${classroom.grade || '-'}
                </span>
            </td>
            <td>${classroom.major ? `${classroom.major.code} - ${classroom.major.name}` : '-'}</td>
            <td>
                <span class="badge ${classroom.isActive ? 'bg-success' : 'bg-secondary'}">
                    ${classroom.isActive ? 'Aktif' : 'Tidak Aktif'}
                </span>
            </td>
            <td>
                <div class="btn-group" role="group">
                    <button class="btn btn-sm btn-outline-primary" onclick="viewClassroom(${classroom.id})" title="Lihat Detail">
                        <i class="fas fa-eye"></i>
                    </button>
                    <button class="btn btn-sm btn-outline-warning" onclick="editClassroom(${classroom.id})" title="Edit">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="btn btn-sm btn-outline-info" onclick="printClassroomDetail(${classroom.id})" title="Print Detail">
                        <i class="fas fa-print"></i>
                    </button>
                    <button class="btn btn-sm btn-outline-danger" onclick="deleteClassroom(${classroom.id})" title="Hapus">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            </td>
        </tr>
    `).join('');
}

function getRoomTypeBadge(type) {
    switch (type) {
        case 'CLASSROOM': return 'bg-primary';
        case 'LABORATORY': return 'bg-warning';
        case 'AUDITORIUM': return 'bg-info';
        case 'LIBRARY': return 'bg-success';
        case 'OFFICE': return 'bg-secondary';
        default: return 'bg-secondary';
    }
}

function getRoomTypeText(type) {
    switch (type) {
        case 'CLASSROOM': return 'Ruang Kelas';
        case 'LABORATORY': return 'Laboratorium';
        case 'AUDITORIUM': return 'Auditorium';
        case 'LIBRARY': return 'Perpustakaan';
        case 'OFFICE': return 'Kantor';
        default: return '-';
    }
}

function filterClassrooms() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();
    const typeFilter = document.getElementById('typeFilter').value;
    const statusFilter = document.getElementById('statusFilter').value;

    const rows = document.querySelectorAll('#classroomsTableBody tr');

    rows.forEach(row => {
        const cells = row.querySelectorAll('td');
        if (cells.length < 6) return; // Skip if not a data row

        const name = cells[0].textContent.toLowerCase();
        const type = cells[2].querySelector('.badge')?.textContent.toLowerCase() || '';
        const status = cells[4].querySelector('.badge')?.textContent.toLowerCase() || '';

        const matchesSearch = !searchTerm || name.includes(searchTerm);
        const matchesType = !typeFilter || type.includes(typeFilter.toLowerCase());
        const matchesStatus = !statusFilter || status.includes(statusFilter === 'true' ? 'aktif' : 'tidak aktif');

        row.style.display = matchesSearch && matchesType && matchesStatus ? '' : 'none';
    });
}

function resetFilters() {
    document.getElementById('searchInput').value = '';
    document.getElementById('typeFilter').value = '';
    document.getElementById('statusFilter').value = '';
    filterClassrooms();
}

function saveClassroom() {
    const form = document.getElementById('addClassroomForm');
    const formData = new FormData(form);

    const classroomData = {
        name: formData.get('name'),
        grade: parseInt(formData.get('grade')),
        capacity: parseInt(formData.get('capacity')),
        classCode: formData.get('classCode'),
        academicYear: formData.get('academicYear'),
        isActive: true,
        currentEnrollment: 0,
        major: {
            id: parseInt(formData.get('majorId'))
        }
    };

    fetch('/api/v1/classrooms', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(classroomData)
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            return response.json().then(error => {
                throw new Error(error.message || 'Failed to save classroom');
            });
        }
    })
    .then(data => {
        if (data.id) {
            showSuccess('Ruang kelas berhasil ditambahkan');
            loadClassrooms();
            bootstrap.Modal.getInstance(document.getElementById('addClassroomModal')).hide();
            form.reset();
        } else {
            showError('Gagal menambahkan ruang kelas');
        }
    })
    .catch(error => {
        console.error('Error saving classroom:', error);
        showError('Gagal menambahkan ruang kelas: ' + error.message);
    });
}

function viewClassroom(classroomId) {
    fetch(`/api/v1/classrooms/${classroomId}`, {
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(classroom => {
        displayClassroomDetails(classroom);
        const modal = new bootstrap.Modal(document.getElementById('viewClassroomModal'));
        modal.show();
    })
    .catch(error => {
        console.error('Error loading classroom details:', error);
        showError('Gagal memuat detail ruang kelas');
    });
}

function editClassroom(classroomId) {
    fetch(`/api/v1/classrooms/${classroomId}`, {
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(classroom => {
        populateEditForm(classroom);
        const modal = new bootstrap.Modal(document.getElementById('editClassroomModal'));
        modal.show();
    })
    .catch(error => {
        console.error('Error loading classroom for edit:', error);
        showError('Gagal memuat data ruang kelas untuk diedit');
    });
}

function displayClassroomDetails(classroom) {
    const detailsContainer = document.getElementById('classroomDetails');
    detailsContainer.innerHTML = `
        <div class="row">
            <div class="col-md-4 text-center mb-3">
                <div class="bg-light p-4 rounded">
                    <i class="fas fa-building fa-5x text-primary mb-3"></i>
                    <h5>${classroom.name}</h5>
                    <p class="text-muted">Kelas ${classroom.grade}</p>
                </div>
            </div>
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header">
                        <h6 class="mb-0">Informasi Ruang Kelas</h6>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6">
                                <p><strong>Nama Ruang:</strong> ${classroom.name}</p>
                                <p><strong>Tingkat:</strong> Kelas ${classroom.grade}</p>
                                <p><strong>Kapasitas:</strong> ${classroom.capacity || '-'} orang</p>
                                <p><strong>Kode Kelas:</strong> ${classroom.classCode || '-'}</p>
                                <p><strong>Jurusan:</strong> ${classroom.major ? `${classroom.major.code} - ${classroom.major.name}` : '-'}</p>
                            </div>
                            <div class="col-md-6">
                                <p><strong>Tahun Akademik:</strong> ${classroom.academicYear || '-'}</p>
                                <p><strong>Siswa Terdaftar:</strong> ${classroom.currentEnrollment || 0}</p>
                                <p><strong>Status:</strong>
                                    <span class="badge ${classroom.isActive ? 'bg-success' : 'bg-secondary'}">
                                        ${classroom.isActive ? 'Aktif' : 'Tidak Aktif'}
                                    </span>
                                </p>
                                <p><strong>Dibuat:</strong> ${classroom.createdAt ? new Date(classroom.createdAt).toLocaleDateString('id-ID') : '-'}</p>
                                <p><strong>Diupdate:</strong> ${classroom.updatedAt ? new Date(classroom.updatedAt).toLocaleDateString('id-ID') : '-'}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `;
}

function populateEditForm(classroom) {
    document.getElementById('editClassroomId').value = classroom.id;
    document.getElementById('editRoomName').value = classroom.name || '';
    document.getElementById('editGrade').value = classroom.grade || '';
    document.getElementById('editCapacity').value = classroom.capacity || '';
    document.getElementById('editMajorId').value = classroom.major ? classroom.major.id : '';
    document.getElementById('editClassCode').value = classroom.classCode || '';
    document.getElementById('editAcademicYear').value = classroom.academicYear || '';
    document.getElementById('editStatus').value = classroom.isActive ? 'true' : 'false';
}

function updateClassroom() {
    const form = document.getElementById('editClassroomForm');
    const formData = new FormData(form);
    const classroomId = document.getElementById('editClassroomId').value;

    const classroomData = {
        name: formData.get('name'),
        grade: parseInt(formData.get('grade')),
        capacity: parseInt(formData.get('capacity')),
        classCode: formData.get('classCode'),
        academicYear: formData.get('academicYear'),
        isActive: formData.get('isActive') === 'true',
        major: {
            id: parseInt(formData.get('majorId'))
        }
    };

    fetch(`/api/v1/classrooms/${classroomId}`, {
        method: 'PUT',
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(classroomData)
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            return response.json().then(error => {
                throw new Error(error.message || 'Failed to update classroom');
            });
        }
    })
    .then(data => {
        if (data.id) {
            showSuccess('Ruang kelas berhasil diperbarui');
            loadClassrooms();
            bootstrap.Modal.getInstance(document.getElementById('editClassroomModal')).hide();
        } else {
            showError('Gagal memperbarui ruang kelas');
        }
    })
    .catch(error => {
        console.error('Error updating classroom:', error);
        showError('Gagal memperbarui ruang kelas: ' + error.message);
    });
}

function deleteClassroom(classroomId) {
    if (confirm('Apakah Anda yakin ingin menghapus ruang kelas ini?')) {
        fetch(`/api/v1/classrooms/${classroomId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${getToken()}`,
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (response.ok) {
                showSuccess('Ruang kelas berhasil dihapus');
                loadClassrooms();
            } else {
                showError('Gagal menghapus ruang kelas');
            }
        })
        .catch(error => {
            console.error('Error deleting classroom:', error);
            showError('Gagal menghapus ruang kelas');
        });
    }
}

function printClassroomDetail(classroomId) {
    // First get classroom details
    fetch(`/api/v1/classrooms/${classroomId}`, {
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(classroom => {
        generateClassroomPrint(classroom);
    })
    .catch(error => {
        console.error('Error loading classroom for print:', error);
        showError('Gagal memuat data ruang kelas untuk print');
    });
}

function printClassroom() {
    // Get classroom details from the modal
    const classroomDetails = document.getElementById('classroomDetails');
    const classroomName = classroomDetails.querySelector('h5')?.textContent || 'Ruang Kelas';
    const classroomType = classroomDetails.querySelector('.text-muted')?.textContent || '';

    // Create a simple print version
    const printWindow = window.open('', '_blank');
    const printContent = `
        <!DOCTYPE html>
        <html lang="id">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Detail Ruang Kelas - ${classroomName}</title>
            <style>
                body { font-family: Arial, sans-serif; margin: 20px; }
                .header { text-align: center; border-bottom: 2px solid #333; padding-bottom: 20px; margin-bottom: 30px; }
                .school-name { font-size: 24px; font-weight: bold; margin-bottom: 10px; }
                .classroom-title { font-size: 18px; margin-bottom: 20px; }
                .info-section { margin-bottom: 20px; }
                .info-row { display: flex; margin-bottom: 8px; }
                .info-label { width: 200px; font-weight: bold; }
                .info-value { flex: 1; }
                @media print { body { margin: 0; } }
            </style>
        </head>
        <body>
            <div class="header">
                <div class="school-name">SMK NEGERI 1 JAKARTA</div>
                <div class="classroom-title">DETAIL RUANG KELAS</div>
            </div>

            <div class="info-section">
                <div class="info-row">
                    <div class="info-label">Nama Ruang:</div>
                    <div class="info-value">${classroomName}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Tipe Ruang:</div>
                    <div class="info-value">${classroomType}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Tanggal Dicetak:</div>
                    <div class="info-value">${new Date().toLocaleDateString('id-ID')}</div>
                </div>
            </div>
        </body>
        </html>
    `;

    printWindow.document.write(printContent);
    printWindow.document.close();
    printWindow.print();
}

function generateClassroomPrint(classroom) {
    const printWindow = window.open('', '_blank');
    const printContent = `
        <!DOCTYPE html>
        <html lang="id">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Detail Ruang Kelas - ${classroom.roomName || classroom.name}</title>
            <style>
                body { font-family: Arial, sans-serif; margin: 20px; }
                .header { text-align: center; border-bottom: 2px solid #333; padding-bottom: 20px; margin-bottom: 30px; }
                .school-name { font-size: 24px; font-weight: bold; margin-bottom: 10px; }
                .classroom-title { font-size: 18px; margin-bottom: 20px; }
                .info-section { margin-bottom: 20px; }
                .info-row { display: flex; margin-bottom: 8px; }
                .info-label { width: 200px; font-weight: bold; }
                .info-value { flex: 1; }
                @media print { body { margin: 0; } }
            </style>
        </head>
        <body>
            <div class="header">
                <div class="school-name">SMK NEGERI 1 JAKARTA</div>
                <div class="classroom-title">DETAIL RUANG KELAS</div>
            </div>

            <div class="info-section">
                <div class="info-row">
                    <div class="info-label">Nama Ruang:</div>
                    <div class="info-value">${classroom.roomName || classroom.name}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Kapasitas:</div>
                    <div class="info-value">${classroom.capacity || '-'} orang</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Tipe Ruang:</div>
                    <div class="info-value">${getRoomTypeText(classroom.roomType)}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Lokasi:</div>
                    <div class="info-value">${classroom.location || '-'}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Status:</div>
                    <div class="info-value">${classroom.isActive ? 'Aktif' : 'Tidak Aktif'}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Fasilitas:</div>
                    <div class="info-value">${classroom.facilities || 'Tidak ada informasi fasilitas'}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Tanggal Dicetak:</div>
                    <div class="info-value">${new Date().toLocaleDateString('id-ID')}</div>
                </div>
            </div>
        </body>
        </html>
    `;

    printWindow.document.write(printContent);
    printWindow.document.close();
    printWindow.print();
}

function downloadClassroomTemplate() {
    fetch('/api/v1/classrooms/excel/template', {
        headers: {
            'Authorization': `Bearer ${getToken()}`
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Failed to download template');
        }
        return response.blob();
    })
    .then(blob => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'classroom_import_template.xlsx';
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
        document.body.removeChild(a);
        showSuccess('Template berhasil diunduh');
    })
    .catch(error => {
        console.error('Error downloading template:', error);
        showError('Gagal mengunduh template');
    });
}

function exportClassroomExcel() {
    fetch('/api/v1/classrooms/excel/export', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Failed to export data');
        }
        return response.blob();
    })
    .then(blob => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'classrooms_export.xlsx';
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
        document.body.removeChild(a);
        showSuccess('Data berhasil diekspor');
    })
    .catch(error => {
        console.error('Error exporting data:', error);
        showError('Gagal mengekspor data');
    });
}

function importClassroomExcel() {
    const fileInput = document.getElementById('classroomExcelFile');
    const file = fileInput.files[0];

    if (!file) {
        showError('Pilih file Excel terlebih dahulu');
        return;
    }

    const formData = new FormData();
    formData.append('file', file);

    // Show progress
    document.getElementById('classroomImportProgress').classList.remove('d-none');
    document.getElementById('classroomImportResults').classList.add('d-none');

    fetch('/api/v1/classrooms/excel/import', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${getToken()}`
        },
        body: formData
    })
    .then(response => response.json())
    .then(result => {
        document.getElementById('classroomImportProgress').classList.add('d-none');
        document.getElementById('classroomImportResults').classList.remove('d-none');

        if (result.successfulImports > 0) {
            document.getElementById('classroomImportSuccessMessage').innerHTML = `
                Berhasil mengimport ${result.successfulImports} dari ${result.totalRows} data ruang kelas.
                ${result.failedImports > 0 ? `<br>Gagal: ${result.failedImports} data` : ''}
            `;
            loadClassrooms(); // Reload the table
            bootstrap.Modal.getInstance(document.getElementById('importClassroomExcelModal')).hide();
            document.getElementById('importClassroomExcelForm').reset();
        } else {
            showError('Import gagal');
        }
    })
    .catch(error => {
        console.error('Error importing Excel:', error);
        document.getElementById('classroomImportProgress').classList.add('d-none');
        showError('Gagal mengimport data');
    });
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