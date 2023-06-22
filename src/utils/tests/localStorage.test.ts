import { mockLocalStorage } from 'test-utils/mockGlobals';
import { getItem, setItem } from 'utils/localStorage';

describe('Utils - localStorage', () => {
  beforeEach(() => {
    mockLocalStorage();
  });

  it('expect setting an item with no userId', () => {
    const key = 'test1';
    const value = 'value1';
    setItem(key, value);
    expect(getItem(key)).toBe(value);
  });

  it('expect setting an item with userId', () => {
    const user = 'random-user-id-for-test';
    const key = 'test2';
    const value = 'value2';
    setItem(key, value, user);
    expect(getItem(key, user)).toBe(value);
  });
});
