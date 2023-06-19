import {
  QuerySearchProductsEclipseArgs,
  QuerySearchProductsKourierArgs
} from '../generated/graphql';
import { BaseAPI } from '../utils/baseAPI';

export class ProductAPI extends BaseAPI {
  baseURL = process.env.API_URL_INVENTORY_SERVICE;

  async getSearchProductsKourier({ input: { keywords, displayName, searchInputType} }: QuerySearchProductsKourierArgs) {
    displayName = 'ProductUPCByName';
    const resp = await this.get(`products/search?keywords=${keywords}&displayName=${displayName}&searchInputType=${searchInputType}`);
      return {
        ...resp,
        prodSearch: (resp.prodSearch || []).map((prdSearch: any) => ({
          ...prdSearch,
          products: (prdSearch.products || []).map((p: any) => ({
            ...p,
            productNumber: p.partNumber
          }))
        }))
        
    }
  }

  async searchProducts({ input }: QuerySearchProductsEclipseArgs) {
    (input.selectedAttributes || []).push({
      attributeType: 'erpSystem',
      attributeValue: this.context.erpSystem
    });
    const resp = await this.post(`products/search`, { ...input });

    return {
      ...resp,
      products: (resp.products || []).map((p: any) => ({
        ...p,
        productNumber: p.partNumber
      }))
    };
  }
}
