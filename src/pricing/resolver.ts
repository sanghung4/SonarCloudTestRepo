import { Resolvers } from '../generated/graphql';
import { Context } from '../context';

const pricingResolvers: Resolvers<Context> = {
  Query: {
    typeaheadSuggestions: (_, args, { dataSources }) => {
      return dataSources.pricingAPI.getTypeaheadSuggestions(args);
    },
    paginatedTypeaheadSuggestions: (_, args, { dataSources }) => {
      return dataSources.pricingAPI.getPaginatedTypeaheadSuggestions(args);
    },
    specialPrices: (_, args, { dataSources }) => {
      return dataSources.pricingAPI.getSpecialPrices(args);
    },
    paginatedSpecialPrices: (_, args, { dataSources }) => {
      return dataSources.pricingAPI.getPaginatedSpecialPrices(args);
    },
    priceLines: (_, args, { dataSources }) => {
      return dataSources.pricingAPI.getPriceLines(args);
    },
    productPrices: (_, args, { dataSources }) => {
      return dataSources.pricingAPI.getProductPrices(args);
    },
  },
  Mutation: {
    specialPrices: (_, args, { dataSources }) => {
      return dataSources.pricingAPI.updateSpecialPrices(args);
    },
  }
};

export default pricingResolvers;
