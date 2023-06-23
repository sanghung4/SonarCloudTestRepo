import axios from 'axios';

import { apiCall, ApiCallKinds, APICoreProps } from 'api/core';
import { apiMockResponse } from 'api/tests/core.mocks';

/**
 * Types
 */
type MockResponse = {
  kind?: ApiCallKinds;
};

/**
 * Mocks
 */
const MOCK_PROPS: APICoreProps<MockResponse> = {
  url: '/',
  kind: 'get',
  header: {}
};

/**
 * Mock Methods
 */
jest.mock('axios');
const mockedAxios = axios as jest.Mocked<typeof axios>;

/**
 * TEST
 */
describe('API/core', () => {
  // 🟢 1 - Axios.get
  it('Expect apiCall to repond GET request', async () => {
    // arrange
    const kind = 'get';
    const mockRes = apiMockResponse<MockResponse>({ kind });
    mockedAxios.get.mockResolvedValue(mockRes);
    // act
    const res = await apiCall<MockResponse>({ ...MOCK_PROPS, kind });
    // assert
    expect(res?.data.kind).toBe(kind);
  });

  // 🟢 2 - Axios.post
  it('Expect apiCall to repond POST request', async () => {
    // arrange
    const kind = 'post';
    const mockRes = apiMockResponse<MockResponse>({ kind });
    mockedAxios.post.mockResolvedValue(mockRes);
    // act
    const res = await apiCall<MockResponse>({ ...MOCK_PROPS, kind });
    // assert
    expect(res?.data.kind).toBe(kind);
  });

  // 🟢 3 - Axios.delete
  it('Expect apiCall to repond DELETE request', async () => {
    // arrange
    const kind = 'delete';
    const mockRes = apiMockResponse<MockResponse>({ kind });
    mockedAxios.delete.mockResolvedValue(mockRes);
    // act
    const res = await apiCall<MockResponse>({ ...MOCK_PROPS, kind });
    // assert
    expect(res?.data.kind).toBe('delete');
  });
});
