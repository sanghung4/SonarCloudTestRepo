import {
  QueryUserBranchArgs,
  MutationAddUserArgs,
  MutationUpdateUserEmailArgs,
  MutationRemoveUserBranchArgs,
  MutationDeleteUserArgs

} from '../generated/graphql';
import { BaseAPI } from '../utils/baseAPI';

export class UserAPI extends BaseAPI {
  baseURL = process.env.API_URL_PRICING_SERVICE;

  async getUserBranch({ username }: QueryUserBranchArgs) {
    try {
      return this.get(`users/${username}`);
    } catch (e) {
      return null;
    }
  }

  async getBranchUsersList() {
    try {
      return this.get(`users/branchUsersList`);
    } catch (e) {
      return null;
    }
  }

  async addUser({ userEmail, branchId }: MutationAddUserArgs) {
    return this.post(`users/${branchId}/${userEmail}`);
  }

  async updateUserEmail({ oldUserEmail, newUserEmail }: MutationUpdateUserEmailArgs) {
    return this.put(`users/${oldUserEmail}/${newUserEmail}`);
  }


  async removeUserBranch({ userEmail, branchId }: MutationRemoveUserBranchArgs) {
    return this.delete(`users/${branchId}/${userEmail}`);
  }

  async deleteUser({ userEmail }: MutationDeleteUserArgs) {
    return this.delete(`users/${userEmail}`);
  }
}
