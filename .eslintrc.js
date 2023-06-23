module.exports = {
  env: {
    browser: true,
    node: true,
    es2021: true
  },
  extends: [
    'eslint:recommended',
    'plugin:@typescript-eslint/recommended',
    'plugin:react-hooks/recommended',
    "react-app",
    "react-app/jest"
  ],
  overrides: [
    {
      files: [
        "**/*.stories.*"
      ],
      rules: {
        "import/no-anonymous-default-export": "off"
      }
    }
  ],
  parser: '@typescript-eslint/parser',
  parserOptions: {
    ecmaVersion: 'latest',
    project: 'tsconfig.json',
  },
  plugins: [
    'react',
    '@typescript-eslint'
  ],
  root: true,
  rules: {
    "react/jsx-filename-extension": [2, { 'extensions': ['.js', '.jsx', '.ts', '.tsx'] }],
    "testing-library/no-unnecessary-act": "off",
    "@typescript-eslint/no-non-null-assertion": "off",
    "@typescript-eslint/no-explicit-any": "off"
  },
  settings: {
    react: {
      version: 'detect'
    }
  }
}
