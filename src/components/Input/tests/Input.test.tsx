import { render, screen } from '@testing-library/react';

import { Input } from 'components/Input';
import { fireEvent } from 'test-util';

/**
 * TEST
 */
describe('components/Input', () => {
  // 🟢 1 - default
  it('Expect to be rendered under default settings', () => {
    // act
    render(<Input data-testid="input" />);
    const input = screen.queryByTestId('input');
    // assert
    expect(input).toBeInTheDocument();
  });

  // 🟢 2 - disabled
  it('Expect to be disabled', () => {
    // act
    render(<Input data-testid="input" disabled />);
    const input = screen.queryByTestId<HTMLInputElement>('input');
    // assert
    expect(input?.disabled).toBeTruthy();
  });

  // 🟢 3 - label
  it('Expect label to be rendered', () => {
    // act
    render(<Input data-testid="input" label="test" />);
    const inputLabel = screen.queryByTestId('input-label');
    // assert
    expect(inputLabel).toBeInTheDocument();
  });

  // 🟢 4 - error
  it('Expect className to contain error styling with error status', () => {
    // act
    render(<Input data-testid="input" status="error" />);
    const input = screen.queryByTestId('input');
    // assert
    expect(input).toBeInTheDocument();
    expect(input?.className).toMatch(/(border-common-error)/m);
  });

  // 🟢 5 - loading
  it('Expect loader icon when loading', () => {
    // act
    render(<Input data-testid="input" loading />);
    const inputLoader = screen.queryByTestId('input-loader');
    // assert
    expect(inputLoader).toBeInTheDocument();
  });

  // 🟢 6 - endIcon
  it('Expect end icon to be rendered', () => {
    // act
    render(<Input endIcon={<div data-testid="end-icon" />} />);
    const endIcon = screen.queryByTestId('end-icon');
    // assert
    expect(endIcon).toBeInTheDocument();
  });

  // 🟢 7 - password
  it('Expect type is password by default', () => {
    // act
    render(<Input data-testid="input" type="password" />);
    const input = screen.queryByTestId<HTMLInputElement>('input');
    // assert
    expect(input?.type).toBe('password');
  });

  // 🟢 8 - show password
  it('Expect type is text when show password is clicked', async () => {
    // act
    render(<Input data-testid="input" type="password" />);
    const input = screen.queryByTestId<HTMLInputElement>('input');
    await fireEvent('click', 'input-toggle-password');
    // assert
    expect(input?.type).toBe('text');
  });
});
