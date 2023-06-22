import { act, fireEvent } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { FormEvent } from 'react';
import { render } from 'test-utils/TestWrapper';
import SearchInput from '../util/SearchInput';

/******************************/
/* Testing Values             */
/******************************/
const testValue = 'test';
const searchInputTestId = 'search-bar-input';
const searchInputClearTestId = 'search-bar-clear';

/******************************/
/* Mocks                      */
/******************************/
const mockOnChange = jest.fn();

/******************************/
/* Render setup               */
/******************************/
const setup = () => {
  return render(
    <SearchInput
      value={testValue}
      onChange={mockOnChange}
      onSubmit={jest.fn()}
      onFocus={jest.fn()}
    />
  );
};

/******************************/
/* Testing                    */
/******************************/
describe('common - Header - SearchInput', () => {
  it('Should register onChange event and clear input', async () => {
    const { getByTestId } = setup();

    const searchInput = getByTestId(searchInputTestId);
    const searchClear = getByTestId(searchInputClearTestId);

    await act(async () => {
      userEvent.type(searchInput, testValue);
    });

    expect(mockOnChange).toBeCalled();

    await act(async () => {
      fireEvent.click(searchClear);
    });

    expect(mockOnChange).toBeCalled();
  });
});
