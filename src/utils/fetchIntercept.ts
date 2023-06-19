import fetchIntercept from 'fetch-intercept';
import firebaseUtils from './firebaseUtils';

type HttpMethod = 'GET' | 'POST' | 'PUT' | 'DELETE';

//Interface of information to send to Firebase about the request
export interface TraceRequest {
  traceName: string;
  url: string;
  method: HttpMethod;
  headers: any;
  operationName?: string;
  query?: string;
  variables?: string | object;
  body?: string | object;
  signal?: string | object;
  response?: string | any;
}

//Interface for registration of every request trace
export interface TraceRequestRegisterer {
  [index: string]: TraceRequest;
}

var traceRequest = {} as TraceRequestRegisterer;

var hostsToTrace = ['api.test.internal.reecedev.us'];


//Request interceptor
export const registerFetchIntercept = () => {
  const unregister = fetchIntercept.register({
    request: function (url, config) {
      if (requestShouldBeTraced(url)) {
        const traceReqStart = accommodateTraceDataAtStart(url, config);
        firebaseUtils.startRequestTrace(traceReqStart);
      }
      return [url, config];
    },

    requestError: function (error) {
      firebaseUtils.crashlyticsRecordError(error);
      return Promise.reject(error);
    },

    response: function (response) {
      if (requestShouldBeTraced(response.url)) {
        const traceReqCompleted = accommodateTraceDataAtCompleted(response);
        firebaseUtils.endRequestTrace(traceReqCompleted);
      }
      return response;
    },

    responseError: function (error) {
      firebaseUtils.crashlyticsRecordError(error);
      return Promise.reject(error);
    },
  });

  return unregister;
};


//check if the request should be traced
function requestShouldBeTraced(url: string) {
  return hostsToTrace.some((host) => url.includes(host));
}

//Set data at start of request
function accommodateTraceDataAtStart(url: string, config: any) {
  const { body, headers, method, signal } = config;
  let traceName = '',
    operationName = '',
    query = '',
    variables;

  //Convert body to object if its possible to know if it's a GraphQL request or not
  const bodyObject = JSON.parse(body);
  if (bodyObject && bodyObject.operationName) {
    traceName = url + bodyObject.operationName;
    operationName = bodyObject.operationName;
    query = bodyObject.query;
    variables = bodyObject.variables;
  } else {
    traceName = url;
  }

  traceRequest[traceName] = {
    traceName,
    url,
    method,
    headers,
    operationName,
    query,
    variables,
    body,
    signal,
  };

  return traceRequest[traceName];
}

//Set data at the end of the request
function accommodateTraceDataAtCompleted(response: any) {
  const {
    request,
    url,
  } = response;
  let traceName = '';

  //Convert body to object if its possible to know if it's a GraphQL request or not
  const bodyObject = JSON.parse(request._bodyText);
  if (bodyObject && bodyObject.operationName) {
    traceName = url + bodyObject.operationName;
  } else {
    traceName = url;
  }

  traceRequest[traceName] = {
    ...traceRequest[traceName],
    response,
  };

  return traceRequest[traceName];
}
