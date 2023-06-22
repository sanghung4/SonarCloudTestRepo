import {
  mockError,
  mockErrorBlank,
  mockErrorInvalid
} from 'Register-old/util/errors.mocks';
import { tryParseError, useOnError } from 'Register-old/util/useOnError';
import { render } from 'test-utils/TestWrapper';

/**
 * Mock values
 */
let mockPushAlert = jest.fn();
let mockDispatch = jest.fn();

/**
 * Mock methods
 */
jest.mock('@dialexa/reece-component-library', () => ({
  ...jest.requireActual('@dialexa/reece-component-library'),
  useSnackbar: () => ({
    pushAlert: mockPushAlert
  })
}));
jest.mock('react-i18next', () => ({
  ...jest.requireActual('react-i18next'),
  t: (t: string) => t
}));

/**
 * Test Setup
 */
function setup() {
  let hookFunctions: ReturnType<typeof useOnError> = {
    onAccountMismatchError: jest.fn(),
    onAccountQueryError: jest.fn(),
    onCreateUserError: jest.fn(),
    onUserApproverError: jest.fn(),
    onUserInviteQueryError: jest.fn()
  };
  function MockComponent() {
    Object.assign(hookFunctions, useOnError(mockDispatch));
    return null;
  }
  render(<MockComponent />);
  return hookFunctions;
}

/**
 * Test
 */
describe('Register - util - useOnError', () => {
  afterEach(() => {
    mockPushAlert = jest.fn();
    mockDispatch = jest.fn();
  });

  describe('useOnError', () => {
    it('expect onAccountMismatchError to call dispatch and pushAlert', () => {
      const { onAccountMismatchError } = setup();
      onAccountMismatchError();
      expect(mockDispatch).toBeCalled();
      expect(mockPushAlert).toBeCalled();
    });

    it('expect onAccountQueryError to call dispatch and pushAlert', () => {
      const { onAccountQueryError } = setup();
      onAccountQueryError();
      expect(mockDispatch).toBeCalled();
      expect(mockPushAlert).toBeCalled();
    });

    it('expect onCreateUserError to call dispatch and pushAlert', () => {
      const { onCreateUserError } = setup();
      onCreateUserError(mockError);
      expect(mockDispatch).toBeCalled();
      expect(mockPushAlert).toBeCalled();
    });

    it('expect onUserApproverError to call only pushAlert', () => {
      const { onUserApproverError } = setup();
      onUserApproverError(mockError);
      expect(mockDispatch).not.toBeCalled();
      expect(mockPushAlert).toBeCalled();
    });

    it('expect onUserInviteQueryError to call only pushAlert', () => {
      const { onUserInviteQueryError } = setup();
      onUserInviteQueryError(mockError);
      expect(mockDispatch).not.toBeCalled();
      expect(mockPushAlert).toBeCalled();
    });
  });

  describe('tryParseError', () => {
    it('expect to parse error normally', () => {
      const props = {
        error: mockError,
        message: 'test',
        pushAlert: mockPushAlert
      };
      tryParseError(props);
      expect(mockPushAlert).toBeCalledWith('test error', { variant: 'error' });
    });

    it('expect to parse error blank', () => {
      const props = {
        error: mockErrorBlank,
        message: 'test',
        pushAlert: mockPushAlert
      };
      const oldConsoleError = console.error;
      console.error = jest.fn();
      tryParseError(props);
      expect(console.error).toBeCalled();
      expect(mockPushAlert).toBeCalledWith(props.message, { variant: 'error' });
      console.error = oldConsoleError;
    });

    it('expect to parse error invalid', () => {
      const props = {
        error: mockErrorInvalid,
        message: 'test',
        pushAlert: mockPushAlert
      };
      const oldConsoleError = console.error;
      console.error = jest.fn();
      tryParseError(props);
      expect(console.error).toBeCalled();
      expect(mockPushAlert).toBeCalledWith(props.message, { variant: 'error' });
      console.error = oldConsoleError;
    });
  });
});
