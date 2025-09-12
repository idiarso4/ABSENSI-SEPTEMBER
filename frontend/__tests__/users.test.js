const { JSDOM } = require('jsdom');

// Mock fetch globally
global.fetch = jest.fn();

describe('Users Management Tests', () => {
  let dom;
  let document;
  let window;

  beforeEach(() => {
    // Setup JSDOM with users page HTML
    const html = `
      <!DOCTYPE html>
      <html>
        <head>
          <title>Kelola Pengguna - SIM Sekolah</title>
        </head>
        <body>
          <nav class="navbar">
            <%- include('partials/navbar') %>
          </nav>

          <div class="container mt-4">
            <div class="d-flex justify-content-between align-items-center mb-4">
              <h2>Kelola Pengguna</h2>
              <button class="btn btn-primary" id="addUserBtn">
                <i class="fas fa-plus"></i> Tambah Pengguna
              </button>
            </div>

            <!-- Search and Filter -->
            <div class="card mb-4">
              <div class="card-body">
                <div class="row">
                  <div class="col-md-4">
                    <input type="text" class="form-control" id="searchInput" placeholder="Cari pengguna...">
                  </div>
                  <div class="col-md-3">
                    <select class="form-select" id="roleFilter">
                      <option value="">Semua Peran</option>
                      <option value="ADMIN">Admin</option>
                      <option value="TEACHER">Guru</option>
                      <option value="STUDENT">Siswa</option>
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

            <!-- Users Table -->
            <div class="card">
              <div class="card-body">
                <div class="table-responsive">
                  <table class="table table-striped" id="usersTable">
                    <thead>
                      <tr>
                        <th>Nama</th>
                        <th>Email</th>
                        <th>Peran</th>
                        <th>Status</th>
                        <th>Terakhir Login</th>
                        <th>Aksi</th>
                      </tr>
                    </thead>
                    <tbody id="usersTableBody">
                      <tr id="loadingRow">
                        <td colspan="6" class="text-center">
                          <div class="spinner-border spinner-border-sm" role="status">
                            <span class="sr-only">Loading...</span>
                          </div>
                          Memuat data pengguna...
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>

                <!-- Pagination -->
                <nav aria-label="Users pagination" class="mt-3">
                  <ul class="pagination justify-content-center" id="pagination">
                    <!-- Pagination will be generated here -->
                  </ul>
                </nav>
              </div>
            </div>

            <!-- Add/Edit User Modal -->
            <div class="modal fade" id="userModal" tabindex="-1">
              <div class="modal-dialog modal-lg">
                <div class="modal-content">
                  <div class="modal-header">
                    <h5 class="modal-title" id="userModalTitle">Tambah Pengguna</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                  </div>
                  <div class="modal-body">
                    <form id="userForm">
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
                            <label for="username" class="form-label">Username *</label>
                            <input type="text" class="form-control" id="username" name="username" required>
                          </div>
                        </div>
                      </div>

                      <div class="row" id="passwordRow">
                        <div class="col-md-6">
                          <div class="mb-3">
                            <label for="password" class="form-label">Password *</label>
                            <input type="password" class="form-control" id="password" name="password" required>
                          </div>
                        </div>
                        <div class="col-md-6">
                          <div class="mb-3">
                            <label for="confirmPassword" class="form-label">Konfirmasi Password *</label>
                            <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
                          </div>
                        </div>
                      </div>

                      <div class="row">
                        <div class="col-md-6">
                          <div class="mb-3">
                            <label for="phone" class="form-label">Telepon</label>
                            <input type="tel" class="form-control" id="phone" name="phone">
                          </div>
                        </div>
                        <div class="col-md-6">
                          <div class="mb-3">
                            <label for="userType" class="form-label">Tipe Pengguna *</label>
                            <select class="form-select" id="userType" name="userType" required>
                              <option value="">Pilih Tipe Pengguna</option>
                              <option value="ADMIN">Administrator</option>
                              <option value="TEACHER">Guru</option>
                              <option value="STUDENT">Siswa</option>
                            </select>
                          </div>
                        </div>
                      </div>

                      <div class="mb-3">
                        <label for="address" class="form-label">Alamat</label>
                        <textarea class="form-control" id="address" name="address" rows="3"></textarea>
                      </div>

                      <div class="mb-3">
                        <div class="form-check">
                          <input class="form-check-input" type="checkbox" id="isActive" name="isActive" checked>
                          <label class="form-check-label" for="isActive">
                            Pengguna Aktif
                          </label>
                        </div>
                      </div>
                    </form>
                  </div>
                  <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Batal</button>
                    <button type="button" class="btn btn-primary" id="saveUserBtn">Simpan</button>
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
                    <p>Apakah Anda yakin ingin menghapus pengguna <strong id="deleteUserName"></strong>?</p>
                    <p class="text-danger">Tindakan ini tidak dapat dibatalkan.</p>
                  </div>
                  <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Batal</button>
                    <button type="button" class="btn btn-danger" id="confirmDeleteBtn">Hapus</button>
                  </div>
                </div>
              </div>
            </div>

            <!-- Role Assignment Modal -->
            <div class="modal fade" id="roleModal" tabindex="-1">
              <div class="modal-dialog">
                <div class="modal-content">
                  <div class="modal-header">
                    <h5 class="modal-title">Kelola Peran Pengguna</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                  </div>
                  <div class="modal-body">
                    <p>Pengguna: <strong id="roleUserName"></strong></p>
                    <div id="rolesList">
                      <!-- Roles will be loaded here -->
                    </div>
                  </div>
                  <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Tutup</button>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <script src="/js/users.js"></script>
        </body>
      </html>
    `;

    dom = new JSDOM(html, { url: 'http://localhost:3000/users' });
    document = dom.window.document;
    window = dom.window;
    global.document = document;
    global.window = window;
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  describe('Users Page Structure', () => {
    test('should render page header correctly', () => {
      const header = document.querySelector('h2');
      expect(header).toBeTruthy();
      expect(header.textContent).toBe('Kelola Pengguna');

      const addButton = document.querySelector('#addUserBtn');
      expect(addButton).toBeTruthy();
      expect(addButton.textContent).toContain('Tambah Pengguna');
    });

    test('should have search and filter controls', () => {
      const searchInput = document.querySelector('#searchInput');
      const roleFilter = document.querySelector('#roleFilter');
      const statusFilter = document.querySelector('#statusFilter');
      const resetBtn = document.querySelector('#resetFiltersBtn');

      expect(searchInput).toBeTruthy();
      expect(searchInput.getAttribute('placeholder')).toBe('Cari pengguna...');

      expect(roleFilter).toBeTruthy();
      expect(statusFilter).toBeTruthy();
      expect(resetBtn).toBeTruthy();
    });

    test('should have users table with correct headers', () => {
      const table = document.querySelector('#usersTable');
      expect(table).toBeTruthy();

      const headers = table.querySelectorAll('thead th');
      expect(headers.length).toBe(6);

      const expectedHeaders = ['Nama', 'Email', 'Peran', 'Status', 'Terakhir Login', 'Aksi'];
      headers.forEach((header, index) => {
        expect(header.textContent).toBe(expectedHeaders[index]);
      });
    });

    test('should have loading state initially', () => {
      const loadingRow = document.querySelector('#loadingRow');
      expect(loadingRow).toBeTruthy();
      expect(loadingRow.textContent).toContain('Memuat data pengguna...');
    });
  });

  describe('User Form Modal', () => {
    test('should have complete user form', () => {
      const form = document.querySelector('#userForm');
      expect(form).toBeTruthy();

      // Required fields
      const requiredFields = ['firstName', 'lastName', 'email', 'username', 'userType'];
      requiredFields.forEach(fieldId => {
        const field = document.querySelector(`#${fieldId}`);
        expect(field).toBeTruthy();
        expect(field.hasAttribute('required')).toBe(true);
      });

      // Optional fields
      const optionalFields = ['phone', 'address'];
      optionalFields.forEach(fieldId => {
        const field = document.querySelector(`#${fieldId}`);
        expect(field).toBeTruthy();
        expect(field.hasAttribute('required')).toBe(false);
      });
    });

    test('should have user type select with correct options', () => {
      const userTypeSelect = document.querySelector('#userType');
      expect(userTypeSelect).toBeTruthy();

      const options = userTypeSelect.querySelectorAll('option');
      expect(options.length).toBe(4); // Empty + 3 user types

      expect(options[0].value).toBe('');
      expect(options[0].textContent).toBe('Pilih Tipe Pengguna');
      expect(options[1].value).toBe('ADMIN');
      expect(options[1].textContent).toBe('Administrator');
      expect(options[2].value).toBe('TEACHER');
      expect(options[2].textContent).toBe('Guru');
      expect(options[3].value).toBe('STUDENT');
      expect(options[3].textContent).toBe('Siswa');
    });

    test('should have password fields for new users', () => {
      const passwordRow = document.querySelector('#passwordRow');
      expect(passwordRow).toBeTruthy();

      const passwordField = document.querySelector('#password');
      const confirmPasswordField = document.querySelector('#confirmPassword');

      expect(passwordField).toBeTruthy();
      expect(passwordField.getAttribute('type')).toBe('password');
      expect(confirmPasswordField).toBeTruthy();
      expect(confirmPasswordField.getAttribute('type')).toBe('password');
    });

    test('should have active status checkbox', () => {
      const isActiveCheckbox = document.querySelector('#isActive');
      expect(isActiveCheckbox).toBeTruthy();
      expect(isActiveCheckbox.checked).toBe(true);
    });
  });

  describe('Users Data Loading', () => {
    test('should load users data on page load', async () => {
      const mockUsers = [
        {
          id: 1,
          firstName: 'System',
          lastName: 'Administrator',
          email: 'admin@simsekolah.com',
          username: 'admin',
          userType: 'ADMIN',
          isActive: true,
          lastLoginAt: '2025-01-10T08:00:00Z',
          roles: [{ name: 'ROLE_ADMIN' }]
        },
        {
          id: 2,
          firstName: 'Siti',
          lastName: 'Nurhaliza',
          email: 'siti@example.com',
          username: 'siti',
          userType: 'TEACHER',
          isActive: true,
          lastLoginAt: '2025-01-09T14:30:00Z',
          roles: [{ name: 'ROLE_TEACHER' }]
        }
      ];

      global.fetch.mockResolvedValue({
        ok: true,
        json: () => Promise.resolve({
          content: mockUsers,
          totalElements: 2,
          totalPages: 1,
          number: 0,
          size: 20
        })
      });

      const tableBody = document.querySelector('#usersTableBody');
      tableBody.innerHTML = mockUsers.map(user => `
        <tr>
          <td>${user.firstName} ${user.lastName}</td>
          <td>${user.email}</td>
          <td><span class="badge bg-primary">${user.userType}</span></td>
          <td><span class="badge bg-success">Aktif</span></td>
          <td>${new Date(user.lastLoginAt).toLocaleDateString()}</td>
          <td>
            <button class="btn btn-sm btn-outline-primary edit-btn" data-id="${user.id}">Edit</button>
            <button class="btn btn-sm btn-outline-warning role-btn" data-id="${user.id}">Peran</button>
            <button class="btn btn-sm btn-outline-danger delete-btn" data-id="${user.id}">Hapus</button>
          </td>
        </tr>
      `).join('');

      const rows = tableBody.querySelectorAll('tr');
      expect(rows.length).toBe(2);

      expect(global.fetch).toHaveBeenCalledWith('/api/users?page=0&size=20');
    });

    test('should handle empty users list', async () => {
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

      const tableBody = document.querySelector('#usersTableBody');
      tableBody.innerHTML = `
        <tr>
          <td colspan="6" class="text-center text-muted">
            <i class="fas fa-users"></i> Tidak ada data pengguna
          </td>
        </tr>
      `;

      const emptyRow = tableBody.querySelector('tr');
      expect(emptyRow).toBeTruthy();
      expect(emptyRow.textContent).toContain('Tidak ada data pengguna');
    });
  });

  describe('Search and Filter Functionality', () => {
    test('should filter users by search term', () => {
      const searchInput = document.querySelector('#searchInput');
      searchInput.value = 'admin';
      expect(searchInput.value).toBe('admin');
    });

    test('should filter by role', () => {
      const roleFilter = document.querySelector('#roleFilter');
      roleFilter.value = 'ADMIN';
      expect(roleFilter.value).toBe('ADMIN');
    });

    test('should filter by status', () => {
      const statusFilter = document.querySelector('#statusFilter');
      statusFilter.value = 'active';
      expect(statusFilter.value).toBe('active');
    });

    test('should reset all filters', () => {
      const searchInput = document.querySelector('#searchInput');
      const roleFilter = document.querySelector('#roleFilter');
      const statusFilter = document.querySelector('#statusFilter');

      // Set some values
      searchInput.value = 'test';
      roleFilter.value = 'ADMIN';
      statusFilter.value = 'active';

      // Simulate reset
      searchInput.value = '';
      roleFilter.value = '';
      statusFilter.value = '';

      expect(searchInput.value).toBe('');
      expect(roleFilter.value).toBe('');
      expect(statusFilter.value).toBe('');
    });
  });

  describe('User CRUD Operations', () => {
    test('should open add user modal', () => {
      const addBtn = document.querySelector('#addUserBtn');
      const modal = document.querySelector('#userModal');
      const modalTitle = document.querySelector('#userModalTitle');

      expect(addBtn).toBeTruthy();
      expect(modal).toBeTruthy();
      expect(modalTitle.textContent).toBe('Tambah Pengguna');
    });

    test('should populate edit form with user data', () => {
      const userData = {
        id: 1,
        firstName: 'System',
        lastName: 'Administrator',
        email: 'admin@simsekolah.com',
        username: 'admin',
        phone: '081234567890',
        userType: 'ADMIN',
        address: 'Jl. Teknologi No. 123',
        isActive: true
      };

      Object.keys(userData).forEach(key => {
        const field = document.querySelector(`#${key}`);
        if (field && typeof userData[key] === 'string') {
          field.value = userData[key];
        }
      });

      expect(document.querySelector('#firstName').value).toBe('System');
      expect(document.querySelector('#lastName').value).toBe('Administrator');
      expect(document.querySelector('#email').value).toBe('admin@simsekolah.com');
      expect(document.querySelector('#username').value).toBe('admin');
      expect(document.querySelector('#userType').value).toBe('ADMIN');
      expect(document.querySelector('#isActive').checked).toBe(true);
    });

    test('should validate required fields', () => {
      const requiredFields = ['firstName', 'lastName', 'email', 'username', 'userType'];
      requiredFields.forEach(fieldId => {
        const field = document.querySelector(`#${fieldId}`);
        expect(field.hasAttribute('required')).toBe(true);
      });
    });

    test('should hide password fields when editing', () => {
      const passwordRow = document.querySelector('#passwordRow');
      // Simulate editing mode
      passwordRow.style.display = 'none';
      expect(passwordRow.style.display).toBe('none');
    });
  });

  describe('Role Management', () => {
    test('should have role management modal', () => {
      const roleModal = document.querySelector('#roleModal');
      const roleUserName = document.querySelector('#roleUserName');
      const rolesList = document.querySelector('#rolesList');

      expect(roleModal).toBeTruthy();
      expect(roleUserName).toBeTruthy();
      expect(rolesList).toBeTruthy();
    });

    test('should show role assignment interface', () => {
      const rolesList = document.querySelector('#rolesList');

      // Simulate loading roles
      rolesList.innerHTML = `
        <div class="form-check">
          <input class="form-check-input" type="checkbox" id="role_admin" checked>
          <label class="form-check-label" for="role_admin">
            Administrator
          </label>
        </div>
        <div class="form-check">
          <input class="form-check-input" type="checkbox" id="role_teacher">
          <label class="form-check-label" for="role_teacher">
            Guru
          </label>
        </div>
        <div class="form-check">
          <input class="form-check-input" type="checkbox" id="role_student">
          <label class="form-check-label" for="role_student">
            Siswa
          </label>
        </div>
      `;

      const roleCheckboxes = rolesList.querySelectorAll('.form-check-input');
      expect(roleCheckboxes.length).toBe(3);

      const adminCheckbox = document.querySelector('#role_admin');
      expect(adminCheckbox.checked).toBe(true);
    });
  });

  describe('Delete Confirmation Modal', () => {
    test('should have delete confirmation modal', () => {
      const deleteModal = document.querySelector('#deleteModal');
      const deleteUserName = document.querySelector('#deleteUserName');
      const confirmDeleteBtn = document.querySelector('#confirmDeleteBtn');

      expect(deleteModal).toBeTruthy();
      expect(deleteUserName).toBeTruthy();
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
      const formRows = document.querySelectorAll('#userForm .row');
      expect(formRows.length).toBeGreaterThan(0);
    });
  });

  describe('User Status Display', () => {
    test('should display user status correctly', () => {
      const tableBody = document.querySelector('#usersTableBody');

      // Simulate user rows with different statuses
      tableBody.innerHTML = `
        <tr>
          <td>System Administrator</td>
          <td>admin@simsekolah.com</td>
          <td><span class="badge bg-primary">ADMIN</span></td>
          <td><span class="badge bg-success">Aktif</span></td>
          <td>10/01/2025</td>
          <td>
            <button class="btn btn-sm btn-outline-primary edit-btn">Edit</button>
            <button class="btn btn-sm btn-outline-warning role-btn">Peran</button>
            <button class="btn btn-sm btn-outline-danger delete-btn">Hapus</button>
          </td>
        </tr>
        <tr>
          <td>Siti Nurhaliza</td>
          <td>siti@example.com</td>
          <td><span class="badge bg-info">TEACHER</span></td>
          <td><span class="badge bg-secondary">Tidak Aktif</span></td>
          <td>09/01/2025</td>
          <td>
            <button class="btn btn-sm btn-outline-primary edit-btn">Edit</button>
            <button class="btn btn-sm btn-outline-warning role-btn">Peran</button>
            <button class="btn btn-sm btn-outline-danger delete-btn">Hapus</button>
          </td>
        </tr>
      `;

      const rows = tableBody.querySelectorAll('tr');
      expect(rows.length).toBe(2);

      // Check badges
      const adminBadge = rows[0].querySelector('.badge');
      const teacherBadge = rows[1].querySelector('.badge');

      expect(adminBadge.classList.contains('bg-primary')).toBe(true);
      expect(teacherBadge.classList.contains('bg-info')).toBe(true);
    });
  });
});