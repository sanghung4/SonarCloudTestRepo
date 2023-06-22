import { format, formatFromCents } from 'utils/currency';

describe('Utils - currency', () => {
  test('converts number string to dollars', () => {
    expect(format(123)).toMatch('$123.00');
  });
  test('converts whole number string, including cents, to dollars', () => {
    expect(formatFromCents(123)).toMatch('$1.23');
  });
});
