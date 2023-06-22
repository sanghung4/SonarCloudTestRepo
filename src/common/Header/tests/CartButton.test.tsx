import { fireEvent } from '@testing-library/react';

import { mockAuthContext } from 'AuthProvider.mocks';
import { mockCartContext, mockCartContract } from 'Cart/tests/mocks';
import CartButton, { useGoToCart } from 'common/Header/CartButton';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

let mockCart = { ...mockCartContext };
let mockAuth = { ...mockAuthContext };
let mockLocation = { pathname: '' };
let mockHistory = { push: jest.fn() };

jest.mock('react-router-dom', () => ({
  useLocation: () => mockLocation,
  useHistory: () => mockHistory
}));

function MockComponent() {
  const goToCart = useGoToCart();
  return <button data-testid="button" onClick={goToCart} />;
}

describe('common - header - CartButton', () => {
  afterEach(() => {
    mockCart = { ...mockCartContext };
    mockAuth = { ...mockAuthContext };
    mockLocation = { pathname: '' };
    mockHistory = { push: jest.fn() };
  });

  it('should match snapshot on desktop', () => {
    setBreakpoint('desktop');
    const { container } = render(<CartButton />);
    expect(container).toMatchSnapshot();
  });
  it('should match snapshot on mobile', () => {
    setBreakpoint('mobile');
    const { container } = render(<CartButton />);
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot while loading on desktop', () => {
    setBreakpoint('desktop');
    mockCart.cartLoading = true;
    mockCart.itemLoading = 'test';
    const { container } = render(<CartButton />, { cartConfig: mockCart });
    expect(container).toMatchSnapshot();
  });
  it('should match snapshot while loading on mobile', () => {
    setBreakpoint('mobile');
    mockCart.cartLoading = true;
    mockCart.itemLoading = 'test';
    const { container } = render(<CartButton />, { cartConfig: mockCart });
    expect(container).toMatchSnapshot();
  });

  it('should result in null when location is "/select-accounts"', () => {
    setBreakpoint('desktop');
    mockLocation.pathname = '/select-accounts';
    const { container } = render(<CartButton />);
    expect(container).toMatchSnapshot();
  });

  it('expect goToCart to call clear quote', () => {
    mockCart.checkingOutWithQuote = true;
    const { getByTestId } = render(<MockComponent />, {
      cartConfig: mockCart,
      authConfig: mockAuth
    });
    fireEvent.click(getByTestId('button'));
    expect(mockCart.clearQuote).toBeCalled();
    expect(mockHistory.push).toBeCalledWith('/login', {});
  });
  it('expect goToCart to call to go to cart', () => {
    mockAuth.authState = { isAuthenticated: true };
    const { getByTestId } = render(<MockComponent />, {
      cartConfig: mockCart,
      authConfig: mockAuth
    });
    fireEvent.click(getByTestId('button'));
    expect(mockHistory.push).toBeCalledWith('/cart', {});
  });
  it('expect goToCart to call to go to cart with contract', () => {
    mockAuth.authState = { isAuthenticated: true };
    mockCart.contract = mockCartContract;
    const { getByTestId } = render(<MockComponent />, {
      cartConfig: mockCart,
      authConfig: mockAuth
    });
    fireEvent.click(getByTestId('button'));
    expect(mockHistory.push).toBeCalledWith('/cart', { canShowNavAlert: true });
  });
});
