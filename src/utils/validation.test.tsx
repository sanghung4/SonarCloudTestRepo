import { validateQuantity } from './validation';

describe('validate quantity', () => {
  it('must be a whole number', () => {
    const valid = { valid: true };
    const invalid = { valid: false };

    expect(validateQuantity('1234')).toEqual(valid);
    expect(validateQuantity('0')).toEqual(valid);
    expect(validateQuantity('')).toEqual(valid);

    expect(validateQuantity('-1234')).toEqual(invalid);
    expect(validateQuantity('abcd')).toEqual(invalid);
    expect(validateQuantity('a1b3')).toEqual(invalid);
    expect(validateQuantity('3.14')).toEqual(invalid);
    expect(validateQuantity('!@#$%')).toEqual(invalid);
    expect(validateQuantity('12 456')).toEqual(invalid);
    expect(validateQuantity('        ')).toEqual(invalid);
  });
});
