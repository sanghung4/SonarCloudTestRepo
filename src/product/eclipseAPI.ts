import { QueryProductDetailsArgs } from '../generated/graphql';

import { BaseAPI } from '../utils/baseAPI';

export class EclipseAPI extends BaseAPI {
    baseURL = process.env.API_URL_ECLIPSE_SERVICE;

    async getProductDetails({ productId }: QueryProductDetailsArgs) {
        return this.get(`product/${productId}`);
    }
}
