import { Maybe } from 'yup';

import { Customer } from 'api/customer.api';
import { APIOptions } from 'api/hooks/useApiBase';
import { useApiMutation, UseAPIMutationProps } from 'api/hooks/useApiMutation';
import { useApiOnMount, UseAPIOnMountProps } from 'api/hooks/useApiOnMount';
import { generateQueryParam } from 'util/generateQueryParam';
import { EncodedFile } from 'util/fileProcessor';

/**
 * Types
 */
// Data
export type Catalog = {
  id: string;
  status: string;
  fileName: Maybe<string>;
  name: string;
  lastUpdate: string;
  dateArchived: Maybe<string>;
  procSystem: Maybe<string>;
  skuQuantity: Maybe<number>;
};

export type ProductCatalog = {
  id: string;
  lastPullDatetime: Maybe<string>;
  listPrice: Maybe<number>;
  partNumber: Maybe<string>;
  product: Maybe<Product>;
  sellPrice: Maybe<number>;
  skuQuantity: Maybe<number>;
  uom: Maybe<string>;
};
export type Product = {
  id: string;
  name: Maybe<string>;
  description: Maybe<string>;
  imageFullSize: Maybe<string>;
  partNumber: Maybe<string>;
  manufacturer: Maybe<string>;
  categoryLevel1Name: Maybe<string>;
  categoryLevel2Name: Maybe<string>;
  categoryLevel3Name: Maybe<string>;
  unspsc: Maybe<string>;
  imageThumb: Maybe<string>;
  manufacturerPartNumber: Maybe<string>;
  deliveryInDays: Maybe<number>;
  maxSyncDatetime: Maybe<string>;
};

// Request
export type CatalogDetailParam = {
  perPage: number;
  page: number;
};
export type PostCatalogTitleRequest = {
  name: Maybe<string>;
};

export type CatalogUploadDetailParam = {
  catalogId?: string;
  perPage: number;
  page: number;
};

// Response
export type CatalogDetailResponse = {
  page: number;
  totalItems: number;
  totalPages: number;
  resultsPerPage: number;
  customer: Maybe<Customer>;
  catalog: Maybe<Catalog>;
  results: ProductCatalog[];
};
export type CatalogUploadResponse = {
  uploadId?: string;
  products: Product[];
  error?: string;
};
export type PostCatalogTitleResponse = {
  success: boolean;
  sessionId: Maybe<string>;
};

/**
 * APIs
 */
// 🔵 GET catalog/view/{catalogId} (unused)
export function useApiCatalogDetailGet(
  catalogId: Maybe<string>,
  param: CatalogDetailParam,
  options?: APIOptions<CatalogDetailResponse>
) {
  // Props
  const url = `catalog/view/${catalogId}`;
  const apiProps: UseAPIOnMountProps<CatalogDetailResponse> = {
    url: generateQueryParam(url, param),
    kind: 'get',
    skip: !catalogId,
    options: { ...options, auth: true },
    header: {}
  };

  // API
  const api = useApiOnMount<CatalogDetailResponse>(apiProps);
  const refetch = async (myParam?: CatalogDetailParam) =>
    await api.refetch({ url: generateQueryParam(url, myParam ?? param) });
  return { ...api, refetch };
}

// 🔵 POST catalog/view/{catalogId}
export function useApiCatalogDetail(
  body: string[],
  param: CatalogUploadDetailParam,
  options?: APIOptions<CatalogDetailResponse>
) {
  // Props
  const url = `catalog/view`;
  const apiProps: UseAPIOnMountProps<CatalogDetailResponse, string[]> = {
    url: generateQueryParam(url, param),
    kind: 'post',
    skip: !param?.catalogId,
    options: { ...options, auth: true },
    header: {},
    body: body
  };

  // API
  const api = useApiOnMount<CatalogDetailResponse, string[]>(apiProps);
  const refetch = async (
    myParam?: CatalogUploadDetailParam,
    myBody?: string[]
  ) =>
    await api.refetch({
      ...apiProps,
      url: generateQueryParam(url, myParam ?? param),
      body: myBody ?? body
    });
  return { ...api, refetch };
}

// 🔵 POST upload/{customer_id}
export function useApiCatalogUpload(
  customerId: Maybe<string>,
  options?: APIOptions<CatalogUploadResponse>
) {
  // Props
  const url = `upload/${customerId}`;
  const apiProps: UseAPIMutationProps<CatalogUploadResponse, EncodedFile> = {
    url,
    kind: 'post',
    options: { ...options, auth: true },
    header: {}
  };

  // API
  const api = useApiMutation<CatalogUploadResponse, EncodedFile>(apiProps);
  const call = async (body?: EncodedFile) =>
    await api.call({ ...apiProps, body });
  return { ...api, call };
}

// 🔵 POST catalog/${catalogId}/rename
export function useApiPostCatalogTitle(
  catalogId: Maybe<string>,
  options?: APIOptions<PostCatalogTitleResponse>
) {
  // Props
  const url = `catalog/${catalogId}/rename`;
  const apiProps: UseAPIMutationProps<PostCatalogTitleResponse> = {
    url: generateQueryParam(url, {}),
    kind: 'post',
    options: { ...options, auth: true },
    header: {}
  };

  // API
  const api = useApiMutation<PostCatalogTitleResponse>(apiProps);
  const call = async (body: PostCatalogTitleRequest) =>
    await api.call({ ...apiProps, body });
  return { ...api, call };
}

// 🔵 DELETE upload/{upload_id}
export function useApiDeleteCatalogUpload(
  uploadId: Maybe<string>,
  options?: APIOptions<null>
) {
  // Props
  const url = `upload/${uploadId}`;
  const apiProps: UseAPIMutationProps<null> = {
    url,
    kind: 'delete',
    options: { ...options, auth: true },
    header: {}
  };

  // API
  const api = useApiMutation<null>(apiProps);
  const call = async () => await api.call({ ...apiProps });
  return { ...api, call };
}
