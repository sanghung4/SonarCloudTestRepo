import { act, fireEvent } from '@testing-library/react';

import {
  authConfigLoggedIn,
  authConfigLoggedOut,
  authConfigWithPayment
} from 'common/Header/tests/mocks';
import UserButton from 'common/Header/UserButton';
import { render } from 'test-utils/TestWrapper';

const mockHistory = { push: jest.fn() };
jest.mock('react-router-dom', () => ({
  useHistory: () => mockHistory
}));

describe('common - Header - UserButton', () => {
  afterEach(() => {
    mockHistory.push = jest.fn();
  });

  it('should match snapshot with no account', async () => {
    const { container } = render(<UserButton />, {
      authConfig: authConfigLoggedOut
    });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot with account', async () => {
    const { container } = render(<UserButton />, {
      authConfig: authConfigLoggedIn
    });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot with payment management', async () => {
    const { container } = render(<UserButton />, {
      authConfig: authConfigWithPayment
    });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('expect to call history.push when login button is clicked', async () => {
    const { getByTestId } = render(<UserButton />, {
      authConfig: authConfigLoggedOut
    });
    await act(() => new Promise((res) => setTimeout(res, 0)));
    fireEvent.click(getByTestId('sign-in-header-button'));
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(mockHistory.push).toBeCalledWith('/login');
  });
});
