// Test to check JSDOM version and basic functionality
const { JSDOM } = require('jsdom');

console.log('JSDOM version:', require('jsdom/package.json').version);

test('should pass a basic test', () => {
  expect(1 + 1).toBe(2);
});

test('should create a simple JSDOM instance', () => {
  const dom = new JSDOM(`<!DOCTYPE html><div>Test</div>`);
  const div = dom.window.document.querySelector('div');
  expect(div.textContent).toBe('Test');
});
