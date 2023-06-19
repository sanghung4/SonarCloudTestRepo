import React from 'react';
import { render } from 'test-utils/render';
import {
  getComponentTestingIds,
  getScreenTestingIds,
} from 'test-utils/testIds';
import MockedNavigator from '__mocks__/MockedNavigator';
import BranchPage from './BranchPage';

const mockFunc = jest.fn();

const screenTestIds = getScreenTestingIds('BranchPage');
const componentTestIds = getComponentTestingIds(
  'ScreenLayout',
  screenTestIds.screenLayout
);

jest.mock('@react-navigation/native', () => {
  const actualNav = jest.requireActual('@react-navigation/native');
  return {
    ...actualNav,
    useNavigation: () => ({
      navigate: mockFunc,
    }),
    useRoute: () => ({
      params: {
        params: {
          bannerText: 'test text',
          renderItem: mockFunc,
          loading: false,
          onFinishLoad: mockFunc,
        },
      },
    }),
  };
});

const navigator = <MockedNavigator component={BranchPage} />;

const utils = render(navigator);

describe('<BranchPage>', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('renders BranchPage Component', () => {
    const component = utils.getByTestId(componentTestIds.component);
    expect(component).toBeTruthy();
  });
});
