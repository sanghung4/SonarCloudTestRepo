import React from 'react';
import { render, fireEvent } from 'test-utils/render';
import { getComponentTestingIds } from 'test-utils/testIds';
import LocationItemsbanner, {
  LocationItemsBannerProps,
} from './LocationItemsBanner';

const testIds = getComponentTestingIds('LocationItemsBanner');

const mockProps = (props?: Partial<LocationItemsBannerProps>) => ({
  title: 'test title',
  isRecount: true,
  ...props,
});

describe('<LocationItemsBanner>', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('renders LocationItemsBanner Component', () => {
    const utils = render(<LocationItemsbanner {...mockProps()} />);
    expect(utils.getByTestId(testIds.component)).toBeTruthy();
  });

  it('renders LocationItemsBanner Component with isRecount', () => {
    const utils = render(<LocationItemsbanner {...mockProps()} />);
    fireEvent(utils.getByTestId(testIds.recountContainer), 'layout', {
      nativeEvent: { layout: { height: 100 } },
    });
  });
});
