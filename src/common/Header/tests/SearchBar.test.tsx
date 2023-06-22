import { FormEvent } from 'react';
import { act, fireEvent } from '@testing-library/react';
import userEvent from '@testing-library/user-event';

import { render } from 'test-utils/TestWrapper';
import SearchBar from '../SearchBar';
import { mockSearchSuggestionQuery } from './mocks';
import { breakpoints, setBreakpoint } from 'test-utils/mockMediaQuery';
import useResizeObserver from 'use-resize-observer';

/******************************/
/* Testing Values             */
/******************************/
const searchInputTestId = 'search-bar-input';
const searchSubmitTestId = 'search-input-submit';
const testInput = 'test';
const searchDrawerBackButton = 'search-drawer-back-button';

/******************************/
/* Mocks                      */
/******************************/
const mockHistory = {
  push: jest.fn()
};
const mockLocation = { pathname: '', search: '' };

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useHistory: () => mockHistory,
  useLocation: () => mockLocation
}));

jest.mock('use-resize-observer', () => ({
  __esModule: true,
  default: jest.fn()
}));

/******************************/
/* Render Setup               */
/******************************/
const setup = () => {
  return render(<SearchBar />, {
    authConfig: { authState: { isAuthenticated: true } },
    mocks: [mockSearchSuggestionQuery]
  });
};

/******************************/
/* Testing                    */
/******************************/
describe('common - Header - SearchBar', () => {
  beforeEach(() => {
    (useResizeObserver as jest.Mock).mockReturnValue({
      ref: null,
      width: breakpoints.desktop
    });

    setBreakpoint('desktop');
  });

  it('Should match snapshot - Desktop', () => {
    const { container } = setup();

    expect(container).toMatchSnapshot();
  });

  it('Should match snapshot - Mobile', () => {
    setBreakpoint('mobile');
    const { container } = setup();

    expect(container).toMatchSnapshot();
  });

  it('Should debounce value on change', async () => {
    const { getByTestId } = setup();

    const searchInput = getByTestId(searchInputTestId);
    const searchSubmitButton = getByTestId(searchSubmitTestId);

    await act(async () => {
      userEvent.type(searchInput, testInput);
    });

    await act(async () => {
      fireEvent.submit(searchSubmitButton);
    });

    expect(mockHistory.push).toBeCalled();
  });

  it('Should close on click away', async () => {
    const { getByTestId, container } = setup();
    const searchInput = getByTestId(searchInputTestId);

    await act(async () => {
      userEvent.type(searchInput, testInput);
    });

    await act(async () => {
      fireEvent.click(document);
    });

    const searchSuggestions = container.querySelector('#orders-range');

    expect(searchSuggestions).toBeFalsy();
  });

  it('expect to match snapshot when submitted search with criteria', async () => {
    mockLocation.pathname = '/search';
    mockLocation.search = '?criteria=pipe';
    const { container } = setup();
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('expect to match snapshot when submitted searched from home page', async () => {
    mockLocation.pathname = '/';
    mockLocation.search = '';
    const { container } = setup();
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });
});
