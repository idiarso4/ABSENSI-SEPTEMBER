// Basic DOM test with minimal setup
describe('Basic DOM Test', () => {
  beforeAll(() => {
    // Create a basic DOM structure
    document.body.innerHTML = `
      <div id="test">Hello, world!</div>
    `;
  });

  test('should find the test div', () => {
    const div = document.getElementById('test');
    expect(div).not.toBeNull();
    expect(div.textContent).toBe('Hello, world!');
  });

  test('should pass a basic test', () => {
    expect(true).toBe(true);
  });
});
