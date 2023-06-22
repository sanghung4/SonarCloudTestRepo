import { useCallback, useEffect, useState } from 'react';

// https://reacthooks.dev/useGeolocation/

export enum GeolocationMode {
  SINGLE = 'single',
  WATCH = 'watch'
}

type GeolocationCoordinates = {
  accuracy: number | null;
  altitude: number | null;
  altitudeAccuracy: number | null;
  heading: number | null;
  latitude: number | null;
  longitude: number | null;
  speed: number | null;
};

export type GeolocationResponse = {
  coords: GeolocationCoordinates;
  timestamp: number;
};

export type GeolocationError = {};

export interface IPositionState {
  position: GeolocationResponse | null;
  positionError: GeolocationError | null;
  positionLoading: boolean;
  previousPositions: Array<GeolocationResponse | null> | null;
}

export function useGeolocation({
  skip = false,
  mode = GeolocationMode.SINGLE,
  stop = false,
  timeout = 12000,
  maximumAge = 60000,
  enableHighAccuracy = true
}) {
  const [positionState, setPositionState] = useState<IPositionState>({
    position: null,
    positionError: null,
    positionLoading: true,
    previousPositions: []
  });

  const onGeolocationSuccess = useCallback(
    (position) => {
      if (!stop) {
        // Reason why this coverage is ignored is because that it is impossible to update the
        //  mocked value of `navigator.geolocation` in mid test to use `oldState.previousPositions`
        /* istanbul ignore next */
        setPositionState((oldState) => ({
          ...oldState,
          position,
          positionLoading: false,
          previousPositions:
            mode === GeolocationMode.SINGLE
              ? [oldState.position]
              : [
                  ...(oldState.previousPositions
                    ? oldState.previousPositions
                    : []),
                  oldState.position
                ]
        }));
      }
    },
    [setPositionState, stop, mode]
  );

  const onGeolocationError = useCallback(
    (error) =>
      setPositionState((oldState) => ({
        ...oldState,
        positionLoading: false,
        positionError: error
      })),
    [setPositionState]
  );

  useEffect(() => {
    const config = {
      timeout,
      maximumAge,
      enableHighAccuracy
    };

    if (!skip) {
      if (navigator.geolocation) {
        if (mode === GeolocationMode.SINGLE) {
          navigator.geolocation.getCurrentPosition(
            onGeolocationSuccess,
            onGeolocationError,
            config
          );
        } else {
          navigator.geolocation.watchPosition(
            onGeolocationSuccess,
            onGeolocationError,
            config
          );
        }
      }
    }
  }, [
    skip,
    mode,
    stop,
    timeout,
    maximumAge,
    enableHighAccuracy,
    onGeolocationError,
    onGeolocationSuccess
  ]);

  return positionState;
}
