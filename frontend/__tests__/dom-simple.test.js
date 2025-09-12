// Simple DOM test with minimal setup
describe('Simple DOM Test', () => {
  beforeAll(() => {
    // Create a basic DOM structure
    document.body.innerHTML = `
      <div id="test">Hello</div>
      <form id="login">
        <input type="text" id="username">
        <button type="submit">Login</button>
      </form>
    `;
  });

  test('should find the test div', () => {
    const div = document.getElementById('test');
    expect(div).not.toBeNull();
    expect(div.textContent).toBe('Hello');
  });

  test('should find the login form', () => {
    const form = document.getElementById('login');
    const input = document.getElementById('username');
    expect(form).not.toBeNull();
    expect(input).not.toBeNull();
  });

  test('should pass a basic test', () => {
    expect(true).toBe(true);
  });
});
