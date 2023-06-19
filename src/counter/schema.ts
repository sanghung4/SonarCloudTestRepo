import gql from 'graphql-tag';

export default gql`
    input CustomerSearchInput {
        id: [String]
        keyword: String
        pageSize: Int
        currentPage: Int
      }

    type CustomerSearchResponse {
        metadata: EclipseSearchMetadata
        results: [CustomerSearchResult]
    }

    type EclipseSearchMetadata {
        startIndex: Int
        pageSize: Int
        totalItems: Int
    }

    type CustomerSearchResult {
        name: String
        addressLine1: String 
        addressLine2: String
        addressLine3: String
        addressLine4: String
        city: String
        state: String
        postalCode: String
        countryCode: String
        isBillTo: Boolean
        isShipTo: Boolean
        isBranchCash: Boolean
        isProspect: Boolean
        sortBy: String
        nameIndex: String
        billToId: String
        defaultPriceClass: String
        ediId: String
        orderEntryMessage: String
        updateKey: String
        shipToLists: [ShipToId!]!
        id: String
    }

    type ShipToId {
        shipToId: String
    }

    type Query {
        getCustomerSearch(input: CustomerSearchInput!): CustomerSearchResponse
    }

`;