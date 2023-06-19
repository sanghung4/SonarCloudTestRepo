import { transform } from 'lodash';
import { getBundleId } from 'react-native-device-info';
import { Platform } from 'react-native';
import { ComponentTestIds, ScreenTestIds } from 'constants/testIds';

export const appBundleId = getBundleId();

type TestScreenNames = keyof typeof ScreenTestIds;
type TestScreenIds<T extends TestScreenNames> = typeof ScreenTestIds[T];

type TestComponentNames = keyof typeof ComponentTestIds;
type TestComponentIds<
  T extends TestComponentNames
> = typeof ComponentTestIds[T];

// Generates formatted test ids object for screens
export const getScreenTestingIds = <T extends TestScreenNames>(
  screenName: T
) => {
  const isAndroid = Platform.OS === 'android';

  return transform<TestScreenIds<T>, { [key in keyof TestScreenIds<T>]: any }>(
    ScreenTestIds[screenName],
    (result, value, key) => {
      result[key] = isAndroid ? `${appBundleId}:id/${value}` : value;
    }
  );
};

// Generates formatted test ids object for components
export const getComponentTestingIds = <T extends TestComponentNames>(
  componentName: T,
  testID?: string
) => {
  const isAndroid = Platform.OS === 'android';
  return transform<
    TestComponentIds<T>,
    { [key in keyof TestComponentIds<T>]: any }
  >(ComponentTestIds[componentName], (result, value, key) => {
    const finalTestId: any = testID ? `${testID}-${value}` : value;
    result[key] = (isAndroid && !finalTestId.includes(appBundleId)) ? `${appBundleId}:id/${finalTestId}` : `${finalTestId}`;
  });
};

// Formats an testid
export const formatTestId = (
  testID: string
) => {
  const isAndroid = Platform.OS === 'android';
  return (isAndroid && !testID.includes(appBundleId)) ? `${appBundleId}:id/${testID}` : `${testID}`;
};

// Generates test id for specific component (intended for unit testing)
export const getTestId = <T extends TestScreenNames>(
  screenName: T,
  screenComponentName: keyof TestScreenIds<T>,
  componentName: TestComponentNames,
  iterator?: string | number
) => {
  return iterator
    ? `${ScreenTestIds[screenName][screenComponentName]}-${iterator}-${ComponentTestIds[componentName].component}`
    : `${ScreenTestIds[screenName][screenComponentName]}-${ComponentTestIds[componentName].component}`;
};
