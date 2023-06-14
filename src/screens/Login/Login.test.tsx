import React from 'react';
import { fireEvent, render } from 'test-utils/render';
import MockedNavigator from '__mocks__/MockedNavigator';
import Login from './Login';

const navigator = <MockedNavigator component={Login} />;

const mockLoginFn = jest.fn();

jest.mock('providers/Auth', () => ({
  useAuth: jest.fn(() => ({
    login: mockLoginFn,
  })),
}));

describe('<Login>', () => {
  it('renders', () => {
    const { getByText } = render(navigator);
    getByText('Branch Inventory');
  });

  it('handles login', () => {
    const { getByText } = render(navigator);
    fireEvent.press(getByText('Login with Okta'));
    expect(mockLoginFn).toHaveBeenCalled();
  });
});
