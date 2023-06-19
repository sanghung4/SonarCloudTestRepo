import { Resolvers } from '../generated/graphql';
import { Context } from '../context';

const metricsResolvers: Resolvers<Context> = {
    Query: {
        totalOverview:(_, args, {dataSources }) => {
            return dataSources.metricsAPI.getTotalOverview(args);
        },
        totalUsage:(_, args, {dataSources }) => {
            return dataSources.metricsAPI.getTotalUsage(args);
        },
        percentageChange:(_, args, {dataSources }) => {
            return dataSources.metricsAPI.getPercentageChange(args);
        },
        percentageTotal:(_, args, {dataSources }) => {
            return dataSources.metricsAPI.getPercentageTotal(args);
        },
    },
    Mutation: {
        registerLogin:(_, args, {dataSources}) => {
            try {
                dataSources.metricsAPI.registerLogin();
                return {
                    "success": true,
                    "message": "Login registration successful"
                }
            } catch(e) {
                console.error('Failed to register Okta login');
            }
            return {
                "success": false,
                "message": "Failed to register login"
            }
        }
    }
};

export default metricsResolvers;