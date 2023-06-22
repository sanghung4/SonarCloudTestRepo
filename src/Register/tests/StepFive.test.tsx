import { fireEvent } from '@testing-library/react';
import { act } from 'react-dom/test-utils';

import StepFive from 'Register/StepFive';
import {
  mockCreateNewEmployeeError,
  mockCreateNewUserError
} from 'test-utils/mockResponses';
import { testIds } from 'test-utils/testIds';
import { render } from 'test-utils/TestWrapper';

/**
 * Constants
 */
const TEST_IDS = testIds.Register.StepFive;
/**
 * Mocks
 */
const mockState = jest.fn();
const mockHistory = { push: jest.fn() };
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useLocation: () => ({ state: mockState() }),
  useHistory: () => mockHistory
}));

const createUserInput = {
  firstName: 'test',
  lastName: 'test',
  phoneNumber: '0000000000',
  phoneType: 'CELL',
  isEmployee: true,
  email: 'test@morsco.com',
  accountNumber: '34568',
  zipCode: '76013',
  accountFound: true,
  tradeAccount: true,
  showAccountMessage: true,
  tosAccepted: true,
  ppAccepted: true,
  brand: 'Reece'
};

describe('Register - StepFive', () => {
  it('Should render the page', async () => {
    mockState.mockReturnValue({
      isEmployee: true,
      contactInfo: createUserInput
    });
    const { findByTestId } = render(<StepFive />);

    const page = await findByTestId(TEST_IDS.page);

    expect(page).toBeInTheDocument();
  });

  it('Should render error message if password is empty with a value (User)', async () => {
    mockState.mockReturnValue({
      isEmployee: false,
      contactInfo: createUserInput
    });
    const { findByTestId } = render(<StepFive />, {
      mocks: [mockCreateNewEmployeeError]
    });
    const passwordInput = await findByTestId(TEST_IDS.passwordInput);
    const checkboxOne = await findByTestId(TEST_IDS.checkPrivacyPolicy);
    const checkboxTwo = await findByTestId(TEST_IDS.checkboxTermsOfSale);

    await act(async () => {
      fireEvent.change(passwordInput!, { target: { value: '' } });
      fireEvent.change(checkboxOne, { target: { value: false } });
      fireEvent.change(checkboxTwo, { target: { value: true } });
      const submitButton = await findByTestId(TEST_IDS.submitButton);
      fireEvent.click(submitButton);
    });

    expect(passwordInput).toBeInTheDocument();
    expect(passwordInput).toHaveValue('');
  });

  it('Should render error message if password is empty with a value (Employee)', async () => {
    mockState.mockReturnValue({
      isEmployee: true,
      contactInfo: createUserInput
    });
    const { findByTestId } = render(<StepFive />, {
      mocks: [mockCreateNewUserError]
    });
    const passwordInput = await findByTestId(TEST_IDS.passwordInput);
    const checkboxOne = await findByTestId(TEST_IDS.checkPrivacyPolicy);
    const checkboxTwo = await findByTestId(TEST_IDS.checkboxTermsOfSale);

    await act(async () => {
      fireEvent.change(passwordInput!, { target: { value: '' } });
      fireEvent.change(checkboxOne, { target: { value: false } });
      fireEvent.change(checkboxTwo, { target: { value: true } });
      const submitButton = await findByTestId(TEST_IDS.submitButton);
      fireEvent.click(submitButton);
    });

    expect(passwordInput).toBeInTheDocument();
    expect(passwordInput).toHaveValue('');
  });

  it('should render an error message if password input is not at least 8 character', async () => {
    mockState.mockReturnValue({
      isEmployee: true,
      contactInfo: createUserInput
    });
    const { findByTestId } = render(<StepFive />);
    const passwordInput = await findByTestId(TEST_IDS.passwordInput);
    const eightCharacter = await findByTestId(TEST_IDS.containEightCharacters);

    await act(async () => {
      fireEvent.change(passwordInput!, { target: { value: 'Password' } });
      const submitButton = await findByTestId(TEST_IDS.submitButton);
      fireEvent.click(submitButton);
    });

    expect(eightCharacter).toBeInTheDocument();
  });

  it('should render an error message if password input doesnt contain one uppercase letter', async () => {
    mockState.mockReturnValue({
      isEmployee: true,
      contactInfo: createUserInput
    });
    const { findByTestId } = render(<StepFive />);
    const passwordInput = await findByTestId(TEST_IDS.passwordInput);
    const uppercaseLetter = await findByTestId(
      TEST_IDS.containOneUppercaseLetter
    );

    await act(async () => {
      fireEvent.change(passwordInput!, { target: { value: 'password1' } });
      const submitButton = await findByTestId(TEST_IDS.submitButton);
      fireEvent.click(submitButton);
    });

    expect(uppercaseLetter).toBeInTheDocument();
  });

  it('should render an error message if password input doesnt contain at least one number', async () => {
    mockState.mockReturnValue({
      isEmployee: true,
      contactInfo: createUserInput
    });
    const { findByTestId } = render(<StepFive />);
    const passwordInput = await findByTestId(TEST_IDS.passwordInput);
    const oneNumber = await findByTestId(TEST_IDS.containOneNumber);

    await act(async () => {
      fireEvent.change(passwordInput!, { target: { value: 'Password' } });
      const submitButton = await findByTestId(TEST_IDS.submitButton);
      fireEvent.click(submitButton);
    });

    expect(oneNumber).toBeInTheDocument();
  });

  it('should navigate to privacy policy', async () => {
    const mockOpen = jest.fn();
    window.open = mockOpen;
    mockState.mockReturnValue({
      isEmployee: true,
      contactInfo: createUserInput
    });
    const { findByTestId } = render(<StepFive />);

    await act(async () => {
      const privacyPolicyLink = await findByTestId(TEST_IDS.privacyPolicyLink);
      fireEvent.click(privacyPolicyLink);
    });

    expect(window.open).toHaveBeenCalledWith('/privacy-policy');
  });

  it('should navigate to terms of sale', async () => {
    const mockOpen = jest.fn();
    window.open = mockOpen;
    mockState.mockReturnValue({
      isEmployee: true,
      contactInfo: createUserInput
    });
    const { findByTestId } = render(<StepFive />);

    await act(async () => {
      const termsOfSaleLink = await findByTestId(TEST_IDS.termsOfSaleLink);
      fireEvent.click(termsOfSaleLink);
    });

    expect(window.open).toHaveBeenCalledWith('/terms-of-sale');
  });

  it('should navigate to previous page for desktop', async () => {
    mockState.mockReturnValue({
      isEmployee: true,
      contactInfo: createUserInput
    });
    const { findByTestId } = render(<StepFive />);

    await act(async () => {
      const previouButton = await findByTestId(TEST_IDS.previousButtonDesktop);
      fireEvent.click(previouButton);
    });

    expect(mockHistory.push).toHaveBeenCalledWith('/register/step-4', {
      contactInfo: createUserInput
    });
  });

  it('should navigate to previous page for mobile', async () => {
    mockState.mockReturnValue({
      isEmployee: true,
      contactInfo: createUserInput
    });
    const { findByTestId } = render(<StepFive />);

    await act(async () => {
      const previouButton = await findByTestId(TEST_IDS.previousButtonMobile);
      fireEvent.click(previouButton);
    });

    expect(mockHistory.push).toHaveBeenCalledWith('/register/step-4', {
      contactInfo: createUserInput
    });
  });

  it('should render an error message if only one check box is checked and user meets password requirements', async () => {
    mockState.mockReturnValue({
      isEmployee: true,
      contactInfo: createUserInput
    });
    const { findByTestId } = render(<StepFive />);
    const passwordInput = await findByTestId(TEST_IDS.passwordInput);
    const checkboxOne = await findByTestId(TEST_IDS.checkPrivacyPolicy);
    const checkboxTwo = await findByTestId(TEST_IDS.checkboxTermsOfSale);

    await act(async () => {
      fireEvent.change(passwordInput!, { target: { value: 'Password1' } });
      fireEvent.change(checkboxOne, { target: { value: true } });
      fireEvent.change(checkboxTwo, { target: { value: false } });
      const submitButton = await findByTestId(TEST_IDS.submitButton);
      fireEvent.click(submitButton);
    });

    const errorMessage = await findByTestId(TEST_IDS.errorMessage);
    expect(errorMessage).toBeInTheDocument();
  });

  it('should not render an error message after both the checboxes are selected', async () => {
    mockState.mockReturnValue({
      isEmployee: true,
      contactInfo: createUserInput
    });
    const { findByTestId, queryByTestId } = render(<StepFive />);
    const passwordInput = await findByTestId(TEST_IDS.passwordInput);
    const checkboxOne = await findByTestId(TEST_IDS.checkPrivacyPolicy);
    const checkboxTwo = await findByTestId(TEST_IDS.checkboxTermsOfSale);

    await act(async () => {
      fireEvent.change(passwordInput!, { target: { value: 'Password1' } });

      const submitButton = await findByTestId(TEST_IDS.submitButton);
      fireEvent.click(submitButton);

      const errorMessage = await findByTestId(TEST_IDS.errorMessage);
      expect(errorMessage).toBeInTheDocument();

      checkboxOne.click();
      checkboxTwo.click();

      const errorMsg = await queryByTestId(TEST_IDS.errorMessage);
      expect(errorMsg).not.toBeInTheDocument();
    });
  });
});
