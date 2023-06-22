import trimSpaces from 'utils/trimSpaces';

// TODO replace with lodash function start case
export function camelToString(text: string) {
  // Split capitalized letter boundaries
  const regex =
    /[a-z]+|[0-9]+|(?:[A-Z][a-z]+)|(?:[A-Z]+(?=(?:[A-Z][a-z])|[^AZa-z]|[$\d\n]))/g;
  // Capitalize the first letter of the text
  const capitalText = `${text[0].toUpperCase()}${text.slice(1, text.length)}`;
  return capitalText.replace(regex, (word) => `${word} `);
}

export function dasherize(text: string) {
  return (
    text
      .toLowerCase()
      .replace(/\s+/g, '-')
      /* eslint-disable no-useless-escape */
      .replace(/[^a-z0-9\-]/g, '')
  );
}

export function truncateText(text: string, maxLength: number) {
  return text.length > maxLength ? `${text.substr(0, maxLength)}...` : text;
}

export function priceStringToFloat(str: string) {
  return parseFloat(str.replaceAll(/[$,]/g, ''));
}

export function filenameValidation(str: string) {
  return str.replace(/[^a-zA-Z0-9_-]/g, '');
}

export function escapeCSVString(str: string) {
  return str.replace(/"/g, '""').replace(/,/g, ',').replace(/'/g, "'");
}

export function TruncateTextWithCentralEllipsis(
  value: string,
  showInitialChar: number,
  showEndChars: number
) {
  return value && trimSpaces(value).length > 13
    ? trimSpaces(value).substring(0, showInitialChar) +
        '\u2026' +
        trimSpaces(value).slice(-showEndChars)
    : trimSpaces(value);
}
