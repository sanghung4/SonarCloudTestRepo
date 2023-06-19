import { Resolvers } from '../generated/graphql';
import { Context } from '../context';

const inventoryResolvers: Resolvers<Context> = {
  Query: {
    count: (_, args, { dataSources }) => {
      return dataSources.inventoryAPI.getCount(args);
    },
    countStatus: (_, args, { dataSources }) => {
      return dataSources.inventoryBatchAPI.getCountStatus(args);
    },
    counts: (_, args, { dataSources }) => {
      return dataSources.inventoryBatchAPI.getCounts(args);
    },
    locations: (_0, _1, { dataSources }) => {
      return dataSources.inventoryAPI.getLocations();
    },
    location: (_, args, { dataSources }) => {
      return dataSources.inventoryAPI.getLocation(args);
    },
    nextLocation: (_, args, { dataSources }) => {
      return dataSources.inventoryAPI.getNextLocation(args);
    },
    writeIns: (_, args, { dataSources }) => {
      return dataSources.inventoryAPI.getWriteIns(args);
    },
    writeIn: (_, args, { dataSources }) => {
      return dataSources.inventoryAPI.getWriteIn(args);
    }
  },
  Mutation: {
    addToCount: (_, args, { dataSources }) => {
      return dataSources.inventoryAPI.addToCount(args);
    },
    updateCount: (_, args, { dataSources }) => {
      return dataSources.inventoryAPI.updateCount(args);
    },
    completeCount: (_, args, { dataSources }) => {
      return dataSources.inventoryAPI.completeCount(args);
    },
    createWriteIn: (_, args, { dataSources }) => {
      return dataSources.inventoryAPI.createWriteIn(args);
    },
    updateWriteIn: (_, args, { dataSources }) => {
      return dataSources.inventoryAPI.updateWriteIn(args);
    },
    resolveWriteIn: (_, args, { dataSources }) => {
      return dataSources.inventoryAPI.resolveWriteIn(args);
    },
    addCompletionMetric: (_, args, { dataSources }) => {
      return dataSources.inventoryAPI.addCompletionMetric(args);
    },
    closeTask: (_, args, { dataSources }) => {
      return dataSources.inventoryAPI.closeTask(args);
    },
    removeCounts: (_, args, { dataSources }) => {
      return dataSources.inventoryBatchAPI.removeCounts(args);
    },
    loadCount: (_, args, { dataSources }) => {
      return dataSources.inventoryBatchAPI.loadCount(args);
    },
    deleteCount: (_, args, { dataSources }) => {
      return dataSources.inventoryBatchAPI.deleteCount(args);
    },
    purgeMincronCounts: (_, args, { dataSources }) => {
      return dataSources.inventoryBatchAPI.purgeMincronCounts(args);
    },
    removeBranchCounts: (_, args, { dataSources }) => {
      return dataSources.inventoryBatchAPI.removeBranchCounts(args);
    },
  }
};

export default inventoryResolvers;
