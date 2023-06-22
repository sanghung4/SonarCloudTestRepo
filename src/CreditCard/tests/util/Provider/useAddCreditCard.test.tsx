import { mockCreditCard } from 'Checkout/tests/mocks/creditCart.mocks';
import {
  mockAddCreditCardMutation,
  mockAddCreditCardMutationWithCreditCard
} from 'CreditCard/tests/mocks/useCreditCardData.mocks';
import {
  addComplete,
  useAddCreditCard
} from 'CreditCard/util/Provider/useAddCreditCard';
import { render } from 'test-utils/TestWrapper';

/**
 * Mock values
 */
const mocks = {
  output: {
    addCreditCardLoading: false,
    addCreditCardMutation: jest.fn(async () => new Promise((res) => res))
  },
  props: {
    setCreditCardData: jest.fn(),
    setUpdatingList: jest.fn()
  }
};

/**
 * Mock methods
 */
jest.mock('generated/graphql', () => ({
  ...jest.requireActual('generated/graphql'),
  useAddCreditCardMutation: () => [
    mocks.output.addCreditCardMutation,
    { loading: mocks.output.addCreditCardLoading }
  ]
}));

/**
 * Mock function
 */
function setup() {
  const output = { ...mocks.output };
  function MockComponent() {
    Object.assign(output, useAddCreditCard(mocks.props));
    return null;
  }
  render(<MockComponent />);
  return output;
}

/**
 * Test
 */
describe('CreditCard - util/Provider/useAddCreditCard', () => {
  afterEach(() => {
    mocks.props = {
      setCreditCardData: jest.fn(),
      setUpdatingList: jest.fn()
    };
  });
  it('Expect `useAddCreditCard` to return data', () => {
    const expected = true;
    mocks.output.addCreditCardLoading = expected;
    const result = setup();
    expect(result.addCreditCardLoading).toBe(expected);
  });

  it('Expect `addComplete` to execute methods with blank data', () => {
    addComplete(mocks.props)({ ...mockAddCreditCardMutation });
    expect(mocks.props.setCreditCardData).toBeCalledWith([]);
    expect(mocks.props.setUpdatingList).toBeCalledWith(false);
  });

  it('Expect `addComplete` to execute methods with data', () => {
    addComplete(mocks.props)({ ...mockAddCreditCardMutationWithCreditCard });
    expect(mocks.props.setCreditCardData).toBeCalledWith([
      { ...mockCreditCard }
    ]);
    expect(mocks.props.setUpdatingList).toBeCalledWith(false);
  });
});
