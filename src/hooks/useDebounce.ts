import { useState, useEffect } from 'react';

// this will debounce a value and store it in state after another changes;
// https://usehooks.com/useDebounce/
function useDebounce<T>(value: T, delay: number) {
  const [debouncedValue, setDebouncedValue] = useState(value);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);

    const handler = setTimeout(() => {
      setDebouncedValue(value);
      setLoading(false);
    }, delay);

    return () => clearTimeout(handler);
  }, [value, delay, setLoading, setDebouncedValue]);

  return { value: debouncedValue, loading };
}

export default useDebounce;
