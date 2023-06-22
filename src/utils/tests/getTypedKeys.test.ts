import { getInitialFilterRefs, getKeyFromValue, getTypedKeys } from 'utils/getTypedKeys';

describe('getTypedKeys utils', () => {
  test('inStockLocation filter at the top of the filter list', () => {
    // mock subset of filter list
    const mockFilter = {
      brands: true,
      lines: false,
      size: false,
      inStockLocation: true,
      flowRate: true,
      environmentalOptions: false,
      material: false
    };
    const mock = getTypedKeys(mockFilter);
    expect(mock[0]).toMatch('inStockLocation');
  });

  test('expect getInitialFilterRefs to return null mapped values', () => {
    const result = getInitialFilterRefs({ test: '123' });
    expect(result.test).toBe(null);
  });

  test('expect getKeyFromValue get object key from value', () => {
    const mockEnum = {
      a: 'A',
      b: 'B',
      c: 'C'
    };
    expect(getKeyFromValue(mockEnum, 'A')).toBe('a');
    expect(getKeyFromValue(mockEnum, null)).toBe('');
  });
});
