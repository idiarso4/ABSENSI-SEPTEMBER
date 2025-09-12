module.exports = {
  testEnvironment: 'jsdom',
  testMatch: [
    '**/__tests__/**/*.test.js',
    '**/__tests__/**/*.test.jsx',
    '**/__tests__/**/*.js',
    '**/?(*.)+(spec|test).js',
    '**/test-*.js',
    '**/test-*.jsx'
  ],
  setupFilesAfterEnv: ['<rootDir>/jest.setup.js'],
  moduleNameMapper: {
    '\\.(css|less|scss|sass)$': 'identity-obj-proxy'
  },
  verbose: true,
  testEnvironmentOptions: {
    url: 'http://localhost:3000'
  },
  transform: {
    '^.+\\.js$': 'babel-jest',
  },
  transformIgnorePatterns: [
    '\\node_modules\\',
    '\\dist\\',
    '\\build\\',
  ],
  testPathIgnorePatterns: [
    '\\node_modules\\',
    '\\dist\\',
    '\\build\\',
  ]
};
