import { act, fireEvent } from '@testing-library/react';

import NavigationAlert, {
  NavigationAlertProps,
  subNavigationAlert
} from 'common/Alerts/NavigationAlert';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';
import { ChildData } from 'react-router-navigation-prompt';

const mockProps: NavigationAlertProps = {
  title: 'test',
  message: 'test',
  confirmBtnTitle: 'test',
  onConfirm: jest.fn(),
  when: false
};
const t = jest.fn();
const mockChildData: ChildData = {
  isActive: true,
  onCancel: jest.fn(),
  onConfirm: jest.fn()
};
describe('Common - Alerts - Navigation Alert', () => {
  it('Expect NavigationAlert to match snapshot of rendering nothing', async () => {
    setBreakpoint('desktop');
    const wrapper = render(<NavigationAlert {...mockProps} />);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(wrapper).toMatchSnapshot();
  });
  it('Expect subNavigationAlert to match snapshot of rendering nothing', async () => {
    setBreakpoint('desktop');
    const wrapper = render(
      subNavigationAlert({ ...mockProps, t })(mockChildData)
    );
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(wrapper).toMatchSnapshot();
  });

  it('Expect NavigationAlert to matche Snapshot of rendering the Alert', async () => {
    setBreakpoint('desktop');
    const wrapper = render(
      subNavigationAlert({ ...mockProps, t })(mockChildData)
    );
    await act(() => new Promise((res) => setTimeout(res, 100)));
    expect(wrapper).toMatchSnapshot();
  });

  it('Expect NavigationAlert to call prop functions by clicking buttons', async () => {
    setBreakpoint('desktop');
    const [onConfirm, onCancel] = [jest.fn(), jest.fn()];
    const { getByTestId } = render(
      subNavigationAlert({ ...mockProps, t, onCancel, onConfirm })(
        mockChildData
      )
    );
    await act(() => new Promise((res) => setTimeout(res, 100)));
    fireEvent.click(getByTestId('alert-confirm-button'));
    expect(onConfirm).toBeCalled();
    fireEvent.click(getByTestId('alert-cancel-button'));
    expect(onCancel).toBeCalled();
  });
});
