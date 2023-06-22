import { act, findByRole } from '@testing-library/react';
import BranchManagement from 'BranchManagement';
import { render } from 'test-utils/TestWrapper';
import { mockGetBranchesListQuery } from './mocks';

describe('Branch Management', () => {
  it('Should render page when permissions are set', async () => {
    const { findByRole, findByText } = render(<BranchManagement />, {
      mocks: [mockGetBranchesListQuery],
      authConfig: {
        authState: { isAuthenticated: true },
        profile: {
          permissions: ['manage_branches'],
          isEmployee: false,
          isVerified: true,
          userId: '123'
        }
      }
    });

    expect(await findByRole('table')).toBeInTheDocument();
    expect(await findByText('1321')).toBeInTheDocument();
  });
});
