import { Product } from 'api';

export const orNA = (value?: string | null, fallback = 'N/A'): string => {
  return typeof value === 'string' && value.length ? value : fallback;
};

/**
 * removes unnecessary line breaks
 * @param text string
 * @returns text with no line breaks
 */
export const removeLineBreaks = (text: string) => {
  return text.replace(/(\r\n|\n|\r)/gm, ' ');
};

/**
 * gets the image uri and replaces http with https
 * @param product
 * @returns string for uri
 */

export const getImageUri = (productImageUrl: String | undefined | null) => {
  let imageUri = productImageUrl ? productImageUrl : '';
  return imageUri ? imageUri.replace('http:', 'https:') : undefined;
};

/**
 * formats the value and returns as fallback if the value is falsy
 * @param value
 * @returns formatted value or fallback if value is undefined or null
 */

export const toNumberString = (
  value: string | number | null | undefined,
  fallback: string = '-'
) => {
  if (value === 'null' || (value !== 0 && !value)) {
    return fallback;
  }

  const roundedValue = Math.round(
    typeof value === 'string' ? parseFloat(value) : value
  );

  return roundedValue.toLocaleString('en-US');
};
