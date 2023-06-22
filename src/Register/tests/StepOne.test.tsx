import { fireEvent } from '@testing-library/react';
import StepOne from 'Register/StepOne';
import { act } from 'react-dom/test-utils';
import { render } from 'test-utils/TestWrapper';
import { mockVerifyUserEmailError } from 'test-utils/mockResponses';
import { testIds } from 'test-utils/testIds';

/**
 * Constants
 */
const TEST_IDS = testIds.Register.StepOne;
/**
 * Mocks
 */
const mockPush = jest.fn();

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useHistory: () => ({ push: mockPush })
}));

describe('Register - StepOne', () => {
  it('Should render the page', async () => {
    const { findByTestId } = render(<StepOne />);

    const page = await findByTestId(TEST_IDS.page);

    expect(page).toBeInTheDocument();
  });

  it('Should show error is email is taken', async () => {
    const { findByTestId } = render(<StepOne />, {
      mocks: [mockVerifyUserEmailError]
    });

    await act(async () => {
      const emailInput = await findByTestId(TEST_IDS.emailInput);
      fireEvent.change(emailInput!, { target: { value: 'example@email.com' } });

      const submitButton = await findByTestId(TEST_IDS.submitButton);
      fireEvent.click(submitButton);
    });

    const errorMessage = await findByTestId(TEST_IDS.alreadyExists);

    expect(errorMessage).toBeInTheDocument();
  });
});
