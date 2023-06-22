import { Dispatch } from 'react';

export function useResizeObserverRoundFn(n: number) {
  return Math.round(n * 10) / 10;
}

export function mobileOption(
  resetOpen: () => void,
  push: Dispatch<string>,
  isAuthenticated: boolean | undefined,
  path: string,
  auth?: boolean
) {
  push(auth ? (isAuthenticated ? path : '/login') : path);
  resetOpen();
}

export function handleCategoriesClose(
  setShow: Dispatch<boolean>,
  setOpen: Dispatch<boolean>
) {
  return (closeDrawer: boolean) => {
    setShow(false);
    if (closeDrawer) {
      setOpen(false);
    }
  };
}
