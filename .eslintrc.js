module.exports = {
  env: {
    node: true,
    es6: true
  },
  extends: [
    'plugin:@typescript-eslint/recommended',
    'prettier/@typescript-eslint',
    'plugin:prettier/recommended'
  ],
  parser: '@typescript-eslint/parser',
  parserOptions: {
    project: './tsconfig.json'
  },
  plugins: ['@typescript-eslint', 'import', 'graphql'],
  rules: {
    '@typescript-eslint/indent': ['error', 'error'],
    '@typescript-eslint/explicit-function-return-type': [
      'error',
      {
        allowExpressions: true,
        allowTypedFunctionExpressions: true
      }
    ],
    '@typescript-eslint/no-unused-vars': [
      'error',
      {
        ignoreRestSiblings: true,
        argsIgnorePattern: '^_'
      }
    ],
    'import/no-default-export': 'error',
    'import/order': 'error',
    semi: 'error',
    'no-multiple-empty-lines': [
      'error',
      {
        max: 1,
        maxEOF: 0,
        maxBOF: 0
      }
    ],
    'no-multi-spaces': 'error',
    quotes: ['error', 'single', { avoidEscape: true }],
    'prefer-arrow-callback': 'error',
    '@typescript-eslint/explicit-member-accessibility': [
      'error',
      {
        overrides: {
          constructors: 'no-public'
        }
      }
    ],
    '@typescript-eslint/no-parameter-properties': 'off',
    '@typescript-eslint/array-type': ['error', 'array-simple'],
    'newline-before-return': 'error',
    'quote-props': ['error', 'as-needed'],
    '@typescript-eslint/no-empty-interface': 'off',
    'object-curly-spacing': ['error', 'always'],
    'array-bracket-spacing': ['error', 'never'],
    'comma-spacing': ['error', { before: false, after: true }],
    'comma-dangle': ['error', 'never']
  },
  'graphql/template-strings': [
    'error',
    {
      env: 'apollo'
    }
  ]
};
