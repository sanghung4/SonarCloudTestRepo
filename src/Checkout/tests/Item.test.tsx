import ItemDesktop from 'Checkout/Item.desktop';
import ItemMobile from 'Checkout/Item.mobile';
import { lineItemMocks } from 'Order/tests/mocks';
import { render } from 'test-utils/TestWrapper';

describe('Checkout - Item', () => {
  describe('Item.desktop', () => {
    it('should match snapshot with mostly undefined parameters', () => {
      const { container } = render(
        <ItemDesktop lineItem={{}} qtyAvailable={0} />
      );
      expect(container).toMatchSnapshot();
    });

    it('should match snapshot with valid lineItem', () => {
      const { container } = render(
        <ItemDesktop lineItem={lineItemMocks[3]} qtyAvailable={10} index={3} />
      );
      expect(container).toMatchSnapshot();
    });

    it('should match snapshot when disabled', () => {
      const { container } = render(
        <ItemDesktop lineItem={lineItemMocks[1]} qtyAvailable={1} disabled />
      );
      expect(container).toMatchSnapshot();
    });
  });
  describe('Item.mobile', () => {
    it('should match snapshot with mostly undefined parameters', () => {
      const { container } = render(
        <ItemMobile lineItem={{}} qtyAvailable={0} />
      );
      expect(container).toMatchSnapshot();
    });

    it('should match snapshot with valid lineItem', () => {
      const { container } = render(
        <ItemMobile lineItem={lineItemMocks[3]} qtyAvailable={10} index={3} />
      );
      expect(container).toMatchSnapshot();
    });
  });
});
