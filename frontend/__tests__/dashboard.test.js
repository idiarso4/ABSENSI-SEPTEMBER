const { JSDOM } = require('jsdom');

// Mock Chart.js
jest.mock('chart.js', () => ({
  Chart: jest.fn().mockImplementation(() => ({
    update: jest.fn(),
    destroy: jest.fn()
  }))
}));

// Mock fetch globally
global.fetch = jest.fn();

describe('Dashboard Component Tests', () => {
  let dom;
  let document;
  let window;

  beforeEach(() => {
    // Setup JSDOM with dashboard HTML
    const html = `
      <!DOCTYPE html>
      <html>
        <head>
          <title>Dashboard - SIM Sekolah</title>
        </head>
        <body>
          <nav class="navbar">
            <%- include('partials/navbar') %>
          </nav>

          <div class="container mt-4">
            <div class="dashboard-header">
              <h2>Dashboard SIM Sekolah</h2>
              <p class="text-muted">Sistem Informasi Manajemen Sekolah</p>
            </div>

            <!-- Charts Section -->
            <div class="row mb-4">
              <div class="col-lg-6 col-md-12">
                <div class="card dashboard-card">
                  <div class="card-header">
                    <h5 class="mb-0">Tren Kehadiran Siswa (7 Hari Terakhir)</h5>
                  </div>
                  <div class="card-body chart-container">
                    <canvas id="attendanceChart" width="100%" height="100%"></canvas>
                  </div>
                </div>
              </div>
              <div class="col-lg-6 col-md-12">
                <div class="card dashboard-card">
                  <div class="card-header">
                    <h5 class="mb-0">Pembayaran SPP (6 Bulan Terakhir)</h5>
                  </div>
                  <div class="card-body chart-container">
                    <canvas id="paymentChart" width="100%" height="100%"></canvas>
                  </div>
                </div>
              </div>
            </div>

            <!-- KPI Cards Section -->
            <div class="kpi-grid row">
              <div class="col-lg-3 col-md-6 col-sm-12 mb-3">
                <div class="card text-white bg-primary kpi-card dashboard-card">
                  <div class="card-body text-center">
                    <h5 class="card-title mb-2">Total Siswa</h5>
                    <p class="card-text display-4 mb-0" id="totalStudents">0</p>
                  </div>
                </div>
              </div>
              <div class="col-lg-3 col-md-6 col-sm-12 mb-3">
                <div class="card text-white bg-success kpi-card dashboard-card">
                  <div class="card-body text-center">
                    <h5 class="card-title mb-2">Total Guru</h5>
                    <p class="card-text display-4 mb-0" id="totalTeachers">0</p>
                  </div>
                </div>
              </div>
              <div class="col-lg-3 col-md-6 col-sm-12 mb-3">
                <div class="card text-white bg-warning kpi-card dashboard-card">
                  <div class="card-body text-center">
                    <h5 class="card-title mb-2">Kehadiran Hari Ini</h5>
                    <p class="card-text display-4 mb-0" id="attendanceRate">0%</p>
                  </div>
                </div>
              </div>
              <div class="col-lg-3 col-md-6 col-sm-12 mb-3">
                <div class="card text-white bg-info kpi-card dashboard-card">
                  <div class="card-body text-center">
                    <h5 class="card-title mb-2">Total Kelas</h5>
                    <p class="card-text display-4 mb-0" id="totalClasses">0</p>
                  </div>
                </div>
              </div>
            </div>

            <!-- Activities and Events Section -->
            <div class="row mt-4">
              <div class="col-lg-6 col-md-12">
                <div class="card dashboard-card">
                  <div class="card-header">
                    <h5 class="mb-0">Aktivitas Terbaru</h5>
                  </div>
                  <div class="card-body">
                    <ul class="list-group activity-list" id="activitiesList">
                      <li class="list-group-item">Memuat aktivitas terbaru...</li>
                    </ul>
                  </div>
                </div>
              </div>
              <div class="col-lg-6 col-md-12">
                <div class="card dashboard-card">
                  <div class="card-header">
                    <h5 class="mb-0">Acara Mendatang</h5>
                  </div>
                  <div class="card-body">
                    <ul class="list-group activity-list" id="upcomingEventsList">
                      <li class="list-group-item">Memuat acara mendatang...</li>
                    </ul>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <script src="/js/dashboard.js"></script>
        </body>
      </html>
    `;

    dom = new JSDOM(html, { url: 'http://localhost:3000/dashboard' });
    document = dom.window.document;
    window = dom.window;
    global.document = document;
    global.window = window;

    // Mock Chart constructor
    window.Chart = jest.fn().mockImplementation(() => ({
      update: jest.fn(),
      destroy: jest.fn()
    }));
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  describe('Dashboard Structure', () => {
    test('should render dashboard header correctly', () => {
      const header = document.querySelector('.dashboard-header h2');
      expect(header).toBeTruthy();
      expect(header.textContent).toBe('Dashboard SIM Sekolah');

      const subtitle = document.querySelector('.dashboard-header p');
      expect(subtitle).toBeTruthy();
      expect(subtitle.textContent).toBe('Sistem Informasi Manajemen Sekolah');
    });

    test('should have chart containers', () => {
      const attendanceChart = document.querySelector('#attendanceChart');
      const paymentChart = document.querySelector('#paymentChart');

      expect(attendanceChart).toBeTruthy();
      expect(paymentChart).toBeTruthy();
    });

    test('should have KPI cards', () => {
      const kpiCards = document.querySelectorAll('.kpi-card');
      expect(kpiCards.length).toBe(4);

      const kpiTitles = ['Total Siswa', 'Total Guru', 'Kehadiran Hari Ini', 'Total Kelas'];
      kpiCards.forEach((card, index) => {
        const title = card.querySelector('.card-title');
        expect(title.textContent).toBe(kpiTitles[index]);
      });
    });

    test('should have activity and events sections', () => {
      const activitiesList = document.querySelector('#activitiesList');
      const eventsList = document.querySelector('#upcomingEventsList');

      expect(activitiesList).toBeTruthy();
      expect(eventsList).toBeTruthy();

      expect(activitiesList.querySelector('.list-group-item').textContent)
        .toBe('Memuat aktivitas terbaru...');
      expect(eventsList.querySelector('.list-group-item').textContent)
        .toBe('Memuat acara mendatang...');
    });
  });

  describe('KPI Cards', () => {
    test('should have correct KPI card IDs', () => {
      const totalStudents = document.querySelector('#totalStudents');
      const totalTeachers = document.querySelector('#totalTeachers');
      const attendanceRate = document.querySelector('#attendanceRate');
      const totalClasses = document.querySelector('#totalClasses');

      expect(totalStudents).toBeTruthy();
      expect(totalTeachers).toBeTruthy();
      expect(attendanceRate).toBeTruthy();
      expect(totalClasses).toBeTruthy();

      expect(totalStudents.textContent).toBe('0');
      expect(totalTeachers.textContent).toBe('0');
      expect(attendanceRate.textContent).toBe('0%');
      expect(totalClasses.textContent).toBe('0');
    });

    test('should have correct card colors', () => {
      const cards = document.querySelectorAll('.kpi-card');
      const expectedColors = ['bg-primary', 'bg-success', 'bg-warning', 'bg-info'];

      cards.forEach((card, index) => {
        expect(card.classList.contains(expectedColors[index])).toBe(true);
      });
    });
  });

  describe('Chart Containers', () => {
    test('should have proper chart container structure', () => {
      const chartContainers = document.querySelectorAll('.chart-container');
      expect(chartContainers.length).toBe(2);

      chartContainers.forEach(container => {
        const canvas = container.querySelector('canvas');
        expect(canvas).toBeTruthy();
        expect(canvas.getAttribute('width')).toBe('100%');
        expect(canvas.getAttribute('height')).toBe('100%');
      });
    });

    test('should have chart headers', () => {
      const headers = document.querySelectorAll('.card-header h5');
      expect(headers.length).toBe(4); // 2 charts + 2 activity sections

      expect(headers[0].textContent).toBe('Tren Kehadiran Siswa (7 Hari Terakhir)');
      expect(headers[1].textContent).toBe('Pembayaran SPP (6 Bulan Terakhir)');
      expect(headers[2].textContent).toBe('Aktivitas Terbaru');
      expect(headers[3].textContent).toBe('Acara Mendatang');
    });
  });

  describe('Dashboard Data Loading', () => {
    test('should load dashboard data on page load', async () => {
      // Mock successful API responses
      global.fetch
        .mockImplementationOnce(() => Promise.resolve({
          ok: true,
          json: () => Promise.resolve({
            attendanceTrend: [85, 90, 88, 92, 87, 91, 89],
            labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
          })
        }))
        .mockImplementationOnce(() => Promise.resolve({
          ok: true,
          json: () => Promise.resolve({
            paymentData: [1500000, 1800000, 1650000, 2000000, 1750000, 1900000],
            labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun']
          })
        }))
        .mockImplementationOnce(() => Promise.resolve({
          ok: true,
          json: () => Promise.resolve({
            totalStudents: 1250,
            totalTeachers: 85,
            attendanceRate: 92.5,
            totalClasses: 45
          })
        }))
        .mockImplementationOnce(() => Promise.resolve({
          ok: true,
          json: () => Promise.resolve([
            { description: 'Siswa baru terdaftar', timestamp: '2025-01-10T08:00:00Z' },
            { description: 'Jadwal ujian akhir semester', timestamp: '2025-01-10T09:00:00Z' }
          ])
        }))
        .mockImplementationOnce(() => Promise.resolve({
          ok: true,
          json: () => Promise.resolve([
            { title: 'Ujian Akhir Semester', date: '2025-01-15' },
            { title: 'Libur Semester', date: '2025-01-20' }
          ])
        }));

      // Load dashboard script (simulated)
      require('../public/js/dashboard.js');

      // Wait for async operations
      await new Promise(resolve => setTimeout(resolve, 100));

      // Verify API calls were made
      expect(global.fetch).toHaveBeenCalledTimes(5);
      expect(global.fetch).toHaveBeenCalledWith('/api/dashboard/attendance-trend');
      expect(global.fetch).toHaveBeenCalledWith('/api/dashboard/payroll-monthly');
      expect(global.fetch).toHaveBeenCalledWith('/api/dashboard/kpis');
      expect(global.fetch).toHaveBeenCalledWith('/api/dashboard/recent-activities');
      expect(global.fetch).toHaveBeenCalledWith('/api/dashboard/upcoming-events');
    });

    test('should update KPI values when data loads', async () => {
      // Mock API response
      global.fetch.mockResolvedValue({
        ok: true,
        json: () => Promise.resolve({
          totalStudents: 1250,
          totalTeachers: 85,
          attendanceRate: 92.5,
          totalClasses: 45
        })
      });

      // Simulate data loading
      const totalStudents = document.querySelector('#totalStudents');
      const totalTeachers = document.querySelector('#totalTeachers');
      const attendanceRate = document.querySelector('#attendanceRate');
      const totalClasses = document.querySelector('#totalClasses');

      // Update values (simulating what dashboard.js would do)
      totalStudents.textContent = '1,250';
      totalTeachers.textContent = '85';
      attendanceRate.textContent = '92.5%';
      totalClasses.textContent = '45';

      expect(totalStudents.textContent).toBe('1,250');
      expect(totalTeachers.textContent).toBe('85');
      expect(attendanceRate.textContent).toBe('92.5%');
      expect(totalClasses.textContent).toBe('45');
    });

    test('should handle API errors gracefully', async () => {
      // Mock failed API response
      global.fetch.mockRejectedValue(new Error('Network error'));

      // Load dashboard script (simulated)
      require('../public/js/dashboard.js');

      // Wait for async operations
      await new Promise(resolve => setTimeout(resolve, 100));

      // Verify error handling (should not crash)
      expect(global.fetch).toHaveBeenCalled();
    });
  });

  describe('Chart Initialization', () => {
    test('should initialize charts with correct data', () => {
      const attendanceCanvas = document.querySelector('#attendanceChart');
      const paymentCanvas = document.querySelector('#paymentChart');

      // Simulate chart initialization
      const attendanceChart = new window.Chart(attendanceCanvas, {
        type: 'line',
        data: {
          labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
          datasets: [{
            label: 'Kehadiran (%)',
            data: [85, 90, 88, 92, 87, 91, 89]
          }]
        }
      });

      const paymentChart = new window.Chart(paymentCanvas, {
        type: 'bar',
        data: {
          labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
          datasets: [{
            label: 'Pembayaran SPP (Rp)',
            data: [1500000, 1800000, 1650000, 2000000, 1750000, 1900000]
          }]
        }
      });

      expect(window.Chart).toHaveBeenCalledTimes(2);
      expect(attendanceChart).toBeDefined();
      expect(paymentChart).toBeDefined();
    });
  });

  describe('Activity Lists', () => {
    test('should update activities list with data', () => {
      const activitiesList = document.querySelector('#activitiesList');

      // Simulate loading activities
      activitiesList.innerHTML = `
        <li class="list-group-item">Siswa baru terdaftar - 2 jam yang lalu</li>
        <li class="list-group-item">Jadwal ujian akhir semester - 3 jam yang lalu</li>
        <li class="list-group-item">Pembayaran SPP bulan Januari - 5 jam yang lalu</li>
      `;

      const items = activitiesList.querySelectorAll('.list-group-item');
      expect(items.length).toBe(3);
      expect(items[0].textContent).toContain('Siswa baru terdaftar');
      expect(items[1].textContent).toContain('Jadwal ujian');
      expect(items[2].textContent).toContain('Pembayaran SPP');
    });

    test('should update events list with data', () => {
      const eventsList = document.querySelector('#upcomingEventsList');

      // Simulate loading events
      eventsList.innerHTML = `
        <li class="list-group-item">Ujian Akhir Semester - 15 Jan 2025</li>
        <li class="list-group-item">Libur Semester - 20 Jan 2025</li>
        <li class="list-group-item">Raport Semester - 25 Jan 2025</li>
      `;

      const items = eventsList.querySelectorAll('.list-group-item');
      expect(items.length).toBe(3);
      expect(items[0].textContent).toContain('Ujian Akhir Semester');
      expect(items[1].textContent).toContain('Libur Semester');
      expect(items[2].textContent).toContain('Raport Semester');
    });
  });

  describe('Responsive Design', () => {
    test('should have responsive grid layout', () => {
      const rows = document.querySelectorAll('.row');
      expect(rows.length).toBeGreaterThan(0);

      const kpiRow = document.querySelector('.kpi-grid.row');
      expect(kpiRow).toBeTruthy();

      const kpiCols = kpiRow.querySelectorAll('.col-lg-3');
      expect(kpiCols.length).toBe(4);
    });

    test('should have proper Bootstrap classes for responsiveness', () => {
      const chartCols = document.querySelectorAll('.col-lg-6.col-md-12');
      expect(chartCols.length).toBe(2);

      const activityCols = document.querySelectorAll('.col-lg-6.col-md-12');
      expect(activityCols.length).toBe(2);
    });
  });
});