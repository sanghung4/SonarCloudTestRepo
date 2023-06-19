import { Text } from 'components/Text';
import React from 'react';
import { render } from 'test-utils/render';
import { getComponentTestingIds } from 'test-utils/testIds';

import Banner from './Banner';

import { BannerProps } from './types';

const testIds = getComponentTestingIds('Banner');

const TEST_TEXT = {
  TITLE: 'Test Title',
  SUBTITLE: 'Test Subtitle',
  RIGHT_COMPONENT: 'Test Right Component',
};

const mockProps = (props?: Partial<BannerProps>) => ({
  title: TEST_TEXT.TITLE,
  ...props,
});

describe('<Banner>', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('renders', () => {
    const utils = render(<Banner {...mockProps()} />);
    utils.getByText(TEST_TEXT.TITLE);
  });
  it('renders rightComponent', () => {
    const utils = render(
      <Banner
        {...mockProps({
          rightComponent: <Text>{TEST_TEXT.RIGHT_COMPONENT}</Text>,
        })}
      />
    );
    utils.getByText(TEST_TEXT.RIGHT_COMPONENT);
  });
  it('renders with conditional props', () => {
    const utils = render(
      <Banner
        {...mockProps({
          titleStyle: { fontSize: 2 },
          rightComponent: 'text',
        })}
      />
    );
    // Title
    const title = utils.getByTestId(testIds.title);
    expect(title.props.style.fontSize).toEqual(2);
    // Right Component
    const rightComponent = utils.getByTestId(testIds.rightComponent);
    expect(rightComponent.children).toEqual(['text']);

    utils.getByText(TEST_TEXT.TITLE);
  });
});
