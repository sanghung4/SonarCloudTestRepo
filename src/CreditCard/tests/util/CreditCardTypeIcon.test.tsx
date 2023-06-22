import { mockCreditCard } from 'CreditCard/tests/mocks/index.mocks';
import CreditCardTypeIcon from 'CreditCard/util/CreditCardTypeIcon';
import { render } from 'test-utils/TestWrapper';

/**
 * Mock values
 */
const mocks = {
  props: {
    creditCard: { ...mockCreditCard },
    expired: false
  }
};

/**
 * Setup function
 */
function setup(p: typeof mocks) {
  return render(<CreditCardTypeIcon {...p.props} />);
}

/**
 * Test
 */
describe('CreditCard - util/CreditCardTypeIcon', () => {
  it('Expect to render Amex card', () => {
    mocks.props.creditCard = {
      ...mockCreditCard,
      creditCardType: 'Amex'
    };
    const { container } = setup(mocks);
    expect(container).toMatchSnapshot();
  });

  it('Expect to render Discover card', () => {
    mocks.props.creditCard = {
      ...mockCreditCard,
      creditCardType: 'Discover'
    };
    const { container } = setup(mocks);
    expect(container).toMatchSnapshot();
  });

  it('Expect to render Mastercard card', () => {
    mocks.props.creditCard = {
      ...mockCreditCard,
      creditCardType: 'Mastercard'
    };
    const { container } = setup(mocks);
    expect(container).toMatchSnapshot();
  });

  it('Expect to render Visa card', () => {
    mocks.props.creditCard = {
      ...mockCreditCard,
      creditCardType: 'Visa'
    };
    const { container } = setup(mocks);
    expect(container).toMatchSnapshot();
  });

  it('Expect to render Generic card', () => {
    mocks.props.creditCard = {
      ...mockCreditCard,
      creditCardType: 'Generic'
    };
    const { container } = setup(mocks);
    expect(container).toMatchSnapshot();
  });

  it('Expect to render expired card', () => {
    mocks.props.expired = true;
    const { container } = setup(mocks);
    expect(container).toMatchSnapshot();
  });
});
