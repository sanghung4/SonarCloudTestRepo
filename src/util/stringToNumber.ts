import { isNaN } from 'lodash-es';

export function stringToNumber(input: string, allowZero?: boolean) {
  const rawNumber = parseInt(input);
  if (isNaN(rawNumber)) {
    return allowZero ? 0 : 1;
  }
  return allowZero ? rawNumber : rawNumber || 1;
}
