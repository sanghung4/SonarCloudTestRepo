import { waitFor } from '@testing-library/react';

import { Permission } from 'common/PermissionRequired';
import Support from 'Support';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

const profile = {
  userId: 'testuser',
  permissions: Object.values(Permission),
  isEmployee: true,
  isVerified: true
};

describe('Support', () => {
  it('should match snapshot on desktop', async () => {
    setBreakpoint('desktop');
    const { container } = render(<Support />, {
      authConfig: { profile, authState: {} }
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));
    expect(container).toMatchSnapshot();
  });
  it('should match snapshot on mobile', async () => {
    setBreakpoint('mobile');
    const { container } = render(<Support />, {
      authConfig: { profile, authState: {} }
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 1)));
    expect(container).toMatchSnapshot();
  });
});
