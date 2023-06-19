import { Resolvers } from '../generated/graphql';
import { Context } from '../context';

const customerResolvers: Resolvers<Context> = {
  Query: {
    customer: (_, args, { dataSources }) => {
      return dataSources.customerAPI.getCustomer(args);
    }
  }
};

export default customerResolvers;
