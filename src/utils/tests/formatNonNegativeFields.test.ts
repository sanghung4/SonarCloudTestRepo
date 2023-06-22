import { formatNonNegativeFieldsData } from 'utils/formatNonNegativeFields';

describe('formatNonNegativeFields', () => {
  it('With Negative Value', () => {
    expect(formatNonNegativeFieldsData(-10)).toBe('(10)');
  });

  it('With Positive Value', () => {
    expect(formatNonNegativeFieldsData(10)).toBe('10');
  });

  it('With No Value', () => {
    expect(formatNonNegativeFieldsData()).toBe('');
  });
});
