import React from 'react';
import { Count, ErpSystem } from 'api';
import { Config } from 'apollo/local';
import { render } from 'test-utils/render';

import MockedNavigator from '__mocks__/MockedNavigator';

import * as hooks from 'hooks/useConfig';
import { ManualLocationEntry } from '.';
import { useNavigation, useRoute } from '@react-navigation/native';
import { getTestId } from 'test-utils/testIds';

// Generate Testing Ids for components
const locationInputTestId = getTestId(
  'ManualLocationEntry',
  'locationInput',
  'Input'
);

const mockedNavigate = jest.fn();

const COUNT_ID = 'IDS222';
const BRANCH_ID = '1149';

const navigator = (
  <MockedNavigator
    component={() => (
      <ManualLocationEntry
        navigation={{ ...useNavigation(), navigate: mockedNavigate }}
        route={useRoute()}
      />
    )}
  />
);

const mockCount = (opts: Partial<Count> = {}) => ({
  id: COUNT_ID,
  erpSystem: ErpSystem.ECLIPSE,
  branch: { id: BRANCH_ID, name: 'Arlington' },
  ...opts,
});

const mockConfig = (opts: Partial<Config> = {}) =>
  ({
    count: mockCount(),
    location: null,
    ...opts,
  } as Config);

const mockConfigResp = (
  config = {},
  setConfig = jest.fn()
): ReturnType<typeof hooks.useConfig> => [mockConfig(config), setConfig];

describe('<ManualLocationEntry>', () => {
  afterEach(jest.resetAllMocks);

  it('renders', () => {
    jest.spyOn(hooks, 'useConfig').mockImplementation(() => mockConfigResp());

    const utils = render(navigator);
    utils.getByText(`Branch Num:${BRANCH_ID} Count ID:${COUNT_ID}`);
  });

  it('renders correct inputs', () => {
    const count = mockCount({ erpSystem: ErpSystem.ECLIPSE });
    jest
      .spyOn(hooks, 'useConfig')
      .mockImplementation(() => mockConfigResp({ count }));

    const utils = render(navigator);

    expect(utils.queryByTestId(locationInputTestId)).not.toBeNull();
  });
});
