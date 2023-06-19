import {
    QueryVarianceNextLocationArgs,
    QueryVarianceLocationArgs,
    MutationUpdateCountArgs,
    MutationCompleteVarianceCountArgs
} from './../generated/graphql';
import { BaseAPI } from '../utils/baseAPI';

export class VarianceAPI extends BaseAPI {
    baseURL = process.env.API_URL_INVENTORY_SERVICE;

    async getVarianceSummary() {
        return this.get(`variance/summary`);
    }

    async getVarianceDetails() {
        return this.get(`variance/details`);
    }

    async loadVarianceDetails() {
        try {
            await this.post(`variance/_load`);
            return {
                "success": true,
                "message": "Variance loaded successfully"
            }
        } catch(e) {
            return {
                "success": false,
                "message": "Variance failed to load"
            }
        }
    }

    async getVarianceLocations() {
        const { countId, branchId } = this.context;
        const { locations, ...resp } = await this.get(
          `variance/branches/${branchId}/counts/${countId}/locations`
        );
        return {
          content: locations,
          ...resp
        };
    }

    async getVarianceLocation({id}: QueryVarianceLocationArgs) {
        const { products, ...resp } = await this.get(`variance/locations/${id}`);
        return {
          items: products,
          ...resp
        };
    }

    async getVarianceNextLocation({id}: QueryVarianceNextLocationArgs) {
        const { locationId } = await this.get(`variance/locations/${id}/_next`);
        return {
          locationId
        };
    }

    async updateVarianceCount({ item }: MutationUpdateCountArgs) {
      try {
        const { locationId, ...body } = item;
        await this.post(`variance/locations/${locationId}`, body);
        return {
          success: true,
          message: 'successfully updated count'
        };
      } catch(e) {
        console.warn(e);
        return {
          success: false,
          message: 'failed to update count'
        }
      }

    }

    async completeVarianceCount({locationId}: MutationCompleteVarianceCountArgs) {
      try {
        await this.post(`variance/locations/${locationId}/_commit`);
        return {
          success: true,
          message: 'successfully completed count'
        };
      } catch(e) {
        console.warn(e);
        return {
          success: false,
          message: 'failed to complete count'
        };
      }
    }
}
