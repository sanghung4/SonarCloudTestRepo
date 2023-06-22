import { waitFor } from '@testing-library/react';

import ViewAccount from 'Account/ViewAccount';
import { mockUser } from 'Account/tests/mocks';
import { render } from 'test-utils/TestWrapper';
import { setBreakpoint } from 'test-utils/mockMediaQuery';

describe('Account - View Account', () => {
  it('should match snapshot on desktop', async () => {
    setBreakpoint('desktop');
    const { container, getByTestId } = render(
      <ViewAccount user={mockUser} onEditClicked={jest.fn()} />
    );
    await waitFor(() =>
      expect(getByTestId('viewaccount-component')).toBeInTheDocument()
    );
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot on mobile', async () => {
    setBreakpoint('mobile');
    const { container, getByTestId } = render(
      <ViewAccount user={mockUser} onEditClicked={jest.fn()} />
    );
    await waitFor(() =>
      expect(getByTestId('viewaccount-component')).toBeInTheDocument()
    );
    expect(container).toMatchSnapshot();
  });
});
