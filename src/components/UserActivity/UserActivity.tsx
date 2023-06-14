/* eslint-disable react-hooks/exhaustive-deps */
import React, { useEffect, useMemo, useState } from 'react';
import { Keyboard, PanResponder, View } from 'react-native';
import { useLazyTimeout } from 'hooks/useTimeout';
import { getComponentTestingIds } from 'test-utils/testIds';

export interface UserActivityProps {
  timeoutHandler: () => void;
  track: boolean;
  duration: number;
  children?: React.ReactNode;
  testID?: string | undefined;
}

function UserActivity({
  timeoutHandler,
  track: trackProp,
  duration,
  children,
  testID,
}: UserActivityProps): JSX.Element {
  const onActivity = () => {
    if (track) {
      timer.start();
    }
    return false;
  };

  const onExpired = () => {
    timeoutHandler();
    timer.clear();
    setTrack(false);
  };

  const [track, setTrack] = useState(trackProp);
  const timer = useLazyTimeout(onExpired, duration);

  const panResponder = useMemo(
    () =>
      PanResponder.create({
        onMoveShouldSetPanResponderCapture: onActivity,
        onPanResponderTerminationRequest: onActivity,
        onStartShouldSetPanResponderCapture: onActivity,
      }),
    []
  );

  useEffect(() => {
    const hideEvent = Keyboard.addListener('keyboardDidHide', onActivity);
    const showEvent = Keyboard.addListener('keyboardDidShow', onActivity);

    return () => {
      hideEvent.remove();
      showEvent.remove();
    };
  }, []);

  useEffect(() => {
    setTrack(trackProp);
  }, [trackProp]);

  useEffect(() => {
    if (track) {
      timer.start();
    } else {
      timer.clear();
    }
  }, [track]);

  const testIds = getComponentTestingIds('UserActivity', testID);

  return (
    <View
      testID={testIds.component}
      collapsable={false}
      style={styles.container}
      {...panResponder.panHandlers}
    >
      {children}
    </View>
  );
}

const styles = {
  container: {
    flex: 1,
  },
} as const;

export default UserActivity;
