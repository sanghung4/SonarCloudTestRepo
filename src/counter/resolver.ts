import { Resolvers } from '../generated/graphql';
import { Context } from '../context';

const counterResolvers: Resolvers<Context> = {
  Query: {
    getCustomerSearch: (_, args, { dataSources }) => {
      return dataSources.counterAPI.getCustomerSearch(args);
    }
  },
};

export default counterResolvers;
