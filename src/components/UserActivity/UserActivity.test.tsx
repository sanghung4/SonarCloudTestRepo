import React from 'react';
import { DeviceEventEmitter } from 'react-native';
import { act, render } from 'test-utils/render';

import UserActivity, { UserActivityProps } from './UserActivity';

jest.useFakeTimers();

const mockProps = (opts: Partial<UserActivityProps> = {}) => ({
  timeoutHandler: jest.fn(),
  track: true,
  duration: 6000,
  ...opts,
});

describe('<UserActivity>', () => {
  beforeEach(jest.clearAllMocks);

  it('should call timeout handler', () => {
    const props = mockProps();
    render(<UserActivity {...props} />);
    act(jest.runAllTimers);
    expect(props.timeoutHandler).toHaveBeenCalled();
  });

  it('should not call timeout handler', () => {
    const props = mockProps({ track: false });
    render(<UserActivity {...props} />);
    act(jest.runAllTimers);
    expect(props.timeoutHandler).not.toHaveBeenCalled();
  });

  it('should restart timer on activity', () => {
    const props = mockProps();
    render(<UserActivity {...props} />);

    act(() => {
      jest.advanceTimersByTime(3000);
    });

    act(() => {
      DeviceEventEmitter.emit('keyboardDidShow');
      // Keyboard.emit('keyboardDidShow', {});
    });

    act(() => {
      jest.advanceTimersByTime(3000);
    });

    expect(props.timeoutHandler).not.toHaveBeenCalled();

    act(() => {
      jest.advanceTimersByTime(3000);
    });

    expect(props.timeoutHandler).toHaveBeenCalled();
  });
});
