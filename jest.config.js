const expoPreset = require('jest-expo/jest-preset');

module.exports = {
  roots: ['src'],
  preset: 'react-native',
  setupFiles: [...expoPreset.setupFiles, '<rootDir>/jest.setup.js'],
  setupFilesAfterEnv: [
    '@testing-library/jest-native/extend-expect',
    '<rootDir>/src/setup-tests.js',
  ],
  transformIgnorePatterns: [
    'node_modules/(?!(jest-)?@react-native|react-native|@okta|unimodules-permissions-interface|expo-barcode-scanner|react-clone-referenced-element|expo(nent)?|@expo(nent)?/.*|react-navigation|@react-navigation/.*|@unimodules/.*|unimodules|sentry-expo|native-base|@react-native-community)',
  ],
  moduleFileExtensions: ['ts', 'tsx', 'js', 'jsx', 'json'],
  testPathIgnorePatterns: ['node_modules', 'e2e'],
  moduleNameMapper: {},
  collectCoverageFrom: [
    'src/**/*.{js,jsx,ts,tsx}',
    '!**/node_modules/**',
    '!e2e/**',
  ],
};
