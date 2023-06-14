import { useLayoutEffect, useEffect, useRef } from 'react';
import { useRoute } from '@react-navigation/native';
import firebaseUtils from 'utils/firebaseUtils';


//For trace render time, for analytics information and logs for crashlytics debug data
const useRenderListener = () => {
  const route = useRoute();
  const startTimeRef = useRef<number | null>(null);

  useLayoutEffect(() => {
    startTimeRef.current = Date.now();
    firebaseUtils.setActualScreenView(route.name);
    firebaseUtils.crashlyticsLog("User its in screen: " + route.name);
  }, [route]);

  useEffect(() => {
    if (startTimeRef.current !== null) {
      const timeElapsed = Date.now() - startTimeRef.current;
      firebaseUtils.crashlyticsLog("The screen now finish rendering: " + route.name);
      firebaseUtils.makeScreenTrace("Screen: "+route.name, timeElapsed);
    }
    return () => {
      firebaseUtils.crashlyticsLog("The screen " + route.name+" its unmounted.");
    };
  }, []);
};

export default useRenderListener;
