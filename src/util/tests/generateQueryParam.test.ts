import { generateQueryParam } from 'util/generateQueryParam';

/**
 * TEST
 */
describe('util/generateQueryParam', () => {
  // 🟢 1 - blank parameter
  it('Expect to only return URL when parameter is blank', () => {
    // arrange
    const url = 'test';
    // act
    const res = generateQueryParam(url, {});
    // assert
    expect(res).toBe(url);
  });

  // 🟢 2 - with parameters
  it('Expect return URL and parameters', () => {
    // arrange
    const url = 'test';
    const parameters = { key1: true, key2: 'test' };
    // act
    const res = generateQueryParam(url, parameters);
    // assert
    expect(res).toBe(`${url}?key1=true&key2=test`);
  });
});
