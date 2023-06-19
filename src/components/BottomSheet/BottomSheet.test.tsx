import React from 'react';
import { render } from 'test-utils/render';
import { getComponentTestingIds } from 'test-utils/testIds';
import BottomSheet from './BottomSheet';
import { BottomSheetProps } from './types';

const testIds = getComponentTestingIds('BottomSheet');

const mockProps = (props?: Partial<BottomSheetProps>) => ({
  isVisible: true,
  containerStyle: { width: 100 },
  ...props,
});

const secondaryMockProps = (props?: Partial<BottomSheetProps>) => ({
  isVisible: false,
  containerStyle: { width: 100 },
  ...props,
});

describe('<BottomSheet>', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('renders BottomSheet Component', () => {
    const utils = render(<BottomSheet {...mockProps()} />);
    expect(utils.getByTestId(testIds.component)).toBeTruthy();
  });

  it('renders BottomSheet with useSpring(0)', () => {
    const utils = render(<BottomSheet {...secondaryMockProps()} />);
    expect(utils.getByTestId(testIds.component)).toBeTruthy();
  });

  it('renders BottomSheet Title', () => {
    const utils = render(<BottomSheet.Title {...secondaryMockProps()} />);
    expect(utils.getByTestId(testIds.title)).toBeTruthy();
  });

  it('renders BottomSheet Subtitle', () => {
    const utils = render(<BottomSheet.Subtitle {...secondaryMockProps()} />);
    expect(utils.getByTestId(testIds.subtitle)).toBeTruthy();
  });

  it('renders BottomSheet Description', () => {
    const utils = render(<BottomSheet.Description {...secondaryMockProps()} />);
    expect(utils.getByTestId(testIds.description)).toBeTruthy();
  });
});
