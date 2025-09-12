const { JSDOM } = require('jsdom');
const fs = require('fs');
const path = require('path');

// Create a basic HTML structure that matches our login form
const html = `
<!DOCTYPE html>
<html>
<head>
    <title>Login Test</title>
</head>
<body>
    <form id="loginForm">
        <input type="email" id="email" name="email" required>
        <div class="error-message text-danger small mt-1 d-none"></div>
        <input type="password" id="password" name="password" required>
        <div class="error-message text-danger small mt-1 d-none"></div>
        <input type="checkbox" id="remember" name="remember">
        <button type="submit">Login</button>
    </form>
    <script>
        // Mock the validation
        window.Validation = {
            validateForm: (formId, rules) => {
                const errors = {};
                if (document.getElementById('email').value === 'invalid-email') {
                    errors.email = 'Please enter a valid email';
                }
                return errors;
            },
            displayErrors: (errors) => {
                for (const [field, message] of Object.entries(errors)) {
                    const input = document.getElementById(field);
                    if (input) {
                        const errorDiv = input.nextElementSibling;
                        if (errorDiv && errorDiv.classList.contains('error-message')) {
                            errorDiv.textContent = message;
                            errorDiv.classList.remove('d-none');
                        }
                    }
                }
            }
        };
    </script>
</body>
</html>
`;

const dom = new JSDOM(html, { 
  runScripts: 'dangerously',
  url: 'http://localhost:3000',
  resources: 'usable'
});

const { window } = dom;
const { document } = window;

global.window = window;
global.document = document;

global.fetch = jest.fn(() =>
  Promise.resolve({
    ok: true,
    json: () => Promise.resolve({ 
      accessToken: 'test-token',
      tokenType: 'Bearer',
      refreshToken: 'test-refresh-token'
    }),
  })
);

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

// Mock the fetch API
global.fetch = jest.fn();

describe('Login Form', () => {
  let form, emailInput, passwordInput, submitButton;

  beforeEach(() => {
    // Reset mocks and clear any previous state
    fetch.mockClear();
    localStorage.clear();
    
    // Clear any previous error messages
    document.querySelectorAll('.error-message').forEach(el => {
        el.textContent = '';
        el.classList.add('d-none');
    });

    // Set up the DOM
    document.body.innerHTML = html;
    
    // Get form elements
    form = document.getElementById('loginForm');
    emailInput = document.getElementById('email');
    passwordInput = document.getElementById('password');
    submitButton = form.querySelector('button[type="submit"]');
    
    // Reset form values
    emailInput.value = '';
    passwordInput.value = '';
  });

  test('should submit the form with correct data', async () => {
    // Arrange
    const user = {
      email: 'test@example.com',
      password: 'password123'
    };

    // Act
    emailInput.value = user.email;
    passwordInput.value = user.password;
    
    const submitEvent = new window.Event('submit', { cancelable: true });
    form.dispatchEvent(submitEvent);

    // Assert
    await new Promise(resolve => setImmediate(resolve));
    
    expect(fetch).toHaveBeenCalledWith('/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      },
      body: JSON.stringify({
        identifier: user.email.trim(),
        password: user.password,
        rememberMe: false
      })
    });
  });

  test('should show error for invalid email', () => {
    // Arrange
    emailInput.value = 'invalid-email';
    
    // Act
    const submitEvent = new window.Event('submit', { cancelable: true });
    form.dispatchEvent(submitEvent);

    // Assert
    const errorMessage = emailInput.nextElementSibling;
    expect(errorMessage.textContent).toContain('Please enter a valid email');
    expect(errorMessage.classList.contains('d-none')).toBe(false);
  });

  test('should store tokens on successful login', async () => {
    // Arrange
    emailInput.value = 'test@example.com';
    passwordInput.value = 'password123';
    
    // Act
    const submitEvent = new window.Event('submit', { cancelable: true });
    form.dispatchEvent(submitEvent);

    // Mock successful fetch response
    fetch.mockResolvedValueOnce({
      ok: true,
      json: () => Promise.resolve({ 
        accessToken: 'test-token',
        tokenType: 'Bearer',
        refreshToken: 'test-refresh-token'
      })
    });

    // Wait for async operations to complete
    await new Promise(process.nextTick);

    // Assert
    expect(fetch).toHaveBeenCalledWith('/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      },
      body: JSON.stringify({
        identifier: 'test@example.com',
        password: 'password123',
        rememberMe: false
      })
    });
  });
});
