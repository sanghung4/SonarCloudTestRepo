import { act, fireEvent } from '@testing-library/react';

import Shipments from 'Cart/Shipments';
import { mockCartContext } from 'Cart/tests/mocks';
import { CartContext } from 'providers/CartProvider';
import { render } from 'test-utils/TestWrapper';

const mockCartContextVal = { ...mockCartContext };

describe('Cart - Shipments', () => {
  it('Should match snapshot when it is closed', async () => {
    const wrapper = render(
      <CartContext.Provider value={mockCartContextVal}>
        <Shipments
          isDisabled={false}
          stockAlertOpen={false}
          shouldShipFullOrder=""
          setShouldShipFullOrder={jest.fn()}
          setStockAlertOpen={jest.fn()}
        />
      </CartContext.Provider>
    );
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(wrapper).toMatchSnapshot();
  });

  it('Should match snapshot with normal data', async () => {
    const wrapper = render(
      <CartContext.Provider value={mockCartContextVal}>
        <Shipments
          isDisabled={false}
          stockAlertOpen={true}
          shouldShipFullOrder="test"
          setShouldShipFullOrder={jest.fn()}
          setStockAlertOpen={jest.fn()}
        />
      </CartContext.Provider>
    );
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(wrapper).toMatchSnapshot();
  });

  it('Expect X to be called when clicking on the close button', async () => {
    const closeFn = jest.fn();
    const wrapper = render(
      <CartContext.Provider value={mockCartContextVal}>
        <Shipments
          isDisabled={false}
          stockAlertOpen={true}
          shouldShipFullOrder="test"
          setShouldShipFullOrder={jest.fn()}
          setStockAlertOpen={closeFn}
        />
      </CartContext.Provider>
    );
    await act(() => new Promise((res) => setTimeout(res, 0)));
    fireEvent.click(wrapper.getByTestId('shipment-close-button'));
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(closeFn).toBeCalledWith(false);
  });
});
