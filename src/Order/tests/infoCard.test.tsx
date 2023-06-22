import InfoCard from 'Order/InfoCard';
import { orderMocks } from 'Order/tests/mocks';
import OrderInfoToolTip from 'Order/utils/OrderInfoToolTip';
import { render } from 'test-utils/TestWrapper';

describe('Order - InfoCard', () => {
  it('Should match snapshot when loading', () => {
    const results = render(<InfoCard loading />);
    expect(results).toMatchSnapshot();
  });

  it('Should match snapshot with no credit card', () => {
    const results = render(<InfoCard loading={false} order={orderMocks[0]} />);
    expect(results).toMatchSnapshot();
  });

  it('Should match snapshot with credit card', () => {
    const results = render(<InfoCard loading={false} order={orderMocks[1]} />);
    expect(results).toMatchSnapshot();
  });

  it('Should match snapshot with undefined Order data', () => {
    const results = render(<InfoCard loading={false} />);
    expect(results).toMatchSnapshot();
  });

  it('Should match snapshot when OrderInfoToolTip is loading', () => {
    const results = render(<OrderInfoToolTip loading key="" />);
    expect(results).toMatchSnapshot();
  });
  it('Should match snapshot when OrderInfoToolTip is empty', () => {
    const results = render(<OrderInfoToolTip key="" />);
    expect(results).toMatchSnapshot();
  });
});
