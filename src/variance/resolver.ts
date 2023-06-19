import { Resolvers } from '../generated/graphql';
import { Context } from '../context';

const varianceResolvers: Resolvers<Context> = {
  Query: {
    varianceSummary: (_, args, { dataSources }) => {
      return dataSources.varianceAPI.getVarianceSummary();
    },
    varianceDetails: (_, args, { dataSources }) => {
       return dataSources.varianceAPI.getVarianceDetails();
    },
    varianceLocations: (_, args, { dataSources }) => {
      return dataSources.varianceAPI.getVarianceLocations();
    },
    varianceLocation: (_, args, { dataSources }) => {
      return dataSources.varianceAPI.getVarianceLocation(args);
    },
    varianceNextLocation: (_, args, { dataSources }) => {
      return dataSources.varianceAPI.getVarianceNextLocation(args);
    },
  },
  Mutation: {
    loadVarianceDetails: (_, args, { dataSources }) => {
      return dataSources.varianceAPI.loadVarianceDetails();
    },
    updateVarianceCount: (_, args, { dataSources }) => {
      return dataSources.varianceAPI.updateVarianceCount(args);
    },
    completeVarianceCount: (_, args, { dataSources }) => {
      return dataSources.varianceAPI.completeVarianceCount(args);
    },
  }
};

export default varianceResolvers;
