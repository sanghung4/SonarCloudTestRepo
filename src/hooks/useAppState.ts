import { useState, useEffect } from 'react';
import { AppState, AppStateStatus } from 'react-native';

export const useAppState = (
  onChange?: (nextAppState: AppStateStatus) => void
): AppStateStatus => {
  const [appState, setAppState] = useState(AppState.currentState);

  useEffect(() => {
    const onAppStateChange = (nextAppState: AppStateStatus) => {
      onChange?.(nextAppState);
      setAppState(nextAppState);
    };

    AppState.addEventListener('change', onAppStateChange);

    return () => {
      AppState.removeEventListener('change', onAppStateChange);
    };
  }, [onChange]);

  return appState;
};
