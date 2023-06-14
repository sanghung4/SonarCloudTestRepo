import 'react-native-gesture-handler';
import React, { useEffect } from 'react';
import { StatusBar, Alert as RNAlert } from 'react-native';
import {
  JSExceptionHandler,
  setJSExceptionHandler,
} from 'react-native-exception-handler';
import {
  NavigationContainer,
  NavigationContainerRef,
} from '@react-navigation/native';
import { createIconSetFromIcoMoon } from 'react-native-vector-icons';
import { registerCustomIconType } from 'react-native-elements';
import NavigationService from 'navigation/NavigationService';
import Navigator from 'navigation/Navigator';
import { UserActivity } from 'components/UserActivity';
import { OverlayContainer } from 'components/Overlay';
import Providers from 'providers';
import { useOverlay } from 'providers/Overlay';
import { useAuth } from 'providers/Auth';
import icomoonConfig from './selection.json';
import Toast from 'react-native-toast-message';
import { CustomToastConfig } from 'components/ToastConfig';
import analytics from '@react-native-firebase/analytics';
import { registerFetchIntercept } from 'utils/fetchIntercept';
import firebaseUtils from 'utils/firebaseUtils';

const IcomoonIconSet = createIconSetFromIcoMoon(icomoonConfig);

firebaseUtils.setupFirebase();
//for intercepting the requests
const unregisterFetchIntercept = registerFetchIntercept();


const errorHandler: JSExceptionHandler = (error, isFatal) => {
  firebaseUtils.crashlyticsRecordError(error);
  if (isFatal) {
    RNAlert.alert(
      'Unexpected error occurred',
      `
        Error: Fatal: ${error.name} ${error.message}
        We have reported this to our team ! Please close the app and start again!
        `,
      [
        {
          text: 'Close',
        },
      ]
    );
    
  } else {
    console.log(error);
  }
};

setJSExceptionHandler(errorHandler, true);

const INACTIVITY_DURATION = 12 * 60 * 60 * 1000; // hr * min * sec * ms;

const App: React.FC = () => {
  const alert = useOverlay();
  const auth = useAuth();
  const routeNameRef = React.useRef<string | undefined>();
  const navigationRef = React.useRef<NavigationContainerRef | any>(null);

  //for intercept the requests
  useEffect(() => {
    return () => {
      
      unregisterFetchIntercept();

    };
  }, []);

  return (
    <UserActivity
      duration={INACTIVITY_DURATION}
      track={auth.isAuthenticated}
      timeoutHandler={() => {
        if (auth.isAuthenticated) {
          alert.showAlert({
            svg: 'SessionExpiredImage',
            title: 'Session Expired',
            description: 'You will be redirected to login.',
            actions: [{ title: 'Ok', type: 'primary', onPress: auth.logout }],
            options: { fullScreen: true },
            onClose: auth.logout,
          });
        }
      }}
    >
  
      <NavigationContainer
        ref={(navigatorRef) => {
          NavigationService.setTopLevelNavigator(navigatorRef);
        }}
        onReady={() => {
          routeNameRef.current = navigationRef.current?.getCurrentRoute().name;
        }}
        onStateChange={async () => {
          const previousRouteName = routeNameRef.current;
          const currentRouteName = navigationRef.current?.getCurrentRoute()
            ?.name;

          if (previousRouteName !== currentRouteName) {
            await analytics().logScreenView({
              screen_name: currentRouteName,
              screen_class: currentRouteName,
            });
          }
          routeNameRef.current = currentRouteName;
        }}
      >
        <StatusBar barStyle="dark-content" />
        <Navigator />
        <OverlayContainer />
        <Toast config={CustomToastConfig} />
      </NavigationContainer>
    </UserActivity>
  );
};

const AppShell: React.FC = () => {
  registerCustomIconType('reece', IcomoonIconSet);

  return (
    <>
      <Providers>
        <App />
      </Providers>
    </>
  );
};

export default AppShell;
