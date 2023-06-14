import React from 'react';
import { fireEvent, render } from 'test-utils/render';
import MockedNavigator from '__mocks__/MockedNavigator';
import GetStarted from './GetStarted';
import * as auth from 'providers/Auth';
import { RouteNames } from 'constants/routes';

const mockedNavigate = jest.fn();

jest.mock('hooks/useConfig', () => ({
  useConfig: () => [{ count: { erpSystem: 'ECLIPSE' } }],
}));

jest.mock('@react-navigation/native', () => {
  const actualNav = jest.requireActual('@react-navigation/native');
  return {
    ...actualNav,
    useNavigation: () => ({
      navigate: mockedNavigate,
    }),
  };
});

const navigator = <MockedNavigator component={() => <GetStarted />} />;

const getManager = () => ({ isManager: true } as auth.IAuthContext);

describe('<GetStarted>', () => {
  beforeEach(jest.clearAllMocks);

  it('renders', () => {
    const utils = render(navigator);
    expect(utils.getByText('Get Started')).toBeTruthy();
  });

  it('should be able to navigate to Scan Location', () => {
    jest.spyOn(auth, 'useAuth').mockImplementationOnce(getManager);
    const utils = render(navigator);
    fireEvent.press(utils.getByText('View Variance Summary'));
    expect(mockedNavigate).toHaveBeenCalledWith(RouteNames.VARIANCE_SUMMARY);
  });
});
