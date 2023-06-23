import { LoginResponse } from 'api/login.api';

export const mockLoginFail: LoginResponse = {
  success: false,
  sessionId: null
};

export const mockLoginSuccess: LoginResponse = {
  success: true,
  sessionId: 'TEST_SESSION_ID'
};
