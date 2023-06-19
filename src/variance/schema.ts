import gql from 'graphql-tag';

export default gql`
    enum VarianceItemStatus {
        NONVARIANCE
        UNCOUNTED
        STAGED
        COMMITTED
      }

    type VarianceSummaryResponse {
        netTotalCost: String
        grossTotalCost: String
        locationQuantity: Int
        productQuantity: Int
        differenceQuantity: Int
        differencePercentage: Float
    }

    type VarianceDetailsResponse {
        success: Boolean!
        message: String!
    }

    type VarianceLocationResponse {
        id: String!
        totalProducts: Int!
        totalCounted: Int!
        committed: Boolean!
        netVarianceCost: Float!
        grossVarianceCost: Float!
        items: [VarianceLocationItem!]!
    }

    type VarianceLocationItem {
        id: ID
        locationId: String
        prodDesc: String
        prodNum: String!
        tagNum: String
        catalogNum: String
        uom: String
        quantity: Int
        status: LocationItemStatus
        sequence: Int
        varianceCost: Float
        varianceStatus: VarianceItemStatus
        productImageUrl: String
    }

    type VarianceNextLocationResponse {
        locationId: String!
    }

    type VarianceLocationsResponse {
        totalLocations: Int!
        content: [VarianceLocationSummary!]!
      }

      type VarianceLocationSummary {
        id: String!
        totalProducts: Int!
        netVarianceCost: Float!
        grossVarianceCost: Float!
      }

    type DetailedVarianceResponse {
        counts: [VarianceDetails]
    }

    type VarianceDetails {
        location: String!
        erpProductID: String!
        productDescription: String!
        countQty: Int
        onHandQty: Int
        qtyDeviance: Int
        onHandCost: Float
        percentDeviance: Float
        countedCost: Float
        notCountedFlag: Boolean
        recount1Qty: Int
        recount2Qty: Int
        recount3Qty: Int
    }

    type Query {
        varianceSummary: VarianceSummaryResponse!
        varianceLocations: VarianceLocationsResponse!
        varianceLocation(id: String!): VarianceLocationResponse!
        varianceNextLocation(id: String!): VarianceNextLocationResponse!
        varianceDetails: DetailedVarianceResponse!
    }

    type Mutation {
        loadVarianceDetails:  VarianceDetailsResponse!
        updateVarianceCount(item: ItemInput!): CountMutationResponse!
        completeVarianceCount(locationId: String!): CountMutationResponse!
    }
`;
