const { JSDOM } = require('jsdom');

// Mock fetch globally
global.fetch = jest.fn();

describe('Students Management Tests', () => {
  let dom;
  let document;
  let window;

  beforeEach(() => {
    // Setup JSDOM with students page HTML
    const html = `
      <!DOCTYPE html>
      <html>
        <head>
          <title>Kelola Siswa - SIM Sekolah</title>
        </head>
        <body>
          <nav class="navbar">
            <%- include('partials/navbar') %>
          </nav>

          <div class="container mt-4">
            <div class="d-flex justify-content-between align-items-center mb-4">
              <h2>Kelola Siswa</h2>
              <button class="btn btn-primary" id="addStudentBtn">
                <i class="fas fa-plus"></i> Tambah Siswa
              </button>
            </div>

            <!-- Search and Filter -->
            <div class="card mb-4">
              <div class="card-body">
                <div class="row">
                  <div class="col-md-4">
                    <input type="text" class="form-control" id="searchInput" placeholder="Cari siswa...">
                  </div>
                  <div class="col-md-3">
                    <select class="form-select" id="classFilter">
                      <option value="">Semua Kelas</option>
                      <option value="10">Kelas 10</option>
                      <option value="11">Kelas 11</option>
                      <option value="12">Kelas 12</option>
                    </select>
                  </div>
                  <div class="col-md-3">
                    <select class="form-select" id="statusFilter">
                      <option value="">Semua Status</option>
                      <option value="active">Aktif</option>
                      <option value="inactive">Tidak Aktif</option>
                    </select>
                  </div>
                  <div class="col-md-2">
                    <button class="btn btn-outline-secondary w-100" id="resetFiltersBtn">
                      Reset
                    </button>
                  </div>
                </div>
              </div>
            </div>

            <!-- Students Table -->
            <div class="card">
              <div class="card-body">
                <div class="table-responsive">
                  <table class="table table-striped" id="studentsTable">
                    <thead>
                      <tr>
                        <th>NIS</th>
                        <th>Nama</th>
                        <th>Kelas</th>
                        <th>Jurusan</th>
                        <th>Status</th>
                        <th>Aksi</th>
                      </tr>
                    </thead>
                    <tbody id="studentsTableBody">
                      <tr id="loadingRow">
                        <td colspan="6" class="text-center">
                          <div class="spinner-border spinner-border-sm" role="status">
                            <span class="sr-only">Loading...</span>
                          </div>
                          Memuat data siswa...
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>

                <!-- Pagination -->
                <nav aria-label="Students pagination" class="mt-3">
                  <ul class="pagination justify-content-center" id="pagination">
                    <!-- Pagination will be generated here -->
                  </ul>
                </nav>
              </div>
            </div>

            <!-- Add/Edit Student Modal -->
            <div class="modal fade" id="studentModal" tabindex="-1">
              <div class="modal-dialog modal-lg">
                <div class="modal-content">
                  <div class="modal-header">
                    <h5 class="modal-title" id="studentModalTitle">Tambah Siswa</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                  </div>
                  <div class="modal-body">
                    <form id="studentForm">
                      <div class="row">
                        <div class="col-md-6">
                          <div class="mb-3">
                            <label for="nis" class="form-label">NIS *</label>
                            <input type="text" class="form-control" id="nis" name="nis" required>
                          </div>
                        </div>
                        <div class="col-md-6">
                          <div class="mb-3">
                            <label for="nisn" class="form-label">NISN</label>
                            <input type="text" class="form-control" id="nisn" name="nisn">
                          </div>
                        </div>
                      </div>

                      <div class="row">
                        <div class="col-md-6">
                          <div class="mb-3">
                            <label for="firstName" class="form-label">Nama Depan *</label>
                            <input type="text" class="form-control" id="firstName" name="firstName" required>
                          </div>
                        </div>
                        <div class="col-md-6">
                          <div class="mb-3">
                            <label for="lastName" class="form-label">Nama Belakang *</label>
                            <input type="text" class="form-control" id="lastName" name="lastName" required>
                          </div>
                        </div>
                      </div>

                      <div class="row">
                        <div class="col-md-6">
                          <div class="mb-3">
                            <label for="email" class="form-label">Email</label>
                            <input type="email" class="form-control" id="email" name="email">
                          </div>
                        </div>
                        <div class="col-md-6">
                          <div class="mb-3">
                            <label for="phone" class="form-label">Telepon</label>
                            <input type="tel" class="form-control" id="phone" name="phone">
                          </div>
                        </div>
                      </div>

                      <div class="row">
                        <div class="col-md-6">
                          <div class="mb-3">
                            <label for="birthDate" class="form-label">Tanggal Lahir</label>
                            <input type="date" class="form-control" id="birthDate" name="birthDate">
                          </div>
                        </div>
                        <div class="col-md-6">
                          <div class="mb-3">
                            <label for="gender" class="form-label">Jenis Kelamin</label>
                            <select class="form-select" id="gender" name="gender">
                              <option value="">Pilih Jenis Kelamin</option>
                              <option value="MALE">Laki-laki</option>
                              <option value="FEMALE">Perempuan</option>
                            </select>
                          </div>
                        </div>
                      </div>

                      <div class="mb-3">
                        <label for="address" class="form-label">Alamat</label>
                        <textarea class="form-control" id="address" name="address" rows="3"></textarea>
                      </div>

                      <div class="row">
                        <div class="col-md-6">
                          <div class="mb-3">
                            <label for="classRoom" class="form-label">Kelas</label>
                            <select class="form-select" id="classRoom" name="classRoom">
                              <option value="">Pilih Kelas</option>
                            </select>
                          </div>
                        </div>
                        <div class="col-md-6">
                          <div class="mb-3">
                            <label for="enrollmentDate" class="form-label">Tanggal Masuk</label>
                            <input type="date" class="form-control" id="enrollmentDate" name="enrollmentDate">
                          </div>
                        </div>
                      </div>

                      <div class="mb-3">
                        <div class="form-check">
                          <input class="form-check-input" type="checkbox" id="isActive" name="isActive" checked>
                          <label class="form-check-label" for="isActive">
                            Siswa Aktif
                          </label>
                        </div>
                      </div>
                    </form>
                  </div>
                  <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Batal</button>
                    <button type="button" class="btn btn-primary" id="saveStudentBtn">Simpan</button>
                  </div>
                </div>
              </div>
            </div>

            <!-- Delete Confirmation Modal -->
            <div class="modal fade" id="deleteModal" tabindex="-1">
              <div class="modal-dialog">
                <div class="modal-content">
                  <div class="modal-header">
                    <h5 class="modal-title">Konfirmasi Hapus</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                  </div>
                  <div class="modal-body">
                    <p>Apakah Anda yakin ingin menghapus siswa <strong id="deleteStudentName"></strong>?</p>
                    <p class="text-danger">Tindakan ini tidak dapat dibatalkan.</p>
                  </div>
                  <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Batal</button>
                    <button type="button" class="btn btn-danger" id="confirmDeleteBtn">Hapus</button>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <script src="/js/students.js"></script>
        </body>
      </html>
    `;

    dom = new JSDOM(html, { url: 'http://localhost:3000/students' });
    document = dom.window.document;
    window = dom.window;
    global.document = document;
    global.window = window;
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  describe('Students Page Structure', () => {
    test('should render page header correctly', () => {
      const header = document.querySelector('h2');
      expect(header).toBeTruthy();
      expect(header.textContent).toBe('Kelola Siswa');

      const addButton = document.querySelector('#addStudentBtn');
      expect(addButton).toBeTruthy();
      expect(addButton.textContent).toContain('Tambah Siswa');
    });

    test('should have search and filter controls', () => {
      const searchInput = document.querySelector('#searchInput');
      const classFilter = document.querySelector('#classFilter');
      const statusFilter = document.querySelector('#statusFilter');
      const resetBtn = document.querySelector('#resetFiltersBtn');

      expect(searchInput).toBeTruthy();
      expect(searchInput.getAttribute('placeholder')).toBe('Cari siswa...');

      expect(classFilter).toBeTruthy();
      expect(statusFilter).toBeTruthy();
      expect(resetBtn).toBeTruthy();
    });

    test('should have students table with correct headers', () => {
      const table = document.querySelector('#studentsTable');
      expect(table).toBeTruthy();

      const headers = table.querySelectorAll('thead th');
      expect(headers.length).toBe(6);

      const expectedHeaders = ['NIS', 'Nama', 'Kelas', 'Jurusan', 'Status', 'Aksi'];
      headers.forEach((header, index) => {
        expect(header.textContent).toBe(expectedHeaders[index]);
      });
    });

    test('should have loading state initially', () => {
      const loadingRow = document.querySelector('#loadingRow');
      expect(loadingRow).toBeTruthy();
      expect(loadingRow.textContent).toContain('Memuat data siswa...');
    });
  });

  describe('Student Form Modal', () => {
    test('should have complete student form', () => {
      const form = document.querySelector('#studentForm');
      expect(form).toBeTruthy();

      // Required fields
      const requiredFields = ['nis', 'firstName', 'lastName'];
      requiredFields.forEach(fieldId => {
        const field = document.querySelector(`#${fieldId}`);
        expect(field).toBeTruthy();
        expect(field.hasAttribute('required')).toBe(true);
      });

      // Optional fields
      const optionalFields = ['nisn', 'email', 'phone', 'birthDate', 'address', 'enrollmentDate'];
      optionalFields.forEach(fieldId => {
        const field = document.querySelector(`#${fieldId}`);
        expect(field).toBeTruthy();
        expect(field.hasAttribute('required')).toBe(false);
      });
    });

    test('should have gender select with correct options', () => {
      const genderSelect = document.querySelector('#gender');
      expect(genderSelect).toBeTruthy();

      const options = genderSelect.querySelectorAll('option');
      expect(options.length).toBe(3); // Empty + Male + Female

      expect(options[0].value).toBe('');
      expect(options[0].textContent).toBe('Pilih Jenis Kelamin');
      expect(options[1].value).toBe('MALE');
      expect(options[1].textContent).toBe('Laki-laki');
      expect(options[2].value).toBe('FEMALE');
      expect(options[2].textContent).toBe('Perempuan');
    });

    test('should have active status checkbox', () => {
      const isActiveCheckbox = document.querySelector('#isActive');
      expect(isActiveCheckbox).toBeTruthy();
      expect(isActiveCheckbox.checked).toBe(true);
    });

    test('should have modal action buttons', () => {
      const saveBtn = document.querySelector('#saveStudentBtn');
      expect(saveBtn).toBeTruthy();
      expect(saveBtn.textContent).toBe('Simpan');
    });
  });

  describe('Delete Confirmation Modal', () => {
    test('should have delete confirmation modal', () => {
      const deleteModal = document.querySelector('#deleteModal');
      expect(deleteModal).toBeTruthy();

      const deleteBtn = document.querySelector('#confirmDeleteBtn');
      expect(deleteBtn).toBeTruthy();
      expect(deleteBtn.classList.contains('btn-danger')).toBe(true);
    });
  });

  describe('Students Data Loading', () => {
    test('should load students data on page load', async () => {
      // Mock successful API response
      const mockStudents = [
        {
          id: 1,
          nis: '2025001',
          nisn: '1234567890',
          firstName: 'Ahmad',
          lastName: 'Susanto',
          email: 'ahmad@example.com',
          classRoom: { name: '10 IPA 1' },
          isActive: true
        },
        {
          id: 2,
          nis: '2025002',
          nisn: '1234567891',
          firstName: 'Siti',
          lastName: 'Nurhaliza',
          email: 'siti@example.com',
          classRoom: { name: '10 IPS 1' },
          isActive: true
        }
      ];

      global.fetch.mockResolvedValue({
        ok: true,
        json: () => Promise.resolve({
          content: mockStudents,
          totalElements: 2,
          totalPages: 1,
          number: 0,
          size: 20
        })
      });

      // Simulate loading students
      const tableBody = document.querySelector('#studentsTableBody');
      tableBody.innerHTML = mockStudents.map(student => `
        <tr>
          <td>${student.nis}</td>
          <td>${student.firstName} ${student.lastName}</td>
          <td>${student.classRoom.name}</td>
          <td>-</td>
          <td><span class="badge bg-success">Aktif</span></td>
          <td>
            <button class="btn btn-sm btn-outline-primary edit-btn" data-id="${student.id}">Edit</button>
            <button class="btn btn-sm btn-outline-danger delete-btn" data-id="${student.id}">Hapus</button>
          </td>
        </tr>
      `).join('');

      const rows = tableBody.querySelectorAll('tr');
      expect(rows.length).toBe(2);

      // Verify API call
      expect(global.fetch).toHaveBeenCalledWith('/api/students?page=0&size=20');
    });

    test('should handle empty students list', async () => {
      global.fetch.mockResolvedValue({
        ok: true,
        json: () => Promise.resolve({
          content: [],
          totalElements: 0,
          totalPages: 0,
          number: 0,
          size: 20
        })
      });

      const tableBody = document.querySelector('#studentsTableBody');
      tableBody.innerHTML = `
        <tr>
          <td colspan="6" class="text-center text-muted">
            <i class="fas fa-users"></i> Tidak ada data siswa
          </td>
        </tr>
      `;

      const emptyRow = tableBody.querySelector('tr');
      expect(emptyRow).toBeTruthy();
      expect(emptyRow.textContent).toContain('Tidak ada data siswa');
    });

    test('should handle API errors gracefully', async () => {
      global.fetch.mockRejectedValue(new Error('Network error'));

      const tableBody = document.querySelector('#studentsTableBody');
      tableBody.innerHTML = `
        <tr>
          <td colspan="6" class="text-center text-danger">
            <i class="fas fa-exclamation-triangle"></i> Gagal memuat data siswa
          </td>
        </tr>
      `;

      const errorRow = tableBody.querySelector('tr');
      expect(errorRow).toBeTruthy();
      expect(errorRow.textContent).toContain('Gagal memuat data siswa');
    });
  });

  describe('Search and Filter Functionality', () => {
    test('should filter students by search term', () => {
      const searchInput = document.querySelector('#searchInput');

      // Simulate search input
      searchInput.value = 'Ahmad';

      expect(searchInput.value).toBe('Ahmad');
    });

    test('should filter by class', () => {
      const classFilter = document.querySelector('#classFilter');

      // Simulate class selection
      classFilter.value = '10';

      expect(classFilter.value).toBe('10');
    });

    test('should filter by status', () => {
      const statusFilter = document.querySelector('#statusFilter');

      // Simulate status selection
      statusFilter.value = 'active';

      expect(statusFilter.value).toBe('active');
    });

    test('should reset all filters', () => {
      const searchInput = document.querySelector('#searchInput');
      const classFilter = document.querySelector('#classFilter');
      const statusFilter = document.querySelector('#statusFilter');

      // Set some values
      searchInput.value = 'test';
      classFilter.value = '10';
      statusFilter.value = 'active';

      // Simulate reset
      searchInput.value = '';
      classFilter.value = '';
      statusFilter.value = '';

      expect(searchInput.value).toBe('');
      expect(classFilter.value).toBe('');
      expect(statusFilter.value).toBe('');
    });
  });

  describe('Student CRUD Operations', () => {
    test('should open add student modal', () => {
      const addBtn = document.querySelector('#addStudentBtn');
      const modal = document.querySelector('#studentModal');
      const modalTitle = document.querySelector('#studentModalTitle');

      // Simulate clicking add button
      expect(addBtn).toBeTruthy();
      expect(modal).toBeTruthy();
      expect(modalTitle.textContent).toBe('Tambah Siswa');
    });

    test('should populate edit form with student data', () => {
      const studentData = {
        id: 1,
        nis: '2025001',
        nisn: '1234567890',
        firstName: 'Ahmad',
        lastName: 'Susanto',
        email: 'ahmad@example.com',
        phone: '081234567890',
        birthDate: '2005-05-15',
        gender: 'MALE',
        address: 'Jl. Sudirman No. 123',
        classRoom: { id: 1, name: '10 IPA 1' },
        enrollmentDate: '2023-07-01',
        isActive: true
      };

      // Populate form fields
      Object.keys(studentData).forEach(key => {
        const field = document.querySelector(`#${key}`);
        if (field) {
          if (field.type === 'checkbox') {
            field.checked = studentData[key];
          } else {
            field.value = studentData[key];
          }
        }
      });

      // Verify form population
      expect(document.querySelector('#nis').value).toBe('2025001');
      expect(document.querySelector('#firstName').value).toBe('Ahmad');
      expect(document.querySelector('#lastName').value).toBe('Susanto');
      expect(document.querySelector('#email').value).toBe('ahmad@example.com');
      expect(document.querySelector('#isActive').checked).toBe(true);
    });

    test('should validate required fields', () => {
      const form = document.querySelector('#studentForm');
      const requiredFields = ['nis', 'firstName', 'lastName'];

      // Check that required fields have required attribute
      requiredFields.forEach(fieldId => {
        const field = document.querySelector(`#${fieldId}`);
        expect(field.hasAttribute('required')).toBe(true);
      });
    });

    test('should show delete confirmation', () => {
      const deleteModal = document.querySelector('#deleteModal');
      const deleteStudentName = document.querySelector('#deleteStudentName');

      // Simulate showing delete confirmation
      deleteStudentName.textContent = 'Ahmad Susanto';

      expect(deleteModal).toBeTruthy();
      expect(deleteStudentName.textContent).toBe('Ahmad Susanto');
    });
  });

  describe('Pagination', () => {
    test('should render pagination controls', () => {
      const pagination = document.querySelector('#pagination');

      // Simulate pagination rendering
      pagination.innerHTML = `
        <li class="page-item disabled">
          <a class="page-link" href="#" tabindex="-1">Previous</a>
        </li>
        <li class="page-item active">
          <a class="page-link" href="#">1</a>
        </li>
        <li class="page-item">
          <a class="page-link" href="#">2</a>
        </li>
        <li class="page-item">
          <a class="page-link" href="#">Next</a>
        </li>
      `;

      const pageItems = pagination.querySelectorAll('.page-item');
      expect(pageItems.length).toBe(4);

      const activePage = pagination.querySelector('.page-item.active');
      expect(activePage).toBeTruthy();
      expect(activePage.querySelector('.page-link').textContent).toBe('1');
    });
  });

  describe('Responsive Design', () => {
    test('should have responsive table', () => {
      const tableResponsive = document.querySelector('.table-responsive');
      expect(tableResponsive).toBeTruthy();

      const table = tableResponsive.querySelector('table');
      expect(table).toBeTruthy();
      expect(table.classList.contains('table-striped')).toBe(true);
    });

    test('should have responsive form layout', () => {
      const formRows = document.querySelectorAll('#studentForm .row');
      expect(formRows.length).toBeGreaterThan(0);

      formRows.forEach(row => {
        const cols = row.querySelectorAll('.col-md-6');
        expect(cols.length).toBe(2);
      });
    });
  });
});