import { ReactNode } from 'react';

import { render, screen } from '@testing-library/react';

import ConditionalWrapper from 'components/ConditionalWrapper';

/**
 * Setup Test
 */
function setup(condition: boolean) {
  // arrange
  const wrapperElement = (children: ReactNode) => (
    <span data-testid="wrapper">{children}</span>
  );
  // act
  render(
    <ConditionalWrapper condition={condition} wrapper={wrapperElement}>
      <div data-testid="child" />
    </ConditionalWrapper>
  );
  const child = screen.queryByTestId('child');
  const wrapper = screen.queryByTestId('wrapper');

  return { child, wrapper };
}

/**
 * TEST
 */
describe('components/ConditionalWrapper', () => {
  // 🟢 1 - falsey
  it('Expect wrapper NOT to be rendered when false', () => {
    // act
    const { child, wrapper } = setup(false);
    // assert
    expect(child).toBeInTheDocument();
    expect(wrapper).not.toBeInTheDocument();
  });

  // 🟢 2 - truthy
  it('Expect wrapper NOT to be rendered when true', () => {
    // act
    const { child, wrapper } = setup(true);
    // assert
    expect(child).toBeInTheDocument();
    expect(wrapper).toBeInTheDocument();
  });
});
