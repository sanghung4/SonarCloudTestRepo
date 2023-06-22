import userEvent from '@testing-library/user-event';

import { authConfig } from 'AuthProvider.mocks';
import Home from 'Home';
import CompanyListHeader from 'common/Header/CompanyList';
import { render } from 'test-utils/TestWrapper';
import { setBreakpoint } from 'test-utils/mockMediaQuery';

const mockHistory = { push: jest.fn() };
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useHistory: () => mockHistory
}));

describe('Verify Your Email', () => {
  it('redirects to "Verify Your Email" for unverified employees', async () => {
    setBreakpoint('desktop');
    const { findByTestId } = render(
      <>
        <CompanyListHeader />
        <Home />
      </>,
      { authConfig }
    );

    const verifyEmail = await findByTestId('verifyemail-component');
    expect(verifyEmail).toBeInTheDocument();

    userEvent.click(await findByTestId('location-search-button'));
    expect(mockHistory.push).toHaveBeenCalledWith('/location-search');

    // eslint-disable-next-line no-restricted-globals
    expect(location.pathname).toEqual('/');
    expect(verifyEmail).toBeInTheDocument();
  });
});
