import { render as jestRender } from '@testing-library/react';
import { BrowserRouter as Router } from 'react-router-dom';

import {
  AuthContext,
  AuthContextType,
  defaultAuthContext
} from 'providers/AuthProvider';

/**
 * Types
 */
type RenderProps = {
  authContext?: AuthContextType;
};

/**
 * MAIN RENDER WRAPPER
 * @param components (React Node)
 * @param config (see RenderProps)
 * @returns
 */
export function render(
  components: Parameters<typeof jestRender>[0],
  config?: RenderProps
) {
  return jestRender(
    <AuthContext.Provider value={config?.authContext ?? defaultAuthContext}>
      <Router>{components}</Router>
    </AuthContext.Provider>
  );
}
