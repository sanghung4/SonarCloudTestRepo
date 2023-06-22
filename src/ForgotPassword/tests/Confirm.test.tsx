import { fireEvent } from '@testing-library/react';

import ForgotPasswordConfirmation, {
  ForgotPasswordConfirmationProps
} from 'ForgotPassword/Confirm';
import { render } from 'test-utils/TestWrapper';

/**
 * Mock values
 */
const mocks: ForgotPasswordConfirmationProps = {
  email: '',
  onResendClicked: jest.fn()
};

/**
 * Setup test
 */
const setup = (props: ForgotPasswordConfirmationProps) =>
  render(<ForgotPasswordConfirmation {...props} />);

/**
 * TEST
 */
describe('ForgetPassword - Confirm', () => {
  afterEach(() => {
    mocks.email = '';
    mocks.onResendClicked = jest.fn();
  });

  it('Expect to match snapshot', () => {
    mocks.email = 'farmer.macjoy@yahoo.com';
    const { container } = setup(mocks);
    expect(container).toMatchSnapshot();
  });

  it('Expect to call mocked function when the resend-link button is clicked', () => {
    const { getByTestId } = setup(mocks);
    const button = getByTestId('resend-link-button');
    fireEvent.click(button);
    expect(mocks.onResendClicked).toBeCalled();
  });
});
