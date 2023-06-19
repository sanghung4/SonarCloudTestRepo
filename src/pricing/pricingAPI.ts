import querystring from 'querystring';
import {
  QueryTypeaheadSuggestionsArgs,
  QuerySpecialPricesArgs,
  MutationSpecialPricesArgs,
  QueryPaginatedSpecialPricesArgs,
  QueryPaginatedTypeaheadSuggestionsArgs,
  QueryPriceLinesArgs,
  QueryProductPricesArgs
} from '../generated/graphql';
import { BaseAPI } from '../utils/baseAPI';

export class PricingAPI extends BaseAPI {
  baseURL = process.env.API_URL_PRICING_SERVICE;

  async getTypeaheadSuggestions({ input }: QueryTypeaheadSuggestionsArgs) {
    const query = querystring.stringify({
      ...input
    });

    return this.get(`v1/typeahead`, query);
  }

  async getPaginatedTypeaheadSuggestions({ input, pagingContext }: QueryPaginatedTypeaheadSuggestionsArgs) {
    const query = querystring.stringify({
      ...input,
      ...pagingContext
    });

    return this.get(`v1/typeahead`, query);
  }

  async getSpecialPrices({ input }: QuerySpecialPricesArgs) {
    const query = querystring.stringify({
      ...input
    });

    return this.get(`v1/product/specialPrice`, query);
  }

  async getPaginatedSpecialPrices({ input, pagingContext }: QueryPaginatedSpecialPricesArgs) {
    const query = querystring.stringify({
      ...input,
      ...pagingContext
    });

    return this.get(`v1/product/specialPrice`, query);
  }

  async updateSpecialPrices({ input }: MutationSpecialPricesArgs) {
    return this.post(`v1/product/specialPrice`, input);
  }

  async getPriceLines({ input }: QueryPriceLinesArgs) {
    const query = querystring.stringify({
      ...input
    });

    return this.get(`v1/product/specialPrice/priceLines`, query);
  }

  async getProductPrices({ input }: QueryProductPricesArgs) {
    const query = querystring.stringify(input);

    return this.get(`/v1/product/specialPrice/productPrice`, query);
  }

}
