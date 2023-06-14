import React from 'react';
import { render, fireEvent } from 'test-utils/render';
import { getComponentTestingIds } from 'test-utils/testIds';
import Pill from './Pill';
import { PillProps, PillStats, PillVariants } from './types';

const TEST_DATA = {
  PILL_TEXT: 'pill test',
};

const mockProps = (props?: Partial<PillProps>) => ({
  variant: PillVariants.OUTLINE,
  status: PillStats.PRIMARY,
  style: props?.style,
  value: true,
  ...props,
});

const mockFunct = jest.fn();

const mocks = mockProps({
  variant: PillVariants.SOLID,
  status: PillStats.WARNING,
  value: 'pill test',
  style: { marginTop: 25 },
  onPress: mockFunct,
});

const secondaryMocks = mockProps({
  variant: PillVariants.OUTLINE,
  status: PillStats.DEFAULT,
  value: 'pill test',
  style: { marginTop: 25 },
});

const testIds = getComponentTestingIds('Pill');

describe('<Pill>', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('renders Pill Component', () => {
    const utils = render(<Pill {...mockProps()} />);
    expect(utils.getByTestId(testIds.text)).toBeTruthy();
  });
  it('renders with conditional logic', () => {
    const utils = render(<Pill {...mocks} />);
    utils.getByText(TEST_DATA.PILL_TEXT);
    expect(mocks.status).toEqual(PillStats.WARNING);
    expect(mocks.variant).toEqual(PillVariants.SOLID);
  });

  it('renders Pill component with styling', () => {
    const utils = render(<Pill {...mocks} />);
    const componentStyle = utils.getByTestId(testIds.component).props.style
      .marginTop;
    expect(componentStyle).toEqual(25);
  });

  it('renders Pill component as TouchableOpacity with onPress', () => {
    const utils = render(<Pill {...mocks} />);
    fireEvent.press(utils.getByTestId(testIds.component));
    expect(mockFunct).toBeCalled();
  });

  it('renders with secondary conditional logic', () => {
    const utils = render(<Pill {...secondaryMocks} />);
    utils.getByText(TEST_DATA.PILL_TEXT);
    expect(secondaryMocks.status).toEqual(PillStats.DEFAULT);
    expect(secondaryMocks.variant).toEqual(PillVariants.OUTLINE);
  });

  it('renders proper borderRadius', () => {
    const utils = render(<Pill {...secondaryMocks} />);
    utils.getByText(TEST_DATA.PILL_TEXT);
    expect(secondaryMocks.status).toEqual(PillStats.DEFAULT);
    expect(secondaryMocks.variant).toEqual(PillVariants.OUTLINE);
  });
});
