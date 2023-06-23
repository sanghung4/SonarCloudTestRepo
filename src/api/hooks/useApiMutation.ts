import { useState } from 'react';

import { Maybe } from 'yup';

import { APICoreProps } from 'api/core';
import { useApiBase, UseAPIBaseProps } from 'api/hooks/useApiBase';

/**
 * Types
 */
export type UseAPIMutationProps<Response, Request = object> = Omit<
  UseAPIBaseProps<Response, Request>,
  'body'
>;

/**
 * Hook
 */
export function useApiMutation<Response, Request = object>(
  props: UseAPIMutationProps<Response, Request>
) {
  /**
   * API
   */
  const { call, ...rest } = useApiBase<Response, Request>(props);

  /**
   * State
   */
  const [data, setData] = useState<Maybe<Response>>();

  /**
   * Callback
   */
  const asyncCall = async (myProps: APICoreProps<Request>) => {
    const res = await call(myProps);
    setData(res?.data);
    return res;
  };

  /**
   * Output
   */
  return { ...rest, data, call: asyncCall };
}
