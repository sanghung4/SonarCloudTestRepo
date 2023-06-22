import { act, fireEvent } from '@testing-library/react';
import PoshMarketing from 'Brands/Posh/index';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

describe('Brands - Posh', () => {
  it('index should match the snapshot on desktop', () => {
    setBreakpoint('desktop');
    const { container } = render(<PoshMarketing />);
    expect(container).toMatchSnapshot();
  });
  it('index should match the snapshot on mobile', () => {
    setBreakpoint('mobile');
    const { container } = render(<PoshMarketing />);
    expect(container).toMatchSnapshot();
  });
  it('should render the Posh page with finishes on desktop', async () => {
    setBreakpoint('desktop');
    const { getByTestId } = render(<PoshMarketing />);

    await act(() => new Promise((res) => setTimeout(res, 0)));

    const button = getByTestId('gold-finish-button-0');
    expect(button).toBeInTheDocument();

    fireEvent.click(button);
    await act(() => new Promise((res) => setTimeout(res, 0)));

    expect(getByTestId('posh-product-image-0')).toHaveAttribute(
      'src',
      'kitchenFaucetGold.png'
    );
  });
  it('should render the Posh page with finishes on mobile', async () => {
    setBreakpoint('mobile');
    const { getByTestId } = render(<PoshMarketing />);

    await act(() => new Promise((res) => setTimeout(res, 0)));

    const button = getByTestId('black-finish-button-1');
    expect(button).toBeInTheDocument();

    fireEvent.click(button);
    await act(() => new Promise((res) => setTimeout(res, 0)));

    expect(getByTestId('posh-product-image-1')).toHaveAttribute(
      'src',
      'showerBlack.png'
    );
  });
  it('should render the Posh page with different finishes on desktop', async () => {
    setBreakpoint('mobile');
    const { getByTestId } = render(<PoshMarketing />);

    await act(() => new Promise((res) => setTimeout(res, 0)));

    const nickelButton = getByTestId('nickel-finish-button-1');
    expect(nickelButton).toBeInTheDocument();

    fireEvent.click(nickelButton);
    await act(() => new Promise((res) => setTimeout(res, 0)));

    expect(getByTestId('posh-product-image-1')).toHaveAttribute(
      'src',
      'showerNickel.png'
    );

    const chromeButton = getByTestId('chrome-finish-button-1');
    expect(chromeButton).toBeInTheDocument();

    fireEvent.click(chromeButton);
    await act(() => new Promise((res) => setTimeout(res, 0)));

    expect(getByTestId('posh-product-image-1')).toHaveAttribute(
      'src',
      'showerChrome.png'
    );
  });
});
