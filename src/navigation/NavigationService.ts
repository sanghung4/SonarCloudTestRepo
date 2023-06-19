import {
  CommonActions,
  DrawerActions,
  NavigationContainerRef,
  NavigationState,
  PartialState,
  Route,
} from '@react-navigation/native';
import { RouteNames } from '../constants/routes';

let _navigator: NavigationContainerRef | null;

const setTopLevelNavigator = (navigatorRef: typeof _navigator): void => {
  _navigator = navigatorRef;
};

const navigate = (
  routeName: string,
  params?: Record<string, unknown>
): void => {
  _navigator?.dispatch(CommonActions.navigate({ name: routeName, params }));
};

const toggleDrawer = (): void => {
  _navigator?.dispatch(DrawerActions.toggleDrawer());
};

const goBack = (): void => {
  _navigator?.dispatch(CommonActions.goBack());
};

const reset = (
  state:
    | PartialState<NavigationState>
    | NavigationState
    | (Omit<NavigationState, 'routes'> & {
      routes: Omit<Route<string>, 'key'>[];
    })
): void => {
  _navigator?.dispatch(CommonActions.reset(state));
};

const popToLaunch = (): void => {
  //only navigate to launch screen if user has already entered a count.
  const currentRouteName = _navigator?.getCurrentRoute()?.name;
  if (
    currentRouteName === RouteNames.VERIFY_COUNT
  ) {
    return;
  }

  _navigator?.dispatch(
    CommonActions.navigate({ name: RouteNames.GET_STARTED })
  );
};

export default {
  goBack,
  navigate,
  reset,
  toggleDrawer,
  setTopLevelNavigator,
  popToLaunch,
};
