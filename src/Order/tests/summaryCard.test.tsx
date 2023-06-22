import { fireEvent } from '@testing-library/react';

import { mockCartContext } from 'Cart/tests/mocks';
import SummaryCard from 'Order/SummaryCard';
import { orderMocks, pricingDataMocks2 } from 'Order/tests/mocks';
import { CartContext } from 'providers/CartProvider';
import { render } from 'test-utils/TestWrapper';

/**
 * Mock values
 */
const mockProps: Parameters<typeof SummaryCard>[0] = {};
const mocks = {
  cart: { ...mockCartContext },
  props: { ...mockProps }
};

/**
 * Mock methods
 */
jest.mock('@dialexa/reece-component-library', () => ({
  ...jest.requireActual('@dialexa/reece-component-library'),
  __esModule: true,
  useSnackbar: () => ({ pushAlert: jest.fn() })
}));

/**
 * Setup test function
 */
function setup(m: typeof mocks) {
  return render(<SummaryCard {...m.props} />, { cartConfig: m.cart });
}

/**
 * TEST
 */
describe('Order - SummaryCard', () => {
  afterEach(() => {
    mocks.cart = { ...mockCartContext };
    mocks.props = {};
  });

  it('Should match snapshot when loading', () => {
    mocks.props.loading = true;
    const { container } = setup(mocks);
    expect(container).toMatchSnapshot();
  });

  it('Should match snapshot with undefined order data', () => {
    const { container } = setup(mocks);
    expect(container).toMatchSnapshot();
  });

  it('Should match snapshot with an order', () => {
    mocks.props.order = { ...orderMocks[0] };
    mocks.props.pricingData = pricingDataMocks2;
    const { container } = setup(mocks);
    expect(container).toMatchSnapshot();
  });

  it('Should expect addToCart is not called with empty pricing data', () => {
    mocks.props.order = { ...orderMocks[0] };
    mocks.props.pricingData = [];
    const { getByTestId } = setup(mocks);
    fireEvent.click(getByTestId('add-all-to-cart-button'));
    expect(mocks.cart.addItemsToCart).toBeCalledTimes(0);
  });

  it('Should expect addToCart is called', () => {
    mocks.props.order = { ...orderMocks[0] };
    mocks.props.pricingData = pricingDataMocks2;
    const { getByTestId } = setup(mocks);
    fireEvent.click(getByTestId('add-all-to-cart-button'));
    expect(mocks.cart.addItemsToCart).toBeCalledTimes(0);
  });
});
