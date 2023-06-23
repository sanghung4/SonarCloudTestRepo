// import { useOktaAuth } from '@okta/okta-react';
import { WrapperProps } from '@reece/global-types';
import { useLocation } from 'react-router-dom';

import Menu from 'common/Sidebar/Menu';
import { useAuthContext } from 'providers/AuthProvider';
import logo from 'resources/images/logo-small.svg';

/**
 * Component
 */
function Sidebar({ children }: WrapperProps) {
  /**
   * Custom Hooks
   */
  const { pathname } = useLocation();
  // const { oktaAuth } = useOktaAuth();

  /**
   * Conext
   */
  const { sessionId, authState } = useAuthContext();

  /**
   * Render
   */
  if ((!sessionId && !authState?.isAuthenticated) || pathname === '/login') {
    return <>{children}</>;
  }
  return (
    <div className="flex" data-testid="sidebar_container">
      <div className="bg-primary-1-100 w-[250px] h-screen">
        <div className="px-4 py-6">
          <img src={logo} alt="logo" />
        </div>
        <div className="mt-1 px-4">
          <h3 className="text-white text-3xl font-medium">Punchout</h3>
          <h6 className="text-white text-sm font-bold uppercase">
            Admin Portal
          </h6>
        </div>
        <div className="px-2 mt-16">
          <Menu />
        </div>
        {/* {Boolean(authState?.isAuthenticated) && (
          <button
            children="OUT"
            className="bg-green-500"
            onClick={() => oktaAuth.signOut()}
          />
        )} */}
      </div>
      <div className="flex-1">{children}</div>
    </div>
  );
}

export default Sidebar;
