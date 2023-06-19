import { Observable, ApolloError } from '@apollo/client';
import { GraphQLError } from 'graphql';
import {
  ACCESS_TOKEN,
  BRANCH_ID,
  COUNT_ID,
  ERP_SYSTEM,
} from 'constants/storage';
import AsyncStorage from '@react-native-async-storage/async-storage';
import {
  introspectAccessToken,
  introspectRefreshToken,
  refreshTokens,
  signOut,
  Tokens,
} from '@okta/okta-react-native';
import { Count, ErpSystem, KourierProduct, Maybe, Product } from 'api';
import firebaseUtils from './firebaseUtils';

export const promiseToObservable = <T = Response>(
  promise: Promise<T>
): Observable<T> => {
  return new Observable((subscriber) => {
    promise
      .then((value) => {
        if (subscriber.closed) {
          return;
        }
        subscriber.next(value);
        subscriber.complete();
      })
      .catch((error) => {
        firebaseUtils.crashlyticsRecordError(error);
        subscriber.error(error)
      });
  });
};

export const hasErrorMessage = (
  message: string | string[] = [],
  graphQLError: GraphQLError
): boolean => {
  return typeof message === 'string'
    ? graphQLError.message.includes(message)
    : message.some((msg) => graphQLError.message.includes(msg));
};

export const getAccessToken = async (): Promise<string> => {
  await checkTokens();

  return AsyncStorage.getItem(ACCESS_TOKEN)
    .then((token) => (token ? `Bearer ${token}` : ''))
    .catch(() => '');
};

export const getActivityConfigs = (): Promise<Record<string, string>> => {
  return AsyncStorage.multiGet([BRANCH_ID, COUNT_ID, ERP_SYSTEM])
    .then((storage) =>
      storage.reduce((obj, [key, value]) => {
        if (value) {
          obj[`x-${key}`] = value;
        }
        return obj;
      }, {} as Record<string, string>)
    )
    .catch(() => ({}));
};

export const onExpiredTokenError = (): Promise<Tokens | void> => {
  return refreshTokens()
    .then((resp) =>
      AsyncStorage.setItem(ACCESS_TOKEN, resp.access_token).then(() => resp)
    )
    .catch(() => signOut());
};

export const isMincron = (count: Maybe<Count>): boolean =>
  count?.erpSystem === ErpSystem.MINCRON;

/**
 * Remaps product object to correct object names
 * @param {Product} product
 * @returns returns remapped product item
 */
export const transformProduct = (product: Product) => {
  return {
    ...product,
    productNumber: product.id,
    catalogNumber: product.productNumber,
  };
};

/**
 * Remaps product object to correct object names
 * @param {KourierProduct} product
 * @returns returns remapped product item
 */
export const transformKourierProduct = (product: KourierProduct) => {
  return {
    ...product,
    productNumber: product.productId,
    catalogNumber: product.productNumber,
  };
};

export const handleMutationComplete = <T>(
  response: {
    success: boolean;
    message?: string | null;
  } & T
) => {
  if (response.success) {
    return;
  }

  throw new ApolloError({
    errorMessage: response.message || 'something went wrong',
  });
};

export const checkTokens = async () => {
  const accessObject = await introspectAccessToken()
    .then((resp) => resp)
    .catch(() => ({ active: false }));

  const refreshObject = await introspectRefreshToken()
    .then((resp) => resp)
    .catch(() => ({ active: false }));

  if (!accessObject.active && !refreshObject.active) {
    return signOut();
  }

  if (!accessObject.active && refreshObject.active) {
    return refreshTokens().then((resp) =>
      AsyncStorage.setItem(ACCESS_TOKEN, resp.access_token || '')
    );
  }
};
