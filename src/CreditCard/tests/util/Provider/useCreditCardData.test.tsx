import { act } from '@testing-library/react';
import { CheckoutContext } from 'Checkout/CheckoutProvider';
import { mockCheckoutContext } from 'Checkout/tests/mocks/provider.mocks';
import { mockCreditCard } from 'CreditCard/tests/mocks/index.mocks';
import {
  mockCreditCardData,
  mockCreditCardFn
} from 'CreditCard/tests/mocks/useCreditCardData.mocks';
import useCreditCardData from 'CreditCard/util/Provider/useCreditCardData';
import { omit } from 'lodash-es';
import { render } from 'test-utils/TestWrapper';
import { mockSelectedAccounts } from 'hooks/tests/mocks/useSelectedAccounts.mocks';

/**
 * Mock values
 */
const mocks = {
  checkout: { ...mockCheckoutContext },
  deleteMutation: jest.fn(async () => new Promise((res) => res)),
  output: { loading: false, mutation: jest.fn() },
  selectedAccounts: { ...mockSelectedAccounts }
};

/**
 * Mock methods
 */
jest.mock('generated/graphql', () => ({
  ...jest.requireActual('generated/graphql'),
  useCreditCardListQuery: () => ({ loading: mocks.output.loading }),
  useCreditCardSetupUrlLazyQuery: () => [mocks.output.mutation],
  useCreditCardElementInfoLazyQuery: () => [mocks.output.mutation],
  useDeleteCreditCardMutation: () => [
    mocks.deleteMutation,
    { loading: mocks.output.loading }
  ]
}));
jest.mock('CreditCard/util/Provider/useCreditCardFn', () => ({
  ...jest.requireActual('CreditCard/util/Provider/useCreditCardFn'),
  __esModule: true,
  default: () => ({ ...mockCreditCardFn })
}));

/**
 * Mock function
 */
function setup(p: typeof mocks) {
  const output = { ...mockCreditCardData };
  function MockComponent() {
    Object.assign(output, useCreditCardData());
    return null;
  }
  render(
    <CheckoutContext.Provider value={p.checkout}>
      <MockComponent />
    </CheckoutContext.Provider>
  );
  return output;
}

/**
 * Test
 */
describe('CreditCard - util/Provider/useAddCreditCard', () => {
  afterEach(() => {
    mocks.checkout = { ...mockCheckoutContext };
    mocks.deleteMutation = jest.fn(async () => new Promise((res) => res));
    mocks.output = { loading: false, mutation: jest.fn() };
    mocks.selectedAccounts = { ...mockSelectedAccounts };
  });

  it('Expect to match snapshot of the output', () => {
    const result = setup(mocks);
    expect(result).toMatchSnapshot();
  });

  it('Expect `updateCartCreditCard` to call with no credit card', () => {
    const { updateCartCreditCard } = setup(mocks);
    updateCartCreditCard();
    expect(mocks.checkout.setPaymentData).toBeCalledWith({
      ...mocks.checkout.paymentData
    });
  });

  it('Expect `updateCartCreditCard` to call with credit card', () => {
    const { updateCartCreditCard } = setup(mocks);
    updateCartCreditCard(mockCreditCard);
    expect(mocks.checkout.setPaymentData).toBeCalledWith({
      ...mocks.checkout.paymentData,
      creditCard: omit(mockCreditCard, '__typename')
    });
  });

  it('Expect `deleteCreditCard` to be called', async () => {
    const { deleteCreditCard } = setup(mocks);
    await act(async () => await deleteCreditCard('', ''));
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mocks.deleteMutation).toBeCalled();
  });
});
