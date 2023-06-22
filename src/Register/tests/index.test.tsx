import { testIds } from 'test-utils/testIds';
import { render } from 'test-utils/TestWrapper';
import Register from 'Register';

/**
 * Constants
 */
const TEST_IDS = testIds.Register;

/**
 * Mocks
 */
const mockPathName = jest.fn();
const mockReplace = jest.fn();

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useLocation: () => ({ pathname: mockPathName }),
  useHistory: () => ({ replace: mockReplace })
}));

describe('Register', () => {
  it('Should render the register page', async () => {
    const { findByTestId } = render(<Register />, {
      authConfig: { authState: { isAuthenticated: false } }
    });

    const page = await findByTestId(TEST_IDS.page);

    expect(page).toBeInTheDocument();
  });
});
