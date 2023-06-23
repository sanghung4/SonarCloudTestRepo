import { screen } from '@testing-library/react';
import {
  CatalogUploadResponse,
  useApiCatalogUpload,
  useApiDeleteCatalogUpload
} from 'api/catalog.api';
import { apiMockResponse } from 'api/tests/core.mocks';
import UploadModal from 'pages/Catalog/UploadModal';
import uploadMocks from 'pages/Catalog/tests/upload.mocks';
import { fireEvent, render } from 'test-util';
import { convertFileList } from 'util/fileProcessor';

/**
 * Types
 */
type Mocks = {
  apiUploadLoading: boolean;
  apiUploadData?: CatalogUploadResponse;
  apiUploadCall: jest.Mock;
  apiUploadStatus: number;
  apiDeleteLoading: boolean;
  apiDeleteCall: jest.Mock;
  apiDeleteStatus: number;
};

/**
 * Mocks
 */
const mockFile = new File(['dummy'], 'dummy.txt');
const dataTransfer = { files: [mockFile], clearData: jest.fn() };
const defaultMocks: Mocks = {
  apiUploadLoading: false,
  apiUploadCall: jest.fn(),
  apiUploadStatus: 200,
  apiDeleteLoading: false,
  apiDeleteCall: jest.fn(),
  apiDeleteStatus: 200
};
let mocks: Mocks = { ...defaultMocks };

/**
 * Mock Methods
 */
jest.mock('api/catalog.api', () => ({
  ...jest.requireActual('api/catalog.api'),
  useApiCatalogUpload: jest.fn(),
  useApiDeleteCatalogUpload: jest.fn()
}));
jest.mock('util/fileProcessor', () => ({
  ...jest.requireActual('util/fileProcessor'),
  convertFileList: jest.fn()
}));

/**
 * Test setup
 */
function setup(testFn?: jest.Mock) {
  render(
    <UploadModal
      disabled={false}
      customerId={''}
      name={undefined}
      onChangeValue={testFn}
    />
  );
}

/**
 * TEST
 */
describe('pages/Catalog/UploadModal', () => {
  // ⚪ Reset mocks
  afterEach(() => {
    mocks = { ...defaultMocks };
  });

  // 🔵 Mock API functions
  beforeEach(() => {
    // 🔹 useApiCatalogUpload hook
    (useApiCatalogUpload as jest.Mock).mockImplementation(() => ({
      loading: mocks.apiUploadLoading,
      call: mocks.apiUploadCall.mockImplementation(() =>
        apiMockResponse(mocks.apiUploadData, mocks.apiUploadStatus)
      )
    }));
    // 🔹 useApiDeleteCatalogUpload hook
    (useApiDeleteCatalogUpload as jest.Mock).mockImplementation(() => ({
      loading: mocks.apiDeleteLoading,
      call: mocks.apiDeleteCall.mockImplementation(() =>
        apiMockResponse(null, mocks.apiDeleteStatus)
      )
    }));
    // 🔹 convertFileList function
    (convertFileList as jest.Mock).mockImplementation(() => [
      { fileName: mockFile.name, encoded: 'test' }
    ]);
  });

  // 🟢 1 - Default
  it('Expect to render components on default', () => {
    // act
    setup();
    const button = screen.queryByTestId('catalog_upload-button');
    const submit = screen.queryByTestId('catalog_upload-save-button');
    const uploadContainer = screen.queryByTestId(
      'catalog_upload-uploader-container'
    );
    // assert
    expect(button).toBeInTheDocument();
    expect(submit).not.toBeInTheDocument();
    expect(uploadContainer).not.toBeInTheDocument();
  });

  // 🟢 2 - Click to show modal
  it('Expect to render modal components upon clicking the upload button', async () => {
    // act
    setup();
    await fireEvent('click', 'catalog_upload-button');
    const submit = screen.queryByTestId('catalog_upload-save-button');
    const uploadContainer = screen.queryByTestId(
      'catalog_upload-uploader-container'
    );
    // assert
    expect(submit).toBeInTheDocument();
    expect(uploadContainer).toBeInTheDocument();
  });

  // 🟢 3 - Upload file
  it('Expect to upload file with normal response', async () => {
    // arrange
    mocks.apiUploadData = { ...uploadMocks.success };
    // act
    setup();
    await fireEvent('click', 'catalog_upload-button');
    await fireEvent('drop', 'catalog_upload-uploader-container', {
      dataTransfer
    });
    const fileItems = screen.queryAllByTestId('catalog_upload-file-item');
    const errors = screen.queryAllByTestId('catalog_upload-file-item-error');
    // assert
    expect(fileItems.length).toBeTruthy();
    expect(errors.length).toBeFalsy();
  });

  // 🟢 4 - Upload error
  it('Expect to upload fike with error response', async () => {
    // arrange
    mocks.apiUploadData = { ...uploadMocks.error };
    // act
    setup();
    await fireEvent('click', 'catalog_upload-button');
    await fireEvent('drop', 'catalog_upload-uploader-container', {
      dataTransfer
    });
    const [error] = screen.queryAllByTestId('catalog_upload-file-item-error');
    // assert
    expect(error).toHaveTextContent(uploadMocks.error.error!);
  });

  // 🟢 5 - Upload exception
  it('Expect to upload file with network error response', async () => {
    // arrange
    const dataTransfer = { files: [mockFile], clearData: jest.fn() };
    mocks.apiUploadData = { ...uploadMocks.success };
    mocks.apiUploadStatus = 500;
    // act
    setup();
    await fireEvent('click', 'catalog_upload-button');
    await fireEvent('drop', 'catalog_upload-uploader-container', {
      dataTransfer
    });
    const fileItems = screen.queryAllByTestId('catalog_upload-file-item');
    const [error] = screen.queryAllByTestId('catalog_upload-file-item-error');
    // assert
    expect(fileItems.length).toBeTruthy();
    expect(error).toHaveTextContent("There's an issue uploading the file");
  });

  // 🟢 6 - Upload file then delete success
  it('Expect uploaded item count while uploading and then deleting file', async () => {
    // arrange
    mocks.apiUploadData = { ...uploadMocks.success };
    // act 1
    setup();
    await fireEvent('click', 'catalog_upload-button');
    await fireEvent('drop', 'catalog_upload-uploader-container', {
      dataTransfer
    });
    const fileItems = screen.queryAllByTestId('catalog_upload-file-item');
    // assert 1
    expect(fileItems.length).toBeTruthy();
    // act 2
    await fireEvent('click', 'catalog_upload-file-item-delete');
    const fileItemsReload = screen.queryAllByTestId('catalog_upload-file-item');
    // assert 2
    expect(fileItemsReload.length).toBeFalsy();
  });

  // 🟢 7 - Upload file then delete error
  it('Expect uploaded item count while uploading but file deletion returns error', async () => {
    // arrange
    mocks.apiUploadData = { ...uploadMocks.success };
    mocks.apiDeleteStatus = 500;
    // act 1
    setup();
    await fireEvent('click', 'catalog_upload-button');
    await fireEvent('drop', 'catalog_upload-uploader-container', {
      dataTransfer
    });
    const fileItems = screen.queryAllByTestId('catalog_upload-file-item');
    // assert 1
    expect(fileItems.length).toBeTruthy();
    // act 2
    await fireEvent('click', 'catalog_upload-file-item-delete');
    const fileItemsReload = screen.queryAllByTestId('catalog_upload-file-item');
    const [error] = screen.queryAllByTestId('catalog_upload-file-item-error');
    // assert 2
    expect(fileItemsReload.length).toBeTruthy();
    expect(error).toHaveTextContent("There's an issue deleting the file");
  });

  // 🟢 8 - Upload error and delete
  it('Expect uploaded item count while uploading with error and then deleting file (no delete API Call)', async () => {
    // arrange
    mocks.apiUploadData = { ...uploadMocks.error };
    // act 1
    setup();
    await fireEvent('click', 'catalog_upload-button');
    await fireEvent('drop', 'catalog_upload-uploader-container', {
      dataTransfer
    });
    const [error] = screen.queryAllByTestId('catalog_upload-file-item-error');
    // assert 1
    expect(error).toHaveTextContent(uploadMocks.error.error!);
    // act 2
    await fireEvent('click', 'catalog_upload-file-item-delete');
    const fileItemsReload = screen.queryAllByTestId('catalog_upload-file-item');
    // assert 2
    expect(mocks.apiDeleteCall).not.toBeCalled();
    expect(fileItemsReload.length).toBeFalsy();
  });

  // 🟢 9 - Close modal (no files)
  it('Expect closing modal not to call refresh', async () => {
    // arrage
    const refresh = jest.fn();
    // act
    setup(refresh);
    await fireEvent('click', 'catalog_upload-button');
    await fireEvent('click', 'catalog_upload-save-button');
    // assert
    expect(refresh).not.toBeCalled();
  });

  // 🟢 10 - Close modal (error only)
  it('Expect closing modal not to call refresh with only error file upload', async () => {
    // arrage
    const refresh = jest.fn();
    mocks.apiUploadData = { ...uploadMocks.error };
    // act
    setup(refresh);
    await fireEvent('click', 'catalog_upload-button');
    await fireEvent('drop', 'catalog_upload-uploader-container', {
      dataTransfer
    });
    await fireEvent('click', 'catalog_upload-save-button');
    // assert
    expect(refresh).not.toBeCalled();
  });

  // 🟢 10 - Close modal (with proper upload items)
  it('Expect closing modal call refresh with only error file upload', async () => {
    // arrage
    const refresh = jest.fn();
    mocks.apiUploadData = { ...uploadMocks.success };
    // act
    setup(refresh);
    await fireEvent('click', 'catalog_upload-button');
    await fireEvent('drop', 'catalog_upload-uploader-container', {
      dataTransfer
    });
    await fireEvent('click', 'catalog_upload-save-button');
    // assert
    expect(refresh).toBeCalled();
  });
});
