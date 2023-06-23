import { CustomerListResponse } from 'api/customer.api';
import {
  mockCustomer1,
  mockCustomer2,
  mockCustomer3,
  mockRegion1,
  mockRegion2,
  mockRegion3
} from 'api/mocks/customer.mocks';

/**
 * Mocks
 */
const mocks: CustomerListResponse = {
  customers: [
    { ...mockCustomer1, regions: [mockRegion1] },
    { ...mockCustomer2, regions: [mockRegion2] },
    { ...mockCustomer3, regions: [mockRegion3] }
  ]
};
export default mocks;
