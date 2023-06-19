import gql from 'graphql-tag';

export default gql`
    type MetricsBranch {
        id: String
        city: String
        state: String
        userCount: Int
        loginCount: Int
    }

    type MetricsChange {
        metric: String!
        quantity: String
        percentageChange: String
    }

    type MetricsDivision {
        division: String!
        userCount: Int
        loginCount: Int
        branchCount: Int
        branches: [MetricsBranch]
    }

    type MetricsOverview {
        type: String!
        response: [MetricsChange]
    }

    type MetricsPercentageChangeDivision {
        division: String!
        userChange: String
        loginChange: String
        branchChange: String
    }

    type MetricsPercentageChange {
        type: String!
        response: [MetricsPercentageChangeDivision]
    }

    type MetricsPercentageTotalDivision {
        division: String!
        userCount: Int
        userPercentage: Float
        loginCount: Int
        loginPercentage: Float
        branchCount: Int
        branchPercentage: Float
    }

    type MetricsPercentageTotal {
        type: String!
        response: [MetricsPercentageTotalDivision]
    }

    type MetricsLoginResponse {
        success: Boolean!
        message: String!
    }

    type MetricsUsage {
        type: String!
        response: [MetricsDivision]
    }

    input MetricsSingleRangeInput {
        startDate: String
        endDate: String
    }

    input MetricsDoubleRangeInput {
        startDateWeekOld: String!
        endDateWeekOld: String!
        startDateWeekNew: String!
        endDateWeekNew: String!
    }

    type Query {
        totalOverview(input: MetricsDoubleRangeInput!): MetricsOverview!
        totalUsage(input: MetricsSingleRangeInput!): MetricsUsage!
        percentageChange(input: MetricsDoubleRangeInput!): MetricsPercentageChange!
        percentageTotal(input: MetricsSingleRangeInput!): MetricsPercentageTotal!
    }

    type Mutation {
        registerLogin: MetricsLoginResponse! 
    }


`;