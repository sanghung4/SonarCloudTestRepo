import mediaQuery from 'css-mediaquery';

function createMatchMedia(width: number) {
  return (query: string): MediaQueryList => ({
    matches: mediaQuery.match(query, { width }),
    media: query,
    onchange: null,
    addListener: () => jest.fn(),
    removeListener: () => jest.fn(),
    addEventListener: jest.fn(),
    removeEventListener: jest.fn(),
    dispatchEvent: jest.fn()
  });
}

export const breakpoints = {
  mobile: 414,
  desktop: 1200
};

export const setBreakpoint = (breakpoint: 'mobile' | 'desktop' = 'desktop') => {
  Object.defineProperty(window, 'innerWidth', {
    writable: true,
    configurable: true,
    value: breakpoints[breakpoint]
  });
  window.dispatchEvent(new Event('resize'));

  window.matchMedia = createMatchMedia(window.innerWidth);
};
