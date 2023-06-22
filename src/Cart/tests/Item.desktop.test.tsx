import { act } from '@testing-library/react';

import Cart from 'Cart';

import ItemDesktop from 'Cart/Item.desktop';
import {
  mockCartContext,
  mockCart,
  mockCartContract,
  mockCartProducts
} from 'Cart/tests/mocks';
import { LineItem } from 'generated/graphql';
import { CartContext } from 'providers/CartProvider';
import {
  actClickOnElement,
  actUpdateInput,
  updateInput
} from 'test-utils/commonFireEvents';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

/**
 * Types
 */
type MockProps = {
  updateItemQuantity?: (
    itemId: string,
    quantity: number,
    minIncrementQty: number,
    productName: string
  ) => void;
  handleQuantityUpdate: (quantity: number, id: string) => void;
  readOnly?: boolean;
  index?: number;
};

/**
 * Mock values
 */
const mocks = { cart: { ...mockCartContext } };

/**
 * Setup
 */
function setup(m: typeof mocks, lineItem: LineItem, props?: MockProps) {
  return render(
    <CartContext.Provider value={m.cart}>
      <ItemDesktop
        handleQuantityUpdate={jest.fn()}
        lineItem={lineItem}
        index={0}
        {...props}
      />
    </CartContext.Provider>
  );
}

/**
 * Test
 */
describe('Cart - Item.desktop', () => {
  afterEach(() => {
    mocks.cart = { ...mockCartContext };
  });

  it('Should match snapshot with product', () => {
    const lineItem = mockCartProducts[0];
    const { container } = setup(mocks, lineItem);
    act(() => {
      expect(container).toMatchSnapshot();
    });
  });

  it('Should match snapshot as readOnly', () => {
    const lineItem = mockCartProducts[0];
    const props = { readOnly: true, handleQuantityUpdate: jest.fn() };
    const { container } = setup(mocks, lineItem, props);
    act(() => {
      expect(container).toMatchSnapshot();
    });
  });

  it('Should match snapshot as contract with line notes', () => {
    const lineItem = mockCartProducts[0];
    mocks.cart.contract = mockCartContract;
    mocks.cart.lineNotes = { [lineItem.id!]: 'test' };
    const props = { readOnly: true, handleQuantityUpdate: jest.fn() };
    const { container } = setup(mocks, lineItem, props);
    act(() => {
      expect(container).toMatchSnapshot();
    });
  });

  it('Expect deleteItem is called when clicking on the delete button', () => {
    const lineItem = mockCartProducts[0];
    const { getByTestId } = setup(mocks, lineItem);
    actClickOnElement(getByTestId('remove-item-button-0'));
    act(() => {
      expect(mocks.cart.deleteItem).toBeCalled();
    });
  });

  it('Should delete all items in the cart when remove all from cart is selected and confirmed', () => {
    setBreakpoint('desktop');
    mockCartContext.cartLoading = true;
    mockCartContext.cart = mockCart;
    mockCartContext.itemCount = 2;
    const { getByTestId } = render(
      <CartContext.Provider value={mockCartContext}>
        <Cart />
      </CartContext.Provider>
    );
    actClickOnElement(getByTestId('remove-all-from-cart'));
    actClickOnElement(getByTestId('remove-all-items-dialog-confirm'));
    act(() => {
      expect(mocks.cart.deleteCartItems).toBeCalled();
    });
  });

  it('Expect updateItemQuantity is called when changing the qty input', () => {
    const updateQtyFn = jest.fn();
    const lineItem = mockCartProducts[0];
    const props = {
      updateItemQuantity: updateQtyFn,
      handleQuantityUpdate: jest.fn()
    };
    const { getByTestId } = setup(mocks, lineItem, props);
    updateInput(getByTestId('quantity-input-0'), '20');
    act(() => {
      expect(updateQtyFn).toBeCalled();
    });
  });

  it('Expect updateItemQuantity is called when changing the qty input with no name', () => {
    const updateQtyFn = jest.fn();
    const lineItem = mockCartProducts[1];
    const props = {
      updateItemQuantity: updateQtyFn,
      handleQuantityUpdate: jest.fn(),
      index: 1
    };
    const { getByTestId } = setup(mocks, lineItem, props);
    updateInput(getByTestId('quantity-input-1'), '20');
    act(() => {
      expect(updateQtyFn).toBeCalled();
    });
  });

  it('Expect modal save button to be in the document when opening line notes modal', () => {
    const lineItem = mockCartProducts[0];
    mocks.cart.contract = mockCartContract;
    const { getByTestId } = setup(mocks, lineItem);
    actClickOnElement(getByTestId('cart-item-open-line-notes-0'));
    expect(getByTestId('line-notes-modal-save')).toBeInTheDocument();
  });

  it('Expect setLineNotes to be called when updating the line notes from modal', () => {
    const testValue = 'test';
    const lineItem = mockCartProducts[0];
    mocks.cart.contract = mockCartContract;
    const { getByTestId } = setup(mocks, lineItem);

    actClickOnElement(getByTestId('cart-item-open-line-notes-0'));
    const input = getByTestId('line-notes-modal-textfield');
    actUpdateInput(input, testValue);
    actClickOnElement(getByTestId('line-notes-modal-save'));

    expect(input).toHaveDisplayValue(testValue);
    expect(mocks.cart.setLineNotes).toBeCalledWith({
      [lineItem.id!]: testValue
    });
  });

  it('Expect setLineNotes not to be called when the value is the same from updating the line notes from modal', () => {
    const testValue = 'test';
    const lineItem = mockCartProducts[0];
    mocks.cart.contract = mockCartContract;
    mocks.cart.lineNotes = { [mockCartProducts[0].id!]: testValue };
    const { getByTestId } = setup(mocks, lineItem);

    actClickOnElement(getByTestId('cart-item-open-line-notes-0'));
    const input = getByTestId('line-notes-modal-textfield');
    actUpdateInput(input, testValue);
    actClickOnElement(getByTestId('line-notes-modal-save'));

    expect(input).toHaveDisplayValue(testValue);
    expect(mocks.cart.setLineNotes).not.toBeCalled();
  });

  it('Expect qty and qtyAvailable to set as 0 when they are undefined', () => {
    const lineItem = {
      ...mockCartProducts[0],
      qtyAvailable: null,
      quantity: null
    };
    lineItem.product = {
      ...mockCartProducts[0].product!,
      minIncrementQty: null
    };
    const { getByTestId } = setup(mocks, lineItem);
    expect(getByTestId(`cart-item-out-of-stock-warning-0`)).toBeInTheDocument();
  });

  it('Should show Add all to list button in the cart', () => {
    setBreakpoint('desktop');
    mockCartContext.cartLoading = false;
    mockCartContext.cart = mockCart;
    mockCartContext.itemCount = 2;
    const index = -1;
    const { getByTestId } = render(
      <CartContext.Provider value={mockCartContext}>
        <Cart />
      </CartContext.Provider>
    );
    expect(getByTestId(`add-to-list-button-${index}`)).toBeInTheDocument();
  });

  it('Should show Add all to list button in the cart on mobile', () => {
    setBreakpoint('mobile');
    mockCartContext.cartLoading = true;
    mockCartContext.cart = mockCart;
    mockCartContext.itemCount = 2;
    const index = -1;
    const { getByTestId } = render(
      <CartContext.Provider value={mockCartContext}>
        <Cart />
      </CartContext.Provider>
    );
    expect(getByTestId(`add-to-list-button-${index}`)).toBeInTheDocument();
  });
});
