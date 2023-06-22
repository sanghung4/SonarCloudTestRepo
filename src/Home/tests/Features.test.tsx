import { fireEvent } from '@testing-library/react';

import Features from 'Home/Features';
import { act } from 'react-dom/test-utils';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

describe('Home - features section - desktop', () => {
  beforeEach(() => {
    setBreakpoint('desktop');
  });

  it('should match snapshot', () => {
    const { container } = render(<Features />);
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot when hovering over "easy to use"', () => {
    const { container, getByTestId } = render(<Features />);
    const easyToUse = getByTestId('easy-to-use') as HTMLElement;
    const featuresList = getByTestId('features-list') as HTMLElement;

    act(() => {
      fireEvent.mouseEnter(easyToUse);
    });
    expect(container).toMatchSnapshot();

    act(() => {
      fireEvent.mouseLeave(featuresList);
    });
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot when hovering over "build businesses"', () => {
    const { container, getByTestId } = render(<Features />);
    const buildBusinesses = getByTestId('build-businesses') as HTMLElement;
    const featuresList = getByTestId('features-list') as HTMLElement;

    act(() => {
      fireEvent.mouseEnter(buildBusinesses);
    });
    expect(container).toMatchSnapshot();

    act(() => {
      fireEvent.mouseLeave(featuresList);
    });
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot when hovering over "saves time"', () => {
    const { container, getByTestId } = render(<Features />);
    const savesTime = getByTestId('saves-time') as HTMLElement;
    const featuresList = getByTestId('features-list') as HTMLElement;

    act(() => {
      fireEvent.mouseEnter(savesTime);
    });
    expect(container).toMatchSnapshot();

    act(() => {
      fireEvent.mouseLeave(featuresList);
    });
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot when hovering over "faster future"', () => {
    const { container, getByTestId } = render(<Features />);
    const fasterFuture = getByTestId('faster-future') as HTMLElement;
    const featuresList = getByTestId('features-list') as HTMLElement;

    act(() => {
      fireEvent.mouseEnter(fasterFuture);
    });
    expect(container).toMatchSnapshot();

    act(() => {
      fireEvent.mouseLeave(featuresList);
    });
    expect(container).toMatchSnapshot();
  });
});

describe('Home - features section - mobile', () => {
  beforeEach(() => {
    setBreakpoint('mobile');
  });

  it('should match snapshot', () => {
    const { container } = render(<Features />);
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot when hovering over "easy to use"', () => {
    const { container, getByTestId } = render(<Features />);
    const easyToUse = getByTestId('easy-to-use') as HTMLElement;
    const featuresList = getByTestId('features-list') as HTMLElement;

    act(() => {
      fireEvent.mouseEnter(easyToUse);
    });
    expect(container).toMatchSnapshot();

    act(() => {
      fireEvent.mouseLeave(featuresList);
    });
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot when hovering over "build businesses"', () => {
    const { container, getByTestId } = render(<Features />);
    const buildBusinesses = getByTestId('build-businesses') as HTMLElement;
    const featuresList = getByTestId('features-list') as HTMLElement;

    act(() => {
      fireEvent.mouseEnter(buildBusinesses);
    });
    expect(container).toMatchSnapshot();

    act(() => {
      fireEvent.mouseLeave(featuresList);
    });
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot when hovering over "saves time"', () => {
    const { container, getByTestId } = render(<Features />);
    const savesTime = getByTestId('saves-time') as HTMLElement;
    const featuresList = getByTestId('features-list') as HTMLElement;

    act(() => {
      fireEvent.mouseEnter(savesTime);
    });
    expect(container).toMatchSnapshot();

    act(() => {
      fireEvent.mouseLeave(featuresList);
    });
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot when hovering over "faster future"', () => {
    const { container, getByTestId } = render(<Features />);
    const fasterFuture = getByTestId('faster-future') as HTMLElement;
    const featuresList = getByTestId('features-list') as HTMLElement;

    act(() => {
      fireEvent.mouseEnter(fasterFuture);
    });
    expect(container).toMatchSnapshot();

    act(() => {
      fireEvent.mouseLeave(featuresList);
    });
    expect(container).toMatchSnapshot();
  });
});
