import {
  ApolloClient,
  ApolloLink,
  NormalizedCacheObject,
  Operation,
} from '@apollo/client';
import link from './link';
import cache from './cache';

const makeApolloClient = (): ApolloClient<NormalizedCacheObject> => {
  const client = new ApolloClient({link, cache });
  return client;
};

export default makeApolloClient();
