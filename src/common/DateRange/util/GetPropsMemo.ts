import { ChangeEvent, Dispatch, RefObject } from 'react';

import { DateRange } from 'react-day-picker';

import { EMPTY_RANGE } from 'common/DateRange';
import { formatDate } from 'utils/dates';

export type GetPropsMemoLogicProps = {
  fromEl: RefObject<HTMLInputElement>;
  onChange?: Dispatch<DateRange>;
  setFocused: Dispatch<'from' | 'to' | null>;
  setMonth: Dispatch<Date>;
  toEl: RefObject<HTMLInputElement>;
  value?: Partial<DateRange>;
};

export function getPropsMemoLogic({
  fromEl,
  onChange,
  setFocused,
  setMonth,
  toEl,
  value
}: GetPropsMemoLogicProps) {
  const fromTo = ['from', 'to'] as Array<keyof DateRange>;
  return fromTo.map((input) => {
    const onBlur = onBlurAction({
      input,
      onChange,
      setMonth,
      value
    });

    const onFocus = () => {
      setFocused(input);
    };

    return {
      onBlur,
      onFocus,
      ref: input === 'from' ? fromEl : toEl,
      value: formatDate(value?.[input] ?? ''),
      defaultValue: formatDate(value?.[input] ?? '')
    };
  });
}

export type OnBlurActionProps = {
  input: keyof DateRange;
  onChange?: Dispatch<DateRange>;
  setMonth: Dispatch<Date>;
  value?: Partial<DateRange>;
};

export function onBlurAction({
  input,
  onChange,
  setMonth,
  value
}: OnBlurActionProps) {
  return (e: ChangeEvent<HTMLInputElement>) => {
    const date = new Date(e.target.value || 'NaN');
    const isDateNaN = isNaN(date.getTime());
    onChange?.({
      ...EMPTY_RANGE,
      ...value,
      [input]: isDateNaN ? undefined : date
    });
    if (!isDateNaN) {
      setMonth(date);
    }
  };
}
