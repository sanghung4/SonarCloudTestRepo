import { useContext, useEffect } from 'react';

import { Box, useTheme } from '@dialexa/reece-component-library';
import { Redirect } from 'react-router-dom';

import { AuthContext } from 'AuthProvider';
import Loader from 'common/Loader';

export default function Logout() {
  /**
   * Custom hooks
   */
  const { spacing } = useTheme();

  /**
   * Context
   */
  const { authState, handleLogout, isLoggingOut } = useContext(AuthContext);

  /**
   * Effect
   */
  // eslint-disable-next-line react-hooks/exhaustive-deps
  useEffect(() => handleLogout?.(), []);

  /**
   * Render
   */
  if (authState?.isAuthenticated || isLoggingOut) {
    return (
      <Box minHeight={spacing(20)}>
        <Loader />
      </Box>
    );
  }
  return <Redirect to="/" />;
}
