import perf from '@react-native-firebase/perf';
import analytics from '@react-native-firebase/analytics';
import crashlytics from '@react-native-firebase/crashlytics';
import { TraceRequest } from './fetchIntercept';
import { getUser } from '@okta/okta-react-native';
import Config from 'react-native-config';

interface Traces {
  [index: string]: any;
}

//Save the traces
let traces = {} as Traces;

// check the firebase integrations
async function checkFirebaseIntegrations() {
  const isFirebaseIntegrated =
    perf().isPerformanceCollectionEnabled &&
    crashlytics().isCrashlyticsCollectionEnabled;

  console.log('Firebase is integrated: ', isFirebaseIntegrated);
  return isFirebaseIntegrated;
}

async function setupFirebase() {
  if (Config.FIREBASE_METRICS == 'true') {
    // Enable Analytics
    analytics().setAnalyticsCollectionEnabled(true);

    // Enable Performance Monitoring
    perf().dataCollectionEnabled = true;

    // Enable Crashlytics
    crashlytics().setCrashlyticsCollectionEnabled(true);
  } else {
    // Disable Analytics
    analytics().setAnalyticsCollectionEnabled(false);

    // Disable Performance Monitoring
    perf().dataCollectionEnabled = false;

    // Disable Crashlytics
    crashlytics().setCrashlyticsCollectionEnabled(false);
  }
}

//Create a new custom trace
async function startCustomTrace(traceName: string, operation: any = {}) {
  if (!checkFirebaseIntegrations()) {
    return;
  }
  try {
    // Define & start a trace
    traces[traceName] = await perf().startTrace(traceName);

    // Define trace meta details
    setAttributesToTrace(traceName, operation);
  } catch (e) {
    // rejects if iOS or (Android == 8 || Android == 8.1)
    // or if hardware acceleration is off
    console.log('Warning: hardware acceleration is off so tracing is disabled');
  }
}

//finish the custom trace
async function endCustomTrace(traceName: string) {
  if (!checkFirebaseIntegrations()) {
    return;
  }
  if (traces[traceName]) {
    traces[traceName].stop();
  }
}

//Start a request tracing
async function startRequestTrace(traceRequest: TraceRequest) {
  if (!checkFirebaseIntegrations()) {
    return;
  }
  const { traceName, method, headers, query, variables } = traceRequest;

  try {
    // Define the network metric
    traces[traceName] = perf().newHttpMetric(traceName, method);

    // Define trace meta details
    setAttributesToTrace(traceName, { headers, traceName, query, variables });

    // Start the metric traces[traceName]
    await traces[traceName].start();
  } catch (e) {
    // rejects if iOS or (Android == 8 || Android == 8.1)
    // or if hardware acceleration is off
    console.log('Warning: hardware acceleration is off so tracing is disabled');
  }
}

//finish the request trace
async function endRequestTrace(traceRequest: TraceRequest) {
  if (!checkFirebaseIntegrations()) {
    return;
  }
  const { traceName, response } = traceRequest;

  // add some information of the request response data if exists
  traces[traceName].setHttpResponseCode(response.status);
  traces[traceName].setResponseContentType(
    response.headers.map['content-type']
  );
  traces[traceName].setRequestPayloadSize(response._bodyInit._data.size);
  setAttributesToTrace(traceName, { response: response._bodyInit });

  // console.log('Request Data: ', JSON.stringify(traces[traceName]));

  // Stop the metric traces[traceName]
  traces[traceName].stop();
}

//start a screen trace
async function makeScreenTrace(traceName: string, timeElapsed: number) {
  if (!checkFirebaseIntegrations()) {
    return;
  }
  try {
    // Define & start a screen trace
    traces[traceName] = await perf().startScreenTrace(traceName);

    //to do a custom trace too for android
    traces[traceName + 'android'] = await perf().startTrace(traceName);
  } catch (e: any) {
    // rejects if iOS or (Android == 8 || Android == 8.1)
    // or if hardware acceleration is off
    traces[traceName] = await perf().startTrace(traceName);
    crashlyticsRecordError(e);
    console.log('Warning: hardware acceleration is off so tracing is disabled');
  }

  await new Promise((resolve) => setTimeout(resolve, timeElapsed)); // wait for milliseconds
  //finish the screen trace
  traces[traceName].stop();

  //to finish the custom trace for android in case exists
  if (traces[traceName + 'android']) {
    traces[traceName + 'android'].stop();
  }
}

//send the actual user route to firebase analytics
async function setActualScreenView(routeName: string) {
  if (!checkFirebaseIntegrations()) {
    return;
  }
  analytics().logEvent(`Page_${routeName}`, {});
  analytics().logScreenView({
    screen_name: routeName,
    screen_class: routeName,
  });
}

//Set attributes to be saved for the trace
async function setAttributesToTrace(traceName: string, attributes: any) {
  if (!checkFirebaseIntegrations()) {
    return;
  }
  if (traces[traceName]) {
    // Define trace meta details
    for (const key in attributes) {
      if (attributes.hasOwnProperty(key)) {
        traces[traceName].putAttribute(
          key,
          JSON.stringify(attributes[key]).substring(0, 96) + '...'
        );
      }
    }
  }
}

//make crashlytics log of events
async function crashlyticsLog(logText: string) {
  if (!checkFirebaseIntegrations()) {
    return;
  }
  crashlytics().log(logText);
}

//save attributes for crashlytics of actual events
async function crashlyticsSetAttributes(attributes: { [key: string]: string }) {
  if (!checkFirebaseIntegrations()) {
    return;
  }
  crashlytics().setAttributes(attributes);
}

//save actual user id for crashlytics
async function crashlyticsSetUserId(userId: string = '') {
  if (!checkFirebaseIntegrations()) {
    return;
  }
  let user: any = await getUser();
  crashlytics().setUserId(user.preferred_username);
}

//record error for crashlytics
async function crashlyticsRecordError(error: Error) {
  if (!checkFirebaseIntegrations()) {
    return;
  }
  crashlytics().recordError(error);
}

//Simulate a app crash
async function crashlyticsSimulateAppCrash() {
  if (!checkFirebaseIntegrations()) {
    return;
  }
  crashlytics().crash();
}

const firebaseUtils = {
  setupFirebase,
  startCustomTrace,
  makeScreenTrace,
  startRequestTrace,
  endCustomTrace,
  endRequestTrace,
  setActualScreenView,
  crashlyticsLog,
  crashlyticsSetAttributes,
  crashlyticsSetUserId,
  crashlyticsRecordError,
  crashlyticsSimulateAppCrash,
};

export default firebaseUtils;
