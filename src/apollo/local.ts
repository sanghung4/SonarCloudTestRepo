import { makeVar } from '@apollo/client';
import { Count, Maybe } from 'api';
import { GraphQLError } from 'graphql';

export type Config = {
  count: Maybe<Count>;
  errors: GraphQLError[];
};

export const initialConfig = {
  count: null,
  errors: [],
};

export const configVar = makeVar<Config>(initialConfig);
