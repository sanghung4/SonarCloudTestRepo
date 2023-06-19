import { MutationVerifyEclipseCredentialsArgs } from '../generated/graphql';
import { BaseAPI } from '../utils/baseAPI';

export class AccountAPI extends BaseAPI {
  baseURL = process.env.API_URL_ECLIPSE_SERVICE;

  async validateEclipseCredentials({ username, password }: MutationVerifyEclipseCredentialsArgs) {
    try {
      await this.post(`credentials/_valid`, { username, password, sessionId: `${username}:${password}` });
      return {
        success: true,
        message: 'Eclipse credentials validated'
      }
    } catch (e) {
      return {
        success: false,
        message: 'Failed to validate Eclipse credentials'
      }
    }
  }
}
