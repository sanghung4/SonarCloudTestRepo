import React, { useState } from 'react';
import { render, fireEvent } from 'test-utils/render';
import MockedNavigator from '__mocks__/MockedNavigator';
import Scanner from './Scanner';
import { ScannerMask } from './ScannerMask';
import { ScannerProps } from './types';
import { act, renderHook } from '@testing-library/react-hooks';
import { SCANNER_TEST_IDS } from './types';

const mockFunct = jest.fn();

function useScannerHook() {
  const [scannerWidth, setScannerWidth] = useState(0);
  return { scannerWidth, setScannerWidth };
}

const mockProps = (props?: Partial<ScannerProps>) => ({
  scanning: false,
  showMask: false,
  showPlaceholder: false,
  onBarCodeScanned: mockFunct,
  ...props,
});

const secondaryMockProps = (props?: Partial<ScannerProps>) => ({
  scanning: true,
  showMask: true,
  showPlaceholder: true,
  onBarCodeScanned: mockFunct,
  ...props,
});

const ScannerComponent = () => {
  return <Scanner {...mockProps()} />;
};
const SecondaryScannerComponent = () => {
  return <Scanner {...secondaryMockProps()} />;
};
const navigator = <MockedNavigator component={ScannerComponent} />;

const secondaryNavigator = (
  <MockedNavigator component={SecondaryScannerComponent} />
);

describe('<Scanner>', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('renders Scanner Component', () => {
    const utils = render(navigator);
    expect(utils.getByTestId(SCANNER_TEST_IDS.SCANNER)).toBeTruthy();
  });

  it('renders Scanner with Mask Component', () => {
    const utils = render(secondaryNavigator);
    expect(utils.getByTestId(SCANNER_TEST_IDS.SCANNER)).toBeTruthy();
  });
});

describe('<ScannerMask>', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('renders ScannerMask Component', () => {
    const utils = render(<ScannerMask />);
    expect(utils.getByTestId(SCANNER_TEST_IDS.SCANNER_MASK)).toBeTruthy();
  });

  it('renders ScannerMask Component with onLayout', () => {
    const utils = render(<ScannerMask />);
    fireEvent(utils.getByTestId(SCANNER_TEST_IDS.SCANNER_MASK), 'layout', {
      nativeEvent: { layout: { height: 100 } },
    });
    const { result } = renderHook(() => useScannerHook());
    act(() => {
      result.current.setScannerWidth(100);
    });
    expect(result.current.scannerWidth).toEqual(100);
  });
});
