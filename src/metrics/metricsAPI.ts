import { QueryTotalUsageArgs,
    QueryTotalOverviewArgs, 
    QueryPercentageChangeArgs,
    QueryPercentageTotalArgs} from './../generated/graphql';
import { BaseAPI } from '../utils/baseAPI';

export class MetricsAPI extends BaseAPI {
    baseURL = process.env.API_URL_INVENTORY_SERVICE;

    async getTotalOverview({input: { startDateWeekOld, endDateWeekOld, startDateWeekNew, endDateWeekNew }}: QueryTotalOverviewArgs) {
        return this.get(`metrics/totalOverview?startDateWeekOld=${startDateWeekOld}&endDateWeekOld=${endDateWeekOld}&startDateWeekNew=${startDateWeekNew}&endDateWeekNew=${endDateWeekNew}`);
    }

    async getTotalUsage({input: {startDate, endDate}}: QueryTotalUsageArgs) {
        return this.get(`metrics/totalUsage?startDate=${startDate}&endDate=${endDate}`);
    }

    async getPercentageChange({input: { startDateWeekOld, endDateWeekOld, startDateWeekNew, endDateWeekNew }}: QueryPercentageChangeArgs) {
        return this.get(`metrics/percentageChange?startDateWeekOld=${startDateWeekOld}&endDateWeekOld=${endDateWeekOld}&startDateWeekNew=${startDateWeekNew}&endDateWeekNew=${endDateWeekNew}`);
    }

    async getPercentageTotal({input: {startDate, endDate}}: QueryPercentageTotalArgs) {
        return this.get(`metrics/percentageTotal?startDate=${startDate}&endDate=${endDate}`);
    }

    async registerLogin() {
        return this.post(`metrics/_login`); // Auth token and x-branchid header passed via BaseAPI
    }
}