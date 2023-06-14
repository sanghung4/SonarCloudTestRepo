import React from 'react';
import { render } from 'test-utils/render';
import { getComponentTestingIds } from 'test-utils/testIds';
import CustomIcon from './CustomIcon';
import { CustomIconNames } from './types';

const testIds = getComponentTestingIds('CustomIcon');

describe('<CustomIcon>', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('renders all CustomIcons', () => {
    Object.values(CustomIconNames).map((icon) => {
      const utils = render(<CustomIcon name={icon} />);
      const testId = `${testIds.svg}-${icon}`;
      expect(utils.getByTestId(testId)).toBeTruthy();
    });
  });
});
