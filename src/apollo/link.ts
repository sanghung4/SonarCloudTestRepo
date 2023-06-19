import { createHttpLink, ApolloLink, Observable } from '@apollo/client';
import { setContext } from '@apollo/client/link/context';
import { onError } from '@apollo/client/link/error';
import { signOut } from '@okta/okta-react-native';
import { EXPIRED_TOKEN, INVALID_TOKEN, linkOptions } from 'constants/apollo';
import * as utils from 'utils/error';
import {
  getAccessToken,
  getActivityConfigs,
  hasErrorMessage,
  onExpiredTokenError,
  promiseToObservable,
} from 'utils/apollo';
import { configVar } from './local';
import { GraphQLError } from 'graphql';

const httpLink = createHttpLink(linkOptions);

const authLink = setContext(async (_, { headers }) => {
  const token = await getAccessToken();
  return {
    headers: {
      ...headers,
      authorization: token,
    },
  };
});

const activityLink = setContext(async (_, { headers }) => {
  const config = await getActivityConfigs();
  return {
    headers: {
      ...headers,
      ...config,
    },
  };
});

const logError = (error: GraphQLError) => {
  if (!utils.logError()) {
    return;
  }
  const config = configVar();
  config.errors.push(error);
  
  configVar(config);
};

const errorLink = onError(({ graphQLErrors, forward, operation }) => {
  if (graphQLErrors) {
    for (const graphQLError of graphQLErrors) {
      logError(graphQLError);

      if (hasErrorMessage(EXPIRED_TOKEN, graphQLError)) {
        return promiseToObservable(onExpiredTokenError()).flatMap((value) =>
          value ? forward(operation) : Observable.of()
        );
      }
      if (hasErrorMessage(INVALID_TOKEN, graphQLError)) {
        signOut();
        return Observable.of();
      }
    }
  }
});

export default ApolloLink.from([errorLink, authLink, activityLink, httpLink]);
