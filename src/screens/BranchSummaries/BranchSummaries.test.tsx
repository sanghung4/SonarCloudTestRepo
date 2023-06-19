import React from 'react';
import { GetLocationsDocument } from 'api';
import { act, render } from 'test-utils/render';
import MockedNavigator from '__mocks__/MockedNavigator';
import { Config } from 'apollo/local';
import { GraphQLError } from 'graphql';
import wait from 'waait';

import { BranchSummaries } from '.';
import { OverlayContainer } from 'components/Overlay';
import { getScreenTestingIds } from 'test-utils/testIds';

jest.mock('hooks/useConfig', () => ({
  useConfig: () => [
    ({
      count: {
        id: 'AB345',
        branch: {
          id: 'CD435',
          name: 'Mocked Branch',
        },
      },
    } as unknown) as Config,
    () => {},
  ],
}));

const navigator = <MockedNavigator component={BranchSummaries} />;

const locationsMock = {
  request: {
    query: GetLocationsDocument,
  },
  result: {
    data: {
      locations: {
        totalLocations: 3,
        totalCounted: 1,
        content: [
          {
            id: 'A - A000A000',
            committed: true,
            totalProducts: 6,
            totalCounted: 6,
          },
          {
            id: 'A - A001A000',
            committed: false,
            totalProducts: 6,
            totalCounted: 4,
          },
          {
            id: 'A - A002A000',
            committed: false,
            totalProducts: 6,
            totalCounted: 0,
          },
        ],
      },
    },
  },
};

const locationsErrorMock = {
  request: {
    query: GetLocationsDocument,
  },
  result: {
    errors: [new GraphQLError('Error!')],
  },
};

const testIds = getScreenTestingIds('Overlay');

describe('<BranchSummaries>', () => {
  beforeEach(jest.clearAllMocks);

  it('should render', async () => {
    const utils = render(navigator, [locationsMock]);

    await act(async () => {
      await wait();
    });
    utils.findAllByText('Completed');
    utils.findAllByText('Not Started');
    utils.findAllByText('Started');
  });

  it('should show error', async () => {
    const utils = render(
      <>
        {navigator}
        <OverlayContainer />
      </>,
      [locationsErrorMock]
    );

    await act(async () => {
      await wait();
    });

    utils.getByTestId(testIds.alert);
  });
});
