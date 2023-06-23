import { CatalogDetailResponse } from 'api/catalog.api';
import {
  mockCatalog1,
  mockCatalog2,
  mockProductCatalog1,
  mockProductCatalog4
} from 'api/mocks/catalog.mocks';
import { mockCustomer1, mockCustomer2 } from 'api/mocks/customer.mocks';
import { times } from 'lodash-es';

/**
 * Types
 */
type Mocks = {
  success: CatalogDetailResponse;
  empty: CatalogDetailResponse;
  null: CatalogDetailResponse;
};

/**
 * Configs
 */
const itemsPerPage = 10;
const count = 24;

/**
 * Prepare
 */
const dummyProductCatalog = (i: number) => ({
  ...mockProductCatalog1,
  id: `product-${i}`
});
const productCatalogs = times(count, dummyProductCatalog);
const totalPages = Math.ceil(count / itemsPerPage);

/**
 * Mocks
 */
const mocks: Mocks = {
  success: {
    page: totalPages - 1,
    totalItems: count,
    totalPages,
    resultsPerPage: itemsPerPage,
    customer: { ...mockCustomer1 },
    catalog: { ...mockCatalog1 },
    results: [...productCatalogs]
  },
  empty: {
    page: 1,
    totalItems: 1,
    totalPages: 1,
    resultsPerPage: itemsPerPage,
    customer: { ...mockCustomer2 },
    catalog: { ...mockCatalog2 },
    results: [mockProductCatalog4]
  },
  null: {
    page: 0,
    totalItems: 0,
    totalPages: 0,
    resultsPerPage: 0,
    customer: null,
    catalog: null,
    results: []
  }
};

export default mocks;
