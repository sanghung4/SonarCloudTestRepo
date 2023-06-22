import { fireEvent } from '@testing-library/react';

import { mockCartContext } from 'Cart/tests/mocks';
import LineItem, { OrderLineItemProps } from 'Order/LineItem';
import {
  lineItemMocks,
  lineItemMocks3,
  pricingDataMocks,
  lineItemMocks4,
  pricingDataMocks2
} from 'Order/tests/mocks';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

/**
 * Values
 */
const mocks = {
  cart: { ...mockCartContext },
  props: {
    isMincron: true,
    lineItem: {
      uom: 'bx',
      pricingUom: 'bx',
      shipQuantity: 1,
      orderQuantity: 1
    }
  } as OrderLineItemProps
};

/**
 * Set up test function
 */
function setup(m: typeof mocks) {
  return render(<LineItem {...m.props} />, { cartConfig: m.cart });
}

/**
 * TEST
 */
describe('Order - LineItem', () => {
  afterEach(() => {
    mocks.cart = { ...mockCartContext };
    mocks.props = { loading: false };
  });

  describe('desktop', () => {
    beforeEach(() => setBreakpoint('desktop'));

    it('Should match snapshot', () => {
      const { container } = setup(mocks);
      expect(container).toMatchSnapshot();
    });

    it('Should match snapshot when loading', () => {
      mocks.props.loading = true;
      const { container } = setup(mocks);
      expect(container).toMatchSnapshot();
    });

    it('Should expect addItemToCart is called when reorder button is clicked', () => {
      const lineItem = lineItemMocks![0];
      mocks.props.lineItem = lineItem;
      mocks.props.pricingData = pricingDataMocks[0];
      const { getByTestId } = setup(mocks);
      fireEvent.click(getByTestId('reorder-button'));
      const { erpPartNumber: partNum, orderQuantity: qty } = lineItem;
      expect(mocks.cart.addItemToCart).toBeCalledWith(
        partNum,
        qty,
        0,
        pricingDataMocks[0]
      );
    });

    it('Should expect addItemToCart is called when reorder button is clicked with undefined lineItem', () => {
      const lineItem = lineItemMocks3![0];
      mocks.props.lineItem = lineItem;
      mocks.props.pricingData = pricingDataMocks2[0];
      const { getByTestId } = setup(mocks);
      fireEvent.click(getByTestId('reorder-button'));
      expect(mocks.cart.addItemToCart).toBeCalledWith(
        '',
        0,
        0,
        pricingDataMocks2[0]
      );
    });

    it('Should match snapshot when cart is loading', () => {
      mocks.props.lineItem = lineItemMocks![0];
      mocks.cart.cartLoading = true;
      const { container } = setup(mocks);
      expect(container).toMatchSnapshot();
    });

    it('Should match snapshot with comment line item', () => {
      mocks.props.lineItem = lineItemMocks3![1];
      const { container } = setup(mocks);
      expect(container).toMatchSnapshot();
    });

    it('Should match snapshot with comment line item when loading', () => {
      mocks.props.lineItem = lineItemMocks3![1];
      mocks.props.loading = true;
      const { container } = setup(mocks);
      expect(container).toMatchSnapshot();
    });

    it("Should match snapshot with comment line item that's nullish", () => {
      mocks.props.lineItem = lineItemMocks3![2];
      const { container } = setup(mocks);
      expect(container).toMatchSnapshot();
    });

    it('Should match snapshot with lineComment while on Mincron', () => {
      mocks.props.lineItem = {
        ...lineItemMocks![0],
        lineComments: 'TEST 1234'
      };
      mocks.props.isMincron = true;
      const { container } = setup(mocks);
      expect(container).toMatchSnapshot();
    });

    it('Should match snapshot with lineComment while on Mincron nad loading', () => {
      mocks.props.lineItem = {
        ...lineItemMocks![0],
        lineComments: 'TEST 1234'
      };
      mocks.props.isMincron = true;
      mocks.props.loading = true;
      const { container } = setup(mocks);
      expect(container).toMatchSnapshot();
    });

    it('Should match snapshot with lineNumber', () => {
      mocks.props.lineItem = {
        ...lineItemMocks4![0],
        productId: '123'
      };
      const { container } = setup(mocks);
      expect(container).toMatchSnapshot();
    });
  });

  describe('mobile', () => {
    beforeEach(() => setBreakpoint('mobile'));

    it('Should match snapshot', () => {
      const { container } = setup(mocks);
      expect(container).toMatchSnapshot();
    });

    it('Should match snapshot when loading', () => {
      mocks.props.loading = true;
      const { container } = setup(mocks);
      expect(container).toMatchSnapshot();
    });

    it('Should expect addItemToCart is called when reorder button is clicked', () => {
      const lineItem = lineItemMocks![0];
      mocks.props.lineItem = lineItem;
      mocks.props.pricingData = pricingDataMocks[0];
      const { getByTestId } = setup(mocks);
      fireEvent.click(getByTestId('reorder-button'));
      const { erpPartNumber: partNum, orderQuantity: qty } = lineItem;
      expect(mocks.cart.addItemToCart).toBeCalledWith(
        partNum,
        qty,
        0,
        pricingDataMocks[0]
      );
    });

    it('Should expect addItemToCart is called when reorder button is clicked with undefined lineItem', () => {
      const lineItem = lineItemMocks3![0];
      mocks.props.lineItem = lineItem;
      mocks.props.pricingData = pricingDataMocks2[0];
      const { getByTestId } = setup(mocks);
      fireEvent.click(getByTestId('reorder-button'));
      expect(mocks.cart.addItemToCart).toBeCalledWith(
        '',
        0,
        0,
        pricingDataMocks2[0]
      );
    });

    it('Should match snapshot when cart is loading', () => {
      mocks.props.lineItem = lineItemMocks![0];
      mocks.cart.cartLoading = true;
      const { container } = setup(mocks);
      expect(container).toMatchSnapshot();
    });

    it('Should match snapshot with comment line item', () => {
      mocks.props.lineItem = lineItemMocks3![1];
      const { container } = setup(mocks);
      expect(container).toMatchSnapshot();
    });

    it('Should match snapshot with comment line item when loading', () => {
      mocks.props.lineItem = lineItemMocks3![1];
      mocks.props.loading = true;
      const { container } = setup(mocks);
      expect(container).toMatchSnapshot();
    });

    it("Should match snapshot with comment line item that's nullish", () => {
      mocks.props.lineItem = lineItemMocks3![2];
      const { container } = setup(mocks);
      expect(container).toMatchSnapshot();
    });

    it('Should match snapshot with lineComment while on Mincron', () => {
      mocks.props.lineItem = {
        ...lineItemMocks![0],
        lineComments: 'TEST 1234'
      };
      mocks.props.isMincron = true;
      const { container } = setup(mocks);
      expect(container).toMatchSnapshot();
    });

    it('Should match snapshot when erpPartNumber is null on Mincron', () => {
      mocks.props.lineItem = {
        ...lineItemMocks![0],
        erpPartNumber: null
      };
      mocks.props.isMincron = true;
      const { container } = setup(mocks);
      expect(container).toMatchSnapshot();
    });

    it('Should match snapshot with lineComment while on Mincron nad loading', () => {
      mocks.props.lineItem = {
        ...lineItemMocks![0],
        lineComments: 'TEST 1234'
      };
      mocks.props.isMincron = true;
      mocks.props.loading = true;
      const { container } = setup(mocks);
      expect(container).toMatchSnapshot();
    });

    it('Should match snapshot with lineNumber', () => {
      mocks.props.lineItem = {
        ...lineItemMocks4![0],
        productId: '123'
      };
      const { container } = setup(mocks);
      expect(container).toMatchSnapshot();
    });
  });
});
