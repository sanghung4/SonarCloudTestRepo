import querystring from 'querystring';
import {
  MutationAddToCountArgs,
  MutationCompleteCountArgs,
  MutationCreateWriteInArgs,
  MutationUpdateCountArgs,
  MutationUpdateWriteInArgs,
  MutationResolveWriteInArgs,
  QueryLocationArgs,
  QueryWriteInArgs,
  QueryWriteInsArgs,
  QueryNextLocationArgs,
  QueryPickingOrdersArgs,
  MutationAssignPickTaskArgs,
  QueryUserPicksArgs,
  MutationCompleteUserPickArgs,
  QueryGetProductSerialNumbersArgs,
  MutationUpdateProductSerialNumbersArgs,
  MutationStagePickTaskArgs,
  MutationAddCompletionMetricArgs,
  MutationStagePickTotePackagesArgs,
  MutationCloseTaskArgs,
  QueryShippingDetailsArgs
} from '../generated/graphql';
import { QueryCountArgs } from '../generated/graphql';
import { BaseAPI } from '../utils/baseAPI';

export class InventoryAPI extends BaseAPI {
  baseURL = process.env.API_URL_INVENTORY_SERVICE;

  async getCount({ id, branchId }: QueryCountArgs) {
    const resp = await this.get(`branches/${branchId}/counts/${id}`);

    return {
      id: resp.countId,
      erpSystem: resp.erpSystem,
      branch: {
        id: resp.branchId,
        name: resp.branchName
      }
    };
  }

  async getLocations() {
    const { countId, branchId } = this.context;
    const { locations, ...resp } = await this.get(
      `branches/${branchId}/counts/${countId}/locations`
    );

    return {
      content: locations,
      ...resp
    };
  }

  async getLocation({ id }: QueryLocationArgs) {
    const { products, ...resp } = await this.get(`locations/${id}`);

    return {
      items: products,
      ...resp
    };
  }

  async getNextLocation({ id }: QueryNextLocationArgs) {
    const { locationId } = await this.get(`locations/${id}/_next`);
    return {
      locationId
    };
  }

  async addToCount({ item }: MutationAddToCountArgs) {
    const { locationId, ...body } = item;
    const resp = await this.post(`locations/${locationId}/_add`, body);

    return {
      success: true,
      message: 'successfully added item',
      item: resp
    };
  }

  async updateCount({ item }: MutationUpdateCountArgs) {
    const { locationId, ...body } = item;
    await this.post(`locations/${locationId}`, body);

    return {
      success: true,
      message: 'successfully updated count'
    };
  }

  async completeCount({ locationId }: MutationCompleteCountArgs) {
    await this.post(`locations/${locationId}/_commit`);

    return {
      success: true,
      message: 'successfully completed count'
    };
  }

  async getWriteIns({ options }: QueryWriteInsArgs) {
    const { sort, ...input } = options;

    const query = querystring.stringify({
      sort: sort ? `${sort.property},${sort.direction}` : '',
      ...input
    });

    return this.get(`write-ins`, query);
  }

  async getWriteIn({ id }: QueryWriteInArgs) {
    return this.get(`write-ins/${id}`);
  }

  async createWriteIn({ writeIn }: MutationCreateWriteInArgs) {
    const resp = await this.post(`write-ins`, writeIn);

    return {
      success: true,
      message: 'successfully created write-in',
      content: resp
    };
  }

  async updateWriteIn({ id, writeIn }: MutationUpdateWriteInArgs) {
    const resp = await this.put(`write-ins/${id}`, writeIn);

    return {
      success: true,
      message: 'successfully updated write-in',
      content: resp
    };
  }

  async resolveWriteIn({ id }: MutationResolveWriteInArgs) {
    const resp = await this.patch(`write-ins/${id}/_resolve`);

    return {
      success: true,
      message: 'successfully resolved write-in',
      content: resp
    };
  }

  async getPickingTasks({ input }: QueryPickingOrdersArgs) {
    const { branchId, userId } = input;
    const resp = await this.get(`picking/tasks?branchId=${branchId}&userId=${userId}`);

    return resp.results;
  }

  async assignPickingTasks({ input }: MutationAssignPickTaskArgs) {
    const requestBody = {
      warehousePickTasksList: [
        { ...input }
      ]
    }
    return await this.put('picking/tasks', requestBody);
  }

  async getUserPicks({ input }: QueryUserPicksArgs) {
    const { branchId, orderId, userId } = input;
    const resp = await this.get(`picking/user?branchId=${branchId}&userId=${userId}&orderId=${orderId}`);

    return resp.results;
  }

  async completeUserPick({ input }: MutationCompleteUserPickArgs) {
    const resp = await this.put(`picking/user/pick/${input.warehouseID}`, input);

    return resp;
  }

  async getProductSerialNumbers({ warehouseId }: QueryGetProductSerialNumbersArgs) {
    const resp = await this.get(`picking/pick/${warehouseId}/serialNumbers`);

    return resp;
  }

  async updateProductSerialNumbers({ input }: MutationUpdateProductSerialNumbersArgs) {
    const { warehouseId } = input;
    const resp = await this.put(`picking/pick/${warehouseId}/serialNumbers`, input);

    return resp;
  }

  async stagePickTask({ input }: MutationStagePickTaskArgs) {
    const { orderId, branchId, invoiceId, tote, location } = input;
    await this.put(`picking/tasks/stageLocation`, {
      invoiceNumber: `${orderId}.${invoiceId}`,
      branchId,
      tote,
      location
    });

    return {
      success: true,
      message: `Successfully staged picking tasks for ${orderId}`
    }
  }

  async closeTask({ input }: MutationCloseTaskArgs) {
    const { orderId, branchId, invoiceId, tote, finalLocation } = input;
    await this.put(`picking/close`, {
      invoiceNumber: `${orderId}.${invoiceId}`,
      branchId,
      finalLocation,
      tote,
      skipStagedWarningFlag: true,
      skipInvalidLocationWarningFlag: true,
      updateLocationOnly: false
    });

    return {
      success: true,
      message: `Successfully closed picking tasks for ${orderId}`
    }
  }

  async addCompletionMetric({ metric }: MutationAddCompletionMetricArgs) {
    const resp = await this.post(`/metrics/completion`, metric);

    return {
      success: true,
      message: 'Successfully recorded completion metric',
      content: resp
    }
  }

  async getShippingDetails({ input }: QueryShippingDetailsArgs) {
    const { invoiceNumber } = input;
    return this.get(`/picking/shipping?invoiceNumber=${invoiceNumber}`);
  }

  async stagePickTotePackages({ input }: MutationStagePickTotePackagesArgs) {
    const { orderId, branchId, invoiceId, tote, packageList } = input;
    await this.put(`picking/tasks/stage/totePackages`, {
      invoiceNumber: `${orderId}.${invoiceId}`,
      branchId,
      tote,
      packageList
    });

    return {
      success: true,
      message: `Successfully updated package quantities for ${orderId}`
    }
  }
}
