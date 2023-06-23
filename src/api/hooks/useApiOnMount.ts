import { useEffect, useState } from 'react';

import { AxiosResponse } from 'axios';
import { Maybe } from 'yup';

import { APIBaseCall, useApiBase, UseAPIBaseProps } from 'api/hooks/useApiBase';
import { useAuthContext } from 'providers/AuthProvider';

/**
 * Types
 */
export type UseAPIOnMountProps<Res, Req = object> = UseAPIBaseProps<
  Res,
  Req
> & {
  skip?: boolean;
};
export type APIOnMountRefetchProps<Req = object> = Partial<
  Pick<UseAPIBaseProps<object, Req>, 'header' | 'body' | 'url'>
>;
export type APIOnMountRefetch<Response, Request = object> = (
  props?: APIOnMountRefetchProps<Request>
) => Promise<Maybe<AxiosResponse<Response, Request>>>;

/**
 * Hook
 */
export function useApiOnMount<Response, Request = object>({
  skip,
  ...props
}: UseAPIOnMountProps<Response, Request>) {
  /**
   * Context
   */
  const { sessionId, authState } = useAuthContext();

  /**
   * API
   */
  const { called, call, ...rest } = useApiBase<Response, Request>(props);

  /**
   * State
   */
  const [data, setData] = useState<Maybe<Response>>();

  /**
   * Callback
   */
  const asyncCall: APIBaseCall<Response, Request> = async (myProps) => {
    const res = await call(myProps);
    setData(res?.data);
    return res;
  };
  const refetch: APIOnMountRefetch<Response, Request> = async (myProps) =>
    await asyncCall({ ...props, ...myProps });

  /**
   * Effect
   */
  useEffect(() => {
    const loggedIn =
      props.options.auth && (sessionId || authState?.isAuthenticated);
    const noAuth = !props.options.auth;
    if (!called && !skip && (loggedIn || noAuth)) {
      asyncCall(props);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [called, skip, sessionId, authState?.isAuthenticated]);

  /**
   * Output
   */
  return { ...rest, called, data, refetch };
}
