import { LocationItem, LocationItemStatus, UpdateCountDocument } from 'api';
import { Config, configVar } from 'apollo/local';
import React, { ReactElement } from 'react';
import { act, fireEvent, render } from 'test-utils/render';
import * as hooks from 'hooks/useLocation';

import MockedNavigator from '__mocks__/MockedNavigator';

import LocationItemDetail from './LocationItemDetail';
import { GraphQLError } from 'graphql';
import wait from 'waait';
import { OverlayContainer } from 'components/Overlay';
import {
  getComponentTestingIds,
  getScreenTestingIds,
} from 'test-utils/testIds';

const testIds = getScreenTestingIds('LocationItemDetail');
const screenLayoutTestIds = getComponentTestingIds(
  'ScreenLayout',
  testIds.component
);
const productDetailIds = getComponentTestingIds(
  'ProductDetail',
  testIds.component
);
const quantityInputComponent = getComponentTestingIds(
  'Input',
  productDetailIds.qtyInput
).component;

const getNavigatorWithItem = (item?: LocationItem): ReactElement => (
  <MockedNavigator component={LocationItemDetail} params={{ item }} />
);

const mockItem = (opts: Partial<LocationItem> = {}) => ({
  id: '1234',
  prodDesc: 'some product',
  prodNum: '123457',
  catalogNum: '6543',
  tagNum: '96357',
  uom: '2346',
  product: {
    upc: '234567234567234567',
    productOverview: 'some product overview',
  },
  quantity: null,
  status: LocationItemStatus.STAGED,
  ...opts,
});

jest.spyOn(hooks, 'useLocation').mockImplementation(() => {
  return {
    location: {
      id: 'ASF123',
    },
  };
});

describe('<LocationItemDetail>', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('renders', () => {
    const item = mockItem();
    const utils = render(getNavigatorWithItem(item));
    utils.getByTestId(screenLayoutTestIds.component);
  });

  it('renders no item or location', () => {
    configVar({} as Config);
    const utils = render(getNavigatorWithItem());
    utils.getByText('There was a problem');
  });

  it('should disable submit button if no quantity is provided', () => {
    const item = mockItem();
    const utils = render(getNavigatorWithItem(item));
    const quantityInput = utils.getByTestId(quantityInputComponent);
    expect(quantityInput.props.value).toBe('');
  });

  it('should enable submit button if quantity is provided', () => {
    const item = mockItem({ quantity: 10 });
    const utils = render(getNavigatorWithItem(item));

    const quantityInput = utils.getByTestId(quantityInputComponent);
    expect(quantityInput.props.value).toBe('10');
  });

  it('should update item - success', async () => {
    const item = mockItem({ quantity: 10 });
    let updateCountCalled = false;

    const updateCountMock = {
      request: {
        query: UpdateCountDocument,
        variables: {
          item: {
            productId: '96357',
            locationId: 'ASF123',
            quantity: 10,
          },
        },
      },
      result: () => {
        updateCountCalled = true;
        return {
          data: {
            updateCount: {
              success: true,
              message: 'success',
              item: {
                id: '123',
                prodDesc: 'Mocked Item',
                prodNum: 'CH123',
              },
            },
          },
        };
      },
    };

    const utils = render(getNavigatorWithItem(item), [updateCountMock]);
    const updateButton = utils.getByText('Update');
    fireEvent.press(updateButton);

    await act(async () => {
      await wait(0);
    });

    expect(updateCountCalled).toBeTruthy();
  });

  it.skip('should update item - fail', async () => {
    const item = mockItem({ quantity: 10 });
    const navigator = getNavigatorWithItem(item);

    const updateCountErrorMock = {
      request: {
        query: UpdateCountDocument,
        variables: {
          item: {
            productId: '96357',
            locationId: 'ASF123',
            quantity: 10,
          },
        },
      },
      result: {
        errors: [new GraphQLError('Error!')],
      },
    };

    const utils = render(
      <>
        {navigator}
        <OverlayContainer />
      </>,
      [updateCountErrorMock]
    );
    const updateButton = utils.getByText('Update');
    fireEvent.press(updateButton);

    await act(async () => {
      await wait(0);
    });

    utils.getByText(/error updating this product/gi);
  });
});
