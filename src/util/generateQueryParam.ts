/**
 * Use for generate parameters for URL
 * @param url string
 * @param parameters Parameters <Generic> type
 * @returns string
 */
export function generateQueryParam<Parameters extends object>(
  url: string,
  parameters: Parameters
) {
  // Turn parameters OBJ into an array of string consists of "key=value"
  const entries = Object.entries(parameters).map(
    ([key, value]) => `${key}=${value}`
  );
  // Combine the entries with the URL as output
  if (entries.length) {
    return `${url}?${entries.join('&')}`;
  }
  // return just the URL if no parameters were applied
  return url;
}
