import {
  InternalAxiosRequestConfig,
  AxiosResponse,
  AxiosError,
  AxiosHeaders
} from 'axios';

export const apiMockResponse = <Res, Req = { headers: '' }>(
  res: Res,
  status?: number
): AxiosResponse<Res, Req> => ({
  data: res,
  status: status ?? 200,
  statusText: '',
  headers: {},
  config: {} as InternalAxiosRequestConfig
});

export const apiMockError = (message?: string, code?: number) =>
  new AxiosError(
    message,
    '',
    { headers: new AxiosHeaders() },
    {},
    {
      status: code ?? 200,
      data: {},
      statusText: '',
      headers: {},
      config: { headers: new AxiosHeaders() }
    }
  );
