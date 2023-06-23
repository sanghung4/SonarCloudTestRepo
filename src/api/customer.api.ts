import { Maybe } from 'yup';

import { Catalog } from 'api/catalog.api';
import { APIOptions } from 'api/hooks/useApiBase';
import { useApiOnMount, UseAPIOnMountProps } from 'api/hooks/useApiOnMount';

/**
 * Types
 */
// Data
export type CustomerRegion = {
  id: string;
  name: string;
  customerId: string;
};
export type Customer = {
  id: string;
  customerId: string;
  branchId: Maybe<string>;
  branchName: Maybe<string>;
  erpId: Maybe<string>;
  name: string;
  erpName: Maybe<string>;
  billTo: Maybe<boolean>;
  lastUpdate: Maybe<string>;
  contactName: Maybe<string>;
  contactPhone: Maybe<string>;
  regions: CustomerRegion[];
  catalogs: Catalog[];
};

// Response
export type CustomerListResponse = {
  customers: Customer[];
};

/**
 * APIs
 */
// 🔵 GET customer/list
export function useApiCustomerList(options?: APIOptions<CustomerListResponse>) {
  // Props
  const apiProps: UseAPIOnMountProps<CustomerListResponse> = {
    url: 'customer/list',
    kind: 'get',
    options: { ...options, auth: true },
    header: {}
  };

  // API
  const api = useApiOnMount<CustomerListResponse>(apiProps);
  const refetch = async () => await api.refetch();
  return { ...api, refetch };
}

// 🔵 GET customer/detail/{customerId}
export function useApiCustomerDetail(
  customerId?: string,
  options?: APIOptions<Customer>
) {
  // Props
  const apiProps: UseAPIOnMountProps<Customer> = {
    url: `customer/detail/${customerId}`,
    kind: 'get',
    skip: !customerId,
    options: { ...options, auth: true },
    header: {}
  };
  // API
  const api = useApiOnMount<Customer>(apiProps);
  const refetch = async () => await api.refetch();

  // Output
  return { ...api, refetch };
}
