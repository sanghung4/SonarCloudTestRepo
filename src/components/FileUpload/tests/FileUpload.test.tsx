import { render, screen } from '@testing-library/react';
import FileUpload from 'components/FileUpload/FileUpload';
import { fireEvent } from 'test-util';

/**
 * Types
 */
type Mocks = {
  loading: boolean;
  onSelectingFiles: jest.Mock;
};

/**
 * Mocks
 */
const mockFile = new File(['dummy'], 'dummy.txt');
const defaultMocks: Mocks = {
  loading: false,
  onSelectingFiles: jest.fn()
};
let mocks = { ...defaultMocks };

/**
 * Test setup
 */
function setup(m: Mocks) {
  render(
    <FileUpload
      loading={m.loading}
      onSelectingFiles={m.onSelectingFiles}
      data-testid="test"
    />
  );
}

/**
 * TEST
 */
describe('components/FileUpload', () => {
  // ⚪ Reset mocks
  afterEach(() => {
    mocks = { ...defaultMocks };
  });

  // 🟢 1 - default
  it('Expect component to render text by default', () => {
    // act
    setup(mocks);
    const container = screen.queryByTestId('test-container');
    // assert
    expect(container).toHaveTextContent(
      'Click to browse desktop or drag any file here to upload'
    );
  });

  // 🟢 2 - loading
  it('Expect loading icon to be rendered when loading', () => {
    // arrange
    mocks.loading = true;
    // act
    setup(mocks);
    const container = screen.queryByTestId('test-loading');
    // assert
    expect(container).toBeInTheDocument();
  });

  // 🟢 3 - dragEnter (file)
  it('Expect component to have highlight background class with a dragged file', async () => {
    // arrange
    const dataTransfer = { files: [mockFile] };
    // act
    setup(mocks);
    const container = screen.queryByTestId('test-container');
    await fireEvent('dragEnter', 'test-container', { dataTransfer });
    // assert
    expect(container).toHaveClass('bg-primary-1-30');
  });

  // 🟢 4 - dragEnter (empty)
  it('Expect component NOT include highlight background class with no dragged file', async () => {
    // arrange
    const dataTransfer = { files: [] };
    // act
    setup(mocks);
    const container = screen.queryByTestId('test-container');
    await fireEvent('dragEnter', 'test-container', { dataTransfer });
    // assert
    expect(container).not.toHaveClass('bg-primary-1-30');
  });

  // 🟢 5 - dragOut
  it('Expect component to remove highlight background class with a dragged file', async () => {
    // arrange
    const dataTransfer = { files: [mockFile] };
    // act 1
    setup(mocks);
    const container = screen.queryByTestId('test-container');
    await fireEvent('dragEnter', 'test-container', { dataTransfer });
    await fireEvent('dragOver', 'test-container', { dataTransfer });
    // assert 1
    expect(container).toHaveClass('bg-primary-1-30');
    // act 2
    await fireEvent('dragLeave', 'test-container', { dataTransfer });
    // assert 2
    expect(container).not.toHaveClass('bg-primary-1-30');
  });

  // 🟢 6 - drop file
  it('Expect `onSelectingFiles` is called when dropping a file', async () => {
    // arrange
    const dataTransfer = { files: [mockFile], clearData: jest.fn() };
    // act
    setup(mocks);
    await fireEvent('drop', 'test-container', { dataTransfer });
    // assert
    expect(mocks.onSelectingFiles).toHaveBeenCalled();
  });

  // 🟢 7 - drop file (loading)
  it('Expect not to call `onSelectingFiles` when dropping a file while loading', async () => {
    // arrange
    mocks.loading = true;
    const dataTransfer = { files: [mockFile], clearData: jest.fn() };
    // act
    setup(mocks);
    await fireEvent('drop', 'test-container', { dataTransfer });
    // assert
    expect(mocks.onSelectingFiles).not.toHaveBeenCalled();
  });

  // 🟢 8 - click to upload
  it('Expect to call `onSelectingFiles` when click to select a file', async () => {
    // arrange
    const target = { files: [mockFile] };
    // act
    setup(mocks);
    await fireEvent('click', 'test-container');
    await fireEvent('change', 'test-input', { target });
    // assert
    expect(mocks.onSelectingFiles).toHaveBeenCalled();
  });
});
