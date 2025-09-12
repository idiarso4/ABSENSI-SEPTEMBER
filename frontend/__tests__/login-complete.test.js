// Mock the DOM environment
const { JSDOM } = require('jsdom');

// Create a simple DOM structure
const dom = new JSDOM(`
  <!DOCTYPE html>
  <html>
    <body>
      <form id="loginForm">
        <input type="email" id="email" name="email" required>
        <div id="emailError" class="error-message"></div>
        <input type="password" id="password" name="password" required>
        <div id="passwordError" class="error-message"></div>
        <input type="checkbox" id="remember" name="remember">
        <button type="submit">Login</button>
      </form>
    </body>
  </html>
`, { url: 'http://localhost:3000' });

// Set up global variables
global.window = dom.window;
global.document = dom.window.document;

describe('Login Form', () => {
  let form, emailInput, passwordInput, rememberCheckbox, emailError, passwordError;

  beforeEach(() => {
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

  test('should have required form elements', () => {
    expect(form).not.toBeNull();
    expect(emailInput).not.toBeNull();
    expect(passwordInput).not.toBeNull();
    expect(rememberCheckbox).not.toBeNull();
  });

  test('should show error when email is empty', () => {
    // Set empty email
    emailInput.value = '';
    
    // Trigger form submission
    const submitEvent = new window.Event('submit', { cancelable: true });
    form.dispatchEvent(submitEvent);
    
    // Check validation
    expect(emailInput.validity.valueMissing).toBe(true);
    expect(emailError.textContent).not.toBe('');
  });

  test('should show error when password is empty', () => {
    // Set empty password
    passwordInput.value = '';
    
    // Trigger form submission
    const submitEvent = new window.Event('submit', { cancelable: true });
    form.dispatchEvent(submitEvent);
    
    // Check validation
    expect(passwordInput.validity.valueMissing).toBe(true);
    expect(passwordError.textContent).not.toBe('');
  });

  test('should show error for invalid email format', () => {
    // Set invalid email
    emailInput.value = 'invalid-email';
    
    // Trigger form submission
    const submitEvent = new window.Event('submit', { cancelable: true });
    form.dispatchEvent(submitEvent);
    
    // Check validation
    expect(emailInput.validity.typeMismatch).toBe(true);
    expect(emailError.textContent).not.toBe('');
  });
});
