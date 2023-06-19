import { Resolvers } from '../generated/graphql';
import { Context } from '../context';
import { BadRequestError } from '../utils/errors';

const pickingResolvers: Resolvers<Context> = {
  UserPick: {
    productImageUrl: ({ productId }, _, { dataSources }) => {
      return dataSources.pickingAPI.getProductImageUrl(productId);
    }
  },
  Query: {
    pickingOrders: (_, args, { dataSources }) => {
      return dataSources.inventoryAPI.getPickingTasks(args);
    },
    userPicks: (_, args, { dataSources }) => {
      return dataSources.inventoryAPI.getUserPicks(args);
    },
    getProductSerialNumbers: (_, args, { dataSources }) => {
      return dataSources.inventoryAPI.getProductSerialNumbers(args);
    },
    productImageUrl: (_, args, { dataSources }) => {
      return dataSources.pickingAPI.getProductImageUrl(args.input);
    },
    shippingDetails: (_, args, { dataSources }) => {
      return dataSources.inventoryAPI.getShippingDetails(args);
    },
    validateBranch: (_, args, { dataSources }) => {
      return dataSources.pickingAPI.validateBranch(args);
    }
  },
  Mutation: {
    assignPickTask: async (_, args, { dataSources }) => {
      const resp = await dataSources.inventoryAPI.assignPickingTasks(args);
      if (resp.warehousePickTasksWarnings.length > 0 && resp.warehousePickTasksList.length === 0) {
        throw new BadRequestError({ message: resp.warehousePickTasksWarnings[0].warningInfo })
      }

      return resp.warehousePickTasksList[0];
    },
    completeUserPick: async (_, args, { dataSources }) => {
      return dataSources.inventoryAPI.completeUserPick(args);
    },
    updateProductSerialNumbers: async (_, args, { dataSources }) => {
      return dataSources.inventoryAPI.updateProductSerialNumbers(args);
    },
    stagePickTask: async (_, args, { dataSources }) => {
      return dataSources.inventoryAPI.stagePickTask(args);
    },
    stagePickTotePackages: async (_, args, { dataSources }) => {
      return dataSources.inventoryAPI.stagePickTotePackages(args);
    },
    splitQuantity: async (_, args, { dataSources }) => {
      return dataSources.pickingAPI.splitQuantity(args);
    },
    closeOrder: async (_, args, { dataSources }) => {
      return dataSources.pickingAPI.closeOrder(args);
    },
  }
};

export default pickingResolvers;
