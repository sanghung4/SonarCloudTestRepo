import { act, fireEvent } from '@testing-library/react';

import { AuthContext } from 'AuthProvider';
import { mockAuthContext } from 'AuthProvider.mocks';

import OrderSummary, { OrderSummaryProps } from 'Cart/OrderSummary';
import { mockCart, mockCartContext } from 'Cart/tests/mocks';
import { VALUE_OVER_10MIL } from 'Cart/util';
import { ErpSystemEnum } from 'generated/graphql';
import * as t from 'locales/en/translation.json';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';
import { SelectedAccounts } from 'providers/SelectedAccountsProvider';
import { CartContext } from 'providers/CartProvider';

/**
 * Mock values
 */
const defaultAccount: SelectedAccounts = {
  billTo: { erpAccountId: '' },
  shipTo: { erpAccountId: '' },
  shippingBranchId: '',
  erpSystemName: ErpSystemEnum.Eclipse
};
const defaultProps: OrderSummaryProps = { page: 'cart' };
const mockApproveData = {
  orderTotal: '23.00',
  subTotal: '20.00',
  tax: '3.00'
};

/**
 * Mock props
 */
const mocks = {
  account: { ...defaultAccount },
  auth: { ...mockAuthContext },
  cart: { ...mockCartContext },
  props: { ...defaultProps }
};

/**
 * Setup
 */
function setup(p: typeof mocks) {
  return render(
    <AuthContext.Provider value={p.auth}>
      <CartContext.Provider value={p.cart}>
        <OrderSummary {...p.props} />
      </CartContext.Provider>
    </AuthContext.Provider>,
    {
      selectedAccountsConfig: { selectedAccounts: p.account }
    }
  );
}

/**
 * TEST
 */
describe('OrderSummary tests', () => {
  afterEach(() => {
    mocks.account = { ...defaultAccount };
    mocks.auth = { ...mockAuthContext };
    mocks.cart = { ...mockCartContext };
    mocks.props = { ...defaultProps };
  });

  it('should not show the Component when there is no cart', async () => {
    setBreakpoint('desktop');

    const { queryByTestId } = setup(mocks);
    await act(() => new Promise((res) => setTimeout(res, 0)));

    const component = queryByTestId('ordersummary-component');
    expect(component).not.toBeInTheDocument();
  });

  it('should show the Skeleton when loading', async () => {
    setBreakpoint('desktop');
    mocks.cart.cart = { ...mockCart };
    mocks.props.orderPreviewLoading = true;

    const { findByTestId } = setup(mocks);
    await act(() => new Promise((res) => setTimeout(res, 0)));

    const skeleton = await findByTestId('ordersummary-skeleton');
    expect(skeleton).toBeInTheDocument();
  });

  it('should match snapshot when loading', async () => {
    setBreakpoint('desktop');
    mocks.cart.cart = { ...mockCart };
    mocks.cart.cartLoading = true;
    mocks.props.orderPreviewLoading = true;

    const { container } = setup(mocks);
    await act(() => new Promise((res) => setTimeout(res, 0)));

    expect(container).toMatchSnapshot();
  });

  it('should match snapshot when loading with additionalCTAText', async () => {
    setBreakpoint('desktop');
    mocks.cart.cart = { ...mockCart };
    mocks.cart.cartLoading = true;
    mocks.props.orderPreviewLoading = true;
    mocks.props.additionalCTAText = 'test';
    mocks.props.onAdditionalCTAClick = jest.fn();

    const { container } = setup(mocks);
    await act(() => new Promise((res) => setTimeout(res, 0)));

    expect(container).toMatchSnapshot();
  });

  it('should show the Component when there is a cart and not loading', async () => {
    setBreakpoint('desktop');
    mocks.cart.cart = { ...mockCart };
    mocks.props.orderPreviewLoading = false;

    const { findByTestId } = setup(mocks);
    await act(() => new Promise((res) => setTimeout(res, 0)));

    const component = await findByTestId('ordersummary-component');
    const content = await findByTestId('ordersummary-component');
    expect(component).toBeInTheDocument();
    expect(content).toBeInTheDocument();
  });

  it('should match snapshot with approve data', async () => {
    setBreakpoint('desktop');
    mocks.cart.cart = { ...mockCart };
    mocks.props.approveData = { ...mockApproveData };
    mocks.props.orderPreviewLoading = false;

    const { container } = setup(mocks);
    await act(() => new Promise((res) => setTimeout(res, 0)));

    expect(container).toMatchSnapshot();
  });

  it('should match snapshot with approve data on mobile', async () => {
    setBreakpoint('mobile');
    mocks.cart.cart = { ...mockCart };
    mocks.props.approveData = { ...mockApproveData };
    mocks.props.orderPreviewLoading = false;

    const { container } = setup(mocks);
    await act(() => new Promise((res) => setTimeout(res, 0)));

    expect(container).toMatchSnapshot();
  });

  it('should match snapshot with approve data and tax', async () => {
    setBreakpoint('desktop');
    mocks.cart.cart = { ...mockCart };
    mocks.props.approveData = { ...mockApproveData };
    mocks.props.orderPreviewLoading = false;
    mocks.props.showTax = true;

    const { container } = setup(mocks);
    await act(() => new Promise((res) => setTimeout(res, 0)));

    expect(container).toMatchSnapshot();
  });

  it('should match snapshot with only tax', async () => {
    setBreakpoint('desktop');
    mocks.cart.cart = { ...mockCart };
    mocks.props.orderPreviewLoading = false;
    mocks.props.showTax = true;

    const { container } = setup(mocks);
    await act(() => new Promise((res) => setTimeout(res, 0)));

    expect(container).toMatchSnapshot();
  });

  it('should match "free" as "confirmation" page with no products', async () => {
    setBreakpoint('desktop');
    mocks.cart.cart = { ...mockCart, products: [] };
    mocks.props.approveData = { ...mockApproveData };
    mocks.props.orderPreviewLoading = false;
    mocks.props.page = 'confirmation';

    const { getByTestId } = setup(mocks);
    await act(() => new Promise((res) => setTimeout(res, 0)));

    expect(getByTestId('cart-shipping-handling')).toHaveTextContent(
      t.cart.free
    );
  });

  it('should match "—" with no products', async () => {
    setBreakpoint('desktop');
    mocks.cart.cart = { ...mockCart, products: [] };
    mocks.props.approveData = { ...mockApproveData };
    mocks.props.orderPreviewLoading = false;

    const { getByTestId } = setup(mocks);
    await act(() => new Promise((res) => setTimeout(res, 0)));

    expect(getByTestId('cart-shipping-handling')).toHaveTextContent('—');
  });

  it('should match snapshot as employee', async () => {
    setBreakpoint('desktop');
    mocks.auth.profile = {
      permissions: [],
      userId: '',
      isEmployee: true,
      isVerified: true
    };
    mocks.cart.cart = { ...mockCart };
    mocks.props.orderPreviewLoading = false;

    const { container } = setup(mocks);
    await act(() => new Promise((res) => setTimeout(res, 0)));

    expect(container).toMatchSnapshot();
  });

  it('should expect cta button is called when clicking', async () => {
    setBreakpoint('desktop');
    mocks.cart.cart = { ...mockCart };
    mocks.props.orderPreviewLoading = true;
    mocks.props.additionalCTAText = 'test';
    mocks.props.onAdditionalCTAClick = jest.fn();

    const { getByTestId } = setup(mocks);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    fireEvent.click(getByTestId('additional-cta-button'));

    expect(mocks.props.onAdditionalCTAClick).toBeCalled();
  });

  it('should disable approve button when approve loading is true', async () => {
    setBreakpoint('desktop');
    mocks.cart.cart = { ...mockCart };
    mocks.props.approveOrderLoading = { approve: true, reject: false };
    mocks.props.orderPreviewLoading = false;
    mocks.props.page = 'approval';

    const { findByTestId } = setup(mocks);
    await act(() => new Promise((res) => setTimeout(res, 0)));

    const component = await findByTestId('ordersummary-component');
    const content = await findByTestId('ordersummary-component');
    const approveButton = await findByTestId('checkout-top-button');
    expect(component).toBeInTheDocument();
    expect(content).toBeInTheDocument();
    expect(approveButton).toBeInTheDocument();
    expect(approveButton).toHaveAttribute('disabled');
  });

  it('Should match snapshot for shipTo account hold', async () => {
    setBreakpoint('desktop');
    mocks.cart.cart = { ...mockCart };
    mocks.cart.cartLoading = false;
    mocks.props.orderPreviewLoading = false;
    mocks.account.shipToErpAccount = { creditHold: true };

    const { container } = setup(mocks);
    await act(() => new Promise((res) => setTimeout(res, 0)));

    expect(container).toMatchSnapshot();
  });

  it('should match snapshot when cart subtotal is over 10M on Waterworks', async () => {
    setBreakpoint('desktop');
    const over10Mil = (VALUE_OVER_10MIL + 1) * 100;
    mocks.cart.cart = { ...mockCart, subtotal: over10Mil };
    mocks.account.erpSystemName = ErpSystemEnum.Mincron;
    mocks.cart.checkingOutWithQuote = true;
    mocks.props.orderPreviewLoading = false;

    const { container, getByTestId } = setup(mocks);
    await act(() => new Promise((res) => setTimeout(res, 0)));

    expect(getByTestId('checkout-top-button')).toBeInTheDocument();
    expect(container).toMatchSnapshot();
  });
});
