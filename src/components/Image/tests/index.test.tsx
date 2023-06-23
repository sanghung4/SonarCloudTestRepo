import { render, screen } from '@testing-library/react';

import { Image } from 'components/Image';
import notfound from 'resources/images/notfound.png';
import { fireEvent } from 'test-util';

/**
 * TEST
 */
describe('components/Image', () => {
  // 🟢 1 - default
  it('Expect to be rendered under default settings', () => {
    // act
    render(<Image src="#" alt="" data-testid="img" />);
    const img = screen.queryByTestId('img');
    // assert
    expect(img).toBeInTheDocument();
  });

  // 🟢 1 - error
  it('Expect src to be error when error is triggered', async () => {
    // act
    render(<Image src="#" alt="" data-testid="img" />);
    const img = screen.queryByTestId('img');
    await fireEvent('error', 'img');
    // assert
    expect(img).toHaveAttribute('src', notfound);
  });
});
