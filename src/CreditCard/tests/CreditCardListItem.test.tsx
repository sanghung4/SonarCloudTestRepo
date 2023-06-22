import CreditCardListItem from 'CreditCard/CreditCardListItem';
import { mockCreditCard } from 'CreditCard/tests/mocks/index.mocks';
import { render } from 'test-utils/TestWrapper';

describe('CreditCard - CreditCardListItem', () => {
  it('expect to match snapshot with minimum props', () => {
    const { container } = render(
      <CreditCardListItem creditCard={mockCreditCard} expired={false} />
    );
    expect(container).toMatchSnapshot();
  });

  it('expect to match snapshot as expired', () => {
    const { container } = render(
      <CreditCardListItem creditCard={mockCreditCard} expired />
    );
    expect(container).toMatchSnapshot();
  });

  it('expect to match snapshot to hide everything', () => {
    const { container } = render(
      <CreditCardListItem
        creditCard={mockCreditCard}
        expired={false}
        hideType
        hideDate
        hideExpired
        noEmphasis
      />
    );
    expect(container).toMatchSnapshot();
  });
});
