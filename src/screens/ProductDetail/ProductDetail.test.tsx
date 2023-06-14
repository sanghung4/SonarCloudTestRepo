import React from 'react';
import { AddToCountDocument, ErpSystem } from 'api';
import { render } from 'test-utils/render';
import * as configHooks from 'hooks/useConfig';
import * as locationHooks from 'hooks/useLocation';
import MockedNavigator from '__mocks__/MockedNavigator';
import { Alert as SystemAlert } from 'react-native';

import ProductDetail from './ProductDetail';
import { OverlayContainer } from 'components/Overlay';

const mockedNavigate = jest.fn();

jest.spyOn(SystemAlert, 'alert');

jest.mock('@react-navigation/native', () => {
  const actualNav = jest.requireActual('@react-navigation/native');
  return {
    ...actualNav,
    useNavigation: () => ({
      navigate: mockedNavigate,
    }),
    useRoute: () => ({
      params: {
        product: {
          id: 'CH123',
          productNumber: 'CH123',
          name: 'Mocked Product',
          imageUrls: '',
          stock: {
            homeBranch: {
              branchName: 'Mocked Primary Branch',
              availability: 0,
            },
            otherBranches: [
              {
                branchName: 'Mocked Secondary Branch',
                availability: 2,
              },
            ],
          },
          manufacturerNumber: '12398314',
          upc: 'CDD12345234',
          productOverview: 'Mocked Product Description',
        },
      },
    }),
  };
});

const navigator = <MockedNavigator component={ProductDetail} />;

const addToCountMock = {
  request: {
    query: AddToCountDocument,
    variables: {
      item: {
        productId: 'CH123',
        locationId: 'DF626',
        quantity: 0,
      },
    },
  },
  result: {
    data: {
      addToCount: {
        success: true,
        message: 'success',
        item: {
          id: '123',
          prodDesc: 'Mocked Item',
          prodNum: 'CH123',
        },
      },
    },
  },
};

const countPrimary = {
  id: 'AB345',
  erpSystem: ErpSystem.MINCRON,
  branch: {
    id: 'CD435',
    name: 'Mocked Primary Branch',
  },
};

const locationNoMatch = {
  id: 'DF626',
  items: [
    {
      id: 'AB653',
      prodNum: '123',
      prodDesc: 'Mocked Item',
    },
  ],
};

describe('<ProductDetail>', () => {
  beforeEach(jest.clearAllMocks);

  it('renders problem', () => {
    const utils = render(navigator);
    utils.getByText('There was a problem');
  });

  it.skip('should disable submit button if quantity is not provided', () => {
    jest
      .spyOn(configHooks, 'useConfig')
      .mockImplementation(() => [{ count: countPrimary }, () => {}]);

    jest.spyOn(locationHooks, 'useLocation').mockImplementation(() => {
      return {
        location: locationNoMatch,
      };
    });

    const utils = render(
      <>
        {navigator}
        <OverlayContainer />
      </>,
      [addToCountMock]
    );

    const addButton = utils.getByText('Add');
    expect(addButton).toBeDisabled();
  });

  // it.skip('should add as item - success', async () => {
  //   jest.spyOn(configHooks, 'useConfig').mockImplementation(() => [
  //     {
  //       count: countPrimary,
  //     },
  //     () => {},
  //   ]);

  //   jest.spyOn(locationHooks, 'useLocation').mockImplementation(() => {
  //     return {
  //       location: locationNoMatch,
  //     };
  //   });

  //   const utils = render(
  //     <>
  //       {navigator}
  //       <OverlayContainer />
  //     </>,
  //     [addToCountMock]
  //   );
  //   const addButton = utils.getByText('Add this Item to my Current Location');
  //   fireEvent.press(addButton);

  //   const confirmAddButton = utils.getByText('Yes, Add Product');
  //   fireEvent.press(confirmAddButton);

  //   await act(async () => {
  //     await wait(0);
  //   });

  //   expect(mockedNavigate).toHaveBeenCalled();
  // });

  // it.skip('should add as item - fail', async () => {
  //   jest.spyOn(hooks, 'useConfig').mockImplementation(() => [
  //     {
  //       count: countPrimary,
  //       location: locationNoMatch,
  //     },
  //     () => {},
  //   ]);
  //   const utils = render(
  //     <>
  //       {navigator}
  //       <OverlayContainer />
  //     </>,
  //     [addToCountErrorMock]
  //   );
  //   const addButton = utils.getByText('Add this Item to my Current Location');
  //   fireEvent.press(addButton);

  //   const confirmAddButton = utils.getByText('Yes, Add Product');
  //   fireEvent.press(confirmAddButton);

  //   await act(async () => {
  //     await wait(0);
  //   });

  //   utils.getByText(/error adding the product/gi);
  // });
});
