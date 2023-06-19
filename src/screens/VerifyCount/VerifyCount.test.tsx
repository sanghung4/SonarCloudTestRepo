import React from 'react';

import MockedNavigator from '__mocks__/MockedNavigator';
import { GetCountDocument } from 'api';
import { EclipseErrorCode, ErrorCode } from 'constants/error';
import { GraphQLError } from 'graphql';
import { render, fireEvent, waitFor } from 'test-utils/render';

import { OverlayContainer } from 'components/Overlay';
import { RouteNames } from 'constants/routes';
import VerifyCount from './VerifyCount';
import { FormKey } from './types';
import { useNavigation, useRoute } from '@react-navigation/native';
import { getTestId } from 'test-utils/testIds';

// Create TestIDs for components
const branchInputTestId = getTestId('VerifyCount', 'branchInput', 'Input');
const countInputTestId = getTestId('VerifyCount', 'countInput', 'Input');

const mockedNavigate = jest.fn();

const navigator = (
  <MockedNavigator
    component={() => (
      <VerifyCount
        route={useRoute()}
        navigation={{ ...useNavigation(), navigate: mockedNavigate }}
      />
    )}
  />
);

const countMock = {
  request: {
    query: GetCountDocument,
    variables: {
      id: '321',
      branchId: '123',
    },
  },
  result: {
    data: {
      count: {
        id: '321',
        erpSystem: 'ECLIPSE',
        branch: {
          id: '123',
          name: 'Mocked Warehouse',
        },
      },
    },
  },
};

const countEclipseErrorMock = {
  request: {
    query: GetCountDocument,
    variables: {
      id: '321',
      branchId: '123',
    },
  },
  result: {
    errors: [
      new GraphQLError(
        '',
        undefined,
        undefined,
        undefined,
        undefined,
        undefined,
        {
          code: ErrorCode.FORBIDDEN,
          response: {
            body: {
              errors: [
                {
                  code: EclipseErrorCode.INVALID_ECLIPSE_CREDENTIALS,
                },
              ],
            },
          },
        }
      ),
    ],
  },
};

const countErrorMock = {
  request: {
    query: GetCountDocument,
    variables: {
      id: '321',
      branchId: '123',
    },
  },
  result: {
    errors: [new GraphQLError('Error!')],
  },
};

describe('<VerifyCount>', () => {
  beforeEach(jest.clearAllMocks);

  it('renders', async () => {
    const utils = render(navigator);
    await utils.findByText('Count ID Entry');
  });

  it('should disable submit button if inputs are not provided', async () => {
    const utils = render(navigator);
    const button = await utils.findByRole('button');
    expect(button).toBeDisabled();
  });

  it('should render an error message for no branch id', async () => {
    const utils = render(navigator);

    const branchIdInput = utils.getByTestId(branchInputTestId);
    fireEvent.changeText(branchIdInput, '');

    await utils.findByText('Branch ID is required');
  });

  it('should render an error message for no count id', async () => {
    const utils = render(navigator);

    const countIdInput = utils.getByTestId(countInputTestId);
    fireEvent.changeText(countIdInput, '');

    await utils.findByText('Cycle / Count ID is required');
  });

  it('should submit - success', async () => {
    const utils = render(navigator, [countMock]);

    const branchIdInput = utils.getByTestId(branchInputTestId);
    fireEvent.changeText(branchIdInput, '123');

    const countIdInput = utils.getByTestId(countInputTestId);
    fireEvent.changeText(countIdInput, '321');

    const submitButton = utils.getByRole('button');
    fireEvent.press(submitButton);

    await waitFor(() =>
      expect(mockedNavigate).toHaveBeenCalledWith(RouteNames.GET_STARTED)
    );
  });

  it.skip('should submit - error', async () => {
    const utils = render(
      <>
        {navigator}
        <OverlayContainer />
      </>,
      [countErrorMock]
    );
    const branchIdInput = utils.getByTestId(FormKey.BRANCH_ID);
    fireEvent.changeText(branchIdInput, '123');

    const countIdInput = utils.getByTestId(FormKey.COUNT_ID);
    fireEvent.changeText(countIdInput, '321');

    const submitButton = utils.getByRole('button');
    fireEvent.press(submitButton);

    await utils.findByText(/error finding your branch/gi);
  });
});
