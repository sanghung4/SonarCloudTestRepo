import { useCallback } from 'react';

import { debounce } from 'lodash-es';

function useDebouncedCallback(
  cb: Function,
  delay: number,
  deps?: React.DependencyList
) {
  // eslint-disable-next-line react-hooks/exhaustive-deps
  const debouncedCallback = useCallback(
    debounce((...args) => cb(...args), delay),
    deps ?? []
  );

  return debouncedCallback;
}

export default useDebouncedCallback;
