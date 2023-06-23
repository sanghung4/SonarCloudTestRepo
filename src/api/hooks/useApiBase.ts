import { useState } from 'react';

import { AxiosError, AxiosResponse } from 'axios';
import { useNavigate } from 'react-router-dom';
import { Maybe } from 'yup';

import { apiCall, ApiCallKinds, APICoreProps, HeaderProps } from 'api/core';
import { useAuthContext } from 'providers/AuthProvider';

/**
 * Types
 */
// Common
export type APIOnCompleted<Response> = (res: AxiosResponse<Response>) => void;
export type APIOnError = (e: unknown) => void;
export type APIOptions<Response> = {
  onCompleted?: APIOnCompleted<Response>;
  onError?: APIOnError;
};
export type APIAuthOption = {
  auth?: boolean;
};

// Base
export type APIBaseCall<Response, Request = object> = (
  myProps?: APICoreProps<Request>
) => Promise<Maybe<AxiosResponse<Response, Request>>>;
export type UseAPIBaseProps<Response, Request = object> = {
  url: string;
  kind: ApiCallKinds;
  options: APIOptions<Response> & APIAuthOption;
  header: HeaderProps;
  body?: Request;
};

/**
 * Hook
 */
export function useApiBase<Response, Request = object>(
  props: UseAPIBaseProps<Response, Request>
) {
  /**
   * props
   */
  const { url, kind, options, body } = props;

  /**
   * Custom hooks
   */
  const navigate = useNavigate();

  /**
   * Context
   */
  const { sessionId, setSessionId, authState } = useAuthContext();
  const oktaToken = authState?.accessToken?.accessToken;
  const xAuthToken = sessionId ?? undefined;
  const header = options.auth
    ? oktaToken
      ? { ...props.header, Authorization: `Bearer ${oktaToken}` }
      : { ...props.header, 'X-Auth-Token': xAuthToken }
    : props.header;

  /**
   * State
   */
  const [loading, setLoading] = useState(false);
  const [called, setCalled] = useState(false);

  /**
   * Callback
   */
  // 🟤 Cb - MAIN async call
  const call: APIBaseCall<Response, Request> = (
    myProps?: APICoreProps<Request>
  ) => {
    // Init
    setLoading(true);
    const props: APICoreProps<Request> = { url, kind, header, body };
    const selectedHeader = { ...header, ...myProps?.header };
    const selectedProps = { ...(myProps ?? props), header: selectedHeader };
    // Call
    const asyncCall = new Promise<Maybe<AxiosResponse<Response, Request>>>(
      (resolve) => {
        apiCall<Response, Request>(selectedProps)
          .then((res) => {
            resolve(res);
            options.onCompleted?.(res);
          })
          .catch((e) => {
            resolve(null);
            handleError(e);
          })
          .finally(() => {
            setCalled(true);
            setLoading(false);
          });
      }
    );
    return asyncCall;
  };
  // 🟤 Cb - Error handling
  const handleError = (e: unknown) => {
    console.error(e);
    options.onError?.(e);
    if (e instanceof AxiosError && e?.response?.status === 403) {
      setSessionId(null);
      navigate('/login');
    }
  };

  /**
   * Output
   */
  return { called, loading, call };
}
