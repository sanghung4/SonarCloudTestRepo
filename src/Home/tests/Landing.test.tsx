import Landing from 'Home/Landing';
import { render } from 'test-utils/TestWrapper';

describe('Home - Landing', () => {
  it('Match snapshot with hero text', () => {
    const htext = 'Business to the max_white.svg';
    const { getByTestId } = render(<Landing />);

    const heroText = getByTestId('hero-text');
    expect(heroText).toHaveTextContent(htext);
  });

  it('Match snapshot with hero sub', () => {
    const tSub =
      'Streamline business. Search product. Shop parts. Pay bills. Find nearby branches. Ready 24/7, on any device.';
    const { getByTestId } = render(<Landing />);

    const heroSub = getByTestId('hero-sub');
    expect(heroSub).toHaveTextContent(tSub);
  });
});
