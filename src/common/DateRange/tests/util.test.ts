import {
  getPropsMemoLogic,
  GetPropsMemoLogicProps,
  handleDayClickCbLogic,
  HandleDayClickCbLogicProps,
  handleSubmitLogic,
  HandleSubmitLogicProps,
  onBlurAction,
  OnBlurActionProps
} from 'common/DateRange/util';
import { ChangeEvent, FormEvent, RefObject } from 'react';

const mockRefElement: RefObject<HTMLInputElement> = {
  current: document.createElement('input')
};
const initialRange = {
  from: '01/01/2020',
  to: '06/01/2020'
};

const getPropsMemoMock: GetPropsMemoLogicProps = {
  fromEl: { ...mockRefElement },
  setFocused: jest.fn(),
  setMonth: jest.fn(),
  toEl: { ...mockRefElement }
};
const onBlurActionMock: OnBlurActionProps = {
  input: 'from',
  onChange: jest.fn(),
  setMonth: jest.fn()
};
const handleDayClickCbMockDefault: HandleDayClickCbLogicProps = {
  day: new Date(initialRange.from),
  focused: 'from',
  fromEl: { ...mockRefElement },
  setOpen: jest.fn(),
  toEl: { ...mockRefElement }
};
let handleDayClickCbMock = handleDayClickCbMockDefault;
const handleSubmitMockDefault: HandleSubmitLogicProps = {
  e: { preventDefault: jest.fn() } as unknown as FormEvent<HTMLFormElement>,
  focused: 'from',
  fromEl: { ...mockRefElement },
  handleDayClick: jest.fn(),
  toEl: { ...mockRefElement }
};
let handleSubmitMock = handleSubmitMockDefault;

describe('common - DateRange - util', () => {
  afterEach(() => {
    handleDayClickCbMock = { ...handleDayClickCbMockDefault };
    handleSubmitMock = { ...handleSubmitMockDefault };
  });

  it('expect `getPropsMemoLogic` to return blank values', () => {
    const result = getPropsMemoLogic(getPropsMemoMock);
    expect(result[0].defaultValue).toBe('');
    expect(result[0].value).toBe('');
    expect(result[1].defaultValue).toBe('');
    expect(result[1].value).toBe('');
  });

  it('expect `onBlurAction` to call onChange with blank values', () => {
    const input = { target: { value: '' } } as ChangeEvent<HTMLInputElement>;
    onBlurAction(onBlurActionMock)(input);
    expect(onBlurActionMock.onChange).toBeCalledWith({
      from: undefined,
      to: undefined
    });
  });

  it('expect `handleDayClickCbLogic` to process "from" date as undefined', () => {
    handleDayClickCbMock.onChange = jest.fn();
    handleDayClickCbLogic(handleDayClickCbMock);
    expect(handleDayClickCbMock.onChange).toBeCalledWith({
      from: new Date(initialRange.from),
      to: undefined
    });
  });
  it('expect `handleDayClickCbLogic` to process "from" as "to" date', () => {
    handleDayClickCbMock.onChange = jest.fn();
    handleDayClickCbMock.value = {
      from: new Date(initialRange.from),
      to: new Date(initialRange.to)
    };
    handleDayClickCbMock.day = new Date('07/26/2021');
    handleDayClickCbLogic(handleDayClickCbMock);
    expect(handleDayClickCbMock.onChange).toBeCalledWith({
      from: new Date(initialRange.to),
      to: handleDayClickCbMock.day
    });
  });
  it('expect `handleDayClickCbLogic` to process "to" date as undefined', () => {
    handleDayClickCbMock.onChange = jest.fn();
    handleDayClickCbMock.focused = 'to';
    handleDayClickCbLogic(handleDayClickCbMock);
    expect(handleDayClickCbMock.onChange).toBeCalledWith({
      from: undefined,
      to: new Date(initialRange.from)
    });
  });
  it('expect `handleDayClickCbLogic` to process "to" as "from" date', () => {
    handleDayClickCbMock.onChange = jest.fn();
    handleDayClickCbMock.focused = 'to';
    handleDayClickCbMock.value = {
      from: new Date(initialRange.from),
      to: new Date(initialRange.to)
    };
    handleDayClickCbMock.day = new Date('07/10/2019');
    handleDayClickCbLogic(handleDayClickCbMock);
    expect(handleDayClickCbMock.onChange).toBeCalledWith({
      from: handleDayClickCbMock.day,
      to: new Date(initialRange.from)
    });
  });
  it('expect `handleDayClickCbLogic` to process "null" and return undefined', () => {
    handleDayClickCbMock.onChange = jest.fn();
    handleDayClickCbMock.focused = null;
    handleDayClickCbLogic(handleDayClickCbMock);
    expect(handleDayClickCbMock.onChange).toBeCalledWith({
      from: undefined,
      to: undefined
    });
  });

  it('expect `handleSubmitLogic` to get "from"', () => {
    const date = '10/07/2020';
    handleSubmitMock.fromEl.current!.value = date;
    handleSubmitLogic(handleSubmitMock);
    expect(handleSubmitMock.handleDayClick).toBeCalledWith(new Date(date));
  });
  it('expect `handleSubmitLogic` to get "to"', () => {
    const date = '10/07/2020';
    handleSubmitMock.focused = 'to';
    handleSubmitMock.toEl.current!.value = date;
    handleSubmitLogic(handleSubmitMock);
    expect(handleSubmitMock.handleDayClick).toBeCalledWith(new Date(date));
  });
  it('expect `handleSubmitLogic` to get "NaN"', () => {
    handleSubmitMock.fromEl = { current: null };
    handleSubmitLogic(handleSubmitMock);
    expect(handleSubmitMock.handleDayClick).not.toBeCalled();
  });
});
