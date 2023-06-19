import {
  QueryCustomerArgs,
} from '../generated/graphql';
import { BaseAPI } from '../utils/baseAPI';

export class CustomerAPI extends BaseAPI {
  baseURL = process.env.API_URL_ECLIPSE_SERVICE;

  async getCustomer({ customerId }: QueryCustomerArgs) {
    try{
      return this.get(`customers/${customerId}`);
    } catch (e) {
      return null;
    }
  }

}
