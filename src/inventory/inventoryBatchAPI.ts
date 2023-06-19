import { MutationDeleteCountArgs, MutationLoadCountArgs, MutationPurgeMincronCountsArgs, MutationRemoveCountsArgs, MutationRemoveBranchCountsArgs, QueryCountsArgs, QueryCountStatusArgs } from '../generated/graphql';
import { BaseAPI } from '../utils/baseAPI';

export class InventoryBatchAPI extends BaseAPI {
    baseURL = process.env.API_URL_INVENTORY_BATCH_SERVICE;

    async getCounts({ input: { startDate, endDate } }: QueryCountsArgs) {
        return this.get(`counts?startDate=${startDate}&endDate=${endDate}`);
    }

    async getCountStatus({ id }: QueryCountStatusArgs) {
        return this.get(`counts/${id}`);
    }

    async loadCount(args: MutationLoadCountArgs) {
        const { branchId, countId } = args;
        return this.put(`counts/_load?countId=${countId}&branchId=${branchId}`);
    }

    async deleteCount(args: MutationDeleteCountArgs) {
        const { id } = args;
        return this.put(`counts/${id}/_delete`);
    }

    async purgeMincronCounts({ input }: MutationPurgeMincronCountsArgs) {
        return this.put(`counts/remove`, input);
    }

    async removeCounts({ input }: MutationRemoveCountsArgs) {
        const resp = await this.put(`counts/_delete`, input);
        return resp;
    }
    async removeBranchCounts({ input }: MutationRemoveBranchCountsArgs) {
        const resp = await this.put(`counts/branch/_delete`, input);
        return resp;
    }
}
