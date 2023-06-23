import axios, { AxiosRequestConfig, AxiosResponse } from 'axios';
import { configuration } from 'util/configurations';

/**
 * Types
 */
export type ApiCallKinds = 'get' | 'delete' | 'post';
export type HeaderProps = {
  'X-Auth-Token'?: string;
  Authorization?: string;
};
export type APICoreProps<Request = object> = {
  url: string;
  kind: ApiCallKinds;
  header: HeaderProps;
  body?: Request;
};

/**
 * Config
 */
const host = configuration.apiUrl || '';
const headerConfig = { 'content-type': 'application/json' };
const otherConfig: AxiosRequestConfig = {
  maxBodyLength: configuration.maxBodyWidth,
  maxContentLength: configuration.maxContentWidth
};

/**
 * Main API Call method
 * @param props - APICoreProps<Request>
 * @returns AxiosResponse<Response>
 */
export async function apiCall<Response, Request = object>({
  kind,
  url,
  header,
  body
}: APICoreProps<Request>) {
  const config = { headers: { ...headerConfig, ...header }, ...otherConfig };
  switch (kind) {
    // GET
    case 'get':
      return await axios.get<Request, AxiosResponse<Response>>(
        `${host}/${url}`,
        config
      );
    // POST
    case 'post':
      return await axios.post<Request, AxiosResponse<Response>>(
        `${host}/${url}`,
        body ?? {},
        config
      );
    // DELETE
    case 'delete':
      return await axios.delete<Request, AxiosResponse<Response>>(
        `${host}/${url}`,
        { ...config, data: body }
      );
  }
}
