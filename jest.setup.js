import mockRNDeviceInfo from 'react-native-device-info/jest/react-native-device-info-mock';

jest.mock('react-native-device-info', () => mockRNDeviceInfo);
jest.mock('react-native/Libraries/EventEmitter/NativeEventEmitter');
jest.mock('@react-native-firebase/crashlytics', () => {
  return {
    // Define mock functions for the Firebase crashlytics methods used in code
    // For example:
    crashlytics: jest.fn(() => {
      return {
        setCrashlyticsCollectionEnabled: jest.fn(),
        isCrashlyticsCollectionEnabled: jest.fn(),
        log: jest.fn(),
        setAttributes: jest.fn(),
        setUserId: jest.fn(),
        recordError: jest.fn(),
        crash: jest.fn(),
        // ...
      };
    }),
    // ...
  };
});

jest.mock('@react-native-firebase/analytics', () => {
  return {
    // Define mock functions for the Firebase analytics methods used in code
    // For example:
    analytics: jest.fn(() => {
      return {
        logEvent: jest.fn(),
        logScreenView: jest.fn(),
        // ...
      };
    }),
    // ...
  };
});

jest.mock('@react-native-firebase/perf', () => {
  return {
    // Define mock functions for the Firebase perf methods used in code
    // For example:
    perf: jest.fn(() => {
      return {
        startTrace: jest.fn(),
        newHttpMetric: jest.fn(),
        startScreenTrace: jest.fn(),
        startTrace: jest.fn(),
        // ...
      };
    }),
    // ...
  };
});
