import PermissionRequired, {
  checkUserPermission,
  Permission
} from 'common/PermissionRequired';
import { authConfig, authConfigNoPerm } from 'common/PermissionRequired/mocks';
import { render } from 'test-utils/TestWrapper';

type MockRedirectToObj = {
  pathname?: string;
  state?: unknown;
};
type MockRedirectProp = {
  to: string | MockRedirectToObj;
};
const mockChild = <div />;
function MockRedirect({ to }: MockRedirectProp) {
  return (
    <span data-testid="redirect">
      {typeof to === 'string' ? to : to.pathname}
    </span>
  );
}

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  Redirect: MockRedirect
}));

describe('common - PermissionRequired', () => {
  describe('checkUserPermission', () => {
    it('Expect to return false with no match', () => {
      const result = checkUserPermission(authConfig.profile, [
        Permission.INVITE_USER
      ]);
      expect(result).toBe(false);
    });
    it('Expect to return false with no permission values', () => {
      const result = checkUserPermission(authConfigNoPerm.profile, [
        Permission.INVITE_USER
      ]);
      expect(result).toBe(false);
    });
    it('Expect to return false with match', () => {
      const result = checkUserPermission(authConfig.profile, [
        Permission.EDIT_LIST
      ]);
      expect(result).toBe(true);
    });
  });

  describe('PermissionRequired', () => {
    it('should match snapshot when there is no match', () => {
      const { container } = render(
        <PermissionRequired permissions={[Permission.INVITE_USER]}>
          {mockChild}
        </PermissionRequired>,
        { authConfig }
      );
      expect(container).toMatchSnapshot();
    });
    it('should match snapshot when there is no userId', () => {
      const { container } = render(
        <PermissionRequired permissions={[Permission.INVITE_USER]}>
          {mockChild}
        </PermissionRequired>,
        { authConfig: authConfigNoPerm }
      );
      expect(container).toMatchSnapshot();
    });
    it('should match snapshot when there is a match', () => {
      const { container } = render(
        <PermissionRequired permissions={[Permission.EDIT_LIST]}>
          {mockChild}
        </PermissionRequired>,
        { authConfig }
      );
      expect(container).toMatchSnapshot();
    });
    it('should match snapshot of mocked redirect', () => {
      const { container } = render(
        <PermissionRequired
          permissions={[Permission.INVITE_USER]}
          redirectTo="/test"
        >
          {mockChild}
        </PermissionRequired>,
        { authConfig }
      );
      expect(container).toMatchSnapshot();
    });
  });
});
