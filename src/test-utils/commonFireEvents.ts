import { act, fireEvent } from '@testing-library/react';

export function actClickOnElement(element: HTMLElement) {
  act(() => {
    fireEvent.click(element);
  });
}
export function actUpdateInput(input: HTMLElement, value: string) {
  act(() => updateInput(input, value));
}
export function updateInput(input: HTMLElement, value: string) {
  fireEvent.focus(input);
  fireEvent.change(input, { target: { value } });
  fireEvent.blur(input);
}
