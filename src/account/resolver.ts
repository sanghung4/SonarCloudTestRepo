import { Resolvers } from '../generated/graphql';
import { Context } from '../context';

const accountResolvers: Resolvers<Context> = {
  Mutation: {
    verifyEclipseCredentials: (_, args, { dataSources }) => dataSources.accountAPI.validateEclipseCredentials(args)
  }
};

export default accountResolvers;
