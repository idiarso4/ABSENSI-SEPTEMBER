// Subject Management JavaScript
document.addEventListener('DOMContentLoaded', function() {
    initializeSubjectManagement();
    loadSubjects();
    setupEventListeners();
});

function initializeSubjectManagement() {
    console.log('Subject management initialized');
}

function setupEventListeners() {
    // Search input
    document.getElementById('searchInput').addEventListener('input', function() {
        filterSubjects();
    });

    // Type filter
    document.getElementById('typeFilter').addEventListener('change', function() {
        filterSubjects();
    });

    // Status filter
    document.getElementById('statusFilter').addEventListener('change', function() {
        filterSubjects();
    });

    // Reset filters
    document.getElementById('resetFilters').addEventListener('click', function() {
        resetFilters();
    });

    // Save subject button
    document.getElementById('saveSubjectBtn').addEventListener('click', function() {
        saveSubject();
    });

    // Update subject button
    const updateBtn = document.getElementById('updateSubjectBtn');
    if (updateBtn) {
        updateBtn.addEventListener('click', function() {
            updateSubject();
        });
    }

    // Print subject button
    const printBtn = document.getElementById('printSubjectBtn');
    if (printBtn) {
        printBtn.addEventListener('click', function() {
            printSubject();
        });
    }

    // Excel import button
    const importBtn = document.getElementById('importSubjectExcelBtn');
    if (importBtn) {
        importBtn.addEventListener('click', function() {
            importSubjectExcel();
        });
    }

    // Download template button
    const templateBtn = document.getElementById('downloadSubjectTemplateBtn');
    if (templateBtn) {
        templateBtn.addEventListener('click', function(e) {
            e.preventDefault();
            downloadSubjectTemplate();
        });
    }

    // Export Excel button
    const exportBtn = document.getElementById('exportSubjectExcelBtn');
    if (exportBtn) {
        exportBtn.addEventListener('click', function(e) {
            e.preventDefault();
            exportSubjectExcel();
        });
    }
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
        displaySubjects(data.content || []);
    })
    .catch(error => {
        console.error('Error loading subjects:', error);
        showError('Gagal memuat data mata pelajaran');
    });
}

function displaySubjects(subjects) {
    const tbody = document.getElementById('subjectsTableBody');

    if (subjects.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="6" class="text-center">
                    <div class="alert alert-info">
                        <i class="fas fa-info-circle"></i> Tidak ada data mata pelajaran ditemukan
                    </div>
                </td>
            </tr>
        `;
        return;
    }

    tbody.innerHTML = subjects.map(subject => `
        <tr>
            <td>${subject.code || '-'}</td>
            <td>${subject.name || '-'}</td>
            <td>
                <span class="badge ${getSubjectTypeBadge(subject.subjectType)}">
                    ${getSubjectTypeText(subject.subjectType)}
                </span>
            </td>
            <td>${subject.creditHours || subject.sks || '-'}</td>
            <td>
                <span class="badge ${subject.isActive ? 'bg-success' : 'bg-secondary'}">
                    ${subject.isActive ? 'Aktif' : 'Tidak Aktif'}
                </span>
            </td>
            <td>
                <div class="btn-group" role="group">
                    <button class="btn btn-sm btn-outline-primary" onclick="viewSubject(${subject.id})" title="Lihat Detail">
                        <i class="fas fa-eye"></i>
                    </button>
                    <button class="btn btn-sm btn-outline-warning" onclick="editSubject(${subject.id})" title="Edit">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="btn btn-sm btn-outline-info" onclick="printSubjectDetail(${subject.id})" title="Print Detail">
                        <i class="fas fa-print"></i>
                    </button>
                    <button class="btn btn-sm btn-outline-danger" onclick="deleteSubject(${subject.id})" title="Hapus">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            </td>
        </tr>
    `).join('');
}

function getSubjectTypeBadge(type) {
    switch (type) {
        case 'THEORY': return 'bg-primary';
        case 'PRACTICE': return 'bg-warning';
        case 'MIXED': return 'bg-info';
        default: return 'bg-secondary';
    }
}

function getSubjectTypeText(type) {
    switch (type) {
        case 'THEORY': return 'Teori';
        case 'PRACTICE': return 'Praktik';
        case 'MIXED': return 'Campuran';
        default: return '-';
    }
}

function filterSubjects() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();
    const typeFilter = document.getElementById('typeFilter').value;
    const statusFilter = document.getElementById('statusFilter').value;

    const rows = document.querySelectorAll('#subjectsTableBody tr');

    rows.forEach(row => {
        const cells = row.querySelectorAll('td');
        if (cells.length < 6) return; // Skip if not a data row

        const code = cells[0].textContent.toLowerCase();
        const name = cells[1].textContent.toLowerCase();
        const type = cells[2].querySelector('.badge')?.textContent.toLowerCase() || '';
        const status = cells[4].querySelector('.badge')?.textContent.toLowerCase() || '';

        const matchesSearch = !searchTerm || code.includes(searchTerm) || name.includes(searchTerm);
        const matchesType = !typeFilter || type.includes(typeFilter.toLowerCase());
        const matchesStatus = !statusFilter || status.includes(statusFilter === 'true' ? 'aktif' : 'tidak aktif');

        row.style.display = matchesSearch && matchesType && matchesStatus ? '' : 'none';
    });
}

function resetFilters() {
    document.getElementById('searchInput').value = '';
    document.getElementById('typeFilter').value = '';
    document.getElementById('statusFilter').value = '';
    filterSubjects();
}

function saveSubject() {
    const form = document.getElementById('addSubjectForm');
    const formData = new FormData(form);

    const subjectData = {
        code: formData.get('subjectCode'),
        name: formData.get('subjectName'),
        subjectType: formData.get('subjectType'),
        creditHours: parseInt(formData.get('sks')),
        description: formData.get('description'),
        isActive: true
    };

    fetch('/api/v1/subjects', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(subjectData)
    })
    .then(response => response.json())
    .then(data => {
        if (data.id) {
            showSuccess('Mata pelajaran berhasil ditambahkan');
            loadSubjects();
            bootstrap.Modal.getInstance(document.getElementById('addSubjectModal')).hide();
            form.reset();
        } else {
            showError('Gagal menambahkan mata pelajaran');
        }
    })
    .catch(error => {
        console.error('Error saving subject:', error);
        showError('Gagal menambahkan mata pelajaran');
    });
}

function viewSubject(subjectId) {
    fetch(`/api/v1/subjects/${subjectId}`, {
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(subject => {
        displaySubjectDetails(subject);
        const modal = new bootstrap.Modal(document.getElementById('viewSubjectModal'));
        modal.show();
    })
    .catch(error => {
        console.error('Error loading subject details:', error);
        showError('Gagal memuat detail mata pelajaran');
    });
}

function editSubject(subjectId) {
    fetch(`/api/v1/subjects/${subjectId}`, {
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(subject => {
        populateEditForm(subject);
        const modal = new bootstrap.Modal(document.getElementById('editSubjectModal'));
        modal.show();
    })
    .catch(error => {
        console.error('Error loading subject for edit:', error);
        showError('Gagal memuat data mata pelajaran untuk diedit');
    });
}

function displaySubjectDetails(subject) {
    const detailsContainer = document.getElementById('subjectDetails');
    detailsContainer.innerHTML = `
        <div class="row">
            <div class="col-md-4 text-center mb-3">
                <div class="bg-light p-4 rounded">
                    <i class="fas fa-book fa-5x text-primary mb-3"></i>
                    <h5>${subject.name}</h5>
                    <p class="text-muted">Kode: ${subject.code}</p>
                </div>
            </div>
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header">
                        <h6 class="mb-0">Informasi Mata Pelajaran</h6>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6">
                                <p><strong>Nama Mata Pelajaran:</strong> ${subject.name}</p>
                                <p><strong>Kode:</strong> ${subject.code}</p>
                                <p><strong>Jenis:</strong>
                                    <span class="badge ${getSubjectTypeBadge(subject.subjectType)}">
                                        ${getSubjectTypeText(subject.subjectType)}
                                    </span>
                                </p>
                                <p><strong>SKS:</strong> ${subject.creditHours || subject.sks || '-'}</p>
                            </div>
                            <div class="col-md-6">
                                <p><strong>Status:</strong>
                                    <span class="badge ${subject.isActive ? 'bg-success' : 'bg-secondary'}">
                                        ${subject.isActive ? 'Aktif' : 'Tidak Aktif'}
                                    </span>
                                </p>
                                <p><strong>Dibuat:</strong> ${subject.createdAt ? new Date(subject.createdAt).toLocaleDateString('id-ID') : '-'}</p>
                                <p><strong>Diupdate:</strong> ${subject.updatedAt ? new Date(subject.updatedAt).toLocaleDateString('id-ID') : '-'}</p>
                            </div>
                        </div>
                        <hr>
                        <div class="row">
                            <div class="col-md-12">
                                <h6>Deskripsi</h6>
                                <p>${subject.description || 'Tidak ada deskripsi'}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `;
}

function populateEditForm(subject) {
    document.getElementById('editSubjectId').value = subject.id;
    document.getElementById('editSubjectCode').value = subject.code || '';
    document.getElementById('editSubjectName').value = subject.name || '';
    document.getElementById('editSubjectType').value = subject.subjectType || 'THEORY';
    document.getElementById('editSks').value = subject.creditHours || subject.sks || '';
    document.getElementById('editDescription').value = subject.description || '';
    document.getElementById('editStatus').value = subject.isActive ? 'true' : 'false';
}

function updateSubject() {
    const form = document.getElementById('editSubjectForm');
    const formData = new FormData(form);
    const subjectId = document.getElementById('editSubjectId').value;

    const subjectData = {
        code: formData.get('code'),
        name: formData.get('name'),
        subjectType: formData.get('subjectType'),
        creditHours: parseInt(formData.get('creditHours')),
        description: formData.get('description'),
        isActive: formData.get('isActive') === 'true'
    };

    fetch(`/api/v1/subjects/${subjectId}`, {
        method: 'PUT',
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(subjectData)
    })
    .then(response => response.json())
    .then(data => {
        if (data.id) {
            showSuccess('Mata pelajaran berhasil diperbarui');
            loadSubjects();
            bootstrap.Modal.getInstance(document.getElementById('editSubjectModal')).hide();
        } else {
            showError('Gagal memperbarui mata pelajaran');
        }
    })
    .catch(error => {
        console.error('Error updating subject:', error);
        showError('Gagal memperbarui mata pelajaran');
    });
}

function deleteSubject(subjectId) {
    if (confirm('Apakah Anda yakin ingin menghapus mata pelajaran ini?')) {
        fetch(`/api/v1/subjects/${subjectId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${getToken()}`,
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (response.ok) {
                showSuccess('Mata pelajaran berhasil dihapus');
                loadSubjects();
            } else {
                showError('Gagal menghapus mata pelajaran');
            }
        })
        .catch(error => {
            console.error('Error deleting subject:', error);
            showError('Gagal menghapus mata pelajaran');
        });
    }
}

function printSubjectDetail(subjectId) {
    // First get subject details
    fetch(`/api/v1/subjects/${subjectId}`, {
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(subject => {
        generateSubjectPrint(subject);
    })
    .catch(error => {
        console.error('Error loading subject for print:', error);
        showError('Gagal memuat data mata pelajaran untuk print');
    });
}

function printSubject() {
    // Get subject details from the modal
    const subjectDetails = document.getElementById('subjectDetails');
    const subjectName = subjectDetails.querySelector('h5')?.textContent || 'Mata Pelajaran';
    const subjectCode = subjectDetails.querySelector('.text-muted')?.textContent.replace('Kode: ', '') || '';

    // Create a simple print version
    const printWindow = window.open('', '_blank');
    const printContent = `
        <!DOCTYPE html>
        <html lang="id">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Detail Mata Pelajaran - ${subjectName}</title>
            <style>
                body { font-family: Arial, sans-serif; margin: 20px; }
                .header { text-align: center; border-bottom: 2px solid #333; padding-bottom: 20px; margin-bottom: 30px; }
                .school-name { font-size: 24px; font-weight: bold; margin-bottom: 10px; }
                .subject-title { font-size: 18px; margin-bottom: 20px; }
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
                <div class="subject-title">DETAIL MATA PELAJARAN</div>
            </div>

            <div class="info-section">
                <div class="info-row">
                    <div class="info-label">Nama Mata Pelajaran:</div>
                    <div class="info-value">${subjectName}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Kode:</div>
                    <div class="info-value">${subjectCode}</div>
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

function generateSubjectPrint(subject) {
    const printWindow = window.open('', '_blank');
    const printContent = `
        <!DOCTYPE html>
        <html lang="id">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Detail Mata Pelajaran - ${subject.name}</title>
            <style>
                body { font-family: Arial, sans-serif; margin: 20px; }
                .header { text-align: center; border-bottom: 2px solid #333; padding-bottom: 20px; margin-bottom: 30px; }
                .school-name { font-size: 24px; font-weight: bold; margin-bottom: 10px; }
                .subject-title { font-size: 18px; margin-bottom: 20px; }
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
                <div class="subject-title">DETAIL MATA PELAJARAN</div>
            </div>

            <div class="info-section">
                <div class="info-row">
                    <div class="info-label">Nama Mata Pelajaran:</div>
                    <div class="info-value">${subject.name}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Kode:</div>
                    <div class="info-value">${subject.code}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Jenis:</div>
                    <div class="info-value">${getSubjectTypeText(subject.subjectType)}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">SKS:</div>
                    <div class="info-value">${subject.creditHours || subject.sks || '-'}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Status:</div>
                    <div class="info-value">${subject.isActive ? 'Aktif' : 'Tidak Aktif'}</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Deskripsi:</div>
                    <div class="info-value">${subject.description || 'Tidak ada deskripsi'}</div>
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

function downloadSubjectTemplate() {
    fetch('/api/v1/subjects/excel/template', {
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
        a.download = 'subject_import_template.xlsx';
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

function exportSubjectExcel() {
    fetch('/api/v1/subjects/excel/export', {
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
        a.download = 'subjects_export.xlsx';
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

function importSubjectExcel() {
    const fileInput = document.getElementById('subjectExcelFile');
    const file = fileInput.files[0];

    if (!file) {
        showError('Pilih file Excel terlebih dahulu');
        return;
    }

    const formData = new FormData();
    formData.append('file', file);

    // Show progress
    document.getElementById('subjectImportProgress').classList.remove('d-none');
    document.getElementById('subjectImportResults').classList.add('d-none');

    fetch('/api/v1/subjects/excel/import', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${getToken()}`
        },
        body: formData
    })
    .then(response => response.json())
    .then(result => {
        document.getElementById('subjectImportProgress').classList.add('d-none');
        document.getElementById('subjectImportResults').classList.remove('d-none');

        if (result.successfulImports > 0) {
            document.getElementById('subjectImportSuccessMessage').innerHTML = `
                Berhasil mengimport ${result.successfulImports} dari ${result.totalRows} data mata pelajaran.
                ${result.failedImports > 0 ? `<br>Gagal: ${result.failedImports} data` : ''}
            `;
            loadSubjects(); // Reload the table
            bootstrap.Modal.getInstance(document.getElementById('importSubjectExcelModal')).hide();
            document.getElementById('importSubjectExcelForm').reset();
        } else {
            showError('Import gagal');
        }
    })
    .catch(error => {
        console.error('Error importing Excel:', error);
        document.getElementById('subjectImportProgress').classList.add('d-none');
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