import { render, screen } from '@testing-library/react';
import { Button } from 'components/Button';

/**
 * TEST
 */
describe('components/Button', () => {
  // 🟢 1 - default
  it('Expect to be rendered under default settings', () => {
    // act
    render(<Button onClick={jest.fn()} data-testid="button" />);
    const button = screen.queryByTestId('button');
    // assert
    expect(button).toBeInTheDocument();
  });

  // 🟢 2 - loading
  it('Expect button to be disabled when loading', () => {
    // act
    render(<Button onClick={jest.fn()} data-testid="button" loading />);
    const button = screen.queryByTestId<HTMLButtonElement>('button');
    // assert
    expect(button?.disabled).toBeTruthy();
  });

  // 🟢 3 - icon not to be rendered
  it('Expect icon not be rendered when iconPosition is not set', () => {
    // act
    render(
      <Button onClick={jest.fn()} icon={<div data-testid="test-icon" />} />
    );
    const testIcon = screen.queryByTestId('test-icon');
    // assert
    expect(testIcon).not.toBeInTheDocument();
  });

  // 🟢 4 - icon to be rendered (left)
  it('Expect icon not be rendered when iconPosition is set as left', () => {
    // act
    render(
      <Button
        onClick={jest.fn()}
        iconPosition="left"
        icon={<div data-testid="test-icon" />}
      />
    );
    const testIcon = screen.queryByTestId('test-icon');
    // assert
    expect(testIcon).toBeInTheDocument();
  });

  // 🟢 5 - icon to be rendered (right)
  it('Expect icon not be rendered when iconPosition is set as right', () => {
    // act
    render(
      <Button
        onClick={jest.fn()}
        iconPosition="right"
        icon={<div data-testid="test-icon" />}
      />
    );
    const testIcon = screen.queryByTestId('test-icon');
    // assert
    expect(testIcon).toBeInTheDocument();
  });
});
