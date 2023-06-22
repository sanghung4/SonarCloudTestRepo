import { fireEvent } from '@testing-library/react';
import { act } from 'react-dom/test-utils';

import RegisterComplete from 'Register/RegisterComplete';
import { mockResendVerificationEmailError } from 'test-utils/mockResponses';
import { render } from 'test-utils/TestWrapper';
import { testIds } from 'test-utils/testIds';

/**
 * Constants
 */
const TEST_IDS = testIds.SignIn;
/**
 * Mocks
 */
const mockState = jest.fn();
const mocks = {
  pushAlert: jest.fn()
};

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useLocation: () => ({ state: mockState() })
}));

jest.mock('@dialexa/reece-component-library', () => ({
  ...jest.requireActual('@dialexa/reece-component-library'),
  useSnackbar: () => ({ pushAlert: mocks.pushAlert })
}));

describe('Register - RegisterComplete', () => {
  it('Should render the page', async () => {
    mockState.mockReturnValue({ isEmployee: true, userId: '28737727' });
    const { findByTestId } = render(<RegisterComplete />);

    const page = await findByTestId(TEST_IDS.page);

    expect(page).toBeInTheDocument();
  });

  it('Should show error is contact fields are taken', async () => {
    const mockErrorMsg = 'Error sending email';
    mocks.pushAlert = jest.fn();
    mockState.mockReturnValue({ isEmployee: true, userId: '28737727' });
    const { findByTestId } = render(<RegisterComplete />, {
      mocks: [mockResendVerificationEmailError]
    });
    const title = await findByTestId(TEST_IDS.title);
    const text = await findByTestId(TEST_IDS.text);

    await act(async () => {
      const submitButton = await findByTestId(TEST_IDS.signInButton);
      fireEvent.click(submitButton);
    });
    expect(mocks.pushAlert).toBeCalledWith(mockErrorMsg, { variant: 'error' });
    expect(title).toBeInTheDocument();
    expect(text).toBeInTheDocument();
  });
});
