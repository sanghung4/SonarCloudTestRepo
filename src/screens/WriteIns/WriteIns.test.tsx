import React from 'react';
import wait from 'waait';
import { GraphQLError } from 'graphql';
import { GetWriteInsDocument } from 'api';
import { act, render, fireEvent } from 'test-utils/render';
import MockedNavigator from '__mocks__/MockedNavigator';
import { Config } from 'apollo/local';
import WriteIns from './WriteIns';
import { getScreenTestingIds } from 'test-utils/testIds';

// Get test id for component
const testIds = getScreenTestingIds('WriteIns');

const mockedNavigate = jest.fn();

jest.mock('@react-navigation/native', () => {
  const actualNav = jest.requireActual('@react-navigation/native');
  return {
    ...actualNav,
    useNavigation: () => ({
      navigate: mockedNavigate,
    }),
  };
});

jest.mock('hooks/useConfig', () => ({
  useConfig: () => [
    ({
      count: {
        id: 'AB345',
        name: 'Mocked Branch',
        branch: {
          id: 'CD435',
        },
      },
    } as unknown) as Config,
    () => {},
  ],
}));

const navigator = <MockedNavigator component={WriteIns} />;

const writeInsMock = {
  request: {
    query: GetWriteInsDocument,
    variables: {
      options: {
        sort: {
          property: 'locationName',
          direction: 'asc',
        },
        page: 0,
        size: 999,
      },
    },
  },
  result: {
    data: {
      writeIns: {
        content: [
          {
            id: '729ad920-d973-4ef1-ac57-32dab0c3e6f6',
            locationId: 'J-ARCH-A17',
            uom: 'SqFt',
            quantity: 95,
            description:
              'Donec quis orci eget orci vehicula condimentum. Curabitur in libero ut massa volutpat convallis. Morbi odio odio, elementum eu, interdum eu, tincidunt in, leo. Maecenas pulvinar lobortis est.',
            catalogNum: '5-45731-3',
            upcNum: '48339-86838',
            comment: 'Maecenas ut massa quis augue luctus tincidunt.',
            createdBy: 'Clayborn Espinheira',
            createdAt: '2021-01-22T19:53:07Z',
            updatedBy: 'Clayborn Espinheira',
            updatedAt: '2021-01-22T19:53:07Z',
            resolved: true,
          },
          {
            id: '729ad920-d973-4ef1-ac57-32dab0c3e6f7',
            locationId: 'J-ARCH-A18',
            uom: 'SqFt',
            quantity: 100,
            description:
              'Donec quis orci eget orci vehicula condimentum. Curabitur in libero ut massa volutpat convallis. Morbi odio odio, elementum eu, interdum eu, tincidunt in, leo. Maecenas pulvinar lobortis est1.',
            catalogNum: '5-45731-4',
            upcNum: '48339-86839',
            comment: 'Maecenas ut massa quis augue luctus tincidunt1.',
            createdBy: 'Clayborn Espinheira',
            createdAt: '2021-01-22T19:53:07Z',
            updatedBy: 'Clayborn Espinheira',
            updatedAt: '2021-01-22T19:53:07Z',
            resolved: true,
          },
          {
            id: '729ad920-d973-4ef1-ac57-32dab0c3e6f8',
            locationId: 'J-ARCH-B19',
            uom: 'SqFt',
            quantity: 105,
            description:
              'Donec quis orci eget orci vehicula condimentum. Curabitur in libero ut massa volutpat convallis. Morbi odio odio, elementum eu, interdum eu, tincidunt in, leo. Maecenas pulvinar lobortis est2.',
            catalogNum: '5-45731-5',
            upcNum: '48339-86830',
            comment: 'Maecenas ut massa quis augue luctus tincidunt2.',
            createdBy: 'Clayborn Espinheira',
            createdAt: '2021-01-22T19:53:07Z',
            updatedBy: 'Clayborn Espinheira',
            updatedAt: '2021-01-22T19:53:07Z',
            resolved: true,
          },
          {
            id: '23ba7759-b998-4c21-8ef4-9ed5d6b36822',
            locationId: 'J-ARCH-C15',
            uom: 'Lb',
            quantity: 13,
            description:
              'Duis bibendum. Morbi non quam nec dui luctus rutrum. Nulla tellus. In sagittis dui vel nisl. Duis ac nibh. Fusce lacus purus, aliquet at, feugiat non, pretium quis, lectus. Suspendisse potenti. In eleifend quam a odio. In hac habitasse platea dictumst. Maecenas ut massa quis augue luctus tincidunt.',
            catalogNum: '8-50714-5',
            upcNum: '37164-28172',
            comment:
              'Maecenas rhoncus aliquam lacus. Morbi quis tortor id nulla ultrices aliquet. Maecenas leo odio, condimentum id, luctus nec, molestie sed, justo. Pellentesque viverra pede ac diam. Cras pellentesque volutpat dui. Maecenas tristique, est et tempus semper, est quam pharetra magna, ac consequat metus sapien ut nunc.',
            createdBy: 'Braden Coolican',
            createdAt: '2020-11-02T01:28:07Z',
            updatedBy: 'Braden Coolican',
            updatedAt: '2020-11-02T01:28:07Z',
            resolved: false,
          },
          {
            id: '23ba7759-b998-4c21-8ef4-9ed5d6b36823',
            locationId: 'J-ARCH-C16',
            uom: 'Lb',
            quantity: 15,
            description:
              'Duis bibendum. Morbi non quam nec dui luctus rutrum. Nulla tellus. In sagittis dui vel nisl. Duis ac nibh. Fusce lacus purus, aliquet at, feugiat non, pretium quis, lectus. Suspendisse potenti. In eleifend quam a odio. In hac habitasse platea dictumst. Maecenas ut massa quis augue luctus tincidunt1.',
            catalogNum: '8-50714-6',
            upcNum: '37164-28173',
            comment:
              'Maecenas rhoncus aliquam lacus. Morbi quis tortor id nulla ultrices aliquet. Maecenas leo odio, condimentum id, luctus nec, molestie sed, justo. Pellentesque viverra pede ac diam. Cras pellentesque volutpat dui. Maecenas tristique, est et tempus semper, est quam pharetra magna, ac consequat metus sapien ut nunc1.',
            createdBy: 'Braden Coolican',
            createdAt: '2020-11-02T01:28:07Z',
            updatedBy: 'Braden Coolican',
            updatedAt: '2020-11-02T01:28:07Z',
            resolved: false,
          },
          {
            id: '23ba7759-b998-4c21-8ef4-9ed5d6b36824',
            locationId: 'J-ARCH-D17',
            uom: 'Lb',
            quantity: 18,
            description:
              'Duis bibendum. Morbi non quam nec dui luctus rutrum. Nulla tellus. In sagittis dui vel nisl. Duis ac nibh. Fusce lacus purus, aliquet at, feugiat non, pretium quis, lectus. Suspendisse potenti. In eleifend quam a odio. In hac habitasse platea dictumst. Maecenas ut massa quis augue luctus tincidunt2.',
            catalogNum: '8-50714-5',
            upcNum: '37164-28172',
            comment:
              'Maecenas rhoncus aliquam lacus. Morbi quis tortor id nulla ultrices aliquet. Maecenas leo odio, condimentum id, luctus nec, molestie sed, justo. Pellentesque viverra pede ac diam. Cras pellentesque volutpat dui. Maecenas tristique, est et tempus semper, est quam pharetra magna, ac consequat metus sapien ut nunc2.',
            createdBy: 'Braden Coolican',
            createdAt: '2020-11-02T01:28:07Z',
            updatedBy: 'Braden Coolican',
            updatedAt: '2020-11-02T01:28:07Z',
            resolved: false,
          },
        ],
        pageable: {
          sort: {
            sorted: true,
            unsorted: false,
            empty: false,
          },
          pageNumber: 0,
          pageSize: 999,
          offset: 10,
          paged: false,
          unpaged: true,
        },
      },
    },
  },
};

const writeInsErrorMock = {
  request: {
    query: GetWriteInsDocument,
    variables: {},
  },
  result: {
    errors: [new GraphQLError('Error!')],
  },
};

describe('<WriteIns>', () => {
  beforeEach(jest.clearAllMocks);

  it('should render banner, search input, and tabs', () => {
    const utils = render(navigator);

    expect(utils.getByTestId('banner-title')).toBeTruthy();
    expect(utils.getByPlaceholderText('Location / Item')).toBeTruthy();
    expect(utils.getByTestId(testIds.unresolvedTab)).toBeTruthy();
    expect(utils.getByTestId(testIds.resolvedTab)).toBeTruthy();
  });

  it('should display write-ins in unresolved and resolved lists', async () => {
    const utils = render(navigator, [writeInsMock]);

    await act(async () => {
      await wait(10);
    });

    /* checks list of write-ins in unresolved tab */
    const item4 = utils.getByTestId(
      `description-${writeInsMock.result.data.writeIns.content[3].description}`
    );
    const item5 = utils.getByTestId(
      `description-${writeInsMock.result.data.writeIns.content[4].description}`
    );
    const item6 = utils.getByTestId(
      `description-${writeInsMock.result.data.writeIns.content[5].description}`
    );
    expect(item4).toBeTruthy();
    expect(item5).toBeTruthy();
    expect(item6).toBeTruthy();
    await act(async () => {
      const resolvedTab = utils.getByTestId('resolved-tab');
      fireEvent.press(resolvedTab);
    });

    /* checks list of write-ins in resolved tab */
    const item1 = utils.getByTestId(
      `description-${writeInsMock.result.data.writeIns.content[0].description}`
    );
    const item2 = utils.getByTestId(
      `description-${writeInsMock.result.data.writeIns.content[1].description}`
    );
    const item3 = utils.getByTestId(
      `description-${writeInsMock.result.data.writeIns.content[2].description}`
    );
    expect(item1).toBeTruthy();
    expect(item2).toBeTruthy();
    expect(item3).toBeTruthy();
  });

  it('should be able to navigate', async () => {
    const utils = render(navigator, [writeInsMock]);

    await act(async () => {
      await wait(10);
    });

    act(() => {
      const listItem = utils.getByTestId('J-ARCH-C15 - 13 Lb');
      fireEvent.press(listItem);
    });

    expect(mockedNavigate).toHaveBeenCalled();
  });

  /* verifies the search/filter input will display the correct items */
  it('should search/filter', async () => {
    const utils = render(navigator, [writeInsMock]);

    await act(async () => {
      await wait(10);
    });

    const searchInput = utils.getByTestId('search-input');
    await act(async () => {
      fireEvent.changeText(searchInput, 'J-ARCH-C');
    });
    const item4 = utils.getByTestId(
      `description-${writeInsMock.result.data.writeIns.content[3].description}`
    );
    const item5 = utils.getByTestId(
      `description-${writeInsMock.result.data.writeIns.content[4].description}`
    );
    expect(() => {
      utils.getByTestId(
        `description-${writeInsMock.result.data.writeIns.content[5].description}`
      );
    }).toThrow();
    expect(item4).toBeTruthy();
    expect(item5).toBeTruthy();
    await act(async () => {
      const resolvedTab = utils.getByTestId('resolved-tab');
      fireEvent.press(resolvedTab);
    });
    await act(async () => {
      fireEvent.changeText(searchInput, 'J-ARCH-A');
    });
    const item1 = utils.getByTestId(
      `description-${writeInsMock.result.data.writeIns.content[0].description}`
    );
    const item2 = utils.getByTestId(
      `description-${writeInsMock.result.data.writeIns.content[1].description}`
    );
    expect(() => {
      utils.getByTestId(
        `description-${writeInsMock.result.data.writeIns.content[2].description}`
      );
    }).toThrow();
    expect(item1).toBeTruthy();
    expect(item2).toBeTruthy();
  });
});
