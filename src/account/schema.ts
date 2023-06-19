import gql from 'graphql-tag';

export default gql`
  type LinkAccountResponse {
    success: Boolean!
    message: String
  }

  type Mutation {
    verifyEclipseCredentials(username: String!, password: String!): LinkAccountResponse!
  }
`;
