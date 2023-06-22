import { waitFor } from '@testing-library/react';
import ProductPage from 'Product';
import { render } from 'test-utils/TestWrapper';
import { trackSearchResult } from 'utils/analytics';
import { success } from './mocks';
import { ListContext, ListContextType } from 'providers/ListsProvider';
import { LIST_PROVIDER_MOCKS } from 'Lists/test/index.mocks';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import * as t from 'locales/en/translation.json';

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useParams: () => ({ id: 'MSC-33860' }),
  useLocation: () => ({
    pathname: '/product/MSC-33860'
  })
}));

jest.mock('hooks/useGeolocation', () => ({
  useGeolocation: () => ({
    position: {
      coords: {
        latitude: 0,
        longitude: 0
      }
    }
  })
}));

jest.mock('utils/analytics', () => ({
  trackSearchResult: jest.fn()
}));

describe('Product Page', () => {
  it('renders the product page correctly', async () => {
    setBreakpoint('desktop');
    const { container } = render(<ProductPage />, {
      mocks: [success],
      selectedAccountsConfig: {
        selectedAccounts: {
          shipTo: {
            id: '123456'
          }
        }
      }
    });

    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    expect(trackSearchResult).toBeCalledTimes(1);
  });
  it('renders the product page correctly in mobile', async () => {
    setBreakpoint('mobile');
    const { container } = render(<ProductPage />, {
      mocks: [success],
      selectedAccountsConfig: {
        selectedAccounts: {
          shipTo: {
            id: '123456'
          }
        }
      }
    });

    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    expect(trackSearchResult).toBeCalledTimes(1);
  });

  it('renders product page with added to list button for selected product', async () => {
    const mockListProvider = {
      ...LIST_PROVIDER_MOCKS.VALID_LIST,
      selectedPartNumber: '33860'
    } as ListContextType;
    const { getByTestId, getByText } = render(
      <ListContext.Provider value={mockListProvider}>
        <ProductPage />
      </ListContext.Provider>,
      {
        mocks: [success],
        selectedAccountsConfig: {
          selectedAccounts: {
            shipTo: {
              id: '123456'
            }
          }
        }
      }
    );

    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    const addToListButton = getByTestId(`add-to-list-button`);

    expect(addToListButton).toBeTruthy();

    expect(getByText(`${t.common.addedToList}`)).toBeTruthy();
  });
});
