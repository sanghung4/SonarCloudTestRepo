import { fireEvent, getByTestId, waitFor } from '@testing-library/react';

import Register from 'Register-old';
import CreatePassword from 'Register-old/CreatePassword';
import VerifyEmail from 'Register-old/VerifyEmail';
import { UserInput } from 'generated/graphql';
import { render } from 'test-utils/TestWrapper';
import ContactInformation from './ContactInformation';
import {
  clickButton,
  fillFormTextInput,
  fillMaskedFormTextInput
} from 'test-utils/actionUtils';

const renderContactInformation = () => {
  return render(
    <ContactInformation
      reenterEmail={false}
      userInviteData={undefined}
      onSubmitContact={jest.fn()}
    />
  );
};

describe('Register - Matching snapshots', () => {
  it('"index" should display correctly', () => {
    const { container } = render(<Register />);

    expect(container).toMatchSnapshot();
  });

  it('"email verify" should display correctly', () => {
    const { container } = render(<VerifyEmail />);

    expect(container).toMatchSnapshot();
  });

  it('"create password" should display correctly', () => {
    const { container } = render(
      <CreatePassword onCreatePassword={() => {}} />
    );

    expect(container).toMatchSnapshot();
  });
});

describe('Register - Contact Information', () => {
  it('Verify errors on clicking continue when the form is empty', async () => {
    const { findByText } = renderContactInformation();

    await fillFormTextInput('registration-first-name', '');
    await fillFormTextInput('registration-last-name', '');
    await fillFormTextInput('registration-phone-number', '');
    await fillFormTextInput('registration-email-address', '');

    await clickButton('registration-continue-button');
    const fNameError = await findByText('First Name is required');
    const lNameError = await findByText('Last Name is required');
    const phnNumError = await findByText('Phone Number is required');
    const emailError = await findByText('Email Address is required');

    expect(fNameError).toBeInTheDocument();
    expect(lNameError).toBeInTheDocument();
    expect(phnNumError).toBeInTheDocument();
    expect(emailError).toBeInTheDocument();
  });

  it('Verify first name and last name to accept alphanumeric', async () => {
    const { baseElement } = renderContactInformation();

    await fillFormTextInput('registration-first-name', 'a1b2c3');
    await fillFormTextInput('registration-last-name', 'a1b2c3');

    await clickButton('registration-continue-button');
    const fNameError = baseElement.querySelector(
      '#contact-first-name-helper-text'
    );
    const lNameError = baseElement.querySelector(
      '#contact-last-name-helper-text'
    );

    expect(fNameError?.textContent).not.toBe('First Name is required');
    expect(lNameError?.textContent).not.toBe('Last Name is required');
  });

  it('Verify errors when Phone Number less than 10 digits', async () => {
    const { findByText } = renderContactInformation();

    await fillMaskedFormTextInput('registration-phone-number', '123456789');

    await clickButton('registration-continue-button');
    const phnNumError = await findByText('Invalid Phone Number format');

    expect(phnNumError).toBeInTheDocument();
  });

  it('Verify errors when email has invalid value', async () => {
    const { findByText } = renderContactInformation();

    await fillFormTextInput('registration-email-address', 'test1.com');

    await clickButton('registration-continue-button');
    const emailError = await findByText('Invalid Email Address');

    expect(emailError).toBeInTheDocument();
  });
});

describe('Register - Create  Password', () => {
  const mockUserInput: UserInput = {
    email: 'user.lastname+dev.test@reece.com'
  };

  it('Verify error message when password is blank', async () => {
    const { findByTestId } = render(
      <CreatePassword onCreatePassword={() => {}} userInput={mockUserInput} />
    );

    const passwordInput = (await findByTestId(
      'registration-password-input'
    )) as HTMLInputElement;
    const passwordError = (await findByTestId(
      'password-msg'
    )) as HTMLInputElement;
    fireEvent.focus(passwordInput);
    fireEvent.blur(passwordInput);
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));

    expect(passwordError).toHaveTextContent('Password is required');
  });

  it('Verify error message when password is less than 8 characters', async () => {
    const { findByTestId } = render(
      <CreatePassword onCreatePassword={() => {}} userInput={mockUserInput} />
    );

    const passwordInput = (await findByTestId(
      'registration-password-input'
    )) as HTMLInputElement;
    const passwordError = (await findByTestId(
      'password-msg'
    )) as HTMLInputElement;
    fireEvent.focus(passwordInput);
    fireEvent.change(passwordInput, { target: { value: 'Try123!' } });
    fireEvent.blur(passwordInput);
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));

    expect(passwordError).toHaveTextContent('Invalid Password');
  });

  it('Verify error message when password has no upper case characters', async () => {
    const { findByTestId } = render(
      <CreatePassword onCreatePassword={() => {}} userInput={mockUserInput} />
    );

    const passwordInput = (await findByTestId(
      'registration-password-input'
    )) as HTMLInputElement;
    const passwordError = (await findByTestId(
      'password-msg'
    )) as HTMLInputElement;
    fireEvent.focus(passwordInput);
    fireEvent.change(passwordInput, { target: { value: 'try1234!' } });
    fireEvent.blur(passwordInput);
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));

    expect(passwordError).toHaveTextContent('Invalid Password');
  });

  it('Verify error message when password has only characters and no numbers', async () => {
    const { findByTestId } = render(
      <CreatePassword onCreatePassword={() => {}} userInput={mockUserInput} />
    );

    const passwordInput = (await findByTestId(
      'registration-password-input'
    )) as HTMLInputElement;
    const passwordError = (await findByTestId(
      'password-msg'
    )) as HTMLInputElement;
    fireEvent.focus(passwordInput);
    fireEvent.change(passwordInput, { target: { value: 'Tryabcd!' } });
    fireEvent.blur(passwordInput);
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));

    expect(passwordError).toHaveTextContent('Invalid Password');
  });

  it('Verify no errors when correct and strong password is used', async () => {
    const { findByTestId } = render(
      <CreatePassword onCreatePassword={() => {}} userInput={mockUserInput} />
    );

    const passwordInput = (await findByTestId(
      'registration-password-input'
    )) as HTMLInputElement;
    const passwordError = (await findByTestId(
      'password-msg'
    )) as HTMLInputElement;
    fireEvent.focus(passwordInput);
    fireEvent.change(passwordInput, { target: { value: 'securePassword1!' } });
    fireEvent.blur(passwordInput);
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));

    expect(passwordError).not.toHaveTextContent('Invalid Password');
    expect(passwordError).not.toHaveTextContent('Password is required');
  });

  it('Verify error messages when a password contains Test keyword', async () => {
    const { findByTestId } = render(
      <CreatePassword onCreatePassword={() => {}} userInput={mockUserInput} />
    );

    const passwordInput = (await findByTestId(
      'registration-password-input'
    )) as HTMLInputElement;
    const passwordError = (await findByTestId(
      'password-msg'
    )) as HTMLInputElement;
    fireEvent.focus(passwordInput);
    fireEvent.change(passwordInput, {
      target: { value: 'secureTestPassword390!' }
    });
    fireEvent.blur(passwordInput);
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));

    expect(passwordError).toHaveTextContent('Invalid Password');
  });

  it('Verify no errors when a password contains dev keyword', async () => {
    const { findByTestId } = render(
      <CreatePassword onCreatePassword={() => {}} userInput={mockUserInput} />
    );

    const passwordInput = (await findByTestId(
      'registration-password-input'
    )) as HTMLInputElement;
    const passwordError = (await findByTestId(
      'password-msg'
    )) as HTMLInputElement;
    fireEvent.focus(passwordInput);
    fireEvent.change(passwordInput, {
      target: { value: 'securedevPassword406@' }
    });
    fireEvent.blur(passwordInput);
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));

    expect(passwordError).not.toHaveTextContent('Invalid Password');
  });

  it('Verify error message when terms of sale or privacy policy is not checked', async () => {
    const { findByTestId } = render(
      <CreatePassword onCreatePassword={() => {}} userInput={mockUserInput} />
    );

    await clickButton('continueButton');
    const chkBoxError = (await findByTestId(
      'agree-to-terms-error-msg'
    )) as HTMLInputElement;

    expect(chkBoxError).toHaveTextContent(
      'Must agree to Terms of Sale and Privacy Policy'
    );
  });
});
