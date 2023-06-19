import { Resolvers } from '../generated/graphql';
import { Context } from '../context';

const userResolvers: Resolvers<Context> = {
  Query: {
    userBranch: (_, args, { dataSources }) => {
      return dataSources.userAPI.getUserBranch(args);
    },
    branchUsersList: (_, args, { dataSources }) => {
      return dataSources.userAPI.getBranchUsersList();
    }
  },

  Mutation: {
    addUser: (_, args, { dataSources }) => {
      return dataSources.userAPI.addUser(args);
    },
    updateUserEmail: (_, args, { dataSources }) => {
      return dataSources.userAPI.updateUserEmail(args);
    },
    removeUserBranch: (_, args, { dataSources }) => {
      return dataSources.userAPI.removeUserBranch(args);
    },
    deleteUser: (_, args, { dataSources }) => {
      return dataSources.userAPI.deleteUser(args);
    }
  }
};

export default userResolvers;
