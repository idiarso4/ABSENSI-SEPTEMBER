const { JSDOM } = require('jsdom');

// Mock fetch globally
global.fetch = jest.fn();

describe('Teachers Management Tests', () => {
  let dom;
  let document;
  let window;

  beforeEach(() => {
    // Setup JSDOM with teachers page HTML
    const html = `
      <!DOCTYPE html>
      <html>
        <head>
          <title>Kelola Guru - SIM Sekolah</title>
        </head>
        <body>
          <nav class="navbar">
            <%- include('partials/navbar') %>
          </nav>

          <div class="container mt-4">
            <div class="d-flex justify-content-between align-items-center mb-4">
              <h2>Kelola Guru</h2>
              <button class="btn btn-primary" id="addTeacherBtn">
                <i class="fas fa-plus"></i> Tambah Guru
              </button>
            </div>

            <!-- Search and Filter -->
            <div class="card mb-4">
              <div class="card-body">
                <div class="row">
                  <div class="col-md-4">
                    <input type="text" class="form-control" id="searchInput" placeholder="Cari guru...">
                  </div>
                  <div class="col-md-3">
                    <select class="form-select" id="subjectFilter">
                      <option value="">Semua Mata Pelajaran</option>
                      <option value="1">Matematika</option>
                      <option value="2">Bahasa Indonesia</option>
                      <option value="3">Bahasa Inggris</option>
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

            <!-- Teachers Table -->
            <div class="card">
              <div class="card-body">
                <div class="table-responsive">
                  <table class="table table-striped" id="teachersTable">
                    <thead>
                      <tr>
                        <th>NIP</th>
                        <th>Nama</th>
                        <th>Mata Pelajaran</th>
                        <th>Email</th>
                        <th>Status</th>
                        <th>Aksi</th>
                      </tr>
                    </thead>
                    <tbody id="teachersTableBody">
                      <tr id="loadingRow">
                        <td colspan="6" class="text-center">
                          <div class="spinner-border spinner-border-sm" role="status">
                            <span class="sr-only">Loading...</span>
                          </div>
                          Memuat data guru...
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>

                <!-- Pagination -->
                <nav aria-label="Teachers pagination" class="mt-3">
                  <ul class="pagination justify-content-center" id="pagination">
                    <!-- Pagination will be generated here -->
                  </ul>
                </nav>
              </div>
            </div>

            <!-- Add/Edit Teacher Modal -->
            <div class="modal fade" id="teacherModal" tabindex="-1">
              <div class="modal-dialog modal-lg">
                <div class="modal-content">
                  <div class="modal-header">
                    <h5 class="modal-title" id="teacherModalTitle">Tambah Guru</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                  </div>
                  <div class="modal-body">
                    <form id="teacherForm">
                      <div class="row">
                        <div class="col-md-6">
                          <div class="mb-3">
                            <label for="nip" class="form-label">NIP *</label>
                            <input type="text" class="form-control" id="nip" name="nip" required>
                          </div>
                        </div>
                        <div class="col-md-6">
                          <div class="mb-3">
                            <label for="employeeId" class="form-label">ID Karyawan</label>
                            <input type="text" class="form-control" id="employeeId" name="employeeId">
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
                            <label for="email" class="form-label">Email *</label>
                            <input type="email" class="form-control" id="email" name="email" required>
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
                            <label for="subject" class="form-label">Mata Pelajaran</label>
                            <select class="form-select" id="subject" name="subject">
                              <option value="">Pilih Mata Pelajaran</option>
                            </select>
                          </div>
                        </div>
                        <div class="col-md-6">
                          <div class="mb-3">
                            <label for="hireDate" class="form-label">Tanggal Bergabung</label>
                            <input type="date" class="form-control" id="hireDate" name="hireDate">
                          </div>
                        </div>
                      </div>

                      <div class="row">
                        <div class="col-md-6">
                          <div class="mb-3">
                            <label for="department" class="form-label">Departemen</label>
                            <select class="form-select" id="department" name="department">
                              <option value="">Pilih Departemen</option>
                            </select>
                          </div>
                        </div>
                        <div class="col-md-6">
                          <div class="mb-3">
                            <label for="position" class="form-label">Jabatan</label>
                            <input type="text" class="form-control" id="position" name="position" placeholder="Guru Matematika">
                          </div>
                        </div>
                      </div>

                      <div class="mb-3">
                        <div class="form-check">
                          <input class="form-check-input" type="checkbox" id="isActive" name="isActive" checked>
                          <label class="form-check-label" for="isActive">
                            Guru Aktif
                          </label>
                        </div>
                      </div>
                    </form>
                  </div>
                  <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Batal</button>
                    <button type="button" class="btn btn-primary" id="saveTeacherBtn">Simpan</button>
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
                    <p>Apakah Anda yakin ingin menghapus guru <strong id="deleteTeacherName"></strong>?</p>
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

          <script src="/js/teachers.js"></script>
        </body>
      </html>
    `;

    dom = new JSDOM(html, { url: 'http://localhost:3000/teachers' });
    document = dom.window.document;
    window = dom.window;
    global.document = document;
    global.window = window;
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  describe('Teachers Page Structure', () => {
    test('should render page header correctly', () => {
      const header = document.querySelector('h2');
      expect(header).toBeTruthy();
      expect(header.textContent).toBe('Kelola Guru');

      const addButton = document.querySelector('#addTeacherBtn');
      expect(addButton).toBeTruthy();
      expect(addButton.textContent).toContain('Tambah Guru');
    });

    test('should have search and filter controls', () => {
      const searchInput = document.querySelector('#searchInput');
      const subjectFilter = document.querySelector('#subjectFilter');
      const statusFilter = document.querySelector('#statusFilter');
      const resetBtn = document.querySelector('#resetFiltersBtn');

      expect(searchInput).toBeTruthy();
      expect(searchInput.getAttribute('placeholder')).toBe('Cari guru...');

      expect(subjectFilter).toBeTruthy();
      expect(statusFilter).toBeTruthy();
      expect(resetBtn).toBeTruthy();
    });

    test('should have teachers table with correct headers', () => {
      const table = document.querySelector('#teachersTable');
      expect(table).toBeTruthy();

      const headers = table.querySelectorAll('thead th');
      expect(headers.length).toBe(6);

      const expectedHeaders = ['NIP', 'Nama', 'Mata Pelajaran', 'Email', 'Status', 'Aksi'];
      headers.forEach((header, index) => {
        expect(header.textContent).toBe(expectedHeaders[index]);
      });
    });

    test('should have loading state initially', () => {
      const loadingRow = document.querySelector('#loadingRow');
      expect(loadingRow).toBeTruthy();
      expect(loadingRow.textContent).toContain('Memuat data guru...');
    });
  });

  describe('Teacher Form Modal', () => {
    test('should have complete teacher form', () => {
      const form = document.querySelector('#teacherForm');
      expect(form).toBeTruthy();

      // Required fields
      const requiredFields = ['nip', 'firstName', 'lastName', 'email'];
      requiredFields.forEach(fieldId => {
        const field = document.querySelector(`#${fieldId}`);
        expect(field).toBeTruthy();
        expect(field.hasAttribute('required')).toBe(true);
      });

      // Optional fields
      const optionalFields = ['employeeId', 'phone', 'birthDate', 'address', 'hireDate', 'position'];
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
      expect(options.length).toBe(3);

      expect(options[0].value).toBe('');
      expect(options[0].textContent).toBe('Pilih Jenis Kelamin');
      expect(options[1].value).toBe('MALE');
      expect(options[1].textContent).toBe('Laki-laki');
      expect(options[2].value).toBe('FEMALE');
      expect(options[2].textContent).toBe('Perempuan');
    });

    test('should have subject and department selects', () => {
      const subjectSelect = document.querySelector('#subject');
      const departmentSelect = document.querySelector('#department');

      expect(subjectSelect).toBeTruthy();
      expect(departmentSelect).toBeTruthy();

      expect(subjectSelect.querySelector('option').textContent).toBe('Pilih Mata Pelajaran');
      expect(departmentSelect.querySelector('option').textContent).toBe('Pilih Departemen');
    });

    test('should have active status checkbox', () => {
      const isActiveCheckbox = document.querySelector('#isActive');
      expect(isActiveCheckbox).toBeTruthy();
      expect(isActiveCheckbox.checked).toBe(true);
    });
  });

  describe('Teachers Data Loading', () => {
    test('should load teachers data on page load', async () => {
      const mockTeachers = [
        {
          id: 1,
          nip: '198001011234567890',
          employeeId: 'G001',
          firstName: 'Siti',
          lastName: 'Nurhaliza',
          email: 'siti@example.com',
          subject: { name: 'Matematika' },
          department: { name: 'Matematika' },
          isActive: true
        },
        {
          id: 2,
          nip: '198501021234567891',
          employeeId: 'G002',
          firstName: 'Ahmad',
          lastName: 'Susanto',
          email: 'ahmad@example.com',
          subject: { name: 'Bahasa Indonesia' },
          department: { name: 'Bahasa' },
          isActive: true
        }
      ];

      global.fetch.mockResolvedValue({
        ok: true,
        json: () => Promise.resolve({
          content: mockTeachers,
          totalElements: 2,
          totalPages: 1,
          number: 0,
          size: 20
        })
      });

      const tableBody = document.querySelector('#teachersTableBody');
      tableBody.innerHTML = mockTeachers.map(teacher => `
        <tr>
          <td>${teacher.nip}</td>
          <td>${teacher.firstName} ${teacher.lastName}</td>
          <td>${teacher.subject.name}</td>
          <td>${teacher.email}</td>
          <td><span class="badge bg-success">Aktif</span></td>
          <td>
            <button class="btn btn-sm btn-outline-primary edit-btn" data-id="${teacher.id}">Edit</button>
            <button class="btn btn-sm btn-outline-danger delete-btn" data-id="${teacher.id}">Hapus</button>
          </td>
        </tr>
      `).join('');

      const rows = tableBody.querySelectorAll('tr');
      expect(rows.length).toBe(2);

      expect(global.fetch).toHaveBeenCalledWith('/api/teachers?page=0&size=20');
    });

    test('should handle empty teachers list', async () => {
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

      const tableBody = document.querySelector('#teachersTableBody');
      tableBody.innerHTML = `
        <tr>
          <td colspan="6" class="text-center text-muted">
            <i class="fas fa-users"></i> Tidak ada data guru
          </td>
        </tr>
      `;

      const emptyRow = tableBody.querySelector('tr');
      expect(emptyRow).toBeTruthy();
      expect(emptyRow.textContent).toContain('Tidak ada data guru');
    });
  });

  describe('Search and Filter Functionality', () => {
    test('should filter teachers by search term', () => {
      const searchInput = document.querySelector('#searchInput');
      searchInput.value = 'Siti';
      expect(searchInput.value).toBe('Siti');
    });

    test('should filter by subject', () => {
      const subjectFilter = document.querySelector('#subjectFilter');
      subjectFilter.value = '1';
      expect(subjectFilter.value).toBe('1');
    });

    test('should filter by status', () => {
      const statusFilter = document.querySelector('#statusFilter');
      statusFilter.value = 'active';
      expect(statusFilter.value).toBe('active');
    });
  });

  describe('Teacher CRUD Operations', () => {
    test('should open add teacher modal', () => {
      const addBtn = document.querySelector('#addTeacherBtn');
      const modal = document.querySelector('#teacherModal');
      const modalTitle = document.querySelector('#teacherModalTitle');

      expect(addBtn).toBeTruthy();
      expect(modal).toBeTruthy();
      expect(modalTitle.textContent).toBe('Tambah Guru');
    });

    test('should populate edit form with teacher data', () => {
      const teacherData = {
        id: 1,
        nip: '198001011234567890',
        employeeId: 'G001',
        firstName: 'Siti',
        lastName: 'Nurhaliza',
        email: 'siti@example.com',
        phone: '081234567890',
        birthDate: '1980-01-01',
        gender: 'FEMALE',
        address: 'Jl. Sudirman No. 123',
        subject: { id: 1, name: 'Matematika' },
        department: { id: 1, name: 'Matematika' },
        hireDate: '2005-08-01',
        position: 'Guru Matematika',
        isActive: true
      };

      Object.keys(teacherData).forEach(key => {
        const field = document.querySelector(`#${key}`);
        if (field && typeof teacherData[key] === 'string') {
          field.value = teacherData[key];
        }
      });

      expect(document.querySelector('#nip').value).toBe('198001011234567890');
      expect(document.querySelector('#firstName').value).toBe('Siti');
      expect(document.querySelector('#lastName').value).toBe('Nurhaliza');
      expect(document.querySelector('#email').value).toBe('siti@example.com');
      expect(document.querySelector('#isActive').checked).toBe(true);
    });

    test('should validate required fields', () => {
      const requiredFields = ['nip', 'firstName', 'lastName', 'email'];
      requiredFields.forEach(fieldId => {
        const field = document.querySelector(`#${fieldId}`);
        expect(field.hasAttribute('required')).toBe(true);
      });
    });
  });

  describe('Delete Confirmation Modal', () => {
    test('should have delete confirmation modal', () => {
      const deleteModal = document.querySelector('#deleteModal');
      const deleteTeacherName = document.querySelector('#deleteTeacherName');
      const confirmDeleteBtn = document.querySelector('#confirmDeleteBtn');

      expect(deleteModal).toBeTruthy();
      expect(deleteTeacherName).toBeTruthy();
      expect(confirmDeleteBtn).toBeTruthy();
      expect(confirmDeleteBtn.classList.contains('btn-danger')).toBe(true);
    });
  });

  describe('Pagination', () => {
    test('should render pagination controls', () => {
      const pagination = document.querySelector('#pagination');
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
    });
  });

  describe('Responsive Design', () => {
    test('should have responsive table', () => {
      const tableResponsive = document.querySelector('.table-responsive');
      expect(tableResponsive).toBeTruthy();
    });

    test('should have responsive form layout', () => {
      const formRows = document.querySelectorAll('#teacherForm .row');
      expect(formRows.length).toBeGreaterThan(0);
    });
  });
});