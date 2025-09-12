// Comprehensive login test with proper setup and teardown
const { JSDOM } = require('jsdom');

// Mock the DOM environment
const dom = new JSDOM(`
  <!DOCTYPE html>
  <html>
    <body>
      <form id="loginForm">
        <input type="email" id="email" name="email" required>
        <div id="emailError" class="error"></div>
        <input type="password" id="password" name="password" required>
        <div id="passwordError" class="error"></div>
        <input type="checkbox" id="remember" name="remember">
        <button type="submit">Login</button>
      </form>
      <script>
        // Simple validation function for testing
        function validateForm() {
          const email = document.getElementById('email');
          const emailError = document.getElementById('emailError');
          const password = document.getElementById('password');
          const passwordError = document.getElementById('passwordError');
          let isValid = true;

          // Reset errors
          emailError.textContent = '';
          passwordError.textContent = '';

          // Email validation
          if (!email.value) {
            emailError.textContent = 'Email is required';
            isValid = false;
          } else if (!/\S+@\S+\.\S+/.test(email.value)) {
            emailError.textContent = 'Please enter a valid email';
            isValid = false;
          }

          // Password validation
          if (!password.value) {
            passwordError.textContent = 'Password is required';
            isValid = false;
          } else if (password.value.length < 6) {
            passwordError.textContent = 'Password must be at least 6 characters';
            isValid = false;
          }

          return isValid;
        }

        // Add form submit handler
        document.getElementById('loginForm').addEventListener('submit', function(e) {
          if (!validateForm()) {
            e.preventDefault();
          }
        });
      </script>
    </body>
  </html>
`, { url: 'http://localhost:3000', runScripts: 'dangerously' });

// Set up global variables
global.window = dom.window;
global.document = dom.window.document;

// Mock fetch
global.fetch = jest.fn();

// Mock localStorage
const localStorageMock = (() => {
  let store = {};
  return {
    getItem: jest.fn((key) => store[key] || null),
    setItem: jest.fn((key, value) => {
      store[key] = value.toString();
    }),
    clear: jest.fn(() => {
      store = {};
    }),
    removeItem: jest.fn((key) => {
      delete store[key];
    })
  };
})();

global.localStorage = localStorageMock;

describe('Login Form', () => {
  let form, emailInput, passwordInput, rememberCheckbox, emailError, passwordError;

  beforeEach(() => {
    // Reset mocks
    fetch.mockClear();
    localStorage.clear();
    
    // Get form elements
    form = document.getElementById('loginForm');
    emailInput = document.getElementById('email');
    passwordInput = document.getElementById('password');
    rememberCheckbox = document.getElementById('remember');
    emailError = document.getElementById('emailError');
    passwordError = document.getElementById('passwordError');
    
    // Reset form
    form.reset();
    emailError.textContent = '';
    passwordError.textContent = '';
  });

  test('form elements should exist', () => {
    expect(form).not.toBeNull();
    expect(emailInput).not.toBeNull();
    expect(passwordInput).not.toBeNull();
    expect(rememberCheckbox).not.toBeNull();
    expect(emailError).not.toBeNull();
    expect(passwordError).not.toBeNull();
  });

  test('should show error when email is empty', () => {
    // Set up form with empty email
    emailInput.value = '';
    passwordInput.value = 'password123';
    
    // Submit form
    const submitEvent = new window.Event('submit', { cancelable: true });
    form.dispatchEvent(submitEvent);
    
    // Check for required validation
    expect(emailError.textContent).toBe('Email is required');
  });

  test('should show error for invalid email format', () => {
    // Set up form with invalid email
    emailInput.value = 'invalid-email';
    passwordInput.value = 'password123';
    
    // Submit form
    const submitEvent = new window.Event('submit', { cancelable: true });
    form.dispatchEvent(submitEvent);
    
    // Check for email validation
    expect(emailError.textContent).toBe('Please enter a valid email');
  });

  test('should show error when password is empty', () => {
    // Set up form with empty password
    emailInput.value = 'test@example.com';
    passwordInput.value = '';
    
    // Submit form
    const submitEvent = new window.Event('submit', { cancelable: true });
    form.dispatchEvent(submitEvent);
    
    // Check for required validation
    expect(passwordError.textContent).toBe('Password is required');
  });

  test('should show error for short password', () => {
    // Set up form with short password
    emailInput.value = 'test@example.com';
    passwordInput.value = '12345';
    
    // Submit form
    const submitEvent = new window.Event('submit', { cancelable: true });
    form.dispatchEvent(submitEvent);
    
    // Check for password length validation
    expect(passwordError.textContent).toBe('Password must be at least 6 characters');
  });
});
