import { CheckoutContext } from 'Checkout/CheckoutProvider';
import { mockCheckoutContext } from 'Checkout/tests/mocks/provider.mocks';
import { mockCreditCard } from 'CreditCard/tests/mocks/index.mocks';
import { mockCreditCardEffectProps } from 'CreditCard/tests/mocks/useCreditCardData.mocks';
import { CreditCardState } from 'CreditCard/util/config';
import useCreditCardEffect from 'CreditCard/util/Provider/useCreditCardEffect';
import { render } from 'test-utils/TestWrapper';
import { mockSelectedAccounts } from 'hooks/tests/mocks/useSelectedAccounts.mocks';

/**
 * Mock values
 */
const mocks = {
  checkout: { ...mockCheckoutContext },
  props: { ...mockCreditCardEffectProps },
  pushAlert: jest.fn(),
  selectedAccounts: { ...mockSelectedAccounts }
};

/**
 * Mock methods
 */
jest.mock('@dialexa/reece-component-library', () => ({
  ...jest.requireActual('@dialexa/reece-component-library'),
  useSnackbar: () => ({ pushAlert: mocks.pushAlert })
}));

/**
 * Mock function
 */
function setup(p: typeof mocks) {
  const output = { parseCCResponse: jest.fn() };
  function MockComponent() {
    Object.assign(output, useCreditCardEffect(p.props));
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
 * Tests
 */
describe('CreditCard - util/Provider/useCreditCardEffect', () => {
  afterEach(() => {
    mocks.checkout = { ...mockCheckoutContext };
    mocks.props = { ...mockCreditCardEffectProps };
    mocks.pushAlert = jest.fn();
    mocks.selectedAccounts = { ...mockSelectedAccounts };
  });

  it('Expect setCreditCard functions not to be called with no credit card', () => {
    setup(mocks);
    expect(mocks.props.setCreditCard).not.toBeCalled();
    expect(mocks.props.setCreditCardState).not.toBeCalled();
  });

  it('Expect setCreditCard functions to be called with valid credit card', () => {
    mocks.checkout.paymentData = { creditCard: { ...mockCreditCard } };
    setup(mocks);
    expect(mocks.props.setCreditCard).toBeCalledWith({ ...mockCreditCard });
    expect(mocks.props.setCreditCardState).toBeCalledWith(
      CreditCardState.SELECTED
    );
  });

  it('Expect global function in `parseCCResponse` to be called', () => {
    mocks.props.resUrl = 'http://test.com';
    setup(mocks);
    expect(mocks.props.setParsingCCResponse).toBeCalledWith(true);
  });

  it('Expect `getCreditCardElementInfo` in `parseCCResponse` to be called', () => {
    mocks.props.resUrl =
      'http://test.com?HostedPaymentStatus=test&ExpressResponseCode=0';
    setup(mocks);
    expect(mocks.props.setGetCreditCardLoading).toBeCalledWith(true);
    expect(mocks.props.getCreditCardElementInfo).toBeCalled();
  });

  it('Expect `pushAlert` in `parseCCResponse` to be called', () => {
    mocks.props.resUrl = 'http://test.com?HostedPaymentStatus=test';
    setup(mocks);
    expect(mocks.props.setResUrl).toBeCalledWith(undefined);
    expect(mocks.props.setCreditCard).toBeCalledWith(undefined);
    expect(mocks.props.setCreditCardState).toBeCalledWith(CreditCardState.ADD);
    expect(mocks.pushAlert).toBeCalled();
  });

  it('Expect `pushAlert` in `parseCCResponse` noit to be called', () => {
    mocks.props.resUrl = 'http://test.com?HostedPaymentStatus=Cancelled';
    setup(mocks);
    expect(mocks.pushAlert).not.toBeCalled();
  });
});
