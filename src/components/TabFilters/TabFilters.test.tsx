import React from 'react';
import { render, fireEvent } from 'test-utils/render';
import {
  getComponentTestingIds,
  getScreenTestingIds,
} from 'test-utils/testIds';

import TabFilters from './TabFilters';

const mockPress = jest.fn();

const screenTestIds = getScreenTestingIds('LocationItems');
const mockProps = () => ({
  filters: [
    {
      title: 'test',
      value: 'test',
      color: 'test',
    },
  ],
  onPress: mockPress,
  active: 'test',
  testID: screenTestIds.tabFilter,
});
const componentTestIds = getComponentTestingIds(
  'TabFilters',
  screenTestIds.tabFilter
);

describe('<TabFilters>', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('renders', () => {
    const utils = render(<TabFilters {...mockProps()} />);
    const firstButton = utils.getByTestId(
      `${componentTestIds.tabButton}-${mockProps().filters[0].value}`
    );
    expect(firstButton).toBeTruthy();
  });

  it('triggers button press', () => {
    const utils = render(<TabFilters {...mockProps()} />);
    const firstButton = utils.getByTestId(
      `${componentTestIds.tabButton}-${mockProps().filters[0].value}`
    );

    fireEvent.press(firstButton);

    expect(mockPress).toHaveBeenCalledTimes(1);
  });
});
