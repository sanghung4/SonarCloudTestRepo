import React from 'react';
import { render, act, fireEvent, RenderAPI } from 'test-utils/render';
import wait from 'waait';

import MockedNavigator from '__mocks__/MockedNavigator';
import { UpdateWriteInDocument, WriteIn } from 'api';
import { GraphQLError } from 'graphql';

import { ALERT_OVERLAY_TEST_ID } from 'constants/overlays';

import UpdateWriteIn from '.';
import { OverlayContainer } from 'components/Overlay';
import { useNavigation, useRoute } from '@react-navigation/native';

const mockParams: { writeIn: WriteIn } = {
  writeIn: {
    id: '23ASZF1',
    locationId: 'J-ARCH-B17',
    uom: 'SqFt',
    quantity: 95,
    description: 'Donec quis orci eget orci vehicula condimentum.',
    catalogNum: '5-45731-3',
    upcNum: '48339-86838',
    comment: 'Maecenas ut massa quis augue luctus tincidunt.',
    createdAt: '',
    createdBy: '',
    resolved: false,
    updatedAt: '',
    updatedBy: '',
  },
};

const navigator = (
  <MockedNavigator
    component={() => (
      <UpdateWriteIn
        navigation={useNavigation()}
        route={{ ...useRoute(), params: { ...mockParams } }}
      />
    )}
  />
);

const id = '23ASZF1';

const writeIn = {
  locationId: 'J-ARCH-B17',
  uom: 'EA', // updated field
  quantity: 95,
  description: 'Donec quis orci eget orci vehicula condimentum.',
  catalogNum: '5-45731-3',
  upcNum: '48339-86838',
  comment: 'Maecenas ut massa quis augue luctus tincidunt.',
};

let successCalled = false;
const writeInMock = {
  request: {
    query: UpdateWriteInDocument,
    variables: { id, writeIn },
  },
  result: () => {
    successCalled = true;
    return {
      data: {
        updateWriteIn: {
          success: true,
          message: 'successfully updated write-in',
        },
      },
    };
  },
};

const writeInFailMock = {
  request: {
    query: UpdateWriteInDocument,
    variables: { id, writeIn },
  },
  result: {
    data: {
      updateWriteIn: {
        success: false,
        message: 'failed to update write-in',
      },
    },
  },
};

const writeInErrorMock = {
  request: {
    query: UpdateWriteInDocument,
    variables: { writeIn },
  },
  result: {
    errors: [new GraphQLError('Error!')],
  },
};

const changeForm = (renderAPI: RenderAPI) => {
  const uomField = renderAPI.getByTestId('uom');

  act(() => {
    fireEvent.changeText(uomField, writeIn.uom);
  });
};

describe('<UpdateWriteIn>', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    successCalled = false;
  });

  it('renders', () => {
    const { getByText } = render(navigator);
    getByText('Update Write-In');
  });

  it('should update write-in - success', async () => {
    const utils = render(navigator, [writeInMock]);
    changeForm(utils);

    act(() => {
      fireEvent.press(utils.getByRole('button'));
    });

    await act(async () => {
      await wait();
    });

    expect(successCalled).toBeTruthy();
  });

  it.skip('should update write-in - failure', async () => {
    const utils = render(
      <>
        {navigator}
        <OverlayContainer />
      </>,
      [writeInFailMock]
    );
    changeForm(utils);

    act(() => {
      fireEvent.press(utils.getByRole('button'));
    });

    await act(async () => {
      await wait();
    });

    utils.getByTestId(ALERT_OVERLAY_TEST_ID);
  });

  it.skip('should update write-in - error', async () => {
    const utils = render(
      <>
        {navigator}
        <OverlayContainer />
      </>,
      [writeInErrorMock]
    );
    changeForm(utils);

    act(() => {
      fireEvent.press(utils.getByRole('button'));
    });

    await act(async () => {
      await wait();
    });

    utils.getByTestId(ALERT_OVERLAY_TEST_ID);
  });
});
