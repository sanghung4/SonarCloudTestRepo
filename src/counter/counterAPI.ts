import { QueryGetCustomerSearchArgs } from './../generated/graphql';
import { BaseAPI } from '../utils/baseAPI';

export class CounterAPI extends BaseAPI {
  baseURL = process.env.API_URL_INVENTORY_SERVICE;

  async getCustomerSearch({input}: QueryGetCustomerSearchArgs) {
    try {
      const {...body} = input;
      const resp = await this.post(`customer/_search`, body);
      return resp;
    } catch (e) {
      return null;
    }
  }
}
