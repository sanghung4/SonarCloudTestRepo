import { ApolloError } from '@apollo/client';
import { GraphQLError } from 'graphql';
import { ErrorCodes, ErrorCode, error, APIError } from 'constants/error';
import { isArray } from 'lodash';
import { AlertPayload } from 'providers/Overlay/types';
import Config from 'react-native-config';
import firebaseUtils from './firebaseUtils';

export const getError = (
  type: keyof typeof error,
  apolloError: ApolloError,
  defaultCode: ErrorCodes = ErrorCode.BAD_REQUEST
): AlertPayload => {
  
  const graphQLError = getGraphQLError(apolloError);
  const apiError = getAPIError(apolloError);

  let payload = error[type][defaultCode];
  let message = '';

  if (apiError) {
    const apiErrorCode: ErrorCodes = apiError.code;

    message = apiError.message
      ? apiError.code + ' ' + apiError.message
      : apiError.code;

    if (error[type][apiErrorCode]) {
      payload = error[type][apiErrorCode];
    }
  } else if (graphQLError && graphQLError.extensions) {
    const graphQLErrorCode: ErrorCodes = graphQLError.extensions.code;

    if (error[type][graphQLErrorCode]) {
      payload = error[type][graphQLErrorCode];
    }
  }

  return { error: message, ...payload } as AlertPayload;
};

export const getGraphQLError = (apolloError: ApolloError): GraphQLError => {
  return isArray(apolloError.graphQLErrors)
    ? apolloError.graphQLErrors[0]
    : apolloError.graphQLErrors;
};

export const getAPIError = (apolloError: ApolloError): APIError | undefined => {
  const graphQLError = getGraphQLError(apolloError);

  if (graphQLError && graphQLError.extensions) {
    const { response } = graphQLError.extensions;

    if (response && response.body) {
      return isArray(response.body.errors)
        ? response.body.errors[0]
        : response.body.errors;
    }
  }
};

export const logError = (): boolean => Config.LOG_ERROR === 'true';
