import { mockCreditCard } from 'CreditCard/tests/mocks/index.mocks';
import useExpiredCreditCardMemo from 'CreditCard/util/Provider/useExpiredCreditCardMemo';
import { ExpiredCreditCardsType } from 'CreditCard/util/types';
import { CreditCard } from 'generated/graphql';
import { render } from 'test-utils/TestWrapper';

/**
 * Mock values
 */
const mocks = {
  props: {
    creditCardData: [] as CreditCard[],
    setUpdatingList: jest.fn()
  }
};

/**
 * Setup function
 */
function setup(p: typeof mocks) {
  const result: ExpiredCreditCardsType = {};
  function MockComponent() {
    Object.assign(result, useExpiredCreditCardMemo(p.props));
    return null;
  }
  render(<MockComponent />);
  return result;
}

/**
 * Test
 */
describe('CreditCard - util/Provider/useExpiredCreditCardMemo', () => {
  it('expect blank data with blank card data', () => {
    const result = setup(mocks);
    expect(Object.keys(result).length).toBe(0);
  });

  it('expect data with card data', () => {
    const cc = { ...mockCreditCard };
    mocks.props.creditCardData = [cc, cc, cc];
    const result = setup(mocks);
    expect(Object.keys(result).length).toBe(1);
  });
});
