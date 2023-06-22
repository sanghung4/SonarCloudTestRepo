import { Dispatch, FormEvent, RefObject } from 'react';

export type HandleSubmitLogicProps = {
  e: FormEvent<HTMLFormElement>;
  focused: 'from' | 'to' | null;
  fromEl: RefObject<HTMLInputElement>;
  toEl: RefObject<HTMLInputElement>;
  handleDayClick: Dispatch<Date>;
};

export function handleSubmitLogic(props: HandleSubmitLogicProps) {
  props.e.preventDefault();

  const date = new Date(
    (props.focused === 'from'
      ? props.fromEl.current?.value
      : props.toEl.current?.value) ?? 'NaN'
  );
  if (!isNaN(date.getTime())) {
    props.handleDayClick(date);
  }
}
