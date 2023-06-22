import { ReactNode } from 'react';

import { fireEvent, waitFor, within } from '@testing-library/react';

import LocationSearch from 'LocationSearch';
import { mockGoogle, mockGeolocation } from 'test-utils/mockGlobals';
import { render } from 'test-utils/TestWrapper';
import mocks from 'LocationSearch/mocks';
import { setBreakpoint } from 'test-utils/mockMediaQuery';

type EmptyCompProps = {
  children: ReactNode;
};

const googleLoadingMock = { isLoaded: true };
jest.mock('@react-google-maps/api', () => ({
  useLoadScript: () => googleLoadingMock,
  Autocomplete: ({ children }: EmptyCompProps) => <div>{children}</div>,
  GoogleMap: ({ children }: EmptyCompProps) => <div>{children}</div>,
  Marker: () => <div />
}));

describe('Branch Search tests', () => {
  beforeEach(() => {
    mockGoogle();
    mockGeolocation(32.972, -96.7914);
  });

  it('Should show loading and load correctly', async () => {
    const { container } = render(<LocationSearch />, { mocks });

    // Empty state
    expect(container).toMatchSnapshot();

    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    expect(container).toMatchSnapshot();
  });

  it('Should test the clear-button', async () => {
    const { findByTestId, findByPlaceholderText } = render(<LocationSearch />, {
      mocks
    });
    const clearButton = await findByTestId('clear-button');
    const input = await findByPlaceholderText('Find your branch');
    fireEvent.click(clearButton);
    expect(input).toHaveFocus();
  });

  it('Should filter by type', async () => {
    const { container, getAllByRole, getByRole, getAllByText, getByTestId } =
      render(<LocationSearch />, {
        mocks
      });

    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    getAllByText('13 results');

    fireEvent.mouseDown(getAllByRole('button')[1]);
    const listbox = within(getByRole('listbox'));
    fireEvent.click(listbox.getByText('Plumbing'));

    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    getAllByText('11 results');
    expect(container).toMatchSnapshot();

    const distaneRadius50miles = getByTestId(`distance-radius-50miles`);
    fireEvent.click(distaneRadius50miles);
    const branchName = getByTestId(`branch-name-1`);
    expect(branchName).toBeTruthy();
  });

  it('Should test the clear-button in mobile', async () => {
    setBreakpoint('mobile');
    const { findByTestId, findByPlaceholderText } = render(<LocationSearch />, {
      mocks
    });
    const clearButton = await findByTestId('clear-button');
    const input = await findByPlaceholderText('Find your branch');
    fireEvent.click(clearButton);
    expect(input).toHaveFocus();
  });

  it('Should test the map-button in mobile', async () => {
    setBreakpoint('mobile');
    const { findByTestId } = render(<LocationSearch />, {
      mocks
    });
    const mapButton = await findByTestId('map-button');
    const resultsCount = await findByTestId('record-count-footer');
    expect(mapButton).toHaveTextContent('Map');
    expect(resultsCount).toHaveTextContent('13 results');
    fireEvent.click(mapButton);
    expect(mapButton).toHaveTextContent('List');
  });

  it('Should not show the location search when google not loaded', async () => {
    setBreakpoint('desktop');
    googleLoadingMock.isLoaded = false;

    const { queryByTestId } = render(<LocationSearch />, {
      mocks
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    const distaneRadius50miles = queryByTestId(`distance-radius-50miles`);
    expect(distaneRadius50miles).not.toBeInTheDocument();
  });
});
