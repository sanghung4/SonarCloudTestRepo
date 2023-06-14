import React from 'react';
import { render } from 'test-utils/render';
import { getScreenTestingIds } from 'test-utils/testIds';
import Alert from './Alert';

describe('<Alert>', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  const testIds = getScreenTestingIds('Alert');

  it('renders Alert Component', () => {
    const utils = render(<Alert />);
    expect(utils.getByTestId(testIds.component)).toBeTruthy();
  });
});
