import { Maybe } from 'generated/graphql';
import { mapValues } from 'lodash-es';

export function getTypedKeys<T extends Record<string, unknown>>(
  obj: T
): Array<keyof T> {
  return Object.keys(obj).sort((a, b) => {
    if (a === 'inStockLocation') return -1;
    else return 0;
  });
}

export function getInitialFilterRefs<T extends Record<string, unknown>>(
  obj?: T | null
) {
  return mapValues(obj, (_) => null);
}

export function getKeyFromValue<T extends Record<string, unknown>>(
  obj: T,
  value: Maybe<T[keyof T]>
): keyof T {
  if (!value) {
    return '';
  }
  const keyIndex = Object.values(obj).indexOf(value);
  return Object.keys(obj)[keyIndex];
}
