import { act, fireEvent } from '@testing-library/react';

import Cart from 'Cart';

import { mockCartContext, mockCart, mockCartContract } from 'Cart/tests/mocks';
import { VALUE_OVER_10MIL } from 'Cart/util';
import { mockData as mockContract } from 'Contract/tests/mocks';
import { DeliveryMethodEnum, ErpSystemEnum } from 'generated/graphql';
import { CartContext } from 'providers/CartProvider';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

/**
 * Mock values
 */
const mockContext = { ...mockCartContext };
const mockPush = jest.fn();

/**
 * Mock methods
 */
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useHistory: () => ({
    push: mockPush
  })
}));

/**
 * Test
 */
describe('Cart', () => {
  it('Cart should show as loading', async () => {
    mockContext.cartLoading = true;
    const { getByTestId } = render(<Cart />, {
      cartConfig: mockContext,
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { erpAccountId: '' },
          shipTo: { erpAccountId: '' },
          shippingBranchId: '',
          erpSystemName: ErpSystemEnum.Eclipse
        }
      }
    });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(getByTestId('loader-component')).toBeInTheDocument();
  });

  it('Cart should show as loading with valid cart', async () => {
    mockContext.cartLoading = true;
    mockContext.cart = mockCart;
    const { container } = render(<Cart />, {
      cartConfig: mockContext,
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { erpAccountId: '' },
          shipTo: { erpAccountId: '' },
          shippingBranchId: '',
          erpSystemName: ErpSystemEnum.Eclipse
        }
      }
    });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('Should match snapshot with valid cart on desktop', async () => {
    mockContext.cartLoading = false;
    mockContext.cart = mockCart;
    setBreakpoint('desktop');
    const { container } = render(<Cart />, {
      cartConfig: mockContext,
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { erpAccountId: '' },
          shipTo: { erpAccountId: '' },
          shippingBranchId: '',
          erpSystemName: ErpSystemEnum.Eclipse
        }
      }
    });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('Should match snapshot with valid cart on mobile', async () => {
    mockContext.cartLoading = false;
    mockContext.cart = mockCart;
    setBreakpoint('mobile');
    const { container } = render(<Cart />, {
      cartConfig: mockContext,
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { erpAccountId: '' },
          shipTo: { erpAccountId: '' },
          shippingBranchId: '',
          erpSystemName: ErpSystemEnum.Eclipse
        }
      }
    });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('Should match snapshot with valid cart and contract on desktop', async () => {
    mockContext.cartLoading = false;
    mockContext.cart = mockCart;
    mockContext.contract = {
      data: mockContract,
      id: mockContract.contractNumber!,
      shipToId: ''
    };
    setBreakpoint('desktop');
    const { container } = render(<Cart />, {
      cartConfig: mockContext,
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { erpAccountId: '' },
          shipTo: { erpAccountId: '' },
          shippingBranchId: '',
          erpSystemName: ErpSystemEnum.Eclipse
        }
      }
    });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('Should match snapshot with valid cart and contract on desktop but no contract description', async () => {
    mockContext.cartLoading = false;
    mockContext.cart = mockCart;
    mockContext.contract = {
      data: { ...mockContract, contractDescription: null },
      id: mockContract.contractNumber!,
      shipToId: ''
    };
    setBreakpoint('desktop');
    const { container } = render(<Cart />, {
      cartConfig: mockContext,
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { erpAccountId: '' },
          shipTo: { erpAccountId: '' },
          shippingBranchId: '',
          erpSystemName: ErpSystemEnum.Eclipse
        }
      }
    });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('Should match snapshot with valid cart and contract on mobile', async () => {
    mockContext.cartLoading = false;
    mockContext.cart = mockCart;
    mockContext.contract = {
      data: mockContract,
      id: mockContract.contractNumber!,
      shipToId: ''
    };
    setBreakpoint('mobile');
    const { container } = render(<Cart />, {
      cartConfig: mockContext,
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { erpAccountId: '' },
          shipTo: { erpAccountId: '' },
          shippingBranchId: '',
          erpSystemName: ErpSystemEnum.Eclipse
        }
      }
    });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('Should match snapshot with valid cart and contract but jobName and contractNumber are null', async () => {
    mockContext.cartLoading = false;
    mockContext.cart = mockCart;
    mockContext.contract = {
      data: { ...mockContract, jobName: null, contractNumber: null },
      id: mockContract.contractNumber!,
      shipToId: ''
    };
    setBreakpoint('desktop');
    const { container } = render(<Cart />, {
      cartConfig: mockContext,
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { erpAccountId: '' },
          shipTo: { erpAccountId: '' },
          shippingBranchId: '',
          erpSystemName: ErpSystemEnum.Eclipse
        }
      }
    });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('Click "Proceed to Checkout" button in order summary', async () => {
    mockContext.cartLoading = false;
    mockContext.cart = {
      ...mockCart,
      deliveryMethod: DeliveryMethodEnum.Delivery
    };
    mockContext.contract = undefined;
    setBreakpoint('mobile');
    const { getByTestId } = render(<Cart />, {
      cartConfig: mockContext,
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { erpAccountId: '' },
          shipTo: { erpAccountId: '' },
          shippingBranchId: '',
          erpSystemName: ErpSystemEnum.Eclipse
        }
      }
    });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    const button = getByTestId('checkout-top-button');
    expect(button).toBeInTheDocument();
    expect(button).not.toBeDisabled();
    fireEvent.click(button);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mockPush).toBeCalledWith('/checkout', {
      canShowCustomNavAlert: false
    });
  });

  it('Cart showing add all to list button after the new item added to cart', async () => {
    mockContext.cartLoading = false;
    mockContext.cart = {
      ...mockCart,
      deliveryMethod: DeliveryMethodEnum.Delivery
    };
    mockContext.contract = undefined;
    mockContext.itemAdded = true;
    mockContext.itemCount = 3;
    const index = -1;
    setBreakpoint('desktop');
    const { getByTestId } = render(<Cart />, {
      cartConfig: mockContext,
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { erpAccountId: '' },
          shipTo: { erpAccountId: '' },
          shippingBranchId: '',
          erpSystemName: ErpSystemEnum.Eclipse
        }
      }
    });
    await act(() => new Promise((res) => setTimeout(res, 0)));

    expect(getByTestId(`add-to-list-button-${index}`)).toBeInTheDocument();
  });

  it('Click "Proceed to Checkout" button with contract', async () => {
    mockContext.cartLoading = false;
    mockContext.cart = {
      ...mockCart,
      deliveryMethod: DeliveryMethodEnum.Delivery
    };
    mockContext.contract = mockCartContract;
    setBreakpoint('mobile');
    const { getByTestId } = render(<Cart />, {
      cartConfig: mockContext,
      selectedAccountsConfig: {
        selectedAccounts: {
          billTo: { erpAccountId: '' },
          shipTo: { erpAccountId: '' },
          shippingBranchId: '',
          erpSystemName: ErpSystemEnum.Eclipse
        }
      }
    });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    const button = getByTestId('checkout-top-button');
    expect(button).toBeInTheDocument();
    expect(button).not.toBeDisabled();
    fireEvent.click(button);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mockPush).toBeCalledWith('/checkout', {
      canShowCustomNavAlert: true
    });
  });

  it('Expect dialog when account is on hold', async () => {
    mockContext.cartLoading = false;
    mockContext.cart = {
      ...mockCart,
      deliveryMethod: DeliveryMethodEnum.Delivery
    };
    mockContext.contract = undefined;
    setBreakpoint('desktop');
    const wrapper = render(<Cart />, {
      cartConfig: mockContext,
      selectedAccountsConfig: {
        selectedAccounts: {
          shipTo: { erpAccountId: '' },
          shippingBranchId: '',
          billToErpAccount: { creditHold: true },
          erpSystemName: ErpSystemEnum.Eclipse
        }
      }
    });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    const checkout = wrapper.getByTestId('checkout-top-button');
    expect(checkout).toBeInTheDocument();
    expect(checkout).not.toBeDisabled();
    fireEvent.click(checkout);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    const closeDialog = wrapper.getByTestId('hold-alert-dialog-close');
    expect(closeDialog).toBeInTheDocument();
    fireEvent.click(closeDialog);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(closeDialog).toBeInTheDocument();
  });

  it('Expect dialog when items were removed', async () => {
    mockContext.cartLoading = false;
    mockContext.cart = { ...mockCart, removedProducts: ['test1', 'test2'] };
    mockContext.contract = undefined;
    setBreakpoint('desktop');
    const wrapper = render(<Cart />, { cartConfig: mockContext });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    const closeDialog = wrapper.getByTestId('item-removed-dialog-close');
    expect(closeDialog).toBeInTheDocument();
    expect(closeDialog).not.toBeDisabled();
    fireEvent.click(closeDialog);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(closeDialog).toBeInTheDocument();
  });

  it('Cleared cart for eclipse should show loader', async () => {
    mockContext.cart = undefined;
    const { getByTestId } = render(<Cart />, { cartConfig: mockContext });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(getByTestId('loader-component')).toBeInTheDocument();
  });

  it('Warning Should be visible with Subtotal above 10 Million and on mincron account', async () => {
    setBreakpoint('desktop');
    mockCart.subtotal = (VALUE_OVER_10MIL + 1) * 100;
    mockContext.cart = mockCart;

    const wrapper = render(<Cart />, { cartConfig: mockContext });

    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(wrapper.getByTestId('over-10mil-warning')).toBeInTheDocument();
  });

  it('Warning Should not be visible with Subtotal below 10 Million and on mincron account', async () => {
    mockCart.subtotal = 0;
    mockContext.cart = mockCart;

    setBreakpoint('desktop');
    const wrapper = render(<Cart />, { cartConfig: mockContext });
    expect(wrapper.queryByText('over-10mil-warning')).not.toBeInTheDocument();
  });

  it('Expect "remove all from cart" to be hidden on a blank Mincron cart', async () => {
    setBreakpoint('desktop');
    mockContext.cart = undefined;
    mockContext.contract = undefined;

    const { queryAllByTestId } = render(<Cart />, {
      cartConfig: mockContext,
      selectedAccountsConfig: { isMincron: true }
    });
    act(() => {
      expect(queryAllByTestId('remove-all-from-cart').length).toBe(0);
    });
  });

  it('should test "remove all from cart"', async () => {
    mockCart.subtotal = 0;
    mockContext.cart = mockCart;
    mockContext.contract = undefined;
    /**
     * setting the cartItems count greater than one to test
     * remove all to cart button
     */
    mockContext.itemCount = 2;

    const { findByTestId } = render(<Cart />, { cartConfig: mockContext });
    const removeFromCartButton = await findByTestId('remove-all-from-cart');

    expect(removeFromCartButton).toBeInTheDocument();
  });
});
