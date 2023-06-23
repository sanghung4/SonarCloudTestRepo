import { AccessToken, AuthState } from '@okta/okta-auth-js';
import { screen } from '@testing-library/react';
import { AxiosError } from 'axios';
import { act } from 'react-dom/test-utils';
import { useNavigate } from 'react-router-dom';
import { Maybe } from 'yup';

import { apiCall, APICoreProps } from 'api/core';
import { APIBaseCall, useApiBase, UseAPIBaseProps } from 'api/hooks/useApiBase';
import { apiMockError, apiMockResponse } from 'api/tests/core.mocks';
import { render } from 'test-util';

/**
 * Types
 */
type MockAPIResponse = {
  res: boolean;
};
type MockHookHandler = {
  call: APIBaseCall<MockAPIResponse>;
};
type Mocks = {
  apiAuth?: boolean;
  apiResponse: Maybe<MockAPIResponse>;
  apiError?: AxiosError;
  apiOnCompleted: jest.Mock;
  apiOnError: jest.Mock;
  apiCoreCall: jest.Mock;
  authState?: AuthState;
  sessionId: Maybe<string>;
  navigate: jest.Mock;
};

/**
 * Mocks
 */
const defaultAPIResponse = { res: true };
const defaultMocks: Mocks = {
  apiResponse: { ...defaultAPIResponse },
  apiOnCompleted: jest.fn(),
  apiOnError: jest.fn(),
  apiCoreCall: jest.fn(),
  sessionId: 'TEST',
  navigate: jest.fn()
};
const defaultAPIProps: APICoreProps = {
  url: '/',
  kind: 'get',
  header: {}
};
let mocks: Mocks = { ...defaultMocks };
let consoleErrorBackup = global.console.error;

/**
 * Mock Methods
 */
jest.mock('api/core', () => ({
  ...jest.requireActual('api/core'),
  apiCall: jest.fn()
}));
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: jest.fn()
}));

/**
 * Test setup function
 */
function setup(m: Mocks) {
  // Props and util
  const props: UseAPIBaseProps<MockAPIResponse> = {
    ...defaultAPIProps,
    options: {
      auth: m.apiAuth,
      onCompleted: m.apiOnCompleted,
      onError: m.apiOnError
    }
  };
  const handler: MockHookHandler = { call: jest.fn() };
  // Dummy component that consumes hook
  const MockConsumer = () => {
    // Hook
    const res = useApiBase<MockAPIResponse>(props);
    // Assign
    Object.assign(handler, { call: res.call });
    // Render
    return (
      <>
        <span data-testid="called">{res.called.toString()}</span>
        <span data-testid="loading">{res.loading.toString()}</span>
      </>
    );
  };
  // Act
  render(<MockConsumer />, {
    authContext: {
      sessionId: m.sessionId,
      setSessionId: jest.fn(),
      authState: m.authState
    }
  });
  // Output
  return handler;
}

/**
 * TEST
 */
describe('api/useApiBase', () => {
  // ⚪ Set mocks
  beforeEach(() => {
    consoleErrorBackup = global.console.error;
    global.console.error = jest.fn();
  });
  // ⚪ Reset mocks
  afterEach(() => {
    global.console.error = consoleErrorBackup;
    mocks = { ...defaultMocks };
  });

  // 🔵 Mock API functions
  beforeEach(() => {
    // 🔹 useNavigate hook
    (useNavigate as jest.Mock).mockImplementation(() => mocks.navigate);
    // 🔹 apiCall function
    (apiCall as jest.Mock).mockImplementation((...params) => {
      mocks.apiCoreCall(...params);
      return new Promise((res, rej) => {
        mocks.apiError
          ? rej(mocks.apiError)
          : res(apiMockResponse(mocks.apiResponse));
      });
    });
  });

  // 🟢 1 - API data
  it('expect onCompleted to be called with valid API data', async () => {
    // act
    const { call } = setup(mocks);
    await act(async () => await call());
    // assert
    const called = screen.queryByTestId('called');
    expect(called).toHaveTextContent('true');
    expect(mocks.apiOnCompleted).toBeCalled();
  });

  // 🟢 2 - Blank API data
  it('expect to output to return blank data', async () => {
    // arrange
    mocks.apiResponse = null;
    // act
    const { call } = setup(mocks);
    await act(async () => await call());
    // assert
    const called = screen.queryByTestId('called');
    expect(called).toHaveTextContent('true');
    expect(mocks.apiOnCompleted).toBeCalled();
  });

  // 🟢 3 - API Data (auth)
  it('expect apiCall core to be called to contain sessionId', async () => {
    // arrange
    mocks.sessionId = 'TEST';
    mocks.apiAuth = true;
    // act
    const { call } = setup(mocks);
    await act(async () => await call());
    // assert
    expect(mocks.apiCoreCall).toBeCalledWith({
      ...defaultAPIProps,
      header: { 'X-Auth-Token': mocks.sessionId }
    });
  });

  // 🟢 4 - API Data (blank auth)
  it('expect apiCall core to be called not contain sessionId when null', async () => {
    // arrange
    mocks.sessionId = null;
    mocks.apiAuth = true;
    // act
    const { call } = setup(mocks);
    await act(async () => await call());
    // assert
    expect(mocks.apiCoreCall).toBeCalledWith(defaultAPIProps);
  });

  // 🟢 5 - Blank Error
  it('expect onError with error', async () => {
    // arrange
    mocks.apiError = new AxiosError();
    // act
    setup(mocks);
    const { call } = setup(mocks);
    await act(async () => await call());
    // assert
    expect(mocks.apiOnError).toBeCalledWith(mocks.apiError);
    expect(mocks.navigate).not.toBeCalled();
  });

  // 🟢 6 - Error 403
  it('expect navigate to be called with 403 error', async () => {
    // arrange
    mocks.apiError = apiMockError('', 403);
    // act
    setup(mocks);
    const { call } = setup(mocks);
    await act(async () => await call());
    // assert
    expect(mocks.apiOnError).toBeCalledWith(mocks.apiError);
    expect(mocks.navigate).toBeCalledWith('/login');
  });

  // 🟢 7 - API Okta Auth
  it('expect apiCall core to be called containined Okta Token', async () => {
    // arrange
    const accessToken = 'test';
    mocks.sessionId = null;
    mocks.apiAuth = true;
    mocks.authState = { accessToken: { accessToken } as AccessToken };
    // act
    const { call } = setup(mocks);
    await act(async () => await call());
    // assert
    expect(mocks.apiCoreCall).toBeCalledWith({
      ...defaultAPIProps,
      header: { Authorization: `Bearer ${accessToken}` }
    });
  });
});
