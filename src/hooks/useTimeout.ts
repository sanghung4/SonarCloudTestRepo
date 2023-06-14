import { useEffect, useRef } from 'react';

export const useTimeout = <T extends () => void>(
  fn: T,
  duration: number
): { clear: () => void } => {
  const timeout = useRef<NodeJS.Timeout>();
  const callback = useRef<T>(fn);

  const clear = () => {
    if (timeout.current) {
      clearTimeout(timeout.current);
    }
  };

  useEffect(() => {
    callback.current = fn;
  }, [fn]);

  useEffect(() => {
    timeout.current = setTimeout(() => {
      callback.current();
    }, duration);

    return clear;
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return { clear };
};

export const useLazyTimeout = <T extends () => void>(
  fn: T,
  duration: number
): { start: (...args: any) => void; clear: () => void } => {
  const timeout = useRef<NodeJS.Timeout>();
  const callback = useRef<T>(fn);

  const clear = () => {
    if (timeout.current) {
      clearTimeout(timeout.current);
    }
  };

  const start = () => {
    clear();
    timeout.current = setTimeout(() => {
      callback.current();
    }, duration);
  };

  useEffect(() => {
    callback.current = fn;
  }, [fn]);

  useEffect(() => () => clear(), []);

  return { start, clear };
};
