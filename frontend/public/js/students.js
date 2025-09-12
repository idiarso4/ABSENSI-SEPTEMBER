// Student Management JavaScript
document.addEventListener('DOMContentLoaded', function() {
    initializeStudentManagement();
    loadStudents();
    loadClasses();
    setupEventListeners();
});

function initializeStudentManagement() {
    console.log('Student management initialized');
}

function setupEventListeners() {
    // Search input
    document.getElementById('searchInput').addEventListener('input', function() {
        filterStudents();
    });

    // Class filter
    document.getElementById('classFilter').addEventListener('change', function() {
        filterStudents();
    });

    // Status filter
    document.getElementById('statusFilter').addEventListener('change', function() {
        filterStudents();
    });

    // Reset filters
    document.getElementById('resetFilters').addEventListener('click', function() {
        resetFilters();
    });

    // Save student button
    document.getElementById('saveStudentBtn').addEventListener('click', function() {
        saveStudent();
    });

    // Update student button
    const updateBtn = document.getElementById('updateStudentBtn');
    if (updateBtn) {
        updateBtn.addEventListener('click', function() {
            updateStudent();
        });
    }

    // Print biodata button
    const printBtn = document.getElementById('printBiodataBtn');
    if (printBtn) {
        printBtn.addEventListener('click', function() {
            printBiodata();
        });
    }

    // Excel import button
    const importBtn = document.getElementById('importExcelBtn');
    if (importBtn) {
        importBtn.addEventListener('click', function() {
            importExcel();
        });
    }

    // Download template button
    const templateBtn = document.getElementById('downloadTemplateBtn');
    if (templateBtn) {
        templateBtn.addEventListener('click', function(e) {
            e.preventDefault();
            downloadTemplate();
        });
    }

    // Export Excel button
    const exportBtn = document.getElementById('exportExcelBtn');
    if (exportBtn) {
        exportBtn.addEventListener('click', function(e) {
            e.preventDefault();
            exportExcel();
        });
    }
}

function loadStudents() {
    fetch('/api/v1/students', {
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(data => {
        displayStudents(data.content || []);
    })
    .catch(error => {
        console.error('Error loading students:', error);
        showError('Gagal memuat data siswa');
    });
}

function loadClasses() {
    // For now, we'll use mock data since the class endpoint might not be implemented yet
    const mockClasses = [
        { id: 1, name: 'X SIJA 1' },
        { id: 2, name: 'X SIJA 2' },
        { id: 3, name: 'XI SIJA 1' },
        { id: 4, name: 'XI SIJA 2' },
        { id: 5, name: 'XII SIJA 1' },
        { id: 6, name: 'XII SIJA 2' }
    ];

    const classSelect = document.getElementById('classId');
    mockClasses.forEach(cls => {
        const option = document.createElement('option');
        option.value = cls.id;
        option.textContent = cls.name;
        classSelect.appendChild(option);
    });
}

function displayStudents(students) {
    const tbody = document.getElementById('studentsTableBody');

    if (students.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="6" class="text-center">
                    <div class="alert alert-info">
                        <i class="fas fa-info-circle"></i> Tidak ada data siswa ditemukan
                    </div>
                </td>
            </tr>
        `;
        return;
    }

    tbody.innerHTML = students.map(student => `
        <tr>
            <td>${student.nisn || '-'}</td>
            <td>${student.namaLengkap || student.fullName || '-'}</td>
            <td>${student.kelas || student.className || '-'}</td>
            <td>${student.jenisKelamin || student.gender || '-'}</td>
            <td>
                <span class="badge ${student.status === 'ACTIVE' ? 'bg-success' : 'bg-secondary'}">
                    ${student.status === 'ACTIVE' ? 'Aktif' : 'Tidak Aktif'}
                </span>
            </td>
            <td>
                <div class="btn-group" role="group">
                    <button class="btn btn-sm btn-outline-primary" onclick="viewStudent(${student.id})" title="Lihat Detail">
                        <i class="fas fa-eye"></i>
                    </button>
                    <button class="btn btn-sm btn-outline-warning" onclick="editStudent(${student.id})" title="Edit">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="btn btn-sm btn-outline-info" onclick="printBiodata(${student.id})" title="Print Biodata">
                        <i class="fas fa-print"></i>
                    </button>
                    <button class="btn btn-sm btn-outline-danger" onclick="deleteStudent(${student.id})" title="Hapus">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            </td>
        </tr>
    `).join('');
}

function filterStudents() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();
    const classFilter = document.getElementById('classFilter').value;
    const statusFilter = document.getElementById('statusFilter').value;

    const rows = document.querySelectorAll('#studentsTableBody tr');

    rows.forEach(row => {
        const cells = row.querySelectorAll('td');
        if (cells.length < 6) return; // Skip if not a data row

        const nisn = cells[0].textContent.toLowerCase();
        const name = cells[1].textContent.toLowerCase();
        const className = cells[2].textContent.toLowerCase();
        const status = cells[4].querySelector('.badge')?.textContent.toLowerCase() || '';

        const matchesSearch = !searchTerm || nisn.includes(searchTerm) || name.includes(searchTerm);
        const matchesClass = !classFilter || className.includes(classFilter);
        const matchesStatus = !statusFilter || status.includes(statusFilter === 'active' ? 'aktif' : 'tidak aktif');

        row.style.display = matchesSearch && matchesClass && matchesStatus ? '' : 'none';
    });
}

function resetFilters() {
    document.getElementById('searchInput').value = '';
    document.getElementById('classFilter').value = '';
    document.getElementById('statusFilter').value = '';
    filterStudents();
}

function saveStudent() {
    const form = document.getElementById('addStudentForm');
    const formData = new FormData(form);

    const studentData = {
        nisn: formData.get('nisn'),
        fullName: formData.get('fullName'),
        birthDate: formData.get('birthDate'),
        gender: formData.get('gender'),
        address: formData.get('address'),
        parentName: formData.get('parentName'),
        parentPhone: formData.get('parentPhone'),
        classId: parseInt(formData.get('classId')),
        status: formData.get('status')
    };

    fetch('/api/v1/students', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(studentData)
    })
    .then(response => response.json())
    .then(data => {
        if (data.id) {
            showSuccess('Siswa berhasil ditambahkan');
            loadStudents();
            bootstrap.Modal.getInstance(document.getElementById('addStudentModal')).hide();
            form.reset();
        } else {
            showError('Gagal menambahkan siswa');
        }
    })
    .catch(error => {
        console.error('Error saving student:', error);
        showError('Gagal menambahkan siswa');
    });
}

function viewStudent(studentId) {
    fetch(`/api/v1/students/${studentId}`, {
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(student => {
        displayStudentDetails(student);
        const modal = new bootstrap.Modal(document.getElementById('viewStudentModal'));
        modal.show();
    })
    .catch(error => {
        console.error('Error loading student details:', error);
        showError('Gagal memuat detail siswa');
    });
}

function editStudent(studentId) {
    fetch(`/api/v1/students/${studentId}`, {
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(student => {
        populateEditForm(student);
        const modal = new bootstrap.Modal(document.getElementById('editStudentModal'));
        modal.show();
    })
    .catch(error => {
        console.error('Error loading student for edit:', error);
        showError('Gagal memuat data siswa untuk diedit');
    });
}

function displayStudentDetails(student) {
    const detailsContainer = document.getElementById('studentDetails');
    detailsContainer.innerHTML = `
        <div class="row">
            <div class="col-md-4 text-center mb-3">
                <div class="bg-light p-4 rounded">
                    <i class="fas fa-user-circle fa-5x text-primary mb-3"></i>
                    <h5>${student.namaLengkap || student.fullName}</h5>
                    <p class="text-muted">NISN: ${student.nisn || '-'}</p>
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
                                <p><strong>Nama Lengkap:</strong> ${student.namaLengkap || student.fullName}</p>
                                <p><strong>NISN:</strong> ${student.nisn || '-'}</p>
                                <p><strong>Jenis Kelamin:</strong> ${student.jenisKelamin || student.gender}</p>
                                <p><strong>Tanggal Lahir:</strong> ${formatDate(student.tanggalLahir || student.birthDate)}</p>
                            </div>
                            <div class="col-md-6">
                                <p><strong>Kelas:</strong> ${student.kelas || student.className || '-'}</p>
                                <p><strong>Status:</strong>
                                    <span class="badge ${student.status === 'ACTIVE' ? 'bg-success' : 'bg-secondary'}">
                                        ${student.status === 'ACTIVE' ? 'Aktif' : 'Tidak Aktif'}
                                    </span>
                                </p>
                                <p><strong>Alamat:</strong> ${student.alamat || student.address || '-'}</p>
                            </div>
                        </div>
                        <hr>
                        <div class="row">
                            <div class="col-md-6">
                                <h6>Informasi Orang Tua</h6>
                                <p><strong>Nama Orang Tua:</strong> ${student.namaAyah || student.parentName || '-'}</p>
                                <p><strong>No. HP Orang Tua:</strong> ${student.noHpOrtu || student.parentPhone || '-'}</p>
                            </div>
                            <div class="col-md-6">
                                <h6>Informasi Tambahan</h6>
                                <p><strong>Asal Sekolah:</strong> ${student.asalSekolah || '-'}</p>
                                <p><strong>Tahun Masuk:</strong> ${student.tahunMasuk || '-'}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `;
}

function populateEditForm(student) {
    document.getElementById('editStudentId').value = student.id;
    document.getElementById('editNisn').value = student.nisn || '';
    document.getElementById('editFullName').value = student.namaLengkap || student.fullName || '';
    document.getElementById('editBirthDate').value = formatDateForInput(student.tanggalLahir || student.birthDate);
    document.getElementById('editGender').value = student.jenisKelamin || student.gender || '';
    document.getElementById('editAddress').value = student.alamat || student.address || '';
    document.getElementById('editParentName').value = student.namaAyah || student.parentName || '';
    document.getElementById('editParentPhone').value = student.noHpOrtu || student.parentPhone || '';
    document.getElementById('editClassId').value = student.classId || '';
    document.getElementById('editStatus').value = student.status || 'ACTIVE';
}

function updateStudent() {
    const form = document.getElementById('editStudentForm');
    const formData = new FormData(form);
    const studentId = document.getElementById('editStudentId').value;

    const studentData = {
        nisn: formData.get('nisn'),
        fullName: formData.get('fullName'),
        birthDate: formData.get('birthDate'),
        gender: formData.get('gender'),
        address: formData.get('address'),
        parentName: formData.get('parentName'),
        parentPhone: formData.get('parentPhone'),
        classId: parseInt(formData.get('classId')),
        status: formData.get('status')
    };

    fetch(`/api/v1/students/${studentId}`, {
        method: 'PUT',
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(studentData)
    })
    .then(response => response.json())
    .then(data => {
        if (data.id) {
            showSuccess('Siswa berhasil diperbarui');
            loadStudents();
            bootstrap.Modal.getInstance(document.getElementById('editStudentModal')).hide();
        } else {
            showError('Gagal memperbarui siswa');
        }
    })
    .catch(error => {
        console.error('Error updating student:', error);
        showError('Gagal memperbarui siswa');
    });
}

function printBiodata(studentId) {
    // First get student details
    fetch(`/api/v1/students/${studentId}`, {
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(student => {
        generateBiodataPrint(student);
    })
    .catch(error => {
        console.error('Error loading student for print:', error);
        showError('Gagal memuat data siswa untuk print');
    });
}

function generateBiodataPrint(student) {
    const printWindow = window.open('', '_blank');
    const biodataHTML = `
        <!DOCTYPE html>
        <html lang="id">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Biodata Siswa - ${student.namaLengkap || student.fullName}</title>
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
                <div class="biodata-title">BIODATA SISWA</div>
            </div>

            <div class="photo-section">
                <div class="photo-placeholder">
                    <span>FOTO<br>3x4</span>
                </div>
            </div>

            <div class="info-section">
                <div class="info-row">
                    <div class="info-label">Nama Lengkap:</div>
                    <div class="info-value">${student.namaLengkap || student.fullName}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">NISN:</div>
                    <div class="info-value">${student.nisn || '-'}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Jenis Kelamin:</div>
                    <div class="info-value">${student.jenisKelamin || student.gender}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Tempat, Tanggal Lahir:</div>
                    <div class="info-value">${student.tempatLahir || '-'}, ${formatDate(student.tanggalLahir || student.birthDate)}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Alamat:</div>
                    <div class="info-value">${student.alamat || student.address || '-'}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Kelas:</div>
                    <div class="info-value">${student.kelas || student.className || '-'}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Tahun Masuk:</div>
                    <div class="info-value">${student.tahunMasuk || '-'}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Asal Sekolah:</div>
                    <div class="info-value">${student.asalSekolah || '-'}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Nama Ayah:</div>
                    <div class="info-value">${student.namaAyah || student.parentName || '-'}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Nama Ibu:</div>
                    <div class="info-value">${student.namaIbu || '-'}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Pekerjaan Ayah:</div>
                    <div class="info-value">${student.pekerjaanAyah || '-'}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Pekerjaan Ibu:</div>
                    <div class="info-value">${student.pekerjaanIbu || '-'}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">No. HP Orang Tua:</div>
                    <div class="info-value">${student.noHpOrtu || student.parentPhone || '-'}</div>
                </div>
            </div>

            <div class="signature-section">
                <p>Jakarta, ${new Date().toLocaleDateString('id-ID')}</p>
                <br><br><br>
                <div class="signature-line"></div>
                <p>Siswa</p>
            </div>
        </body>
        </html>
    `;

    printWindow.document.write(biodataHTML);
    printWindow.document.close();
    printWindow.print();
}

function downloadTemplate() {
    fetch('/api/v1/students/excel/template', {
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
        a.download = 'student_import_template.xlsx';
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

function exportExcel() {
    fetch('/api/v1/students/excel/export', {
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
        a.download = 'students_export.xlsx';
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

function importExcel() {
    const fileInput = document.getElementById('excelFile');
    const file = fileInput.files[0];

    if (!file) {
        showError('Pilih file Excel terlebih dahulu');
        return;
    }

    const formData = new FormData();
    formData.append('file', file);

    // Show progress
    document.getElementById('importProgress').classList.remove('d-none');
    document.getElementById('importResults').classList.add('d-none');

    fetch('/api/v1/students/excel/import', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${getToken()}`
        },
        body: formData
    })
    .then(response => response.json())
    .then(result => {
        document.getElementById('importProgress').classList.add('d-none');
        document.getElementById('importResults').classList.remove('d-none');

        if (result.successfulImports > 0) {
            document.getElementById('importSuccessMessage').innerHTML = `
                Berhasil mengimport ${result.successfulImports} dari ${result.totalRows} data siswa.
                ${result.failedImports > 0 ? `<br>Gagal: ${result.failedImports} data` : ''}
            `;
            loadStudents(); // Reload the table
            bootstrap.Modal.getInstance(document.getElementById('importExcelModal')).hide();
            document.getElementById('importExcelForm').reset();
        } else {
            showError('Import gagal');
        }
    })
    .catch(error => {
        console.error('Error importing Excel:', error);
        document.getElementById('importProgress').classList.add('d-none');
        showError('Gagal mengimport data');
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

function formatDateForInput(dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toISOString().split('T')[0];
}

function deleteStudent(studentId) {
    if (confirm('Apakah Anda yakin ingin menghapus siswa ini?')) {
        fetch(`/api/v1/students/${studentId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${getToken()}`,
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (response.ok) {
                showSuccess('Siswa berhasil dihapus');
                loadStudents();
            } else {
                showError('Gagal menghapus siswa');
            }
        })
        .catch(error => {
            console.error('Error deleting student:', error);
            showError('Gagal menghapus siswa');
        });
    }
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