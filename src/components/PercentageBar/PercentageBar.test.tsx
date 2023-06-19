import { Colors } from 'constants/style';
import React from 'react';
import { render } from 'test-utils/render';
import { getComponentTestingIds } from 'test-utils/testIds';
import PercentageBar from './PercentageBar';

const testIds = getComponentTestingIds('PercentageBar');
describe('<PercentageBar>', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('renders PercentageBar Component', () => {
    const utils = render(
      <PercentageBar progressBarContainerStyle={{ width: 100 }} value={50} />
    );
    expect(utils.getByTestId(testIds.component)).toBeTruthy();
  });

  it('renders PercentageBar Component with dynamic props', () => {
    const utils = render(
      <PercentageBar
        progressBarContainerStyle={{ width: 100 }}
        value={50}
        variation={'COUNT_SUMMARY'}
        minMaxLabelsWrapperStyle={{ backgroundColor: Colors.WHITE }}
        containerStyle={{ width: 150 }}
        labelWrapperStyle={{ backgroundColor: Colors.BLACK }}
      />
    );
    expect(utils.getByTestId(testIds.component)).toBeTruthy();
  });
});
