// Teacher Management JavaScript
document.addEventListener('DOMContentLoaded', function() {
    initializeTeacherManagement();
    loadTeachers();
    loadSubjects();
    setupEventListeners();
});

function initializeTeacherManagement() {
    console.log('Teacher management initialized');
}

function setupEventListeners() {
    // Search input
    document.getElementById('searchInput').addEventListener('input', function() {
        filterTeachers();
    });

    // Subject filter
    document.getElementById('subjectFilter').addEventListener('change', function() {
        filterTeachers();
    });

    // Status filter
    document.getElementById('statusFilter').addEventListener('change', function() {
        filterTeachers();
    });

    // Reset filters
    document.getElementById('resetFilters').addEventListener('click', function() {
        resetFilters();
    });

    // Save teacher button
    document.getElementById('saveTeacherBtn').addEventListener('click', function() {
        saveTeacher();
    });

    // Update teacher button
    const updateBtn = document.getElementById('updateTeacherBtn');
    if (updateBtn) {
        updateBtn.addEventListener('click', function() {
            updateTeacher();
        });
    }

    // Print biodata button
    const printBtn = document.getElementById('printTeacherBiodataBtn');
    if (printBtn) {
        printBtn.addEventListener('click', function() {
            printTeacherBiodata();
        });
    }

    // Excel import button
    const importBtn = document.getElementById('importTeacherExcelBtn');
    if (importBtn) {
        importBtn.addEventListener('click', function() {
            importTeacherExcel();
        });
    }

    // Download template button
    const templateBtn = document.getElementById('downloadTeacherTemplateBtn');
    if (templateBtn) {
        templateBtn.addEventListener('click', function(e) {
            e.preventDefault();
            downloadTeacherTemplate();
        });
    }

    // Export Excel button
    const exportBtn = document.getElementById('exportTeacherExcelBtn');
    if (exportBtn) {
        exportBtn.addEventListener('click', function(e) {
            e.preventDefault();
            exportTeacherExcel();
        });
    }
}

function loadTeachers() {
    fetch('/api/v1/teachers', {
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(data => {
        displayTeachers(data.content || []);
    })
    .catch(error => {
        console.error('Error loading teachers:', error);
        showError('Gagal memuat data guru');
    });
}

function loadSubjects() {
    fetch('/api/v1/subjects', {
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(data => {
        const subjects = data.content || [];
        populateSubjectSelects(subjects);
    })
    .catch(error => {
        console.error('Error loading subjects:', error);
        // Use mock data if API fails
        const mockSubjects = [
            { id: 1, subjectName: 'Matematika' },
            { id: 2, subjectName: 'Bahasa Indonesia' },
            { id: 3, subjectName: 'Bahasa Inggris' },
            { id: 4, subjectName: 'Fisika' },
            { id: 5, subjectName: 'Kimia' }
        ];
        populateSubjectSelects(mockSubjects);
    });
}

function populateSubjectSelects(subjects) {
    const subjectFilter = document.getElementById('subjectFilter');
    const subjectsSelect = document.getElementById('subjects');
    const editSubjectsSelect = document.getElementById('editSubjects');

    // Clear existing options
    subjectFilter.innerHTML = '<option value="">Semua Mata Pelajaran</option>';
    subjectsSelect.innerHTML = '';
    if (editSubjectsSelect) editSubjectsSelect.innerHTML = '';

    subjects.forEach(subject => {
        // Subject filter
        const filterOption = document.createElement('option');
        filterOption.value = subject.id;
        filterOption.textContent = subject.subjectName || subject.name;
        subjectFilter.appendChild(filterOption);

        // Add teacher form
        const addOption = document.createElement('option');
        addOption.value = subject.id;
        addOption.textContent = subject.subjectName || subject.name;
        subjectsSelect.appendChild(addOption);

        // Edit teacher form
        if (editSubjectsSelect) {
            const editOption = document.createElement('option');
            editOption.value = subject.id;
            editOption.textContent = subject.subjectName || subject.name;
            editSubjectsSelect.appendChild(editOption);
        }
    });
}

function displayTeachers(teachers) {
    const tbody = document.getElementById('teachersTableBody');

    if (teachers.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="7" class="text-center">
                    <div class="alert alert-info">
                        <i class="fas fa-info-circle"></i> Tidak ada data guru ditemukan
                    </div>
                </td>
            </tr>
        `;
        return;
    }

    tbody.innerHTML = teachers.map(teacher => `
        <tr>
            <td>${teacher.nip || '-'}</td>
            <td>${teacher.namaLengkap || teacher.fullName || '-'}</td>
            <td>${teacher.mataPelajaran || teacher.subjects || '-'}</td>
            <td>${teacher.email || '-'}</td>
            <td>${teacher.noHp || teacher.phone || '-'}</td>
            <td>
                <span class="badge ${teacher.status === 'ACTIVE' ? 'bg-success' : 'bg-secondary'}">
                    ${teacher.status === 'ACTIVE' ? 'Aktif' : 'Tidak Aktif'}
                </span>
            </td>
            <td>
                <div class="btn-group" role="group">
                    <button class="btn btn-sm btn-outline-primary" onclick="viewTeacher(${teacher.id})" title="Lihat Detail">
                        <i class="fas fa-eye"></i>
                    </button>
                    <button class="btn btn-sm btn-outline-warning" onclick="editTeacher(${teacher.id})" title="Edit">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="btn btn-sm btn-outline-info" onclick="printTeacherBiodata(${teacher.id})" title="Print Biodata">
                        <i class="fas fa-print"></i>
                    </button>
                    <button class="btn btn-sm btn-outline-danger" onclick="deleteTeacher(${teacher.id})" title="Hapus">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            </td>
        </tr>
    `).join('');
}

function filterTeachers() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();
    const subjectFilter = document.getElementById('subjectFilter').value;
    const statusFilter = document.getElementById('statusFilter').value;

    const rows = document.querySelectorAll('#teachersTableBody tr');

    rows.forEach(row => {
        const cells = row.querySelectorAll('td');
        if (cells.length < 7) return; // Skip if not a data row

        const nip = cells[0].textContent.toLowerCase();
        const name = cells[1].textContent.toLowerCase();
        const subjects = cells[2].textContent.toLowerCase();
        const email = cells[3].textContent.toLowerCase();
        const status = cells[5].querySelector('.badge')?.textContent.toLowerCase() || '';

        const matchesSearch = !searchTerm || nip.includes(searchTerm) || name.includes(searchTerm) || email.includes(searchTerm);
        const matchesSubject = !subjectFilter || subjects.includes(subjectFilter);
        const matchesStatus = !statusFilter || status.includes(statusFilter === 'active' ? 'aktif' : 'tidak aktif');

        row.style.display = matchesSearch && matchesSubject && matchesStatus ? '' : 'none';
    });
}

function resetFilters() {
    document.getElementById('searchInput').value = '';
    document.getElementById('subjectFilter').value = '';
    document.getElementById('statusFilter').value = '';
    filterTeachers();
}

function saveTeacher() {
    const form = document.getElementById('addTeacherForm');
    const formData = new FormData(form);

    const selectedSubjects = Array.from(document.getElementById('subjects').selectedOptions).map(option => parseInt(option.value));

    const teacherData = {
        nip: formData.get('nip'),
        fullName: formData.get('fullName'),
        email: formData.get('email'),
        phone: formData.get('phone'),
        address: formData.get('address'),
        subjectIds: selectedSubjects,
        status: formData.get('status')
    };

    fetch('/api/v1/teachers', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(teacherData)
    })
    .then(response => response.json())
    .then(data => {
        if (data.id) {
            showSuccess('Guru berhasil ditambahkan');
            loadTeachers();
            bootstrap.Modal.getInstance(document.getElementById('addTeacherModal')).hide();
            form.reset();
        } else {
            showError('Gagal menambahkan guru');
        }
    })
    .catch(error => {
        console.error('Error saving teacher:', error);
        showError('Gagal menambahkan guru');
    });
}

function viewTeacher(teacherId) {
    fetch(`/api/v1/teachers/${teacherId}`, {
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(teacher => {
        displayTeacherDetails(teacher);
        const modal = new bootstrap.Modal(document.getElementById('viewTeacherModal'));
        modal.show();
    })
    .catch(error => {
        console.error('Error loading teacher details:', error);
        showError('Gagal memuat detail guru');
    });
}

function editTeacher(teacherId) {
    fetch(`/api/v1/teachers/${teacherId}`, {
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(teacher => {
        populateEditForm(teacher);
        const modal = new bootstrap.Modal(document.getElementById('editTeacherModal'));
        modal.show();
    })
    .catch(error => {
        console.error('Error loading teacher for edit:', error);
        showError('Gagal memuat data guru untuk diedit');
    });
}

function displayTeacherDetails(teacher) {
    const detailsContainer = document.getElementById('teacherDetails');
    detailsContainer.innerHTML = `
        <div class="row">
            <div class="col-md-4 text-center mb-3">
                <div class="bg-light p-4 rounded">
                    <i class="fas fa-user-tie fa-5x text-primary mb-3"></i>
                    <h5>${teacher.namaLengkap || teacher.fullName}</h5>
                    <p class="text-muted">NIP: ${teacher.nip || '-'}</p>
                </div>
            </div>
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header">
                        <h6 class="mb-0">Informasi Pribadi</h6>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6">
                                <p><strong>Nama Lengkap:</strong> ${teacher.namaLengkap || teacher.fullName}</p>
                                <p><strong>NIP:</strong> ${teacher.nip || '-'}</p>
                                <p><strong>Email:</strong> ${teacher.email || '-'}</p>
                                <p><strong>No. HP:</strong> ${teacher.noHp || teacher.phone || '-'}</p>
                            </div>
                            <div class="col-md-6">
                                <p><strong>Status:</strong>
                                    <span class="badge ${teacher.status === 'ACTIVE' ? 'bg-success' : 'bg-secondary'}">
                                        ${teacher.status === 'ACTIVE' ? 'Aktif' : 'Tidak Aktif'}
                                    </span>
                                </p>
                                <p><strong>Alamat:</strong> ${teacher.alamat || teacher.address || '-'}</p>
                            </div>
                        </div>
                        <hr>
                        <div class="row">
                            <div class="col-md-12">
                                <h6>Mata Pelajaran yang Diajarkan</h6>
                                <p>${teacher.mataPelajaran || teacher.subjects || 'Belum ada mata pelajaran yang ditentukan'}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `;
}

function populateEditForm(teacher) {
    document.getElementById('editTeacherId').value = teacher.id;
    document.getElementById('editNip').value = teacher.nip || '';
    document.getElementById('editFullName').value = teacher.namaLengkap || teacher.fullName || '';
    document.getElementById('editEmail').value = teacher.email || '';
    document.getElementById('editPhone').value = teacher.noHp || teacher.phone || '';
    document.getElementById('editAddress').value = teacher.alamat || teacher.address || '';
    document.getElementById('editStatus').value = teacher.status || 'ACTIVE';

    // Handle subjects selection
    const editSubjectsSelect = document.getElementById('editSubjects');
    if (editSubjectsSelect && teacher.subjectIds) {
        Array.from(editSubjectsSelect.options).forEach(option => {
            option.selected = teacher.subjectIds.includes(parseInt(option.value));
        });
    }
}

function updateTeacher() {
    const form = document.getElementById('editTeacherForm');
    const formData = new FormData(form);
    const teacherId = document.getElementById('editTeacherId').value;

    const selectedSubjects = Array.from(document.getElementById('editSubjects').selectedOptions).map(option => parseInt(option.value));

    const teacherData = {
        nip: formData.get('nip'),
        fullName: formData.get('fullName'),
        email: formData.get('email'),
        phone: formData.get('phone'),
        address: formData.get('address'),
        subjectIds: selectedSubjects,
        status: formData.get('status')
    };

    fetch(`/api/v1/teachers/${teacherId}`, {
        method: 'PUT',
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(teacherData)
    })
    .then(response => response.json())
    .then(data => {
        if (data.id) {
            showSuccess('Guru berhasil diperbarui');
            loadTeachers();
            bootstrap.Modal.getInstance(document.getElementById('editTeacherModal')).hide();
        } else {
            showError('Gagal memperbarui guru');
        }
    })
    .catch(error => {
        console.error('Error updating teacher:', error);
        showError('Gagal memperbarui guru');
    });
}

function deleteTeacher(teacherId) {
    if (confirm('Apakah Anda yakin ingin menghapus guru ini?')) {
        fetch(`/api/v1/teachers/${teacherId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${getToken()}`,
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (response.ok) {
                showSuccess('Guru berhasil dihapus');
                loadTeachers();
            } else {
                showError('Gagal menghapus guru');
            }
        })
        .catch(error => {
            console.error('Error deleting teacher:', error);
            showError('Gagal menghapus guru');
        });
    }
}

function printTeacherBiodata(teacherId) {
    // First get teacher details
    fetch(`/api/v1/teachers/${teacherId}`, {
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(teacher => {
        generateTeacherBiodataPrint(teacher);
    })
    .catch(error => {
        console.error('Error loading teacher for print:', error);
        showError('Gagal memuat data guru untuk print');
    });
}

function generateTeacherBiodataPrint(teacher) {
    const printWindow = window.open('', '_blank');
    const biodataHTML = `
        <!DOCTYPE html>
        <html lang="id">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Biodata Guru - ${teacher.namaLengkap || teacher.fullName}</title>
            <style>
                body { font-family: Arial, sans-serif; margin: 20px; }
                .header { text-align: center; border-bottom: 2px solid #333; padding-bottom: 20px; margin-bottom: 30px; }
                .school-name { font-size: 24px; font-weight: bold; margin-bottom: 10px; }
                .biodata-title { font-size: 18px; margin-bottom: 20px; }
                .photo-section { float: right; margin-left: 20px; margin-bottom: 20px; }
                .photo-placeholder { width: 120px; height: 160px; border: 1px solid #333; display: flex; align-items: center; justify-content: center; background: #f8f9fa; }
                .info-section { margin-bottom: 20px; }
                .info-row { display: flex; margin-bottom: 8px; }
                .info-label { width: 200px; font-weight: bold; }
                .info-value { flex: 1; }
                .signature-section { margin-top: 50px; text-align: center; }
                .signature-line { border-top: 1px solid #333; width: 200px; margin: 0 auto; margin-top: 50px; }
                @media print { body { margin: 0; } }
            </style>
        </head>
        <body>
            <div class="header">
                <div class="school-name">SMK NEGERI 1 JAKARTA</div>
                <div class="biodata-title">BIODATA GURU</div>
            </div>

            <div class="photo-section">
                <div class="photo-placeholder">
                    <span>FOTO<br>3x4</span>
                </div>
            </div>

            <div class="info-section">
                <div class="info-row">
                    <div class="info-label">Nama Lengkap:</div>
                    <div class="info-value">${teacher.namaLengkap || teacher.fullName}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">NIP:</div>
                    <div class="info-value">${teacher.nip || '-'}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Email:</div>
                    <div class="info-value">${teacher.email || '-'}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">No. HP:</div>
                    <div class="info-value">${teacher.noHp || teacher.phone || '-'}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Alamat:</div>
                    <div class="info-value">${teacher.alamat || teacher.address || '-'}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Mata Pelajaran:</div>
                    <div class="info-value">${teacher.mataPelajaran || teacher.subjects || '-'}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Status:</div>
                    <div class="info-value">${teacher.status === 'ACTIVE' ? 'Aktif' : 'Tidak Aktif'}</div>
                </div>
            </div>

            <div class="signature-section">
                <p>Jakarta, ${new Date().toLocaleDateString('id-ID')}</p>
                <br><br><br>
                <div class="signature-line"></div>
                <p>Guru</p>
            </div>
        </body>
        </html>
    `;

    printWindow.document.write(biodataHTML);
    printWindow.document.close();
    printWindow.print();
}

function downloadTeacherTemplate() {
    fetch('/api/v1/teachers/excel/template', {
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
        a.download = 'teacher_import_template.xlsx';
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

function exportTeacherExcel() {
    fetch('/api/v1/teachers/excel/export', {
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
        a.download = 'teachers_export.xlsx';
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

function importTeacherExcel() {
    const fileInput = document.getElementById('teacherExcelFile');
    const file = fileInput.files[0];

    if (!file) {
        showError('Pilih file Excel terlebih dahulu');
        return;
    }

    const formData = new FormData();
    formData.append('file', file);

    // Show progress
    document.getElementById('teacherImportProgress').classList.remove('d-none');
    document.getElementById('teacherImportResults').classList.add('d-none');

    fetch('/api/v1/teachers/excel/import', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${getToken()}`
        },
        body: formData
    })
    .then(response => response.json())
    .then(result => {
        document.getElementById('teacherImportProgress').classList.add('d-none');
        document.getElementById('teacherImportResults').classList.remove('d-none');

        if (result.successfulImports > 0) {
            document.getElementById('teacherImportSuccessMessage').innerHTML = `
                Berhasil mengimport ${result.successfulImports} dari ${result.totalRows} data guru.
                ${result.failedImports > 0 ? `<br>Gagal: ${result.failedImports} data` : ''}
            `;
            loadTeachers(); // Reload the table
            bootstrap.Modal.getInstance(document.getElementById('importTeacherExcelModal')).hide();
            document.getElementById('importTeacherExcelForm').reset();
        } else {
            showError('Import gagal');
        }
    })
    .catch(error => {
        console.error('Error importing Excel:', error);
        document.getElementById('teacherImportProgress').classList.add('d-none');
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