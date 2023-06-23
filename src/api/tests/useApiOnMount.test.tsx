import { screen } from '@testing-library/react';
import { AxiosError } from 'axios';
import { act } from 'react-dom/test-utils';
import { useNavigate } from 'react-router-dom';
import { Maybe } from 'yup';

import { apiCall } from 'api/core';
import {
  APIOnMountRefetch,
  useApiOnMount,
  UseAPIOnMountProps
} from 'api/hooks/useApiOnMount';
import { apiMockResponse } from 'api/tests/core.mocks';
import { render } from 'test-util';

/**
 * Types
 */
type MockAPIResponse = {
  res: boolean;
};
type MockHookHandler = {
  refetch: APIOnMountRefetch<MockAPIResponse>;
};
type Mocks = {
  apiAuth?: boolean;
  apiResponse: Maybe<MockAPIResponse>;
  apiError?: AxiosError;
  apiOnCompleted: jest.Mock;
  apiOnError: jest.Mock;
  apiCoreCall: jest.Mock;
  sessionId: Maybe<string>;
  skip: boolean;
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
  skip: false,
  navigate: jest.fn()
};
const defaultAPIProps: UseAPIOnMountProps<MockAPIResponse> = {
  url: '/',
  kind: 'get',
  header: {},
  options: {}
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
  const props: UseAPIOnMountProps<MockAPIResponse> = {
    ...defaultAPIProps,
    skip: m.skip,
    options: {
      auth: m.apiAuth,
      onCompleted: m.apiOnCompleted,
      onError: m.apiOnError
    }
  };
  const handler: MockHookHandler = { refetch: jest.fn() };
  // Dummy component that consumes hook
  const MockConsumer = () => {
    // Hook
    const res = useApiOnMount<MockAPIResponse>(props);
    // Assign
    Object.assign(handler, { refetch: res.refetch });
    // Render
    return (
      <>
        <span data-testid="called">{res.called.toString()}</span>
        <span data-testid="loading">{res.loading.toString()}</span>
        <span data-testid="has-data">{Boolean(res.data).toString()}</span>
      </>
    );
  };
  // Act
  render(<MockConsumer />, {
    authContext: { sessionId: m.sessionId, setSessionId: jest.fn() }
  });
  // Output
  return handler;
}

/**
 * TEST
 */
describe('api/useApiOnMount', () => {
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

  // 🟢 1 - Skip
  it('expect falsey data if skip is true', () => {
    // arrange
    mocks.skip = true;
    // act
    setup(mocks);
    const called = screen.queryByTestId('called');
    const hasData = screen.queryByTestId('has-data');
    // assert
    expect(called).toHaveTextContent('false');
    expect(hasData).toHaveTextContent('false');
  });

  // 🟢 2 - Return truthy
  it('expect truthy data', async () => {
    // arrange
    mocks.apiResponse = { ...defaultAPIResponse };
    // act
    setup(mocks);
    await act(async () => await new Promise((res) => setTimeout(res, 0)));
    // assert
    const called = screen.queryByTestId('called');
    const hasData = screen.queryByTestId('has-data');
    expect(called).toHaveTextContent('true');
    expect(hasData).toHaveTextContent('true');
  });

  // 🟢 3 - Skip with auth
  it('expect to falsey data with auth when sessionId is null', async () => {
    // arrange
    mocks.sessionId = null;
    mocks.apiAuth = true;
    // act
    setup(mocks);
    await act(async () => await new Promise((res) => setTimeout(res, 0)));
    // assert
    const called = screen.queryByTestId('called');
    const hasData = screen.queryByTestId('has-data');
    expect(called).toHaveTextContent('false');
    expect(hasData).toHaveTextContent('false');
  });
  // 🟢 4 - Return truthy with auth
  it('expect to truthy data with auth and sessionId', async () => {
    // arrange
    mocks.apiAuth = true;
    // act
    setup(mocks);
    await act(async () => await new Promise((res) => setTimeout(res, 0)));
    // assert
    const called = screen.queryByTestId('called');
    const hasData = screen.queryByTestId('has-data');
    expect(called).toHaveTextContent('true');
    expect(hasData).toHaveTextContent('true');
  });

  // 🟢 5 - Return null
  it('expect to falsey data with null response', async () => {
    // arrange
    mocks.apiResponse = null;
    // act
    setup(mocks);
    await act(async () => await new Promise((res) => setTimeout(res, 0)));
    // assert
    const called = screen.queryByTestId('called');
    const hasData = screen.queryByTestId('has-data');
    expect(called).toHaveTextContent('true');
    expect(hasData).toHaveTextContent('false');
  });

  // 🟢 5 - refetch
  it('expect to refetch to populated data', async () => {
    // arrange
    mocks.apiResponse = undefined;
    // act 1
    const { refetch } = setup(mocks);
    await act(async () => await new Promise((res) => setTimeout(res, 0)));
    // assert 1
    const called = screen.queryByTestId('called');
    const hasData = screen.queryByTestId('has-data');
    expect(called).toHaveTextContent('true');
    expect(hasData).toHaveTextContent('false');
    // act 2
    mocks.apiResponse = { ...defaultAPIResponse };
    await act(async () => refetch());
    expect(hasData).toHaveTextContent('true');
  });
});
