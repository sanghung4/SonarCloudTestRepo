import addItemsToCartCb from 'Order/utils/addItemsToCartCb';
import summaryValuesLogic from 'Order/utils/summaryValues';
import { lineItemMocks, orderMocks, pricingDataMocks } from 'Order/tests/mocks';
import { getUOM } from 'Order/utils/uom';

const t = (data: string) => data;

describe('Orders - utils', () => {
  it('expect summaryValuesLogic to return the correct keys', () => {
    const result = summaryValuesLogic({ t });
    expect(result[0].key).toBe('common.shippedTo');
    expect(result[1].key).toBe('common.shipDate');
    expect(result[2].key).toBe('common.deliveryMethod');
  });

  it('expect summaryValuesLogic to return the correct address', () => {
    const result = summaryValuesLogic({ order: orderMocks[0], t });

    const address =
      Object.values(orderMocks![0]!.shipAddress!)
        .filter((a) => a)
        .join(', ') ?? '-';
    expect(result[0].value).toBe(address);
  });

  it('expect summaryValuesLogic to return "-" when order data is empty', () => {
    const result = summaryValuesLogic({ t });
    expect(result[0].value).toBe('-');
    expect(result[1].value).toBe('-');
    expect(result[2].value).toBe('-');
  });

  it('expect summaryValuesLogic to return "common.delivery" when order data is empty', () => {
    const result = summaryValuesLogic({
      order: { deliveryMethod: 'NOTFOUNDTEST' },
      t
    });
    expect(result[2].value).toBe('common.delivery');
  });

  it('expect functions NOT to be called with addItemsToCartCb when orders is undefined', () => {
    const pushAlert = jest.fn();
    const addItemsToCart = jest.fn();
    addItemsToCartCb({
      pushAlert,
      addItemsToCart,
      pricingData: pricingDataMocks
    });
    expect(pushAlert).not.toBeCalled();
    expect(addItemsToCart).not.toBeCalled();
  });

  it('expect addItemsToCart to be called with addItemsToCartCb with order with no bad item', () => {
    const pushAlert = jest.fn();
    const addItemsToCart = jest.fn();
    addItemsToCartCb({
      pushAlert,
      addItemsToCart,
      order: orderMocks[2],
      pricingData: pricingDataMocks
    });
    expect(pushAlert).not.toBeCalled();
    expect(addItemsToCart).toBeCalled();
  });

  it('expect functions to be called with addItemsToCartCb with order with bad item', () => {
    const pushAlert = jest.fn();
    const addItemsToCart = jest.fn();
    addItemsToCartCb({
      pushAlert,
      addItemsToCart,
      order: orderMocks[3],
      pricingData: []
    });
    expect(pushAlert).toBeCalled();
    expect(addItemsToCart).toBeCalled();
  });

  it('expect getUom to return correct uom value', () => {
    expect(getUOM(true, lineItemMocks[0], t)).toBe('ft');
    expect(getUOM(false, lineItemMocks[0], t)).toBe('product.each');
  });
});
