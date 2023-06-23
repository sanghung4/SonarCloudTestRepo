import { screen } from '@testing-library/react';
import { useSearchParams } from 'react-router-dom';

import {
  CatalogDetailResponse,
  useApiCatalogDetail,
  useApiPostCatalogTitle
} from 'api/catalog.api';
import Catalog from 'pages/Catalog';
import {
  expectToMatchTestIdTextsWhenEmpty,
  expectToMatchTestIdTextsWhenNull,
  expectToMatchTestIdTextsWhenSuccess
} from 'pages/Catalog/tests/assertions';
import mockData from 'pages/Catalog/tests/catalog.mocks';
import { UploadedFile } from 'pages/Catalog/UploadedFileItem';
import UploadModal, { UploadButtonProps } from 'pages/Catalog/UploadModal';
import { expectRowCount, fireEvent, render } from 'test-util';
import { apiMockResponse } from 'api/tests/core.mocks';
import { APIOptions } from 'api/hooks/useApiBase';

/**
 * Types
 */
type Mocks = {
  apiLoading: boolean;
  apiData?: CatalogDetailResponse;
  apiCalled: boolean;
  apiRefetch: jest.Mock;
  setFieldValue: jest.Mock;
  setSearchParam: jest.Mock;
  titleChange: jest.Mock;
  changeValue: jest.Mock;
  uploadFiles: UploadedFile[];
};

/**
 * Mocks
 */
const defaultMocks: Mocks = {
  apiLoading: false,
  apiCalled: false,
  apiRefetch: jest.fn(),
  setFieldValue: jest.fn(),
  setSearchParam: jest.fn(),
  titleChange: jest.fn(),
  changeValue: jest.fn(),
  uploadFiles: []
};
let mocks: Mocks = { ...defaultMocks };

/**
 * Mock Methods
 */
jest.mock('api/catalog.api', () => ({
  ...jest.requireActual('api/catalog.api'),
  useApiCatalogDetail: jest.fn(),
  useApiPostCatalogTitle: jest.fn()
}));
jest.mock('pages/Catalog/UploadModal', () => ({
  ...jest.requireActual('pages/Catalog/UploadModal'),
  __esModule: true,
  default: jest.fn()
}));
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useSearchParams: jest.fn()
}));

/**
 * TEST
 */
describe('pages/Catalog', () => {
  // ⚪ Reset mocks
  afterEach(() => {
    mocks = { ...defaultMocks };
  });

  // 🔵 Mock API functions
  beforeEach(() => {
    // 🔹 useApiCatalogDetail hook
    (useApiCatalogDetail as jest.Mock).mockImplementation(
      (_, __, options: APIOptions<CatalogDetailResponse>) => {
        // arrange data response
        const data = apiMockResponse<CatalogDetailResponse, any>(
          mocks.apiData ?? ({} as CatalogDetailResponse)
        );
        // mock apiRefetch
        mocks.apiRefetch.mockImplementation(() => {
          options.onCompleted?.(data);
        });
        // on mount
        if (!mocks.apiCalled) {
          mocks.apiCalled = true;
          options.onCompleted?.(data);
        }
        // hook output
        return {
          loading: mocks.apiLoading,
          data: mocks.apiData,
          refetch: mocks.apiRefetch
        };
      }
    );
    // 🔹 useSearchParams hook
    (useSearchParams as jest.Mock).mockImplementation(() => [
      [['page', mocks.apiData?.page]],
      mocks.setSearchParam
    ]);
    (useApiPostCatalogTitle as jest.Mock).mockImplementation(() => ({
      call: mocks.titleChange
    }));
    // 🔹 UploadModal component
    (UploadModal as jest.Mock).mockImplementation(
      ({ onChangeValue }: UploadButtonProps) => (
        <button
          onClick={() => onChangeValue?.(mocks.uploadFiles)}
          data-testid="dummy-upload-button"
        />
      )
    );
  });

  // 🟢 1 - Default
  it('Expect to match data by default', () => {
    // act
    render(<Catalog />);
    // assert
    expectToMatchTestIdTextsWhenNull();
  });

  // 🟢 2 - Loading
  it('Expect loader component is rendered when loading', () => {
    // arrange
    mocks.apiLoading = true;
    // act
    render(<Catalog />);
    const loader = screen.queryByTestId('catalog-table-loading');
    // assert
    expect(loader).toBeInTheDocument();
  });

  // 🟢 3 - Data
  it('Expect match data with populated data', async () => {
    // arrange
    mocks.apiData = { ...mockData.success };
    // act
    render(<Catalog />);
    // assert
    expectToMatchTestIdTextsWhenSuccess();
    expectRowCount('catalog-table', mockData.success.results.length);
  });

  // 🟢 4 - Data (1 row)
  it('Expect match data with populated data but 1 row', async () => {
    // arrange
    mocks.apiData = { ...mockData.empty };
    // act
    render(<Catalog />);

    // assert
    expectToMatchTestIdTextsWhenEmpty();
    expectRowCount('catalog-table', mockData.empty.results.length);
  });

  // 🟢 5 - Next Page
  it('Expect functions to be called when next pagination is pressed', () => {
    // arrange
    mocks.apiData = { ...mockData.success };
    // act
    render(<Catalog />);
    fireEvent('click', 'catalog-table-pagination-next');
    // assert
    const page = mockData.success.page + 1;
    const perPage = mockData.success.resultsPerPage;
    expect(mocks.setSearchParam).toBeCalledWith([['page', `${page}`]]);
    expect(mocks.apiRefetch).toBeCalledWith({ undefined, page, perPage }, []);
  });

  // 🟢 6 - Previous Page
  it('Expect functions to be called when previous pagination is pressed', () => {
    // arrange
    mocks.apiData = { ...mockData.success };
    // act
    render(<Catalog />);
    fireEvent('click', 'catalog-table-pagination-previous');
    // assert
    const page = mockData.success.page - 1;
    const perPage = mockData.success.resultsPerPage;
    expect(mocks.setSearchParam).toBeCalledWith([['page', `${page}`]]);
    expect(mocks.apiRefetch).toBeCalledWith({ undefined, page, perPage }, []);
  });

  // 🟢 7 - onChangeValue
  it('Expect refetch is called when onChangeValue is triggered', () => {
    // arrange
    mocks.apiData = { ...mockData.success };
    mocks.uploadFiles = [{ name: 'test' }];
    // act
    render(<Catalog />);
    fireEvent('click', 'dummy-upload-button');
    // assert
    expect(mocks.apiRefetch).toBeCalled();
  });

  // 🟢 7 - onChangeValue
  it('Expect onSubmit to be called when the form is exited by blur', async () => {
    // arrange
    mocks.apiData = { ...mockData.success };
    render(<Catalog />);
    //act
    await fireEvent('click', 'catalog-edit');
    await fireEvent('click', 'catalog-name-input');
    await fireEvent('change', 'catalog-name-input', {
      target: { value: 'New Catalog Name 2' }
    });
    await fireEvent('blur', 'catalog-name-input');
    // assert
    expect(screen.getByText('New Catalog Name 2')).toBeInTheDocument();
  });
});
