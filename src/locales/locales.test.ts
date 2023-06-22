import { resources } from 'locales';

jest.mock(
  './en/translation.json',
  () => ({
    common: {
      greeting: 'Hello, en!'
    }
  }),
  { virtual: true }
);

jest.mock(
  './test/translation.json',
  () => ({
    common: {
      greeting: 'Hello, en!'
    }
  }),
  {
    virtual: true
  }
);

describe('translations', () => {
  test('should return the translations', () => {
    expect(resources).toMatchObject({
      en: {
        translation: {
          common: {
            greeting: 'Hello, en!'
          }
        }
      },
      test: {
        translation: {
          common: {
            greeting: 'Hello, en!'
          }
        }
      }
    });
  });
});
