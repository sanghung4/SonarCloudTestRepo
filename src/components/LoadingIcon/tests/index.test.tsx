import { render, screen } from '@testing-library/react';

import LoadingIcon, {
  loadingIconSizes
} from 'components/LoadingIcon/LoadingIcon';

/**
 * Setup test
 */
function setup(size?: keyof typeof loadingIconSizes | number) {
  render(<LoadingIcon data-testid="icon" size={size} />);
  return screen.queryByTestId('icon');
}

/**
 * TEST
 */
describe('common/LoadingIcon', () => {
  // 🟢 1 - default
  it('Expect to match size classname as default', () => {
    // act
    const icon = setup();
    // assert
    expect(icon?.className).toMatch(new RegExp(`(${loadingIconSizes.md})`));
  });

  // 🟢 2 - keyof loadingIconSizes
  it('Expect to match classname for different size from keys of `loadingIconSizes`', () => {
    // act
    const icon = setup('sm');
    // assert
    expect(icon?.className).toMatch(new RegExp(`(${loadingIconSizes.sm})`));
  });

  // 🟢 3 - keyof loadingIconSizes
  it('Expect to match classname for numbered size', () => {
    // act
    const icon = setup(18);
    // assert
    expect(icon?.className).toMatch(/(-[18px])/);
  });
});
