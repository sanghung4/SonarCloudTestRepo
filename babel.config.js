module.exports = {
  presets: ['module:metro-react-native-babel-preset'],
  plugins: [
    'lodash',
    [
      'module-resolver',
      {
        root: ['./src/'],
        extensions: [
          '.js',
          '.jsx',
          '.ts',
          '.tsx',
          '.android.js',
          '.android.jsx',
          '.android.ts',
          '.android.tsx',
          '.ios.js',
          '.ios.jsx',
          '.ios.ts',
          '.ios.tsx',
        ],
      },
    ],
  ],
};
