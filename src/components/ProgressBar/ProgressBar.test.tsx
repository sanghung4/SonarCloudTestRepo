import React from 'react';
import { render } from 'test-utils/render';
import { getComponentTestingIds } from 'test-utils/testIds';
import ProgressBar, { ProgressBarProps } from './ProgressBar';

const mockProps = (props?: Partial<ProgressBarProps>) => ({
  value: 50,
  progressBarStyle: { width: '50%' },
  containerStyle: { width: '100%' },
  ...props,
});

const testIds = getComponentTestingIds('ProgressBar');

describe('<ProgressBar>', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('renders ProgressBar Component', () => {
    const utils = render(<ProgressBar {...mockProps()} />);
    expect(utils.getByTestId(testIds.component)).toBeTruthy();
  });
});
