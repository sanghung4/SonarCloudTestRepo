import { fireEvent, findByTestId, act } from '@testing-library/react';
import userEvent from '@testing-library/user-event';

export const fillFormTextInput = async (testId: string, value: string) => {
  await act(async () => {
    const textInput = await findByTestId(document.body, `${testId}-input`);
    await fireEvent.change(textInput, { target: { value } });
  });
};

export const fillMaskedFormTextInput = async (
  testId: string,
  value: string
) => {
  await act(async () => {
    const textInput = await findByTestId(document.body, `${testId}-input`);
    await userEvent.type(textInput, value);
  });
};

export const clickButton = async (testId: string) => {
  await act(async () => {
    const button = await findByTestId(document.body, testId);
    await fireEvent.click(button);
  });
};

export const clickRadio = async (testId: string, optionIndex: number) => {
  await act(async () => {
    const radio = await findByTestId(
      document.body,
      `${testId}-radio-${optionIndex}`
    );
    await userEvent.click(radio);
  });
};

export const fillFormSelectInput = async (testId: string, value: string) => {
  await act(async () => {
    const selectInput = await findByTestId(document.body, `${testId}-input`);
    await fireEvent.change(selectInput, { target: { value } });
    await fireEvent.keyDown(window, { key: 'Enter' });
  });
};

export const focusInput = async (testId: string) => {
  await act(async () => {
    const input = await findByTestId(document.body, testId);
    await fireEvent.focus(input);
  });
};

export const changeInput = async (testId: string, value: string) => {
  await act(async () => {
    const textInput = await findByTestId(document.body, testId);
    await fireEvent.change(textInput, { target: { value } });
  });
};

export const blurInput = async (testId: string) => {
  await act(async () => {
    const input = await findByTestId(document.body, testId);
    await fireEvent.blur(input);
  });
};

export const keyboardDown = async (
  key: string,
  element?: Element | Node | Document | Window
) => {
  await act(async () => {
    await fireEvent.keyDown(element ?? window, { key });
  });
};
