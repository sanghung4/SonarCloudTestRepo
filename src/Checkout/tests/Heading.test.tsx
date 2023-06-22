import { fireEvent } from '@testing-library/react';
import Heading, { CheckoutHeadingProps } from 'Checkout/Heading';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

describe('Checkout - Heading', () => {
  it('should match snapshot on desktop', () => {
    setBreakpoint('desktop');
    const { container } = render(<Heading title="test" />);
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot on mobile', () => {
    setBreakpoint('mobile');
    const { container } = render(<Heading title="test" />);
    expect(container).toMatchSnapshot();
  });

  it('expect to call actionCb', () => {
    const props: CheckoutHeadingProps = {
      title: 'test',
      actionText: 'test',
      dataTestId: 'test-button',
      actionCb: jest.fn()
    };
    setBreakpoint('desktop');
    const { getByTestId } = render(<Heading {...props} />);
    fireEvent.click(getByTestId(props.dataTestId!));
    expect(props.actionCb).toBeCalled();
  });
});
