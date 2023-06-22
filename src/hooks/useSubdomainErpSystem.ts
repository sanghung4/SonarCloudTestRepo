import { useContext, useEffect, useState } from 'react';

import { ErpSystemEnum } from 'generated/graphql';
import { useDomainInfo } from 'hooks/useDomainInfo';
import { AuthContext } from 'AuthProvider';

export function useSubdomainErpSystem() {
  /**
   * Custom Hooks
   */
  const { isWaterworks } = useDomainInfo();
  /**
   * Context
   */
  const { authState } = useContext(AuthContext);
  /**
   * State
   */
  const [currentSubdomain, setCurrentSubdomain] = useState(
    ErpSystemEnum.Eclipse
  );

  /**
   * Effects
   */
  useEffect(handleCurrentSubdomain, [
    authState?.isAuthenticated,
    isWaterworks,
    setCurrentSubdomain
  ]);

  /**
   * Effect Definitions
   */

  function handleCurrentSubdomain() {
    if (!authState?.isAuthenticated) {
      if (isWaterworks) {
        setCurrentSubdomain(ErpSystemEnum.Mincron);
      } else {
        setCurrentSubdomain(ErpSystemEnum.Eclipse);
      }
    }
  }

  return { currentSubdomain };
}
