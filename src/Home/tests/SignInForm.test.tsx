import { act, fireEvent } from '@testing-library/react';

import SignInForm from 'Home/SignInForm';
import { render } from 'test-utils/TestWrapper';

describe('Home - SignInForm', () => {
  it('should match snapshot', () => {
    const { container } = render(<SignInForm />, {
      authConfig: { authState: { isAuthenticated: false } }
    });
    expect(container).toMatchSnapshot();
  });

  it('username/password input and submit test', () => {
    const username = 'user@email.com';
    const password = 'Password1';
    const { getByTestId, container } = render(<SignInForm />, {
      authConfig: { authState: { isAuthenticated: false } }
    });

    const usernameInput = getByTestId('sign-in-form-username');
    const passwordInput = getByTestId('sign-in-form-password');
    act(() => {
      fireEvent.focus(usernameInput);
      fireEvent.change(usernameInput, { target: { value: username } });
      fireEvent.blur(usernameInput);

      fireEvent.focus(passwordInput);
      fireEvent.change(passwordInput, { target: { value: password } });
      fireEvent.blur(passwordInput);

      fireEvent.click(getByTestId('sign-in-form-button'));
    });

    expect(usernameInput).toHaveValue(username);
    expect(passwordInput).toHaveValue(password);
    expect(container).toMatchSnapshot();
  });
});
