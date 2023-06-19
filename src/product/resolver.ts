import { Resolvers } from '../generated/graphql';
import { Context } from '../context';

const productResolvers: Resolvers<Context> = {
  Query: {
    productDetails: (_, args, { dataSources }) => {
      return dataSources.eclipseAPI.getProductDetails(args);
    },
    searchProductsEclipse: (_, args, { dataSources }) => {
      return dataSources.productAPI.searchProducts(args);
    },
    searchProductsKourier: (_, args, { dataSources }) => {
      return dataSources.productAPI.getSearchProductsKourier(args);
    }
  }
};

export default productResolvers;
