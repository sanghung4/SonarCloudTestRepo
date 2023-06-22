import { fireEvent } from '@testing-library/react';
import userEvent from '@testing-library/user-event';

import { act } from 'react-dom/test-utils';
import StepThree from 'Register/StepThree';
import { mockVerifyAccountError } from 'test-utils/mockResponses';
import { testIds } from 'test-utils/testIds';
import { render } from 'test-utils/TestWrapper';

/**
 * Constants
 */
const TEST_IDS = testIds.Register.StepThree;
/**
 * Mocks
 */
const mockState = jest.fn();
const mockPush = jest.fn();

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useLocation: () => ({ state: mockState() }),
  useHistory: () => ({ push: mockPush })
}));

describe('Register - StepThree', () => {
  it('Should render the page', async () => {
    const { findByTestId } = render(<StepThree />);

    const page = await findByTestId(TEST_IDS.page);

    expect(page).toBeInTheDocument();
  });

  it('Should show error if account is not found', async () => {
    const { findByTestId } = render(<StepThree />, {
      mocks: [mockVerifyAccountError]
    });

    await act(async () => {
      const accountNumberField = await findByTestId(
        TEST_IDS.accountNumberInput
      );
      const zipcodeField = await findByTestId(TEST_IDS.zipCodeInput);

      fireEvent.change(accountNumberField!, { target: { value: '35648' } });
      userEvent.type(zipcodeField, '76014');

      const findAccountButton = await findByTestId(TEST_IDS.findAccountButton);
      fireEvent.click(findAccountButton);

      const errorMessage = await findByTestId(TEST_IDS.accountMessage);

      expect(errorMessage).toBeInTheDocument();
    });
  });
});
