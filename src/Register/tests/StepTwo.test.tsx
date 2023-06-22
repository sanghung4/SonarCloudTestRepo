import { fireEvent } from '@testing-library/react';
import StepTwo from 'Register/StepTwo';
import { act } from 'react-dom/test-utils';
import { render } from 'test-utils/TestWrapper';
import { testIds } from 'test-utils/testIds';

/**
 * Constants
 */
const TEST_IDS = testIds.Register.StepTwo;
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

describe('Register - StepTwo', () => {
  it('Should render employee view if employee account is used', async () => {
    mockState.mockReturnValue({ isEmployee: true });

    const { findByTestId } = render(<StepTwo />);

    const employeeView = await findByTestId(TEST_IDS.employee);

    expect(employeeView).toBeInTheDocument();
  });
  it('Should render form when not an employee account', async () => {
    mockState.mockReturnValue({ isEmployee: false });

    const { findByTestId } = render(<StepTwo />);
    const form = await findByTestId(TEST_IDS.form);

    expect(form).toBeInTheDocument();
  });

  it('Should allow submission if new account is selected', async () => {
    mockState.mockReturnValue({ isEmployee: false });
    const { findByTestId } = render(<StepTwo />);

    await act(async () => {
      const accountTypeRadio = await findByTestId(TEST_IDS.accountRadio);
      const newAccountOption = await accountTypeRadio.querySelectorAll(
        '.register-company__radio-group__option'
      )[0];

      fireEvent.click(newAccountOption);

      const submitButton = await findByTestId(TEST_IDS.submitButton);
      fireEvent.click(submitButton);
    });
  });

  it('Should allow submission of existing account that is not fortiline', async () => {
    mockState.mockReturnValue({ isEmployee: false });
    const { findByTestId } = render(<StepTwo />);

    await act(async () => {
      const accountTypeRadio = await findByTestId(TEST_IDS.accountRadio);
      const existingAccountOption = await accountTypeRadio.querySelectorAll(
        '.register-company__radio-group__option'
      )[1];

      fireEvent.click(existingAccountOption);

      const fortilineRadio = await findByTestId(TEST_IDS.customerRadio);
      const noRadioOption = await fortilineRadio.querySelectorAll(
        '.radio-input__option'
      )[1];
      fireEvent.click(noRadioOption);

      const submitButton = await findByTestId(TEST_IDS.submitButton);
      fireEvent.click(submitButton);
    });

    expect(mockPush).toHaveBeenCalled();
  });

  it('Should display fortiline link', async () => {
    mockState.mockReturnValue({ isEmployee: false });
    const { findByTestId } = render(<StepTwo />);

    const accountTypeRadio = await findByTestId(TEST_IDS.accountRadio);
    const existingAccountOption = await accountTypeRadio.querySelectorAll(
      '.register-company__radio-group__option'
    )[1];

    fireEvent.click(existingAccountOption);

    const fortilineRadio = await findByTestId(TEST_IDS.customerRadio);
    const yesRadioOption = await fortilineRadio.querySelectorAll(
      '.radio-input__option'
    )[0];
    fireEvent.click(yesRadioOption);

    const link = await findByTestId(TEST_IDS.fortiline);

    expect(link).toBeInTheDocument();
  });
});
