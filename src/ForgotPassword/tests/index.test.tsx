import { act, fireEvent } from '@testing-library/react';

import ForgotPassword from 'ForgotPassword';
import { mockAuthParam, mockUseData } from 'ForgotPassword/tests/mocks';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

/**
 * Mock values
 */
const mocks = { authParam: { ...mockAuthParam }, useData: { ...mockUseData } };

/**
 * Mock methods
 */
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useParams: () => mocks.authParam
}));
jest.mock('ForgotPassword/util/useForgetPasswordData', () => ({
  ...jest.requireActual('ForgotPassword/util/useForgetPasswordData'),
  __esModule: true,
  default: () => mocks.useData
}));

/**
 * TESTS
 */
describe('ForgotPassword', () => {
  afterEach(() => {
    mocks.authParam = { ...mockAuthParam };
    mocks.useData = { ...mockUseData };
  });

  it('Expect to match snapshot on desktop with default data', () => {
    setBreakpoint('desktop');
    const { container } = render(<ForgotPassword />);
    expect(container).toMatchSnapshot();
  });
  it('Expect to match snapshot on mobile with default data', () => {
    setBreakpoint('mobile');
    const { container } = render(<ForgotPassword />);
    expect(container).toMatchSnapshot();
  });

  it('Expect to match snapshot on desktop when transaction is not undefined', () => {
    setBreakpoint('desktop');
    mocks.useData.transaction = { data: {}, status: '' };
    const { container } = render(<ForgotPassword />);
    expect(container).toMatchSnapshot();
  });
  it('Expect to match snapshot on mobile when transaction is not undefined', () => {
    setBreakpoint('mobile');
    mocks.useData.transaction = { data: {}, status: '' };
    const { container } = render(<ForgotPassword />);
    expect(container).toMatchSnapshot();
  });

  it('Expect to match snapshot on desktop when requestSent is true', () => {
    setBreakpoint('desktop');
    mocks.useData.requestSent = true;
    const { container } = render(<ForgotPassword />);
    expect(container).toMatchSnapshot();
  });
  it('Expect to match snapshot on mobile when requestSent is true', () => {
    setBreakpoint('mobile');
    mocks.useData.requestSent = true;
    const { container } = render(<ForgotPassword />);
    expect(container).toMatchSnapshot();
  });

  it('Expect to match snapshot on desktop with email error', async () => {
    setBreakpoint('desktop');
    const { container, getByTestId } = render(<ForgotPassword />);
    fireEvent.focus(getByTestId('email-address-input'));
    fireEvent.blur(getByTestId('email-address-input'));
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('Remove param when autoToken is invalid, then expect the submit button for sending email is rendered', () => {
    setBreakpoint('desktop');
    const { container, getByTestId } = render(<ForgotPassword />);
    const submitButton = getByTestId('forgot-password-submit-button');
    expect(container).toContainElement(submitButton);
  });
});
