import {
  MutationSplitQuantityArgs,
  MutationCloseOrderArgs,
  QueryValidateBranchArgs,
} from '../generated/graphql';

import { Request } from 'apollo-server-env';
import { BaseAPI } from '../utils/baseAPI';

export class PickingAPI extends BaseAPI {
  baseURL = process.env.API_URL_ECLIPSE_SERVICE;

  protected didEncounterError(error: Error, req: Request) {
    return null;
  }

  async getProductImageUrl(productId: String) {
    try {
      return this.get(`product/${productId}/imageUrl`);
    } catch (e) {
      return null;
    }
  }

  async splitQuantity({ input }: MutationSplitQuantityArgs) {
    return this.post(`/picking/splitQuantity`, input);
  }

  async closeOrder({ input }: MutationCloseOrderArgs) {
    return this.post(`/picking/closeOrder`, input);
  }

  async validateBranch({ input }: QueryValidateBranchArgs) {
     const { branchId } = input;
     return this.get(`/picking/validateBranch/${branchId}`);
  }

}
