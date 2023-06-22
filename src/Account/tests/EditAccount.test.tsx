import { waitFor } from '@testing-library/react';
import { render } from 'test-utils/TestWrapper';
import { clickButton } from 'test-utils/actionUtils';
import EditAccount from 'Account/EditAccount';
import { mockUser, mockBlankUserData } from 'Account/tests/mocks';

const setup = () =>
  render(<EditAccount user={mockUser} onFinished={jest.fn()} />);

describe('Account | Edit Account Tests', () => {
  it("should render the component with the User's information", async () => {
    const { container, getByTestId } = setup();

    await waitFor(() =>
      expect(getByTestId('editaccount-component')).toBeInTheDocument()
    );

    expect(container).toMatchSnapshot();
  });

  it('should show error messages if fields are blank', async () => {
    const { findByText } = render(
      <EditAccount user={mockBlankUserData} onFinished={jest.fn()} />
    );

    await clickButton('submit-button');
    const fstNameError = await findByText('First Name is required');
    const lstNameError = await findByText('Last Name is required');
    const emailError = await findByText('Email Address is required');

    expect(fstNameError).toBeInTheDocument();
    expect(lstNameError).toBeInTheDocument();
    expect(emailError).toBeInTheDocument();
  });

  it('should show error messages for invalid phoneNumber and email data', async () => {
    mockUser.phoneNumber = '123-345';
    mockUser.email = 'psttestqa+standarduser2080';
    const { baseElement } = setup();

    await clickButton('submit-button');
    console.debug(mockUser.phoneNumber.toString());
    const phnNumberError = baseElement.querySelector(
      '#phoneNumber-helper-text'
    );
    const emailError = baseElement.querySelector('#email-helper-text');

    expect(phnNumberError?.textContent).toBe('Invalid Phone Number format');
    expect(emailError?.textContent).toBe(
      'This email address is invalid. Please try again.'
    );
  });
});
