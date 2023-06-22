import { DateRange } from '@dialexa/reece-component-library';
import { isAfter, isBefore, isSameDay } from 'date-fns';

/**
 * Add a day to an existing range.
 *
 * The returned range takes in account the `undefined` values and if the added
 * day is already present in the range.
 */

// https://github.com/gpbl/react-day-picker/blob/92525bc74ac8b681cc94dfdb634417017454df28/packages/react-day-picker/src/contexts/SelectRangeContext/utils/addToRange.ts
export function addToRange(
  day: Date,
  range?: DateRange
): DateRange | undefined {
  const { from, to } = range || {};
  if (!from) {
    return { from: day, to: day };
  }
  if (!to && isSameDay(from, day)) {
    return { from: undefined, to: undefined };
  }
  if (!to && isBefore(day, from)) {
    return { from: day, to: from };
  }
  if (!to) {
    return { from, to: day };
  }
  if (isSameDay(to, day) && isSameDay(from, day)) {
    return { from: undefined, to: undefined };
  }
  if (isSameDay(to, day)) {
    return { from: to, to: to };
  }
  if (isSameDay(from, day)) {
    return { from: undefined, to: undefined };
  }
  if (isAfter(from, day)) {
    return { from: day, to };
  }
  return { from, to: day };
}
