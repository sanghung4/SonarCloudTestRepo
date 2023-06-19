module.exports = {
  root: true,
  extends: '@react-native-community',
  env: {
    jest: true,
  },
  rules: {
    '@typescript-eslint/no-unused-vars': ['warn', { ignoreRestSiblings: true }],
    'react-hooks/exhaustive-deps': 'warn',
  },
};
