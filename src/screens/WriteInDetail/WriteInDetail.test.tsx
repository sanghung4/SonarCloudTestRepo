import React from 'react';
import wait from 'waait';
import { GraphQLError } from 'graphql';
import { ResolveWriteInDocument, WriteIn } from 'api';
import { act, render, fireEvent } from 'test-utils/render';
import MockedNavigator from '__mocks__/MockedNavigator';
import { Config } from 'apollo/local';

import WriteInDetail from '.';
import { OverlayContainer } from 'components/Overlay';
import { useNavigation, useRoute } from '@react-navigation/native';

const mockedGoBack = jest.fn();

const mockParams: { writeIn: WriteIn } = {
  writeIn: {
    id: '729ad920-d973-4ef1-ac57-32dab0c3e6f6',
    locationId: 'J-ARCH-B17',
    uom: 'SqFt',
    quantity: 95,
    description:
      'Donec quis orci eget orci vehicula condimentum. Curabitur in libero ut massa volutpat convallis. Morbi odio odio, elementum eu, interdum eu, tincidunt in, leo. Maecenas pulvinar lobortis est.',
    status: 'Resolved',
    catalogNum: '5-45731-3',
    upcNum: '48339-86838',
    comment: 'Maecenas ut massa quis augue luctus tincidunt.',
    createdBy: 'Clayborn Espinheira',
    createdAt: '2021-01-22T19:53:07Z',
    updatedBy: 'Clayborn Espinheira',
    updatedAt: '2021-01-22T19:53:07Z',
  },
};

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

const navigator = (
  <MockedNavigator
    component={() => (
      <WriteInDetail
        navigation={{ ...useNavigation(), goBack: mockedGoBack }}
        route={{ ...useRoute(), params: mockParams }}
      />
    )}
  />
);

const resolveWriteInMock = {
  request: {
    query: ResolveWriteInDocument,
    variables: {
      id: '729ad920-d973-4ef1-ac57-32dab0c3e6f6',
    },
  },
  result: {
    data: {
      resolveWriteIn: {
        success: true,
        message: 'successfully resolved write-in',
      },
    },
  },
};

const resolveWriteInFailMock = {
  request: {
    query: ResolveWriteInDocument,
    variables: {
      id: '729ad920-d973-4ef1-ac57-32dab0c3e6f6',
    },
  },
  result: {
    data: {
      resolveWriteIn: {
        success: false,
        message: 'failed to resolve write-in',
      },
    },
  },
};

const resolveWriteInErrorMock = {
  request: {
    query: ResolveWriteInDocument,
    variables: {
      id: '729ad920-d973-4ef1-ac57-32dab0c3e6f6',
    },
  },
  result: {
    errors: [new GraphQLError('Error!')],
  },
};

describe.skip('<WriteInDetail>', () => {
  beforeEach(jest.clearAllMocks);

  it('should render', () => {
    const utils = render(navigator);
    utils.getByText('Write-In Details');
  });

  it('should be readonly inputs', () => {
    const utils = render(navigator);

    const catalogNumInput = utils.getByTestId('catalogNum');
    const upcNumInput = utils.getByTestId('upcNum');
    const descriptionInput = utils.getByTestId('description');
    const locationIdInput = utils.getByTestId('locationId');
    const uomInput = utils.getByTestId('uom');
    const quantityInput = utils.getByTestId('quantity');
    const createdByInput = utils.getByTestId('createdBy');
    const commentInput = utils.getByTestId('comment');

    expect(catalogNumInput.props.editable).toEqual(false);
    expect(upcNumInput.props.editable).toEqual(false);
    expect(descriptionInput.props.editable).toEqual(false);
    expect(locationIdInput.props.editable).toEqual(false);
    expect(uomInput.props.editable).toEqual(false);
    expect(quantityInput.props.editable).toEqual(false);
    expect(createdByInput.props.editable).toEqual(false);
    expect(commentInput.props.editable).toEqual(false);
  });

  it('should submit - success', async () => {
    const utils = render(navigator, [resolveWriteInMock]);

    act(() => {
      fireEvent.press(utils.getByTestId('pageButton'));
    });

    await act(async () => {
      await wait();
    });

    expect(mockedGoBack).toHaveBeenCalled();
  });

  it('should submit - fail', async () => {
    const utils = render(
      <>
        {navigator}
        <OverlayContainer />
      </>,
      [resolveWriteInFailMock]
    );

    act(() => {
      fireEvent.press(utils.getByTestId('pageButton'));
    });

    await act(async () => {
      await wait();
    });

    utils.getByText(/error resolving this write-in/gi);
  });

  it('should submit - error', async () => {
    const utils = render(
      <>
        {navigator}
        <OverlayContainer />
      </>,
      [resolveWriteInErrorMock]
    );

    act(() => {
      fireEvent.press(utils.getByTestId('pageButton'));
    });

    await act(async () => {
      await wait();
    });

    utils.getByText(/error resolving this write-in/gi);
  });
});
