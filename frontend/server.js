const express = require('express');
const path = require('path');
const axios = require('axios');
require('dotenv').config();

const app = express();
const PORT = process.env.PORT || 3000;

// Set EJS as templating engine
app.set('view engine', 'ejs');
app.set('views', path.join(__dirname, 'views'));

// Middleware
app.use(express.static(path.join(__dirname, 'public')));
app.use(express.urlencoded({ extended: true }));
app.use(express.json());

// Routes
app.get('/', (req, res) => {
  res.render('index');
});

// Health endpoint for smoke tests
app.get('/healthz', (req, res) => {
  res.json({ ok: true, timestamp: new Date().toISOString() });
});

// Temporary endpoint for testing API connectivity without authentication
// Place BEFORE proxy handlers so it's not intercepted
app.get('/api/test', (req, res) => {
  res.json({ message: 'Frontend backend connection works!', timestamp: new Date().toISOString() });
});

// Handle login requests and forward to backend
app.post('/auth/login', async (req, res) => {
  try {
    console.log('Proxying login request to backend...');
    console.log('Request body:', JSON.stringify(req.body, null, 2));
    const response = await axios.post('http://localhost:8080/api/auth/login', req.body, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      }
    });
    console.log('Backend response status:', response.status);
    res.json(response.data);
  } catch (error) {
    console.error('Error proxying login request:', error.message);
    if (error.response) {
      console.error('Backend error response:', error.response.data);
      // Forward the error response from the backend
      res.status(error.response.status).json({
        status: error.response.status,
        message: error.response.data?.message || 'Login failed',
        error: error.response.data?.error
      });
    } else {
      res.status(500).json({
        status: 500,
        message: 'Internal server error',
        error: error.message
      });
    }
  }
});

app.get('/login', (req, res) => {
  res.render('login');
});

app.get('/reset-password', (req, res) => {
  res.render('reset-password');
});

app.get('/dashboard', (req, res) => {
  res.render('dashboard');
});

app.get('/students', (req, res) => {
  res.render('students');
});

app.get('/teachers', (req, res) => {
  res.render('teachers');
});

app.get('/subjects', (req, res) => {
  res.render('subjects');
});

app.get('/classrooms', (req, res) => {
  res.render('classrooms');
});

app.get('/assessments', (req, res) => {
  res.render('assessments');
});

app.get('/grades', (req, res) => {
  res.render('grades');
});

app.get('/attendance', (req, res) => {
  res.render('attendance');
});

// Face + Location Attendance and Student Photo Update
app.get('/attendance/face', (req, res) => {
  res.render('attendance-face');
});

app.get('/students/photo-update', (req, res) => {
  res.render('students-photo-update');
});

app.get('/students/photo-update/manage', (req, res) => {
  res.render('students-photo-update-manage');
});

app.get('/schedules', (req, res) => {
  res.render('schedules');
});

app.get('/payments', (req, res) => {
  res.render('payments');
});

app.get('/reports', (req, res) => {
  res.render('reports');
});

// Additional pages referenced in navbar
app.get('/tasks', (req, res) => { res.render('tasks'); });
app.get('/invoices', (req, res) => { res.render('invoices'); });
app.get('/payment-reminders', (req, res) => { res.render('payment-reminders'); });
app.get('/academic-calendar', (req, res) => { res.render('academic-calendar'); });
app.get('/semester-management', (req, res) => { res.render('semester-management'); });
app.get('/attendance-calendar', (req, res) => { res.render('attendance-calendar'); });
app.get('/analytics-dashboard', (req, res) => { res.render('analytics-dashboard'); });
app.get('/export', (req, res) => { res.render('export'); });

// PKL Management
app.get('/pkl/absensi', (req, res) => { res.render('pkl/absensi'); });
app.get('/pkl/kunjungan', (req, res) => { res.render('pkl/kunjungan'); });
app.get('/pkl/kegiatan', (req, res) => { res.render('pkl/kegiatan'); });
app.get('/pkl/laporan-harian', (req, res) => { res.render('pkl/laporan-harian'); });

// Perizinan Siswa
app.get('/perizinan/izin-masuk', (req, res) => { res.render('perizinan/izin-masuk'); });
app.get('/perizinan/izin-keluar', (req, res) => { res.render('perizinan/izin-keluar'); });
app.get('/perizinan/laporan-guru-piket', (req, res) => { res.render('perizinan/laporan-guru-piket'); });

// Manajemen BK
app.get('/bk/penanganan-siswa', (req, res) => { res.render('bk/penanganan-siswa'); });
app.get('/bk/daftar-kunjungan', (req, res) => { res.render('bk/daftar-kunjungan'); });
app.get('/bk/surat-kesepakatan', (req, res) => { res.render('bk/surat-kesepakatan'); });

// LMS
app.get('/lms/absensi-guru', (req, res) => { res.render('lms/absensi-guru'); });
app.get('/lms/materi', (req, res) => { res.render('lms/materi'); });
app.get('/lms/materi/daftar-soal', (req, res) => { res.render('lms/materi/daftar-soal'); });
app.get('/lms/ulangan', (req, res) => { res.render('lms/ulangan'); });

app.get('/users', (req, res) => {
  res.render('users');
});

app.get('/settings', (req, res) => {
  res.render('settings');
});

app.get('/departments', (req, res) => {
  res.render('departments');
});

app.get('/backup', (req, res) => {
  res.render('backup');
});

app.get('/language', (req, res) => {
  res.render('language');
});

app.get('/account', (req, res) => {
  res.render('account');
});

app.get('/change-password', (req, res) => {
  res.render('change-password');
});

// Proxy API requests to Spring Boot backend with proper header forwarding
app.get('/api/*', async (req, res) => {
  try {
    const backendUrl = `http://localhost:8080${req.originalUrl}`;
    console.log('Proxying GET to:', backendUrl);
    const authHeader = req.headers.authorization || req.headers.Authorization;
    console.log('Auth header:', authHeader ? 'present' : 'missing');
    const response = await axios.get(backendUrl, {
      headers: {
        'Authorization': authHeader
      },
      responseType: 'arraybuffer' // Handle binary data properly
    });

    // Check if this is a file download (Excel, PDF, etc.)
    const contentType = response.headers['content-type'];
    if (contentType && (contentType.includes('spreadsheet') || contentType.includes('excel') || contentType.includes('pdf'))) {
      // Forward binary file response
      res.setHeader('Content-Type', contentType);
      if (response.headers['content-disposition']) {
        res.setHeader('Content-Disposition', response.headers['content-disposition']);
      }
      if (response.headers['content-length']) {
        res.setHeader('Content-Length', response.headers['content-length']);
      }
      res.send(Buffer.from(response.data));
    } else {
      // Regular JSON response
      res.json(JSON.parse(Buffer.from(response.data).toString()));
    }
  } catch (error) {
    console.log('Backend error:', error.response?.status, error.response?.data);
    res.status(error.response?.status || 500).json(error.response?.data || { error: 'Failed to fetch data from backend' });
  }
});

app.post('/api/*', async (req, res) => {
  try {
    const backendUrl = `http://localhost:8080${req.originalUrl}`;
    console.log('Proxying POST to:', backendUrl);
    const authHeader = req.headers.authorization || req.headers.Authorization;

    const isMultipart = (req.headers['content-type'] || '').includes('multipart/form-data');

    const axiosConfig = {
      headers: {
        ...Object.fromEntries(Object.entries(req.headers).filter(([k]) => !['host', 'content-length'].includes(k.toLowerCase()))),
        'Authorization': authHeader
      },
      maxContentLength: Infinity,
      maxBodyLength: Infinity,
      responseType: 'arraybuffer' // allow binary responses for exports/templates
    };

    // Ensure proper content-type for JSON posts
    if (!isMultipart) {
      axiosConfig.headers['Content-Type'] = 'application/json';
    }

    const postData = isMultipart ? req /* stream body as-is */ : req.body;
    const response = await axios.post(backendUrl, postData, axiosConfig);

    const contentType = response.headers['content-type'];
    if (contentType && (contentType.includes('spreadsheet') || contentType.includes('excel') || contentType.includes('pdf') || contentType.includes('octet-stream'))) {
      // Forward binary file response
      res.setHeader('Content-Type', contentType);
      if (response.headers['content-disposition']) {
        res.setHeader('Content-Disposition', response.headers['content-disposition']);
      }
      if (response.headers['content-length']) {
        res.setHeader('Content-Length', response.headers['content-length']);
      }
      res.send(Buffer.from(response.data));
    } else {
      // Attempt to parse JSON from buffer/string
      let data = response.data;
      if (Buffer.isBuffer(data)) {
        try { data = JSON.parse(Buffer.from(data).toString()); } catch (_) {}
      }
      res.json(data);
    }
  } catch (error) {
    console.log('Backend error:', error.response?.status);
    if (error.response && error.response.data) {
      const contentType = error.response.headers?.['content-type'] || '';
      if (contentType.includes('application/json')) {
        try {
          const json = Buffer.isBuffer(error.response.data)
            ? JSON.parse(Buffer.from(error.response.data).toString())
            : error.response.data;
          return res.status(error.response.status).json(json);
        } catch (_) {}
      }
      return res.status(error.response.status).send(error.response.data);
    }
    res.status(500).json({ error: 'Failed to send data to backend' });
  }
});

app.put('/api/*', async (req, res) => {
  try {
    const backendUrl = `http://localhost:8080${req.originalUrl}`;
    console.log('Proxying PUT to:', backendUrl);
    const authHeader = req.headers.authorization || req.headers.Authorization;
    const response = await axios.put(backendUrl, req.body, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': authHeader
      }
    });
    res.json(response.data);
  } catch (error) {
    console.log('Backend error:', error.response?.status, error.response?.data);
    res.status(error.response?.status || 500).json(error.response?.data || { error: 'Failed to update data in backend' });
  }
});

app.delete('/api/*', async (req, res) => {
  try {
    const backendUrl = `http://localhost:8080${req.originalUrl}`;
    console.log('Proxying DELETE to:', backendUrl);
    const authHeader = req.headers.authorization || req.headers.Authorization;
    const response = await axios.delete(backendUrl, {
      headers: {
        'Authorization': authHeader
      }
    });
    res.json(response.data);
  } catch (error) {
    console.log('Backend error:', error.response?.status, error.response?.data);
    res.status(error.response?.status || 500).json(error.response?.data || { error: 'Failed to delete data from backend' });
  }
});


app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});
