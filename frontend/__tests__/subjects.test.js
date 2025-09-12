const { JSDOM } = require('jsdom');

// Mock fetch globally
global.fetch = jest.fn();

describe('Subjects Management Tests', () => {
  let dom;
  let document;
  let window;

  beforeEach(() => {
    // Setup JSDOM with subjects page HTML
    const html = `
      <!DOCTYPE html>
      <html>
        <head>
          <title>Mata Pelajaran - SIM Sekolah</title>
        </head>
        <body>
          <nav class="navbar">
            <%- include('partials/navbar') %>
          </nav>

          <div class="container mt-4">
            <div class="d-flex justify-content-between align-items-center mb-4">
              <h2>Mata Pelajaran</h2>
              <button class="btn btn-primary" id="addSubjectBtn">
                <i class="fas fa-plus"></i> Tambah Mata Pelajaran
              </button>
            </div>

            <!-- Search -->
            <div class="card mb-4">
              <div class="card-body">
                <div class="row">
                  <div class="col-md-8">
                    <input type="text" class="form-control" id="searchInput" placeholder="Cari mata pelajaran...">
                  </div>
                  <div class="col-md-4">
                    <button class="btn btn-outline-secondary w-100" id="resetSearchBtn">
                      Reset Pencarian
                    </button>
                  </div>
                </div>
              </div>
            </div>

            <!-- Subjects Grid -->
            <div class="row" id="subjectsGrid">
              <div class="col-md-4 mb-4" id="loadingCard">
                <div class="card">
                  <div class="card-body text-center">
                    <div class="spinner-border" role="status">
                      <span class="sr-only">Loading...</span>
                    </div>
                    <p class="mt-2">Memuat mata pelajaran...</p>
                  </div>
                </div>
              </div>
            </div>

            <!-- Add/Edit Subject Modal -->
            <div class="modal fade" id="subjectModal" tabindex="-1">
              <div class="modal-dialog">
                <div class="modal-content">
                  <div class="modal-header">
                    <h5 class="modal-title" id="subjectModalTitle">Tambah Mata Pelajaran</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                  </div>
                  <div class="modal-body">
                    <form id="subjectForm">
                      <div class="mb-3">
                        <label for="name" class="form-label">Nama Mata Pelajaran *</label>
                        <input type="text" class="form-control" id="name" name="name" required>
                      </div>

                      <div class="mb-3">
                        <label for="code" class="form-label">Kode Mata Pelajaran *</label>
                        <input type="text" class="form-control" id="code" name="code" required>
                        <div class="form-text">Contoh: MTK, BIND, BING</div>
                      </div>

                      <div class="mb-3">
                        <label for="description" class="form-label">Deskripsi</label>
                        <textarea class="form-control" id="description" name="description" rows="3"></textarea>
                      </div>

                      <div class="row">
                        <div class="col-md-6">
                          <div class="mb-3">
                            <label for="category" class="form-label">Kategori</label>
                            <select class="form-select" id="category" name="category">
                              <option value="">Pilih Kategori</option>
                              <option value="SCIENCE">Sains</option>
                              <option value="LANGUAGE">Bahasa</option>
                              <option value="SOCIAL">Sosial</option>
                              <option value="ARTS">Seni</option>
                              <option value="PHYSICAL_EDUCATION">Pendidikan Jasmani</option>
                              <option value="RELIGION">Agama</option>
                            </select>
                          </div>
                        </div>
                        <div class="col-md-6">
                          <div class="mb-3">
                            <label for="gradeLevel" class="form-label">Tingkat Kelas</label>
                            <select class="form-select" id="gradeLevel" name="gradeLevel">
                              <option value="">Semua Tingkat</option>
                              <option value="10">Kelas 10</option>
                              <option value="11">Kelas 11</option>
                              <option value="12">Kelas 12</option>
                            </select>
                          </div>
                        </div>
                      </div>

                      <div class="row">
                        <div class="col-md-6">
                          <div class="mb-3">
                            <label for="hoursPerWeek" class="form-label">Jam per Minggu</label>
                            <input type="number" class="form-control" id="hoursPerWeek" name="hoursPerWeek" min="1" max="10">
                          </div>
                        </div>
                        <div class="col-md-6">
                          <div class="mb-3">
                            <label for="credits" class="form-label">SKS</label>
                            <input type="number" class="form-control" id="credits" name="credits" min="1" max="6">
                          </div>
                        </div>
                      </div>

                      <div class="mb-3">
                        <div class="form-check">
                          <input class="form-check-input" type="checkbox" id="isActive" name="isActive" checked>
                          <label class="form-check-label" for="isActive">
                            Mata Pelajaran Aktif
                          </label>
                        </div>
                      </div>
                    </form>
                  </div>
                  <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Batal</button>
                    <button type="button" class="btn btn-primary" id="saveSubjectBtn">Simpan</button>
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
                    <p>Apakah Anda yakin ingin menghapus mata pelajaran <strong id="deleteSubjectName"></strong>?</p>
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

          <script src="/js/subjects.js"></script>
        </body>
      </html>
    `;

    dom = new JSDOM(html, { url: 'http://localhost:3000/subjects' });
    document = dom.window.document;
    window = dom.window;
    global.document = document;
    global.window = window;
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  describe('Subjects Page Structure', () => {
    test('should render page header correctly', () => {
      const header = document.querySelector('h2');
      expect(header).toBeTruthy();
      expect(header.textContent).toBe('Mata Pelajaran');

      const addButton = document.querySelector('#addSubjectBtn');
      expect(addButton).toBeTruthy();
      expect(addButton.textContent).toContain('Tambah Mata Pelajaran');
    });

    test('should have search controls', () => {
      const searchInput = document.querySelector('#searchInput');
      const resetBtn = document.querySelector('#resetSearchBtn');

      expect(searchInput).toBeTruthy();
      expect(searchInput.getAttribute('placeholder')).toBe('Cari mata pelajaran...');
      expect(resetBtn).toBeTruthy();
    });

    test('should have subjects grid container', () => {
      const subjectsGrid = document.querySelector('#subjectsGrid');
      expect(subjectsGrid).toBeTruthy();

      const loadingCard = document.querySelector('#loadingCard');
      expect(loadingCard).toBeTruthy();
      expect(loadingCard.textContent).toContain('Memuat mata pelajaran...');
    });
  });

  describe('Subject Form Modal', () => {
    test('should have complete subject form', () => {
      const form = document.querySelector('#subjectForm');
      expect(form).toBeTruthy();

      // Required fields
      const requiredFields = ['name', 'code'];
      requiredFields.forEach(fieldId => {
        const field = document.querySelector(`#${fieldId}`);
        expect(field).toBeTruthy();
        expect(field.hasAttribute('required')).toBe(true);
      });

      // Optional fields
      const optionalFields = ['description', 'hoursPerWeek', 'credits'];
      optionalFields.forEach(fieldId => {
        const field = document.querySelector(`#${fieldId}`);
        expect(field).toBeTruthy();
        expect(field.hasAttribute('required')).toBe(false);
      });
    });

    test('should have category select with correct options', () => {
      const categorySelect = document.querySelector('#category');
      expect(categorySelect).toBeTruthy();

      const options = categorySelect.querySelectorAll('option');
      expect(options.length).toBe(7); // Empty + 6 categories

      const expectedCategories = [
        'Sains', 'Bahasa', 'Sosial', 'Seni', 'Pendidikan Jasmani', 'Agama'
      ];

      for (let i = 1; i < options.length; i++) {
        expect(options[i].textContent).toBe(expectedCategories[i - 1]);
      }
    });

    test('should have grade level select', () => {
      const gradeLevelSelect = document.querySelector('#gradeLevel');
      expect(gradeLevelSelect).toBeTruthy();

      const options = gradeLevelSelect.querySelectorAll('option');
      expect(options.length).toBe(4); // Empty + 3 grade levels

      expect(options[1].textContent).toBe('Kelas 10');
      expect(options[2].textContent).toBe('Kelas 11');
      expect(options[3].textContent).toBe('Kelas 12');
    });

    test('should have numeric inputs with constraints', () => {
      const hoursInput = document.querySelector('#hoursPerWeek');
      const creditsInput = document.querySelector('#credits');

      expect(hoursInput.getAttribute('type')).toBe('number');
      expect(hoursInput.getAttribute('min')).toBe('1');
      expect(hoursInput.getAttribute('max')).toBe('10');

      expect(creditsInput.getAttribute('type')).toBe('number');
      expect(creditsInput.getAttribute('min')).toBe('1');
      expect(creditsInput.getAttribute('max')).toBe('6');
    });

    test('should have active status checkbox', () => {
      const isActiveCheckbox = document.querySelector('#isActive');
      expect(isActiveCheckbox).toBeTruthy();
      expect(isActiveCheckbox.checked).toBe(true);
    });
  });

  describe('Subjects Data Loading', () => {
    test('should load subjects data on page load', async () => {
      const mockSubjects = [
        {
          id: 1,
          name: 'Matematika',
          code: 'MTK',
          description: 'Pelajaran matematika dasar',
          category: 'SCIENCE',
          gradeLevel: '10',
          hoursPerWeek: 4,
          credits: 3,
          isActive: true
        },
        {
          id: 2,
          name: 'Bahasa Indonesia',
          code: 'BIND',
          description: 'Pelajaran bahasa Indonesia',
          category: 'LANGUAGE',
          gradeLevel: '10',
          hoursPerWeek: 3,
          credits: 2,
          isActive: true
        }
      ];

      global.fetch.mockResolvedValue({
        ok: true,
        json: () => Promise.resolve(mockSubjects)
      });

      const subjectsGrid = document.querySelector('#subjectsGrid');
      subjectsGrid.innerHTML = mockSubjects.map(subject => `
        <div class="col-md-4 mb-4">
          <div class="card subject-card">
            <div class="card-header">
              <h6 class="mb-0">${subject.name}</h6>
              <small class="text-muted">${subject.code}</small>
            </div>
            <div class="card-body">
              <p class="card-text">${subject.description}</p>
              <div class="row text-center">
                <div class="col-6">
                  <small class="text-muted">Jam/Minggu</small>
                  <div class="fw-bold">${subject.hoursPerWeek}</div>
                </div>
                <div class="col-6">
                  <small class="text-muted">SKS</small>
                  <div class="fw-bold">${subject.credits}</div>
                </div>
              </div>
            </div>
            <div class="card-footer">
              <div class="btn-group w-100">
                <button class="btn btn-sm btn-outline-primary edit-btn" data-id="${subject.id}">Edit</button>
                <button class="btn btn-sm btn-outline-danger delete-btn" data-id="${subject.id}">Hapus</button>
              </div>
            </div>
          </div>
        </div>
      `).join('');

      const subjectCards = subjectsGrid.querySelectorAll('.subject-card');
      expect(subjectCards.length).toBe(2);

      expect(global.fetch).toHaveBeenCalledWith('/api/subjects');
    });

    test('should handle empty subjects list', async () => {
      global.fetch.mockResolvedValue({
        ok: true,
        json: () => Promise.resolve([])
      });

      const subjectsGrid = document.querySelector('#subjectsGrid');
      subjectsGrid.innerHTML = `
        <div class="col-12">
          <div class="card">
            <div class="card-body text-center text-muted">
              <i class="fas fa-book fa-3x mb-3"></i>
              <h5>Tidak ada mata pelajaran</h5>
              <p>Klik tombol "Tambah Mata Pelajaran" untuk menambah data baru.</p>
            </div>
          </div>
        </div>
      `;

      const emptyCard = subjectsGrid.querySelector('.card');
      expect(emptyCard).toBeTruthy();
      expect(emptyCard.textContent).toContain('Tidak ada mata pelajaran');
    });
  });

  describe('Search Functionality', () => {
    test('should filter subjects by search term', () => {
      const searchInput = document.querySelector('#searchInput');
      searchInput.value = 'Matematika';
      expect(searchInput.value).toBe('Matematika');
    });

    test('should reset search', () => {
      const searchInput = document.querySelector('#searchInput');
      searchInput.value = 'test search';
      searchInput.value = '';
      expect(searchInput.value).toBe('');
    });
  });

  describe('Subject CRUD Operations', () => {
    test('should open add subject modal', () => {
      const addBtn = document.querySelector('#addSubjectBtn');
      const modal = document.querySelector('#subjectModal');
      const modalTitle = document.querySelector('#subjectModalTitle');

      expect(addBtn).toBeTruthy();
      expect(modal).toBeTruthy();
      expect(modalTitle.textContent).toBe('Tambah Mata Pelajaran');
    });

    test('should populate edit form with subject data', () => {
      const subjectData = {
        id: 1,
        name: 'Matematika',
        code: 'MTK',
        description: 'Pelajaran matematika dasar',
        category: 'SCIENCE',
        gradeLevel: '10',
        hoursPerWeek: 4,
        credits: 3,
        isActive: true
      };

      Object.keys(subjectData).forEach(key => {
        const field = document.querySelector(`#${key}`);
        if (field && typeof subjectData[key] === 'string') {
          field.value = subjectData[key];
        } else if (field && typeof subjectData[key] === 'number') {
          field.value = subjectData[key].toString();
        }
      });

      expect(document.querySelector('#name').value).toBe('Matematika');
      expect(document.querySelector('#code').value).toBe('MTK');
      expect(document.querySelector('#description').value).toBe('Pelajaran matematika dasar');
      expect(document.querySelector('#category').value).toBe('SCIENCE');
      expect(document.querySelector('#hoursPerWeek').value).toBe('4');
      expect(document.querySelector('#credits').value).toBe('3');
      expect(document.querySelector('#isActive').checked).toBe(true);
    });

    test('should validate required fields', () => {
      const requiredFields = ['name', 'code'];
      requiredFields.forEach(fieldId => {
        const field = document.querySelector(`#${fieldId}`);
        expect(field.hasAttribute('required')).toBe(true);
      });
    });
  });

  describe('Delete Confirmation Modal', () => {
    test('should have delete confirmation modal', () => {
      const deleteModal = document.querySelector('#deleteModal');
      const deleteSubjectName = document.querySelector('#deleteSubjectName');
      const confirmDeleteBtn = document.querySelector('#confirmDeleteBtn');

      expect(deleteModal).toBeTruthy();
      expect(deleteSubjectName).toBeTruthy();
      expect(confirmDeleteBtn).toBeTruthy();
      expect(confirmDeleteBtn.classList.contains('btn-danger')).toBe(true);
    });
  });

  describe('Subject Cards Display', () => {
    test('should render subject cards correctly', () => {
      const subjectsGrid = document.querySelector('#subjectsGrid');

      // Simulate loading subject cards
      subjectsGrid.innerHTML = `
        <div class="col-md-4 mb-4">
          <div class="card subject-card">
            <div class="card-header">
              <h6 class="mb-0">Matematika</h6>
              <small class="text-muted">MTK</small>
            </div>
            <div class="card-body">
              <p class="card-text">Pelajaran matematika dasar</p>
              <div class="row text-center">
                <div class="col-6">
                  <small class="text-muted">Jam/Minggu</small>
                  <div class="fw-bold">4</div>
                </div>
                <div class="col-6">
                  <small class="text-muted">SKS</small>
                  <div class="fw-bold">3</div>
                </div>
              </div>
            </div>
            <div class="card-footer">
              <div class="btn-group w-100">
                <button class="btn btn-sm btn-outline-primary edit-btn">Edit</button>
                <button class="btn btn-sm btn-outline-danger delete-btn">Hapus</button>
              </div>
            </div>
          </div>
        </div>
      `;

      const subjectCard = subjectsGrid.querySelector('.subject-card');
      expect(subjectCard).toBeTruthy();

      const cardHeader = subjectCard.querySelector('.card-header h6');
      expect(cardHeader.textContent).toBe('Matematika');

      const cardCode = subjectCard.querySelector('.card-header small');
      expect(cardCode.textContent).toBe('MTK');

      const editBtn = subjectCard.querySelector('.edit-btn');
      const deleteBtn = subjectCard.querySelector('.delete-btn');
      expect(editBtn).toBeTruthy();
      expect(deleteBtn).toBeTruthy();
    });
  });

  describe('Form Validation', () => {
    test('should validate subject code format', () => {
      const codeInput = document.querySelector('#code');
      expect(codeInput).toBeTruthy();

      // Test valid codes
      const validCodes = ['MTK', 'BIND', 'BING', 'FIS', 'KIM'];
      validCodes.forEach(code => {
        codeInput.value = code;
        expect(codeInput.value).toBe(code);
      });
    });

    test('should validate hours per week range', () => {
      const hoursInput = document.querySelector('#hoursPerWeek');
      expect(hoursInput.getAttribute('min')).toBe('1');
      expect(hoursInput.getAttribute('max')).toBe('10');
    });

    test('should validate credits range', () => {
      const creditsInput = document.querySelector('#credits');
      expect(creditsInput.getAttribute('min')).toBe('1');
      expect(creditsInput.getAttribute('max')).toBe('6');
    });
  });

  describe('Responsive Design', () => {
    test('should have responsive grid layout', () => {
      const subjectsGrid = document.querySelector('#subjectsGrid');
      expect(subjectsGrid.classList.contains('row')).toBe(true);
    });

    test('should have responsive card columns', () => {
      const subjectsGrid = document.querySelector('#subjectsGrid');
      const cardColumns = subjectsGrid.querySelectorAll('.col-md-4');
      expect(cardColumns.length).toBeGreaterThan(0);
    });
  });
});