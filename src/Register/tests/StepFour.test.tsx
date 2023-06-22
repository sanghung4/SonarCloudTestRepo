import { fireEvent, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { useLocation } from 'react-router-dom';
import { act } from 'react-dom/test-utils';

import StepFour, { UserType } from 'Register/StepFour';
import { render } from 'test-utils/TestWrapper';
import { testIds } from 'test-utils/testIds';

/**
 * Constants
 */
const TEST_IDS = testIds.Register.StepFour;
/**
 * Mocks
 */
const mockState = jest.fn();

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useLocation: () => ({ state: mockState() })
}));

const userInfo: UserType = {
  firstName: 'test',
  lastName: 'test',
  phoneNumber: '0000000000',
  phoneType: 'cell',
  isEmployee: true,
  email: 'test@morsco.com',
  accountNumber: '34568',
  zipCode: '76013',
  accountFound: true,
  tradeAccount: true,
  showAccountMessage: true
};

describe('Register - StepFour', () => {
  it('Should render the page', async () => {
    mockState.mockReturnValue({ isEmployee: true, accountInfo: userInfo });
    const { findByTestId } = render(<StepFour />);

    const page = await findByTestId(TEST_IDS.page);

    expect(page).toBeInTheDocument();
  });

  it('Should show error is contact fields are taken', async () => {
    mockState.mockReturnValue({ isEmployee: true, accountInfo: userInfo });
    const { findByTestId } = render(<StepFour />);
    const firstNameInput = await findByTestId(TEST_IDS.firstName);
    const lastNameInput = await findByTestId(TEST_IDS.lastName);
    const phoneNumberInput = await findByTestId(TEST_IDS.phoneNumber);
    const phoneTypeInput = await findByTestId(TEST_IDS.phoneType);

    await act(async () => {
      fireEvent.change(firstNameInput!, { target: { value: 'testFirst' } });
      fireEvent.change(lastNameInput!, { target: { value: 'testLast' } });
      fireEvent.change(phoneNumberInput!, { target: { value: '2140000000' } });
      const phoneType = await phoneTypeInput.querySelectorAll(
        '.select-input__button'
      )[0];

      fireEvent.click(phoneType);
      // const listItemElements = screen.queryAllByRole('listitem');
      const optionElements = screen.getAllByRole('option');

      // // Assert that the correct number of option elements are present
      expect(optionElements).toHaveLength(3);

      // Simulate a click event on the first Listbox option
      const option1 = screen.getByRole('option', {
        name: 'Home chevron-right.svg'
      });

      userEvent.click(option1);
      const phoneTypeValue = await screen.findByText('Home');
      expect(phoneTypeValue).toBeInTheDocument();
      const submitButton = await findByTestId(TEST_IDS.submitButton);
      fireEvent.click(submitButton);
    });

    expect(firstNameInput).toHaveValue();
    expect(lastNameInput).toHaveValue();
    expect(phoneNumberInput).toHaveValue();
  });
});
