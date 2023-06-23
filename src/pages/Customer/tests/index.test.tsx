import { screen } from '@testing-library/react';
import { useNavigate } from 'react-router-dom';

import { Customer, useApiCustomerDetail } from 'api/customer.api';
import CustomerComponent from 'pages/Customer';
import mockData from 'pages/Customer/tests/customer.mocks';
import {
  expectToMatchTestIdTextsWhenNull,
  expectToMatchTestIdTextsWhenTruthy
} from 'pages/Customer/tests/assertions';
import { fireEvent, render } from 'test-util';

/**
 * Types
 */
type Mocks = {
  apiLoading: boolean;
  navigate: jest.Mock;
  apiData?: Customer;
};

/**
 * Mocks
 */
const defaultMocks: Mocks = {
  apiLoading: false,
  navigate: jest.fn()
};
let mocks: Mocks = { ...defaultMocks };

/**
 * Mock Methods
 */
jest.mock('api/customer.api', () => ({
  ...jest.requireActual('api/customer.api'),
  useApiCustomerDetail: jest.fn()
}));
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: jest.fn()
}));

/**
 * TEST
 */
describe('pages/Customer', () => {
  // ⚪ Reset mocks
  afterEach(() => {
    mocks = { ...defaultMocks };
  });

  // 🔵 Mock API functions
  beforeEach(() => {
    // 🔹 useApiCustomerDetail hook
    (useApiCustomerDetail as jest.Mock).mockImplementation(() => ({
      loading: mocks.apiLoading,
      data: mocks.apiData
    }));
    // 🔹 useNavigate hook
    (useNavigate as jest.Mock).mockImplementation(() => mocks.navigate);
  });

  // 🟢 1 - Rendered as loading
  it('Expect to render loading circle when API is loading', () => {
    // arrange
    mocks.apiLoading = true;
    // act
    render(<CustomerComponent />);
    const loading = screen.queryByTestId('customer_loading');
    // assert
    expect(loading).toBeInTheDocument();
  });

  // 🟢 2 - Rendered as null-ish
  it('Expect to match text contents when API has a response but with undefined data', () => {
    // act
    render(<CustomerComponent />);
    // assert
    expectToMatchTestIdTextsWhenNull();
  });

  // 🟢 3 - Rendered as truthy
  it('Expect to match text context when API has a response but with undefined data', () => {
    // arrange
    mocks.apiData = { ...mockData.customer };
    // act
    render(<CustomerComponent />);
    // assert
    expectToMatchTestIdTextsWhenTruthy();
  });

  // 🟢 4 - Click "View Catalog" button
  it('Expect `navigate` to be called upon clicking the "View Catalog" button', async () => {
    // arrange
    mocks.apiData = { ...mockData.customer };
    const catalogId = mockData.customer.catalogs[0].id;
    // act
    render(<CustomerComponent />);
    await fireEvent('click', 'customer_detail-view-button');
    // assert
    expect(mocks.navigate).toBeCalledWith(`/catalog/detail/${catalogId}`);
  });
});
