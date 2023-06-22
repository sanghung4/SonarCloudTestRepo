import { ChangeEvent } from 'react';

import { mockCartHolderInput } from 'CreditCard/tests/mocks/index.mocks';
import {
  handleSavedCardChange,
  handleSubmit
} from 'CreditCard/util/creditCardHandles';

describe('CreditCard - util/creditCardhandles', () => {
  it('expect `handleSavedCardChange` to call mocked function', () => {
    const mockFn = jest.fn();
    const event = {
      target: { value: 'test' }
    } as ChangeEvent<HTMLInputElement>;
    handleSavedCardChange(mockFn)(event);
    expect(mockFn).toBeCalledWith('test');
  });
  it('expect `handleSubmit` to call mocked function', () => {
    const [mockFnA, mockFnB, mockFnC] = [jest.fn(), jest.fn(), jest.fn()];
    handleSubmit(mockFnA, mockFnB, mockFnC)({ ...mockCartHolderInput }, false);
    expect(mockFnA).toBeCalledWith(false);
    expect(mockFnB).toBeCalled();
    expect(mockFnC).toBeCalledWith(false);
  });
});
