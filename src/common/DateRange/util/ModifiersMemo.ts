import { addDays, differenceInDays, subDays } from 'date-fns';
import { DateRange, Matcher, Modifiers } from 'react-day-picker';

export function modifiersMemoLogic(value?: Partial<DateRange>) {
  const range_middle: Matcher =
    value?.from && value?.to && differenceInDays(value?.to, value?.from) > 1
      ? {
          from: addDays(value!.from, 1),
          to: subDays(value!.to, 1)
        }
      : { from: new Date(), to: new Date() };

  return {
    range_middle,
    range_end: value?.to ?? new Date(),
    range_begin: value?.from ?? new Date()
  } as unknown as Modifiers;
}
