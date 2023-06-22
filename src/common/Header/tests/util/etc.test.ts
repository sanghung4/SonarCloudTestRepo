import {
  handleCategoriesClose,
  mobileOption,
  useResizeObserverRoundFn
} from 'common/Header/util';

describe('common - Header - util - etc', () => {
  it('expect `useResizeObserverRoundFn` to return full number', () => {
    const result = useResizeObserverRoundFn(10);
    expect(result).toBe(10);
  });
  it('expect `useResizeObserverRoundFn` to return rounded number', () => {
    const result = useResizeObserverRoundFn(1.23);
    expect(result).toBe(1.2);
  });

  it('expect `mobileOption` to call push to path', () => {
    const resetOpen = jest.fn();
    const push = jest.fn();
    const path = '/test';
    mobileOption(resetOpen, push, undefined, path);
    expect(resetOpen).toBeCalled();
    expect(push).toBeCalledWith(path);
  });
  it('expect `mobileOption` to call push to "/login" without auth', () => {
    const push = jest.fn();
    const path = '/test';
    mobileOption(jest.fn(), push, false, path, true);
    expect(push).toBeCalledWith('/login');
  });
  it('expect `mobileOption` to call push to path with auth', () => {
    const push = jest.fn();
    const path = '/test';
    mobileOption(jest.fn(), push, true, path, true);
    expect(push).toBeCalledWith(path);
  });

  it('expect `handleCategoriesClose` to not call setOpen when closeDrawer is false', () => {
    const setShow = jest.fn();
    const setOpen = jest.fn();
    handleCategoriesClose(setShow, setOpen)(false);
    expect(setShow).toBeCalled();
    expect(setOpen).not.toBeCalled();
  });
  it('expect `handleCategoriesClose` to not call setOpen when closeDrawer is true', () => {
    const setShow = jest.fn();
    const setOpen = jest.fn();
    handleCategoriesClose(setShow, setOpen)(true);
    expect(setShow).toBeCalled();
    expect(setOpen).toBeCalled();
  });
});
