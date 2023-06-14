import { GetCountDocument, GetLocationDocument } from 'api';
import { OverlayContainer } from 'components/Overlay';
import React from 'react';
import { render } from 'test-utils/render';
import MockedNavigator from '__mocks__/MockedNavigator';
import ScanLocation from './ScanLocation';
import { LOADING_OVERLAY_TEST_ID } from '../../constants/overlays';

jest.mock('react-native/Libraries/AppState/AppState', () => ({
  addEventListener: jest.fn((e, cb) => {
    cb('active');
  }),
  removeEventListener: jest.fn(),
}));

jest.mock('hooks/useLocation', () => ({
  useLocation: () => ({ getLocation: () => {}, loading: true }),
}));

const navigator = <MockedNavigator component={ScanLocation} />;

const countMock = {
  request: {
    query: GetCountDocument,
    variables: {
      branchId: 1,
      countId: 1,
    },
  },
  result: {
    data: {
      count: {
        id: 1,
        name: 'Mocked Warehouse',
      },
    },
  },
};

const locationMock = {
  request: {
    query: GetLocationDocument,
    variables: {
      code: '1234',
    },
  },
  result: {
    data: {
      location: {
        id: 1,
        code: '1234',
        products: [],
      },
    },
  },
};

describe('<ScanLocation>', () => {
  it.skip('renders loading', () => {
    const utils = render(
      <>
        {navigator}
        <OverlayContainer />
      </>,
      [countMock, locationMock]
    );

    expect(utils.getByTestId(LOADING_OVERLAY_TEST_ID)).toBeDefined();
  });
});
