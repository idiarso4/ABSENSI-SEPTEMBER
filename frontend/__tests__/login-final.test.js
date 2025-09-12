// Final login test with essential test cases
const { JSDOM } = require('jsdom');

// Set up a basic DOM environment
const dom = new JSDOM(`
  <!DOCTYPE html>
  <html>
    <body>
      <form id="loginForm">
        <input type="email" id="email" name="email" required>
        <div id="emailError"></div>
        <input type="password" id="password" name="password" required>
        <div id="passwordError"></div>
        <button type="submit">Login</button>
      </form>
    </body>
  </html>
`);

// Set up global variables
global.window = dom.window;
global.document = dom.window.document;

// Simple validation function for testing
function validateLoginForm() {
  const email = document.getElementById('email');
  const password = document.getElementById('password');
  let isValid = true;

  // Reset errors
  document.getElementById('emailError').textContent = '';
  document.getElementById('passwordError').textContent = '';

  // Email validation
  if (!email.value) {
    document.getElementById('emailError').textContent = 'Email is required';
    isValid = false;
  }

  // Password validation
  if (!password.value) {
    document.getElementById('passwordError').textContent = 'Password is required';
    isValid = false;
  }

  return isValid;
}

// Test suite
describe('Login Form - Final', () => {
  let form, emailInput, passwordInput, emailError, passwordError;

  beforeEach(() => {
    // Get form elements
    form = document.getElementById('loginForm');
    emailInput = document.getElementById('email');
    passwordInput = document.getElementById('password');
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
  });

  test('should show error when email is empty', () => {
    // Set up form with empty email
    emailInput.value = '';
    passwordInput.value = 'password123';
    
    // Validate form
    const isValid = validateLoginForm();
    
    // Check validation
    expect(isValid).toBe(false);
    expect(emailError.textContent).toBe('Email is required');
  });

  test('should show error when password is empty', () => {
    // Set up form with empty password
    emailInput.value = 'test@example.com';
    passwordInput.value = '';
    
    // Validate form
    const isValid = validateLoginForm();
    
    // Check validation
    expect(isValid).toBe(false);
    expect(passwordError.textContent).toBe('Password is required');
  });

  test('should validate form with correct input', () => {
    // Set up form with valid data
    emailInput.value = 'test@example.com';
    passwordInput.value = 'password123';
    
    // Validate form
    const isValid = validateLoginForm();
    
    // Check validation
    expect(isValid).toBe(true);
    expect(emailError.textContent).toBe('');
    expect(passwordError.textContent).toBe('');
  });
});
