import { KourierProduct, WriteIn } from './../api/index';
import { NavigatorScreenParams, RouteProp } from '@react-navigation/native';
import { LocationItem, Product } from 'api';
import { RouteNames } from 'constants/routes';
import {
  StackNavigationOptions,
  StackNavigationProp,
} from '@react-navigation/stack';
import { DrawerNavigationOptions } from '@react-navigation/drawer';

export interface AppRoute {
  readonly name: RouteNames;
  readonly component: React.ComponentType<any>;
  readonly options?: StackNavigationOptions;
}

export interface DrawerRoute {
  readonly name: 'AppStackNavigator';
  readonly component: React.ComponentType<any>;
  readonly options?: DrawerNavigationOptions;
}

export interface AuthenticatedRoute {
  readonly name: 'DrawerNavigator';
  readonly component: React.ComponentType<any>;
}

export type AppStackParamList = {
  LocationItemDetail: { item: LocationItem };
  ProductDetail: { product: KourierProduct };
  UpdateWriteIn: { writeIn: WriteIn };
  WriteInDetail: { writeIn: WriteIn };
  BranchSummaries: undefined;
  CountLocations: undefined;
  ErrorLog: undefined;
  FoundProduct: undefined;
  GetStarted: undefined;
  LocationItems: undefined;
  ManualLocationEntry: undefined;
  ScanLocation: undefined;
  ScanProduct: undefined;
  VerifyCount: undefined;
  WriteIns: undefined;
  VarianceSummary: undefined;
  VarianceLocationList: undefined;
  VarianceLocation: { locationId: string; nextLocation?: string };
};

export type DrawerParamList = {
  AppStackNavigator: NavigatorScreenParams<AppStackParamList>;
};

export type RootStackParamList = {
  DrawerNavigator: NavigatorScreenParams<DrawerParamList>;
  Login: undefined;
};

export interface AppScreenProps<P extends keyof AppStackParamList> {
  navigation: StackNavigationProp<AppStackParamList, P>;
  route: RouteProp<AppStackParamList, P>;
}

export type AppNavigation<
  P extends keyof AppStackParamList
> = StackNavigationProp<AppStackParamList, P>;
