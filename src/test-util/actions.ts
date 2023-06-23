import { act, EventType, fireEvent, screen } from '@testing-library/react';

export const fillTextInput = async (
  testId: string,
  value: string,
  noBlur?: boolean
) => {
  const input = screen.queryByTestId(testId);
  if (!input) {
    console.error('❌ TEST ERROR - INPUT TESTID NOT FOUND!');
    return;
  }
  await act(async () => {
    fireEvent.focus(input!);
    fireEvent.change(input!, { target: { value } });
    if (!noBlur) {
      fireEvent.blur(input!);
    }
  });
};

const customFireEvent = async (
  type: EventType,
  testId: string,
  options?: object
) => {
  const element = screen.queryByTestId(testId);
  if (!element) {
    console.error('❌ TEST ERROR - TESTID NOT FOUND!');
    return;
  }
  await act(async () => {
    fireEvent[type](element, options);
  });
};
export { customFireEvent as fireEvent };
