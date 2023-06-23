import { Maybe } from 'yup';

import { APIOptions } from 'api/hooks/useApiBase';
import { useApiMutation, UseAPIMutationProps } from 'api/hooks/useApiMutation';

/**
 * Types
 */
// Request Body
export type LoginRequest = {
  email: string;
  password: string;
};
// Response
export type LoginResponse = {
  success: boolean;
  sessionId: Maybe<string>;
};

/**
 * APIs
 */
// 🔵 POST security/login
export function useApiLogin(options?: APIOptions<LoginResponse>) {
  // Props
  const apiProps: UseAPIMutationProps<LoginResponse> = {
    url: 'security/login',
    kind: 'post',
    options: { ...options, auth: false },
    header: {}
  };
  // API
  const api = useApiMutation<LoginResponse>(apiProps);
  const call = async (body: LoginRequest) =>
    await api.call({ ...apiProps, body });
  return { ...api, call };
}
