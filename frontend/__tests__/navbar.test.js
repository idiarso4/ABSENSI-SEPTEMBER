const { JSDOM } = require('jsdom');

// Mock fetch globally
global.fetch = jest.fn();

describe('Navbar Component Tests', () => {
  let dom;
  let document;

  beforeEach(() => {
    // Setup JSDOM
    const html = `
      <!DOCTYPE html>
      <html>
        <body>
          <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
            <div class="container">
              <a class="navbar-brand" href="/">SIM Sekolah</a>
              <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
              </button>
              <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                  <li class="nav-item"><a class="nav-link" href="/dashboard">Dashboard</a></li>

                  <!-- Akademik -->
                  <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="academicDropdown" role="button" data-bs-toggle="dropdown">Akademik</a>
                    <ul class="dropdown-menu" aria-labelledby="academicDropdown">
                      <li><h6 class="dropdown-header">Data Siswa & Guru</h6></li>
                      <li><a class="dropdown-item" href="/students">Kelola Siswa</a></li>
                      <li><a class="dropdown-item" href="/teachers">Kelola Guru</a></li>
                      <li><hr class="dropdown-divider"></li>
                      <li><h6 class="dropdown-header">Kurikulum</h6></li>
                      <li><a class="dropdown-item" href="/subjects">Mata Pelajaran</a></li>
                      <li><a class="dropdown-item" href="/classrooms">Ruang Kelas</a></li>
                      <li><a class="dropdown-item" href="/assessments">Penilaian</a></li>
                      <li><a class="dropdown-item" href="/grades">Nilai Siswa</a></li>
                      <li><hr class="dropdown-divider"></li>
                      <li><h6 class="dropdown-header">Jadwal</h6></li>
                      <li><a class="dropdown-item" href="/schedules">Jadwal Pelajaran</a></li>
                    </ul>
                  </li>

                  <!-- Kehadiran -->
                  <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="attendanceDropdown" role="button" data-bs-toggle="dropdown">Kehadiran</a>
                    <ul class="dropdown-menu" aria-labelledby="attendanceDropdown">
                      <li><a class="dropdown-item" href="/attendance?view=daily">Kehadiran Harian</a></li>
                      <li><a class="dropdown-item" href="/attendance?view=class">Kehadiran Kelas</a></li>
                      <li><a class="dropdown-item" href="/attendance?view=reports">Laporan Kehadiran</a></li>
                    </ul>
                  </li>

                  <!-- Keuangan -->
                  <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="financeDropdown" role="button" data-bs-toggle="dropdown">Keuangan</a>
                    <ul class="dropdown-menu" aria-labelledby="financeDropdown">
                      <li><h6 class="dropdown-header">Pembayaran</h6></li>
                      <li><a class="dropdown-item" href="/payments?type=spp">SPP Siswa</a></li>
                      <li><a class="dropdown-item" href="/payments?type=activity">Kegiatan</a></li>
                      <li><a class="dropdown-item" href="/payments?type=uniform">Seragam</a></li>
                      <li><a class="dropdown-item" href="/payments?type=books">Buku</a></li>
                      <li><hr class="dropdown-divider"></li>
                      <li><a class="dropdown-item" href="/payments?view=reports">Laporan Keuangan</a></li>
                    </ul>
                  </li>

                  <!-- Laporan -->
                  <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="reportsDropdown" role="button" data-bs-toggle="dropdown">Laporan</a>
                    <ul class="dropdown-menu" aria-labelledby="reportsDropdown">
                      <li><h6 class="dropdown-header">Laporan Akademik</h6></li>
                      <li><a class="dropdown-item" href="/reports?type=academic">Nilai Akademik</a></li>
                      <li><a class="dropdown-item" href="/reports?type=attendance">Kehadiran</a></li>
                      <li><a class="dropdown-item" href="/reports?type=performance">Performa Siswa</a></li>
                      <li><hr class="dropdown-divider"></li>
                      <li><h6 class="dropdown-header">Laporan Keuangan</h6></li>
                      <li><a class="dropdown-item" href="/reports?type=payments">Pembayaran</a></li>
                      <li><a class="dropdown-item" href="/reports?type=financial">Keuangan Sekolah</a></li>
                    </ul>
                  </li>

                  <!-- Sistem -->
                  <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="systemDropdown" role="button" data-bs-toggle="dropdown">Sistem</a>
                    <ul class="dropdown-menu" aria-labelledby="systemDropdown">
                      <li><h6 class="dropdown-header">Manajemen Pengguna</h6></li>
                      <li><a class="dropdown-item" href="/users">Kelola Pengguna</a></li>
                      <li><a class="dropdown-item" href="/users?view=roles">Peran & Izin</a></li>
                      <li><hr class="dropdown-divider"></li>
                      <li><h6 class="dropdown-header">Pengaturan</h6></li>
                      <li><a class="dropdown-item" href="/settings?section=academic">Pengaturan Akademik</a></li>
                      <li><a class="dropdown-item" href="/settings?section=system">Sistem</a></li>
                      <li><a class="dropdown-item" href="/backup">Backup & Restore</a></li>
                      <li><a class="dropdown-item" href="/language">Bahasa</a></li>
                    </ul>
                  </li>
                </ul>

                <ul class="navbar-nav">
                  <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown">Akun</a>
                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
                      <li><a class="dropdown-item" href="/account">Profil Saya</a></li>
                      <li><a class="dropdown-item" href="/users">Manajemen Akun</a></li>
                      <li><a class="dropdown-item" href="/users?view=roles">Peran & Izin</a></li>
                      <li><a class="dropdown-item" href="/change-password">Ubah Password</a></li>
                      <li><hr class="dropdown-divider"></li>
                      <li><a class="dropdown-item text-danger" href="#" id="logoutBtn">Logout</a></li>
                    </ul>
                  </li>
                </ul>
              </div>
            </div>
          </nav>
        </body>
      </html>
    `;

    dom = new JSDOM(html, { url: 'http://localhost:3000' });
    document = dom.window.document;
    global.document = document;
    global.window = dom.window;
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  describe('Navbar Structure', () => {
    test('should render navbar with correct brand', () => {
      const brand = document.querySelector('.navbar-brand');
      expect(brand).toBeTruthy();
      expect(brand.textContent).toBe('SIM Sekolah');
      expect(brand.getAttribute('href')).toBe('/');
    });

    test('should have all main navigation items', () => {
      const navItems = document.querySelectorAll('.navbar-nav.me-auto .nav-item');
      expect(navItems.length).toBe(6); // Dashboard + 5 dropdowns
    });

    test('should have dashboard link', () => {
      const dashboardLink = document.querySelector('a[href="/dashboard"]');
      expect(dashboardLink).toBeTruthy();
      expect(dashboardLink.textContent).toBe('Dashboard');
    });
  });

  describe('Academic Dropdown', () => {
    test('should have academic dropdown with correct items', () => {
      const academicDropdown = document.querySelector('#academicDropdown');
      expect(academicDropdown).toBeTruthy();
      expect(academicDropdown.textContent).toBe('Akademik');

      const academicMenu = document.querySelector('[aria-labelledby="academicDropdown"]');
      expect(academicMenu).toBeTruthy();

      const academicItems = academicMenu.querySelectorAll('.dropdown-item');
      expect(academicItems.length).toBe(7); // 7 academic menu items
    });

    test('should have student management link', () => {
      const studentLink = document.querySelector('a[href="/students"]');
      expect(studentLink).toBeTruthy();
      expect(studentLink.textContent).toBe('Kelola Siswa');
    });

    test('should have teacher management link', () => {
      const teacherLink = document.querySelector('a[href="/teachers"]');
      expect(teacherLink).toBeTruthy();
      expect(teacherLink.textContent).toBe('Kelola Guru');
    });

    test('should have subject management link', () => {
      const subjectLink = document.querySelector('a[href="/subjects"]');
      expect(subjectLink).toBeTruthy();
      expect(subjectLink.textContent).toBe('Mata Pelajaran');
    });

    test('should have classroom management link', () => {
      const classroomLink = document.querySelector('a[href="/classrooms"]');
      expect(classroomLink).toBeTruthy();
      expect(classroomLink.textContent).toBe('Ruang Kelas');
    });

    test('should have assessment link', () => {
      const assessmentLink = document.querySelector('a[href="/assessments"]');
      expect(assessmentLink).toBeTruthy();
      expect(assessmentLink.textContent).toBe('Penilaian');
    });

    test('should have grades link', () => {
      const gradesLink = document.querySelector('a[href="/grades"]');
      expect(gradesLink).toBeTruthy();
      expect(gradesLink.textContent).toBe('Nilai Siswa');
    });

    test('should have schedule link', () => {
      const scheduleLink = document.querySelector('a[href="/schedules"]');
      expect(scheduleLink).toBeTruthy();
      expect(scheduleLink.textContent).toBe('Jadwal Pelajaran');
    });
  });

  describe('Attendance Dropdown', () => {
    test('should have attendance dropdown with correct items', () => {
      const attendanceDropdown = document.querySelector('#attendanceDropdown');
      expect(attendanceDropdown).toBeTruthy();

      const attendanceMenu = document.querySelector('[aria-labelledby="attendanceDropdown"]');
      const attendanceItems = attendanceMenu.querySelectorAll('.dropdown-item');
      expect(attendanceItems.length).toBe(3); // 3 attendance menu items
    });

    test('should have daily attendance link', () => {
      const dailyLink = document.querySelector('a[href="/attendance?view=daily"]');
      expect(dailyLink).toBeTruthy();
      expect(dailyLink.textContent).toBe('Kehadiran Harian');
    });

    test('should have class attendance link', () => {
      const classLink = document.querySelector('a[href="/attendance?view=class"]');
      expect(classLink).toBeTruthy();
      expect(classLink.textContent).toBe('Kehadiran Kelas');
    });

    test('should have attendance reports link', () => {
      const reportsLink = document.querySelector('a[href="/attendance?view=reports"]');
      expect(reportsLink).toBeTruthy();
      expect(reportsLink.textContent).toBe('Laporan Kehadiran');
    });
  });

  describe('Finance Dropdown', () => {
    test('should have finance dropdown with correct items', () => {
      const financeDropdown = document.querySelector('#financeDropdown');
      expect(financeDropdown).toBeTruthy();

      const financeMenu = document.querySelector('[aria-labelledby="financeDropdown"]');
      const financeItems = financeMenu.querySelectorAll('.dropdown-item');
      expect(financeItems.length).toBe(5); // 4 payment types + 1 report
    });

    test('should have SPP payment link', () => {
      const sppLink = document.querySelector('a[href="/payments?type=spp"]');
      expect(sppLink).toBeTruthy();
      expect(sppLink.textContent).toBe('SPP Siswa');
    });

    test('should have activity payment link', () => {
      const activityLink = document.querySelector('a[href="/payments?type=activity"]');
      expect(activityLink).toBeTruthy();
      expect(activityLink.textContent).toBe('Kegiatan');
    });

    test('should have uniform payment link', () => {
      const uniformLink = document.querySelector('a[href="/payments?type=uniform"]');
      expect(uniformLink).toBeTruthy();
      expect(uniformLink.textContent).toBe('Seragam');
    });

    test('should have books payment link', () => {
      const booksLink = document.querySelector('a[href="/payments?type=books"]');
      expect(booksLink).toBeTruthy();
      expect(booksLink.textContent).toBe('Buku');
    });

    test('should have financial reports link', () => {
      const reportsLink = document.querySelector('a[href="/payments?view=reports"]');
      expect(reportsLink).toBeTruthy();
      expect(reportsLink.textContent).toBe('Laporan Keuangan');
    });
  });

  describe('Reports Dropdown', () => {
    test('should have reports dropdown with correct items', () => {
      const reportsDropdown = document.querySelector('#reportsDropdown');
      expect(reportsDropdown).toBeTruthy();

      const reportsMenu = document.querySelector('[aria-labelledby="reportsDropdown"]');
      const reportsItems = reportsMenu.querySelectorAll('.dropdown-item');
      expect(reportsItems.length).toBe(5); // 3 academic + 2 financial reports
    });

    test('should have academic reports link', () => {
      const academicLink = document.querySelector('a[href="/reports?type=academic"]');
      expect(academicLink).toBeTruthy();
      expect(academicLink.textContent).toBe('Nilai Akademik');
    });

    test('should have attendance reports link', () => {
      const attendanceLink = document.querySelector('a[href="/reports?type=attendance"]');
      expect(attendanceLink).toBeTruthy();
      expect(attendanceLink.textContent).toBe('Kehadiran');
    });

    test('should have performance reports link', () => {
      const performanceLink = document.querySelector('a[href="/reports?type=performance"]');
      expect(performanceLink).toBeTruthy();
      expect(performanceLink.textContent).toBe('Performa Siswa');
    });

    test('should have payment reports link', () => {
      const paymentLink = document.querySelector('a[href="/reports?type=payments"]');
      expect(paymentLink).toBeTruthy();
      expect(paymentLink.textContent).toBe('Pembayaran');
    });

    test('should have financial reports link', () => {
      const financialLink = document.querySelector('a[href="/reports?type=financial"]');
      expect(financialLink).toBeTruthy();
      expect(financialLink.textContent).toBe('Keuangan Sekolah');
    });
  });

  describe('System Dropdown', () => {
    test('should have system dropdown with correct items', () => {
      const systemDropdown = document.querySelector('#systemDropdown');
      expect(systemDropdown).toBeTruthy();

      const systemMenu = document.querySelector('[aria-labelledby="systemDropdown"]');
      const systemItems = systemMenu.querySelectorAll('.dropdown-item');
      expect(systemItems.length).toBe(6); // 2 user management + 4 settings
    });

    test('should have user management link', () => {
      const userLink = document.querySelector('a[href="/users"]');
      expect(userLink).toBeTruthy();
      expect(userLink.textContent).toBe('Kelola Pengguna');
    });

    test('should have roles management link', () => {
      const rolesLink = document.querySelector('a[href="/users?view=roles"]');
      expect(rolesLink).toBeTruthy();
      expect(rolesLink.textContent).toBe('Peran & Izin');
    });

    test('should have academic settings link', () => {
      const academicSettingsLink = document.querySelector('a[href="/settings?section=academic"]');
      expect(academicSettingsLink).toBeTruthy();
      expect(academicSettingsLink.textContent).toBe('Pengaturan Akademik');
    });

    test('should have system settings link', () => {
      const systemSettingsLink = document.querySelector('a[href="/settings?section=system"]');
      expect(systemSettingsLink).toBeTruthy();
      expect(systemSettingsLink.textContent).toBe('Sistem');
    });

    test('should have backup link', () => {
      const backupLink = document.querySelector('a[href="/backup"]');
      expect(backupLink).toBeTruthy();
      expect(backupLink.textContent).toBe('Backup & Restore');
    });

    test('should have language link', () => {
      const languageLink = document.querySelector('a[href="/language"]');
      expect(languageLink).toBeTruthy();
      expect(languageLink.textContent).toBe('Bahasa');
    });
  });

  describe('User Account Dropdown', () => {
    test('should have user account dropdown', () => {
      const userDropdown = document.querySelector('#userDropdown');
      expect(userDropdown).toBeTruthy();
      expect(userDropdown.textContent).toBe('Akun');
    });

    test('should have account menu items', () => {
      const userMenu = document.querySelector('[aria-labelledby="userDropdown"]');
      const userItems = userMenu.querySelectorAll('.dropdown-item');
      expect(userItems.length).toBe(5); // 4 menu items + logout
    });

    test('should have profile link', () => {
      const profileLink = document.querySelector('a[href="/account"]');
      expect(profileLink).toBeTruthy();
      expect(profileLink.textContent).toBe('Profil Saya');
    });

    test('should have change password link', () => {
      const passwordLink = document.querySelector('a[href="/change-password"]');
      expect(passwordLink).toBeTruthy();
      expect(passwordLink.textContent).toBe('Ubah Password');
    });

    test('should have logout button', () => {
      const logoutBtn = document.querySelector('#logoutBtn');
      expect(logoutBtn).toBeTruthy();
      expect(logoutBtn.textContent).toBe('Logout');
      expect(logoutBtn.classList.contains('text-danger')).toBe(true);
    });
  });

  describe('Logout Functionality', () => {
    test('should have logout button with correct attributes', () => {
      const logoutBtn = document.querySelector('#logoutBtn');

      expect(logoutBtn).toBeTruthy();
      expect(logoutBtn.textContent).toBe('Logout');
      expect(logoutBtn.classList.contains('text-danger')).toBe(true);
      expect(logoutBtn.getAttribute('href')).toBe('#');
    });

    test('should have logout button in user dropdown', () => {
      const userDropdown = document.querySelector('#userDropdown');
      const logoutBtn = document.querySelector('#logoutBtn');

      expect(userDropdown).toBeTruthy();
      expect(logoutBtn).toBeTruthy();

      // Check that logout button is inside the user dropdown menu
      const userMenu = document.querySelector('[aria-labelledby="userDropdown"]');
      const logoutItem = userMenu.querySelector('#logoutBtn');

      expect(logoutItem).toBeTruthy();
    });
  });

  describe('Responsive Design', () => {
    test('should have navbar toggler for mobile', () => {
      const toggler = document.querySelector('.navbar-toggler');
      expect(toggler).toBeTruthy();
      expect(toggler.getAttribute('data-bs-target')).toBe('#navbarNav');
    });

    test('should have collapsible navbar content', () => {
      const collapseDiv = document.querySelector('.collapse.navbar-collapse');
      expect(collapseDiv).toBeTruthy();
      expect(collapseDiv.id).toBe('navbarNav');
    });
  });
});