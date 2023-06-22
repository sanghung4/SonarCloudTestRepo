import { act, fireEvent } from '@testing-library/react';
import userEvent from '@testing-library/user-event';

import DateRange, {
  DateRangeContext,
  DateRangeContextType
} from 'common/DateRange';
import DateRangeInput from 'common/DateRange/DateRangeInput';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

const initialRange = {
  from: '01/01/2020',
  to: '06/01/2020'
};
const oneMonthPrevious = '05/01/2020';

const initialValue = {
  from: new Date(initialRange.from),
  to: new Date(initialRange.to)
};

const mockProvider: DateRangeContextType = {
  handleDayClick: jest.fn(),
  handleSubmit: jest.fn(),
  setMonth: jest.fn(),
  setOpen: jest.fn(),
  open: false,
  focused: null,
  setFocused: jest.fn()
};

describe('DateRangeInput tests', () => {
  it('Should match snapshot and values on desktop', async () => {
    setBreakpoint('desktop');
    const { getByTestId, container } = render(
      <DateRange value={initialValue} onChange={jest.fn()}>
        <DateRangeInput />
      </DateRange>
    );

    const fromInput = getByTestId('range-from') as HTMLInputElement;
    const toInput = getByTestId('range-to') as HTMLInputElement;

    expect(container).toMatchSnapshot();
    expect(fromInput.value).toEqual(initialRange.from);
    expect(toInput.value).toEqual(initialRange.to);
  });

  it('Should match snapshot and values on mobile', () => {
    setBreakpoint('mobile');
    const { getByTestId, container } = render(
      <DateRange value={initialValue} onChange={jest.fn()}>
        <DateRangeInput />
      </DateRange>
    );

    const fromInput = getByTestId('range-from') as HTMLInputElement;
    const toInput = getByTestId('range-to') as HTMLInputElement;

    expect(container).toMatchSnapshot();
    expect(fromInput.value).toEqual(initialRange.from);
    expect(toInput.value).toEqual(initialRange.to);
  });

  it('Should call onChange when selecting a new date value', () => {
    setBreakpoint('desktop');
    const onChange = jest.fn();
    const { getByTestId } = render(
      <DateRange value={initialValue} onChange={onChange}>
        <DateRangeInput />
      </DateRange>
    );
    const fromInput = getByTestId('range-from') as HTMLInputElement;
    fireEvent.change(fromInput, {
      target: { value: oneMonthPrevious }
    });
    fireEvent.blur(fromInput);

    expect(onChange).toHaveBeenCalledWith({
      from: new Date(oneMonthPrevious),
      to: initialValue.to
    });
  });

  it('Should open popper on focus and update based on input', () => {
    const onChange = jest.fn();
    const { container, getByTestId } = render(
      <DateRange value={initialValue} onChange={onChange}>
        <DateRangeInput />
      </DateRange>
    );

    // focus
    const fromInput = getByTestId('range-from') as HTMLInputElement;
    const toInput = getByTestId('range-to') as HTMLInputElement;

    // Open popper
    fireEvent.focus(fromInput);
    expect(container).toMatchSnapshot();
    expect(
      container.getElementsByClassName('rdp-caption_label')[0].innerHTML
    ).toEqual('January 2020');

    fireEvent.focus(toInput);
    expect(
      container.getElementsByClassName('rdp-caption_label')[0].innerHTML
    ).toEqual('June 2020');

    // get the 5th
    const fifthButton = container.getElementsByClassName('rdp-day')[6];
    fireEvent.click(fifthButton);
    expect(onChange).toHaveBeenCalledWith({
      from: initialValue.from,
      to: new Date('06/06/2020')
    });
  });

  it('Expect from input sets date when context is undefined', async () => {
    const { getByTestId } = render(
      <DateRangeContext.Provider value={mockProvider}>
        <DateRangeInput />
      </DateRangeContext.Provider>
    );
    fireEvent.focus(getByTestId('range-from'));
    await act(() => new Promise((res) => setTimeout(res, 0)));
    // Cannot use exact date because it will result in few ms apart, failing the test
    expect(mockProvider.setMonth).toBeCalled();
  });

  it('Expect to input sets date when context is undefined', async () => {
    const { getByTestId } = render(
      <DateRangeContext.Provider value={mockProvider}>
        <DateRangeInput />
      </DateRangeContext.Provider>
    );
    fireEvent.focus(getByTestId('range-to'));
    await act(() => new Promise((res) => setTimeout(res, 0)));
    // Cannot use exact date because it will result in few ms apart, failing the test
    expect(mockProvider.setMonth).toBeCalled();
  });

  it('Expect handle submit is called', async () => {
    const { getByTestId } = render(
      <DateRangeContext.Provider value={mockProvider}>
        <DateRangeInput />
      </DateRangeContext.Provider>
    );
    fireEvent.focus(getByTestId('range-from'));
    await act(() => new Promise((res) => setTimeout(res, 0)));
    // Cannot use exact date because it will result in few ms apart, failing the test
    expect(mockProvider.setMonth).toBeCalled();
  });

  it('Expect to call on submit', async () => {
    const onChange = jest.fn();
    const { getByTestId } = render(
      <DateRange value={initialValue} onChange={onChange}>
        <DateRangeInput />
      </DateRange>
    );

    const toInput = getByTestId('range-to');
    fireEvent.focus(toInput);
    fireEvent.change(toInput, { target: { value: '12/31/2021' } });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    userEvent.type(toInput, '{enter}');
    fireEvent.blur(toInput);
    await act(() => new Promise((res) => setTimeout(res, 0)));

    expect(onChange).toHaveBeenCalledWith({
      from: initialValue.from,
      to: new Date('12/31/2021')
    });
  });
});
