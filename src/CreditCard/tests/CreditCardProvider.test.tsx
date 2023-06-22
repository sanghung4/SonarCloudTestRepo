import CreditCardProvider, {
  CreditCardContext
} from 'CreditCard/CreditCardProvider';
import { mockCreditCardData } from 'CreditCard/tests/mocks/useCreditCardData.mocks';
import { useContext } from 'react';
import { render } from 'test-utils/TestWrapper';

/**
 * Mock methods
 */
jest.mock('CreditCard/util/Provider/useCreditCardData', () => ({
  ...jest.requireActual('CreditCard/util/Provider/useCreditCardData'),
  __esModule: true,
  default: () => ({ ...mockCreditCardData })
}));
jest.mock('CreditCard/util/Provider/useCreditCardEffect', () => ({
  ...jest.requireActual('CreditCard/util/Provider/useCreditCardEffect'),
  __esModule: true,
  default: () => ({ parseCCResponse: jest.fn() })
}));

/**
 * Setup function
 */
function setup() {
  const result = { ...mockCreditCardData };
  function MockComponent() {
    Object.assign(result, useContext(CreditCardContext));
    return null;
  }
  render(
    <CreditCardProvider>
      <MockComponent />
    </CreditCardProvider>
  );
  return result;
}

/**
 * Test
 */
describe('CreditCard - CreditCardProvider', () => {
  it('expect CreditCard context to return default data', () => {
    const result = setup();
    expect(result).toMatchSnapshot();
  });
});
