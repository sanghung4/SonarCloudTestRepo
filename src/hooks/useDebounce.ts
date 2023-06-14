import { debounce } from 'lodash';
import { useRef, useEffect, useCallback } from 'react';

export const useDebounce = (
  cb: (...args: any[]) => void,
  delay = 300
): ((...args: any[]) => void) => {
  const fn = useRef(cb);

  useEffect(() => {
    fn.current = cb;
  }, [cb]);

  return useCallback(
    (...args) => {
      debounce(() => fn.current(...args), delay)();
    },
    [delay]
  );
};
