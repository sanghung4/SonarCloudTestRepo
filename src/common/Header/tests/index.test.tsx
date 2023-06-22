import { act, fireEvent } from '@testing-library/react';
import useResizeObserver from 'use-resize-observer';

import { mockBranchContext } from 'Branches/tests/mocks';
import BranchSidebar from 'Branches/BranchSidebar';
import { BranchContext } from 'providers/BranchProvider';
import Header from 'common/Header';
import {
  authConfigLoggedOut,
  authConfigLoggedIn,
  mockCompanyList
} from 'common/Header/tests/mocks';
import { breakpoints, setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

const mockHistory = {
  push: jest.fn()
};
let mockSubdomain = 'reece';
let mockIsWaterworks = false;

jest.mock('use-resize-observer', () => ({
  __esModule: true,
  default: jest.fn()
}));
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useHistory: () => mockHistory
}));
jest.mock('hooks/useDomainInfo', () => ({
  useDomainInfo: () => ({
    companyList: mockCompanyList,
    subdomain: mockSubdomain,
    isWaterworks: mockIsWaterworks
  })
}));

describe('common - Header', () => {
  afterEach(() => {
    mockHistory.push = jest.fn();
    mockSubdomain = 'reece';
  });

  describe('desktop', () => {
    beforeEach(() => {
      (useResizeObserver as jest.Mock).mockReturnValue({
        ref: null,
        width: breakpoints.desktop
      });
      setBreakpoint('desktop');
    });

    it('should match snapshot as logged out user', async () => {
      const { container } = render(<Header />, {
        authConfig: authConfigLoggedOut
      });
      await act(() => new Promise((res) => setTimeout(res, 0)));
      expect(container).toMatchSnapshot();
    });

    it('expect my list button to redirect to login as logged out user', async () => {
      const { getByTestId } = render(<Header />, {
        authConfig: authConfigLoggedOut
      });
      await act(() => new Promise((res) => setTimeout(res, 0)));
      fireEvent.click(getByTestId('my-lists-button'));
      expect(mockHistory.push).toHaveBeenCalledWith('/login');
    });

    it('expect change branch button to redirect to login as logged out user', async () => {
      const { getByTestId } = render(<Header />, {
        authConfig: authConfigLoggedOut
      });
      await act(() => new Promise((res) => setTimeout(res, 0)));
      fireEvent.click(getByTestId('change-branch-button'));
      expect(mockHistory.push).toHaveBeenCalledWith('/login');
    });

    it('expect cart button to redirect to login as logged out user', async () => {
      const { getByTestId } = render(<Header />, {
        authConfig: authConfigLoggedOut
      });
      await act(() => new Promise((res) => setTimeout(res, 0)));
      fireEvent.click(getByTestId('go-to-cart-button-desktop'));
      expect(mockHistory.push).toHaveBeenCalledWith('/login', {});
    });

    it('should match snapshot as logged in user', async () => {
      const { container } = render(<Header />);
      await act(() => new Promise((res) => setTimeout(res, 0)));
      expect(container).toMatchSnapshot();
    });

    it('should match snapshot as logged in user on Fortiline subdomain', async () => {
      mockIsWaterworks = true;
      const { container } = render(<Header />);
      await act(() => new Promise((res) => setTimeout(res, 0)));
      expect(container).toMatchSnapshot();
      mockIsWaterworks = false;
    });

    it('expect my lists button to go to lists as logged in user', async () => {
      const { getByTestId } = render(<Header />);
      await act(() => new Promise((res) => setTimeout(res, 0)));
      fireEvent.click(getByTestId('my-lists-button'));
      expect(mockHistory.push).toHaveBeenCalledWith('/lists');
    });

    it('expect branch button to open branch drawer as logged in user', async () => {
      const { getByTestId } = render(
        <BranchContext.Provider value={mockBranchContext}>
          <Header />
          <BranchSidebar />
        </BranchContext.Provider>
      );
      await act(() => new Promise((res) => setTimeout(res, 0)));
      fireEvent.click(getByTestId('change-branch-button'));
      expect(mockBranchContext.setBranchSelectOpen).toHaveBeenCalledWith(true);
    });

    it('expect cart button to go to cart as logged in user', async () => {
      const { getByTestId } = render(<Header />);
      await act(() => new Promise((res) => setTimeout(res, 0)));
      fireEvent.click(getByTestId('go-to-cart-button-desktop'));
      expect(mockHistory.push).toHaveBeenCalledWith('/cart', {});
    });

    it('should match snapshot with morrisonsupply subdomain', async () => {
      mockSubdomain = 'morrisonsupply';
      const { container } = render(<Header />);
      await act(() => new Promise((res) => setTimeout(res, 0)));
      expect(container).toMatchSnapshot();
    });
  });

  describe('mobile', () => {
    beforeEach(() => {
      (useResizeObserver as jest.Mock).mockReturnValue({
        ref: null,
        width: breakpoints.mobile
      });
      setBreakpoint('mobile');
    });

    it('should match snapshot as logged out user', async () => {
      const { container } = render(<Header />, {
        authConfig: authConfigLoggedOut
      });
      await act(() => new Promise((res) => setTimeout(res, 0)));
      expect(container).toMatchSnapshot();
    });

    it('should match snapshot as logged in user', async () => {
      const { container } = render(<Header />, {
        authConfig: authConfigLoggedIn
      });
      await act(() => new Promise((res) => setTimeout(res, 0)));
      expect(container).toMatchSnapshot();
    });

    it('should match snapshot as logged in user on Fortiline subdomain', async () => {
      mockIsWaterworks = true;
      const { container } = render(<Header />, {
        authConfig: authConfigLoggedIn
      });
      await act(() => new Promise((res) => setTimeout(res, 0)));
      expect(container).toMatchSnapshot();
      mockIsWaterworks = false;
    });

    it('should match snapshot with morrisonsupply subdomain', async () => {
      mockSubdomain = 'morrisonsupply';
      const { container } = render(<Header />);
      await act(() => new Promise((res) => setTimeout(res, 0)));
      expect(container).toMatchSnapshot();
    });

    it('expect to open the hamburger menu', async () => {
      const { container, getByTestId } = render(<Header />);
      await act(() => new Promise((res) => setTimeout(res, 0)));
      fireEvent.click(getByTestId('mobile-hamburger-menu'));
      await act(() => new Promise((res) => setTimeout(res, 0)));
      expect(container).toMatchSnapshot();
    });

    it('expect to open the search button', async () => {
      const { container, getByTestId } = render(<Header />);
      await act(() => new Promise((res) => setTimeout(res, 0)));
      fireEvent.click(getByTestId('header-search-button'));
      await act(() => new Promise((res) => setTimeout(res, 0)));
      expect(container).toMatchSnapshot();
    });
  });
});
