import { render, screen } from '@testing-library/react';

import { SearchInput } from 'components/Input';
import { fillTextInput, fireEvent } from 'test-util';

/**
 * TEST
 */
describe('components/SearchInput', () => {
  // 🟢 1 - default
  it('Expect to be rendered under default settings', () => {
    // act
    render(<SearchInput data-testid="input" onSearch={jest.fn()} />);
    const input = screen.queryByTestId('input');
    // assert
    expect(input).toBeInTheDocument();
  });

  // 🟢 2 - disabled
  it('Expect input and button to be disabled', () => {
    // act
    render(<SearchInput data-testid="input" disabled onSearch={jest.fn()} />);
    const input = screen.queryByTestId<HTMLInputElement>('input');
    const button = screen.queryByTestId<HTMLButtonElement>('input-submit');
    // assert
    expect(input?.disabled).toBeTruthy();
    expect(button?.disabled).toBeTruthy();
  });

  // 🟢 3 - label
  it('Expect label to be rendered', () => {
    // act
    render(
      <SearchInput data-testid="input" label="test" onSearch={jest.fn()} />
    );
    const inputLabel = screen.queryByTestId('input-label');
    // assert
    expect(inputLabel).toBeInTheDocument();
  });

  // 🟢 4 - error
  it('Expect className to contain error styling with error status', () => {
    // act
    render(
      <SearchInput data-testid="input" status="error" onSearch={jest.fn()} />
    );
    const input = screen.queryByTestId('input');
    // assert
    expect(input).toBeInTheDocument();
    expect(input?.className).toMatch(/(border-common-error)/m);
  });

  // 🟢 5 - loading
  it('Expect loader icon when loading', () => {
    // act
    render(<SearchInput data-testid="input" loading onSearch={jest.fn()} />);
    const loading = screen.queryByTestId('input-loading');
    // assert
    expect(loading).toBeInTheDocument();
  });

  // 🟢 6 - Input & submit
  it('Expect function to be called with search', async () => {
    // arrange
    const submitFn = jest.fn();
    const value = 'test';
    // act
    render(<SearchInput data-testid="input" onSearch={submitFn} />);
    await fillTextInput('input', value);
    await fireEvent('click', 'input-submit');
    // assert
    expect(submitFn).toBeCalledWith(value);
  });
});
