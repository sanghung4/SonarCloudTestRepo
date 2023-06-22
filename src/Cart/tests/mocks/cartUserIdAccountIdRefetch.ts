import { ApolloError, ApolloQueryResult } from '@apollo/client';
import { GraphQLError } from 'graphql';

import { mockCart } from 'Cart/tests/mocks';
import { CartUserIdAccountIdQuery } from 'generated/graphql';

type MockRefetchFnData = ApolloQueryResult<CartUserIdAccountIdQuery>;

const mockGraphQLError: GraphQLError[] = [
  {
    locations: undefined,
    path: undefined,
    nodes: undefined,
    source: undefined,
    positions: undefined,
    originalError: undefined,
    extensions: {
      code: 404
    },
    toJSON: jest.fn(),
    [Symbol.toStringTag]: typeof Symbol.toStringTag,
    name: 'fail',
    message: '404 not found'
  }
];
export function mockCartUserIdAccountIdRefetch(hasError: boolean) {
  const innerFunction = async () => {
    const data: MockRefetchFnData = {
      data: {
        cartUserIdAccountId: mockCart
      },
      loading: false,
      networkStatus: 7
    };
    const error: ApolloError = {
      message: 'fail',
      graphQLErrors: mockGraphQLError,
      clientErrors: [],
      networkError: null,
      extraInfo: null,
      name: '404'
    };
    const result = await new Promise<MockRefetchFnData>((resolve) => {
      resolve(hasError ? { ...data, error } : data);
    });
    return result;
  };
  return innerFunction;
}
