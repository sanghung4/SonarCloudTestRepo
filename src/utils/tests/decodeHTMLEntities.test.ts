import decodeHTMLEntities from 'utils/decodeHTMLEntities';

describe('Decode HTML entities', () => {
  test('returns an empty string when nothing is provided', () => {
    expect(decodeHTMLEntities('')).toMatch('');
  });

  test('format &quot; to "', () => {
    expect(decodeHTMLEntities('&quot;')).toMatch('"');
  });

  test("format &#39; to '", () => {
    expect(decodeHTMLEntities('&#39;')).toMatch("'");
  });

  test('format &#x2F; to /', () => {
    expect(decodeHTMLEntities('&#x2F;')).toMatch('/');
  });
});
