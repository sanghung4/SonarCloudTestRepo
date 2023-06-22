import LoginAndSupport from 'Home/LoginAndSupport';
import { render } from 'test-utils/TestWrapper';
import { MemoryRouter } from 'react-router-dom';
import userEvent from '@testing-library/user-event';
import { act } from 'react-dom/test-utils';

describe('Home - Login and Support', () => {
  it('Render CallABranch link', () => {
    const { getByTestId } = render(<LoginAndSupport />);

    const callABranch = getByTestId('support-subtitle-1');
    expect(callABranch).toBeInTheDocument();
  });

  it('Redirect to LocationSearch page when CallABranch is clicked', async () => {
    const { getByTestId } = render(
      <MemoryRouter>
        <LoginAndSupport />
      </MemoryRouter>
    );

    const callABranchLink = getByTestId('support-subtitle-1');
    act(() => {
      userEvent.click(callABranchLink);
    });

    // eslint-disable-next-line no-restricted-globals
    expect(location.pathname).toEqual('/');
  });
});
