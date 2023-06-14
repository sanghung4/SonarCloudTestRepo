import React, { useEffect } from 'react';
import { createStackNavigator } from '@react-navigation/stack';
import { createDrawerNavigator } from '@react-navigation/drawer';
import SplashScreen from 'react-native-splash-screen';
import { useAuth } from 'providers/Auth';
import { DrawerContent } from 'components/Drawer';
import { logError } from 'utils/error';
import { defaultOptions } from 'navigation/options';

import { Login } from 'screens/Login';
import { VerifyCount } from 'screens/VerifyCount';
import { ManualLocationEntry } from 'screens/ManualLocationEntry';
import { LocationItems } from 'screens/LocationItems';
import { LocationItemDetail } from 'screens/LocationItemDetail';
import { ProductDetail } from 'screens/ProductDetail';
import { ScanLocation } from 'screens/ScanLocation';
import { BranchSummaries } from 'screens/BranchSummaries';
import { WriteIns } from 'screens/WriteIns';
import ErrorLog from 'screens/ErrorLog';
import WriteInDetail from 'screens/WriteInDetail';
import UpdateWriteIn from 'screens/UpdateWriteIn';
import { RouteNames } from 'constants/routes';
import { CountLocations } from 'screens/CountLocations';
import { FoundProduct } from 'screens/FoundProduct';
import {
  AppRoute,
  DrawerRoute,
  AuthenticatedRoute,
  AppStackParamList,
  DrawerParamList,
  RootStackParamList,
} from './types';
import { GetStarted } from 'screens/GetStarted';
import { VarianceSummary } from 'screens/VarianceSummary';
import { VarianceLocationList } from 'screens/VarianceLocationList';
import { VarianceLocation } from 'screens/VarianceLocation';
import { getScreenTestingIds } from 'test-utils/testIds';

const AppStack = createStackNavigator<AppStackParamList>();
const Drawer = createDrawerNavigator<DrawerParamList>();
const RootStack = createStackNavigator<RootStackParamList>();
const testIds = getScreenTestingIds('Navigator');

const AppRoutes: AppRoute[] = [
  {
    name: RouteNames.GET_STARTED,
    component: GetStarted,
  },
  {
    name: RouteNames.LOCATION_ITEMS,
    component: LocationItems,
  },
  {
    name: RouteNames.LOCATION_ITEM_DETAIL,
    component: LocationItemDetail,
  },
  {
    name: RouteNames.PRODUCT_DETAIL,
    component: ProductDetail,
  },
  {
    name: RouteNames.VERIFY_COUNT,
    component: VerifyCount,
  },
  {
    name: RouteNames.SCAN_LOCATION,
    component: ScanLocation,
  },
  {
    name: RouteNames.MANUAL_LOCATION_ENTRY,
    component: ManualLocationEntry,
  },
  {
    name: RouteNames.COUNT_LOCATIONS,
    component: CountLocations,
  },
  {
    name: RouteNames.BRANCH_SUMMARIES,
    component: BranchSummaries,
  },
  {
    name: RouteNames.WRITE_INS,
    component: WriteIns,
  },
  {
    name: RouteNames.UPDATE_WRITE_IN,
    component: UpdateWriteIn,
  },
  {
    name: RouteNames.WRITE_IN_DETAIL,
    component: WriteInDetail,
  },
  {
    name: RouteNames.VARIANCE_SUMMARY,
    component: VarianceSummary,
  },
  {
    name: RouteNames.VARIANCE_LOCATION_LIST,
    component: VarianceLocationList,
  },
  {
    name: RouteNames.VARIANCE_LOCATION,
    component: VarianceLocation,
  },
  { name: RouteNames.FOUND_PRODUCT, component: FoundProduct },
  ...(logError()
    ? [
      {
        name: RouteNames.ERROR_LOG,
        component: ErrorLog,
      },
    ]
    : []),
];

const AppStackNavigator = () => (
  <AppStack.Navigator
    initialRouteName={RouteNames.VERIFY_COUNT}
    screenOptions={defaultOptions}
  >
    {AppRoutes.map(({ name, ...rest }) => (
      <AppStack.Screen key={name} name={name} {...rest} />
    ))}
  </AppStack.Navigator>
);

const drawerRoutes: DrawerRoute[] = [
  {
    name: 'AppStackNavigator',
    component: AppStackNavigator,
    options: {
      headerShown: false,
    },
  },
];

const DrawerNavigator = () => (
  <Drawer.Navigator
    drawerContent={(props) => (
      <DrawerContent {...props} testID={testIds.navigator} />
    )}
    screenOptions={{ headerShown: false }}
  >
    {drawerRoutes.map(({ name, ...rest }) => (
      <Drawer.Screen key={name} name={name} {...rest} />
    ))}
  </Drawer.Navigator>
);

const authenticatedRoutes: AuthenticatedRoute[] = [
  {
    name: 'DrawerNavigator',
    component: DrawerNavigator,
  },
];

const Navigator = () => {
  const auth = useAuth();

  useEffect(() => {
    SplashScreen.hide();
  }, []);

  return (
    <RootStack.Navigator screenOptions={{ headerShown: false }}>
      {auth.isAuthenticated ? (
        authenticatedRoutes.map(({ name, ...rest }) => (
          <RootStack.Screen key={name} name={name} {...rest} />
        ))
      ) : (
        <RootStack.Screen name="Login" component={Login} />
      )}
    </RootStack.Navigator>
  );
};

export default Navigator;
