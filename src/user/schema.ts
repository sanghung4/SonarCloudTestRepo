import gql from 'graphql-tag';

export default gql`

  type UserResponse {
    username: String!
    branch: [String]
  }

  type BranchUsersListResponse {
    branchId: String!
    assignedUsers: [String]
  }

  type ErrorResponse {
    code:String!
    message: String!
    details: String
  }

  type UserBranchResponse{
    success: Boolean!
    error: ErrorResponse
  }

  type Query {
    userBranch(username: String!): UserResponse!
    branchUsersList: [BranchUsersListResponse]!
  }

  type Mutation {
    addUser(branchId: String!, userEmail: String!) : UserBranchResponse!
    updateUserEmail(oldUserEmail: String!, newUserEmail: String!): UserBranchResponse!
    removeUserBranch(branchId: String!, userEmail: String!): UserBranchResponse!
    deleteUser(userEmail: String!): UserBranchResponse!
  }
`;
