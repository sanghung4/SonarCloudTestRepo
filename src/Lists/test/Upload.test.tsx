import { fireEvent, waitFor } from '@testing-library/react';

import userEvent from '@testing-library/user-event';

import { render, renderWithComponent } from 'test-utils/TestWrapper';
import { setBreakpoint } from 'test-utils/mockMediaQuery';

import ListsProvider from 'providers/ListsProvider';
import ListUpload from 'Lists/Upload';
import {
  ACCEPTED_FILES,
  BASE_USE_DROPZONE,
  CSV_FILE,
  FILE_REJECTIONS,
  JSON_FILE,
  mockData
} from './Upload.mocks';
import * as t from 'locales/en/translation.json';
import { useDropzone } from 'react-dropzone';

window.HTMLElement.prototype.scrollIntoView = jest.fn();

jest.mock('react-router-dom', () => ({
  useHistory: () => ({
    push: jest.fn()
  }),
  useLocation: () => ({
    pathname: '/lists/upload'
  })
}));

jest.mock('react-dropzone', () => ({
  useDropzone: jest.fn()
}));

const ACCOUNTS = {
  selectedAccounts: {
    billTo: {
      id: 'testAccount'
    },
    shipTo: {
      id: 'testAccount'
    }
  }
};

describe('List Upload tests', () => {
  beforeAll(() => setBreakpoint('desktop'));
  beforeEach(() => {
    (useDropzone as jest.Mock).mockReturnValue(BASE_USE_DROPZONE);
  });

  it('Should display a link to download the file upload template file', async () => {
    const { container, queryByTestId } = render(
      <ListsProvider>
        <ListUpload />
      </ListsProvider>,
      {
        selectedAccountsConfig: ACCOUNTS
      }
    );

    await waitFor(() => {
      expect(queryByTestId('download-template-link')).toBeInTheDocument();
    });

    expect(container).toMatchSnapshot();
  });

  it('Should add a CSV file through the file picker', async () => {
    const { getByTestId, queryByTestId } = render(
      <ListsProvider>
        <ListUpload />
      </ListsProvider>,
      {
        selectedAccountsConfig: ACCOUNTS
      }
    );

    await waitFor(() => {
      expect(queryByTestId('choose-file-button')).toBeInTheDocument();
    });

    const dropzone = getByTestId('dropzone-root');
    const input = getByTestId('choose-file-input') as HTMLInputElement;

    userEvent.upload(input, CSV_FILE, undefined, { applyAccept: true });

    expect(input.files?.[0]).toStrictEqual(CSV_FILE);
    expect(input.files?.item(0)).toStrictEqual(CSV_FILE);
    expect(input.files).toHaveLength(1);
    expect(dropzone).toMatchSnapshot();
  });

  it('Should add a CSV file with drag and drop', async () => {
    (useDropzone as jest.Mock).mockReturnValue({
      ...BASE_USE_DROPZONE,
      ...ACCEPTED_FILES
    });

    const {
      render: { getByTestId, queryByTestId, queryByText, rerender },
      component
    } = renderWithComponent(
      <ListsProvider>
        <ListUpload />
      </ListsProvider>,
      {
        selectedAccountsConfig: ACCOUNTS
      }
    );
    const data = mockData([CSV_FILE]);

    await waitFor(() => {
      expect(queryByTestId('choose-file-button')).toBeInTheDocument();
    });

    const dropzone = getByTestId('dropzone-root');
    const input = getByTestId('choose-file-input') as HTMLInputElement;

    fireEvent.drop(dropzone, data);
    fireEvent.change(input, {
      target: {
        files: [CSV_FILE]
      }
    });

    await waitFor(() => rerender(component));
    await waitFor(() => {
      expect(
        queryByText(`${t.lists.fileSelected}: ${CSV_FILE.name}`)
      ).toBeInTheDocument();
    });
    expect(dropzone).toMatchSnapshot();
  });

  it('Should not add a file besides a CSV through the file picker', async () => {
    (useDropzone as jest.Mock).mockReturnValue({
      ...BASE_USE_DROPZONE,
      ...FILE_REJECTIONS
    });

    const { getByTestId, queryByTestId, queryByText } = render(
      <ListsProvider>
        <ListUpload />
      </ListsProvider>,
      {
        selectedAccountsConfig: ACCOUNTS
      }
    );

    await waitFor(() => {
      expect(queryByTestId('choose-file-button')).toBeInTheDocument();
    });

    const dropzone = getByTestId('dropzone-root');
    const input = getByTestId('choose-file-input') as HTMLInputElement;

    userEvent.upload(input, JSON_FILE, undefined, { applyAccept: true });

    expect(
      queryByText(`${t.lists.fileSelected}: ${JSON_FILE.name}`)
    ).not.toBeInTheDocument();

    await waitFor(() => {
      expect(queryByTestId('upload-csv-error')).toBeInTheDocument();
    });
    expect(dropzone).toMatchSnapshot();
  });

  it('Should not add a file besides a CSV with drag and drop', async () => {
    (useDropzone as jest.Mock).mockReturnValue({
      ...BASE_USE_DROPZONE,
      ...FILE_REJECTIONS
    });

    const {
      render: { getByTestId, queryByTestId, queryByText, rerender },
      component
    } = renderWithComponent(
      <ListsProvider>
        <ListUpload />
      </ListsProvider>,
      {
        selectedAccountsConfig: ACCOUNTS
      }
    );
    const data = mockData([JSON_FILE]);

    await waitFor(() => {
      expect(queryByTestId('choose-file-button')).toBeInTheDocument();
    });

    const dropzone = getByTestId('dropzone-root');
    const input = getByTestId('choose-file-input') as HTMLInputElement;

    fireEvent.drop(dropzone, data);
    fireEvent.change(input, {
      target: {
        files: [JSON_FILE]
      }
    });

    await waitFor(() => rerender(component));
    expect(
      queryByText(`${t.lists.fileSelected}: ${JSON_FILE.name}`)
    ).not.toBeInTheDocument();
    expect(dropzone).toMatchSnapshot();

    await waitFor(() => {
      expect(queryByTestId('upload-csv-error')).toBeInTheDocument();
    });
    expect(dropzone).toMatchSnapshot();
  });

  it('Should remove an uploaded file', async () => {
    const { getByTestId, queryByTestId } = render(
      <ListsProvider>
        <ListUpload />
      </ListsProvider>,
      {
        selectedAccountsConfig: ACCOUNTS
      }
    );

    await waitFor(() => {
      expect(queryByTestId('choose-file-button')).toBeInTheDocument();
    });

    const dropzone = getByTestId('dropzone-root');
    const input = getByTestId('choose-file-input') as HTMLInputElement;

    userEvent.upload(input, CSV_FILE, undefined, { applyAccept: true });

    const fileNameLabel = getByTestId('file-name-label');
    expect(fileNameLabel).toBeInTheDocument();
    expect(fileNameLabel).toHaveTextContent(CSV_FILE.name);
    expect(input.files?.length).toEqual(1);
    expect(dropzone).toMatchSnapshot('ADDED');

    const removeFileButton = getByTestId('remove-file-button');
    userEvent.click(removeFileButton);
    expect(dropzone).toMatchSnapshot('REMOVED');
  });
});

describe('List Upload tests - Mobile', () => {
  beforeAll(() => setBreakpoint('mobile'));
  beforeEach(() => {
    (useDropzone as jest.Mock).mockReturnValue(BASE_USE_DROPZONE);
  });

  it('Should not allow drag-and-drop', async () => {
    const { getByTestId, queryByTestId, queryByText } = render(
      <ListsProvider>
        <ListUpload />
      </ListsProvider>,
      {
        selectedAccountsConfig: ACCOUNTS
      }
    );

    await waitFor(() => {
      expect(queryByTestId('choose-file-button')).toBeInTheDocument();
    });

    await waitFor(() => {
      expect(queryByText(t.lists.dragDrop)).not.toBeInTheDocument();
    });

    expect(getByTestId('dropzone-root')).toMatchSnapshot();
  });
});
