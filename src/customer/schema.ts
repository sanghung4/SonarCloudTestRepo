import gql from 'graphql-tag';

export default gql`
  type CustomerResponse {
    id: String!
    homeBranch: String!
    name: String!
    isBillTo: Boolean
    isShipTo: Boolean
    addressLine1: String
    addressLine2: String
    city: String
    state: String
    postalCode: String
    countryCode: String
  }

  type Query {
    customer(customerId: String!): CustomerResponse!
  }
`;
