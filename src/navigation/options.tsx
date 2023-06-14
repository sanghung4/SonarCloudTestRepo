import React from 'react';
import { Platform, TouchableOpacity } from 'react-native';
import {
  HeaderBackButton,
  StackNavigationOptions,
} from '@react-navigation/stack';
import { SvgIcons } from 'components/SVG';
import { IconProps } from 'react-native-elements';
import NavigationService from './NavigationService';
import { getStateByRouteName } from 'utils/navigation';
import { RouteNames } from 'constants/routes';
import { Colors } from 'constants/style';
import { getScreenTestingIds } from 'test-utils/testIds';

const testIds = getScreenTestingIds('Options');

const HeaderRightButton = (props: IconProps) => (
  <TouchableOpacity onPress={props.onPress}>
    <SvgIcons name={props.name} size={45} testID={testIds.screen} />
  </TouchableOpacity>
);

export const defaultOptions: StackNavigationOptions = {
  headerTintColor: '#00294d',
  headerStyle: { backgroundColor: Colors.WHITE },
  headerTitle: () => (
    <TouchableOpacity onPress={() => NavigationService.popToLaunch()}>
      <SvgIcons name={'Logo'} size={50} />
    </TouchableOpacity>
  ),
  headerRight: () => (
    <HeaderRightButton
      name="Account"
      onPress={NavigationService.toggleDrawer}
    />
  ),
  headerTitleAlign: 'center',
  headerBackTitle: '',
  headerShown: true,
};

/**
 * getModalOptions
 * Is this deprecated?
 */
export const getModalOptions = () => ({
  ...defaultOptions,
  headerLeft: () => null,
  headerRight: () => (
    <HeaderRightButton name="exit" onPress={NavigationService.goBack} />
  ),
});

/**
 * getTabRootOptions
 * Is this deprecated?
 */
export const getTabRootOptions = ({ navigation }: { navigation: any }) => ({
  ...defaultOptions,
  headerLeft: () => (
    <HeaderBackButton
      onPress={() => {
        const state = getStateByRouteName(navigation, RouteNames.GET_STARTED);
        if (state) {
          NavigationService.navigate(state.routes[state.index].name);
        } else {
          NavigationService.goBack();
        }
      }}
      accessibilityLabel="header-back"
    />
  ),
});

/**
 * getTabBarOptions
 * Is this deprecated?
 */
export const getTabBarOptions = (height: number) => ({
  keyboardHidesTabBar: Platform.select({ android: true }),
  activeTintColor: Colors.PRIMARY_2100,
  inactiveTintColor: Colors.PRIMARY_1100,
  tabStyle: {
    paddingTop: 10,
    paddingBottom: 15,
  },
  style: {
    height,
  },
});
