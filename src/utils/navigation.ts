import { NavigationState } from '@react-navigation/native';

const getRouteByRouteName = (
  state: any,
  routeName: string
): NavigationState | undefined => {
  const routes = Array.isArray(state.routes) ? state.routes : [];

  for (const route of routes) {
    const isRoute = route.name === routeName;
    if (isRoute) {
      return state;
    }
    if (route.state) {
      return getRouteByRouteName(route.state, routeName);
    }
  }
};

export const getStateByRouteName = (
  navigation: any,
  routeName: string
): NavigationState | null => {
  if (!navigation) {
    return null;
  }

  const state = navigation.dangerouslyGetState();
  const parent = navigation.dangerouslyGetParent();
  if (!state) {
    return null;
  }

  const result = getRouteByRouteName(state, routeName);
  return result || getStateByRouteName(parent, routeName);
};
