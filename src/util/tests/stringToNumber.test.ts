import { stringToNumber } from 'util/stringToNumber';

describe('util/stringToNumber', () => {
  // 🟢 1 - input: "2"
  it('expect to convert string "2" to number 2', () => {
    // act
    const res = stringToNumber('2');
    // assert
    expect(res).toBe(2);
  });

  // 🟢 2 - input: "0"
  it('expect to convert string "0" to number 1', () => {
    // act
    const res = stringToNumber('0');
    // assert
    expect(res).toBe(1);
  });

  // 🟢 3 - input: "0" (allowZero)
  it('expect to convert string "0" to number 1 with allowZero', () => {
    // act
    const res = stringToNumber('0', true);
    // assert
    expect(res).toBe(0);
  });

  // 🟢 4 - input: "not-a-number"
  it('expect to convert string "not-a-number" to number 1', () => {
    // act
    const res = stringToNumber('not-a-number');
    // assert
    expect(res).toBe(1);
  });

  // 🟢 5 - input: "not-a-number" (allowZero)
  it('expect to convert string "not-a-number" to number 1 with allowZero', () => {
    // act
    const res = stringToNumber('not-a-number', true);
    // assert
    expect(res).toBe(0);
  });
});
