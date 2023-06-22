import { useEffect, useRef } from 'react';

/**
 * EXAMPLE:
 *
 * function Component() {
 *   const [value, setValue] = useState(0);
 *   const prevValue = usePrevious(value)
 *
 *   return <span>Now: {value}, before: {prevValue}</span>;
 * }
 */
function usePrevState<T>(value: T) {
  const ref = useRef<T>();

  useEffect(() => {
    ref.current = value;
  });

  return ref.current;
}

export default usePrevState;
