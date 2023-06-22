import { Maybe } from 'graphql/jsutils/Maybe';

export function formatNonNegativeFieldsData(value?: Maybe<number>) {
  if (value === undefined || value === null) {
    return '';
  }
  return value < 0 ? `(${Math.abs(value)})` : `${value}`;
}
