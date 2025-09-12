// Working login test with simplified approach
const { JSDOM } = require('jsdom');

// Create a simple DOM structure
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
    </body>
  </html>
`, { url: 'http://localhost:3000' });

// Set up globals
global.window = dom.window;
global.document = dom.window.document;

describe('Login Form', () => {
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

  test('should show error for empty email', () => {
    // Set up form with empty email
    emailInput.value = '';
    passwordInput.value = 'password123';
    
    // Submit form
    const submitEvent = new window.Event('submit', { cancelable: true });
    form.dispatchEvent(submitEvent);
    
    // Check for required validation
    expect(emailInput.validity.valueMissing).toBe(true);
    expect(emailError.textContent).not.toBe('');
  });

  test('should show error for invalid email format', () => {
    // Set up form with invalid email
    emailInput.value = 'not-an-email';
    passwordInput.value = 'password123';
    
    // Submit form
    const submitEvent = new window.Event('submit', { cancelable: true });
    form.dispatchEvent(submitEvent);
    
    // Check for email validation
    expect(emailInput.validity.typeMismatch).toBe(true);
    expect(emailError.textContent).not.toBe('');
  });

  test('should show error for empty password', () => {
    // Set up form with empty password
    emailInput.value = 'test@example.com';
    passwordInput.value = '';
    
    // Submit form
    const submitEvent = new window.Event('submit', { cancelable: true });
    form.dispatchEvent(submitEvent);
    
    // Check for required validation
    expect(passwordInput.validity.valueMissing).toBe(true);
    expect(passwordError.textContent).not.toBe('');
  });
});
