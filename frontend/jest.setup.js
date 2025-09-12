// Test setup file
console.log('Jest setup file is being loaded');

// Polyfills for JSDOM compatibility
const { TextEncoder, TextDecoder } = require('util');
global.TextEncoder = TextEncoder;
global.TextDecoder = TextDecoder;

// Mock fetch globally for all tests
global.fetch = jest.fn();

// Mock localStorage
const localStorageMock = {
  getItem: jest.fn(),
  setItem: jest.fn(),
  removeItem: jest.fn(),
  clear: jest.fn(),
};
global.localStorage = localStorageMock;

// Mock sessionStorage
const sessionStorageMock = {
  getItem: jest.fn(),
  setItem: jest.fn(),
  removeItem: jest.fn(),
  clear: jest.fn(),
};
global.sessionStorage = sessionStorageMock;

// Mock window.location
delete global.window.location;
global.window.location = {
  href: 'http://localhost:3000',
  pathname: '/',
  search: '',
  hash: '',
  reload: jest.fn(),
  assign: jest.fn(),
  replace: jest.fn(),
};

// Add any global test setup here
global.testSetup = true;

// Simple test to verify the setup is working
test('setup test', () => {
  console.log('Running setup test');
  expect(true).toBe(true);
  expect(global.TextEncoder).toBeDefined();
  expect(global.TextDecoder).toBeDefined();
  expect(global.fetch).toBeDefined();
  expect(global.localStorage).toBeDefined();
  expect(global.sessionStorage).toBeDefined();
});
