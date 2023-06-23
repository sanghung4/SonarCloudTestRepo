import { Catalog } from 'api/catalog.api';
import { Customer, CustomerRegion } from 'api/customer.api';
import { mockCatalog1 } from 'api/mocks/catalog.mocks';
import { mockCustomer1, mockRegion1 } from 'api/mocks/customer.mocks';

/**
 * Types
 */
type Mocks = {
  catalog: Catalog;
  customer: Customer;
  region: CustomerRegion;
};

/**
 * Mocks
 */
const mocks: Mocks = {
  catalog: { ...mockCatalog1 },
  customer: {
    ...mockCustomer1,
    regions: [mockRegion1],
    catalogs: [mockCatalog1]
  },
  region: { ...mockRegion1 }
};

export default mocks;
