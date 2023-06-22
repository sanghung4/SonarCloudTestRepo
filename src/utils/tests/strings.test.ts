import {
  camelToString,
  dasherize,
  escapeCSVString,
  filenameValidation,
  truncateText
} from 'utils/strings';

describe('Utils - strings', () => {
  test('converts a string from camel case to capital case', () => {
    const test = 'thisIsATestOfACamelCaseString';

    expect(camelToString(test)).toMatch(
      'This Is A Test Of A Camel Case String '
    );
  });

  test('dasherizes a string', () => {
    const test = 'Da@#$sHERizes   a stRIng 1234';

    expect(dasherize(test)).toMatch('dasherizes-a-string-1234');
  });

  test('truncates a long string to 30 characters', () => {
    const test = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit';
    const result = truncateText(test, 30);

    expect(result).toMatch('Lorem ipsum dolor sit amet, co...');
    expect(result.length).toEqual(33);
  });

  test('does not truncate a short string', () => {
    const test = 'Lorem ipsum dolor sit amet';
    const result = truncateText(test, 30);

    expect(result).toMatch('Lorem ipsum dolor sit amet');
    expect(result.length).toEqual(26);
  });

  test('should replace special characters for filename', () => {
    const test = '12@34';
    const result = filenameValidation(test);

    expect(result).toMatch('1234');
  });

  test('should escape special characters from csv fileds', () => {
    const test = '12"34';
    const result = escapeCSVString(test);
    expect(result).toMatch('12""34');
  });
});
