import trimSpaces from 'utils/trimSpaces';

describe('Utils - trimSpaces', () => {
  it('expect "     string     " to be trimed down to "string"', () => {
    const input = '     string     ';
    const output = 'string';
    expect(trimSpaces(input)).toBe(output);
  });

  it('expect "G          A   P" to be trimed down to "G A P"', () => {
    const input = 'G          A   P';
    const output = 'G A P';
    expect(trimSpaces(input)).toBe(output);
  });
  it('expect " multiple spaces   thingy    " to be trimed down to "multiple spaces thingy"', () => {
    const input = ' multiple spaces   thingy    ';
    const output = 'multiple spaces thingy';
    expect(trimSpaces(input)).toBe(output);
  });
  it('expect "I am unaffected" to be the same', () => {
    const unaffected = 'I am unaffected';
    expect(trimSpaces(unaffected)).toBe(unaffected);
  });
  it('expect `undefined` to be the ""', () => {
    expect(trimSpaces()).toBe('');
  });
});
