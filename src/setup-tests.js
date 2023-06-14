import '@testing-library/jest-native/extend-expect';
import { NativeModules } from 'react-native';
import Enzyme from 'enzyme';
import Adapter from '@wojtekmaj/enzyme-adapter-react-17';
import MockAsyncStorage from 'mock-async-storage';

if (typeof window !== 'object') {
  global.window = global;
  global.Blob = (content, options) => ({ content, options });
}

Enzyme.configure({ adapter: new Adapter() });

global.XMLHttpRequest = jest.fn();
global.fetch = jest.fn();

if (typeof window !== 'object') {
  global.window = global;
  global.window.navigator = {};
}

jest.mock('react-native/Libraries/LayoutAnimation/LayoutAnimation');
jest.mock('react-native/Libraries/Animated/NativeAnimatedHelper');
jest.mock('react-native/Libraries/Animated/animations/TimingAnimation');

jest.mock('react-native-reanimated', () => {
  const Reanimated = require('react-native-reanimated/mock');

  // The mock for `call` immediately calls the callback which is incorrect
  // So we override it with a no-op
  Reanimated.default.call = () => {};

  return Reanimated;
});

const mockAsyncStorage = new MockAsyncStorage();
jest.mock('@react-native-async-storage/async-storage', () => mockAsyncStorage);

NativeModules.OktaSdkBridge = {
  createConfig: jest.fn(),
  signIn: jest.fn(),
  signOut: jest.fn(),
  getAccessToken: jest.fn(),
  getIdToken: jest.fn(),
  getUser: jest.fn(),
  isAuthenticated: jest.fn(),
  revokeAccessToken: jest.fn(),
  revokeIdToken: jest.fn(),
  revokeRefreshToken: jest.fn(),
  introspectAccessToken: jest.fn(),
  introspectIdToken: jest.fn(),
  introspectRefreshToken: jest.fn(),
  refreshTokens: jest.fn(),
};

jest.mock('react-native-gesture-handler', () => {
  const View = require('react-native/Libraries/Components/View/View');
  return {
    Swipeable: View,
    DrawerLayout: View,
    State: {},
    ScrollView: View,
    Slider: View,
    Switch: View,
    TextInput: View,
    ToolbarAndroid: View,
    ViewPagerAndroid: View,
    DrawerLayoutAndroid: View,
    WebView: View,
    NativeViewGestureHandler: View,
    TapGestureHandler: View,
    FlingGestureHandler: View,
    ForceTouchGestureHandler: View,
    LongPressGestureHandler: View,
    PanGestureHandler: View,
    PinchGestureHandler: View,
    RotationGestureHandler: View,
    /* Buttons */
    RawButton: View,
    BaseButton: View,
    RectButton: View,
    BorderlessButton: View,
    /* Other */
    FlatList: View,
    gestureHandlerRootHOC: jest.fn(),
    Directions: {},
  };
});

jest.mock('react-native-keyboard-aware-scroll-view', () => {
  const KeyboardAwareScrollView = ({ children }) => children;
  return { KeyboardAwareScrollView };
});

NativeModules.Linking = {
  canOpenUrl: jest.fn().mockResolvedValue(true),
  openUrl: jest.fn().mockResolvedValue(true),
};
