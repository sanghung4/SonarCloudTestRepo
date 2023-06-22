import { EMPTY_RANGE } from 'common/DateRange';
import { Dispatch, RefObject } from 'react';
import { DateRange } from 'react-day-picker';

export type HandleDayClickCbLogicProps = {
  day: Date;
  focused: 'from' | 'to' | null;
  fromEl: RefObject<HTMLInputElement>;
  onChange?: Dispatch<DateRange>;
  setOpen: Dispatch<boolean>;
  toEl: RefObject<HTMLInputElement>;
  value?: Partial<DateRange>;
};

export function handleDayClickCbLogic({
  day,
  focused,
  fromEl,
  onChange,
  setOpen,
  toEl,
  value
}: HandleDayClickCbLogicProps) {
  let newRange = { ...EMPTY_RANGE, ...value };

  if (focused === 'from') {
    if (value?.to && day > value?.to) {
      newRange.from = value.to;
      newRange.to = day;
      fromEl.current?.focus();
    } else {
      newRange.from = day;
      toEl.current?.focus();
    }
  } else if (focused === 'to') {
    if (value?.from && day < value?.from) {
      newRange.from = day;
      newRange.to = value.from;
      toEl.current?.focus();
    } else {
      newRange.to = day;
      fromEl.current?.focus();
    }
    setOpen(false);
    fromEl.current?.blur();
  }

  onChange?.(newRange);
}
