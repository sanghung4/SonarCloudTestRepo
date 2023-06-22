import { useCallback, useState } from 'react';

/**
 * Creates a stateful ref so that we can use it in effects
 * Code from Joseph Campuzano
 */
export const useCallbackRef = <T>(init?: T) => {
  const [stateRef, setStateRef] = useState<T | null>(init ?? null);
  const refCallback = useCallback((node: T) => {
    if (node) {
      setStateRef(node);
    }
  }, []);
  return [stateRef, refCallback] as const;
};
