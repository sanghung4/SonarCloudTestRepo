import { Customer, CustomerRegion } from 'api/customer.api';

export const mockRegion1: CustomerRegion = {
  id: 'region-id-1',
  name: 'EHGDFWTX',
  customerId: 'test-id-1'
};
export const mockRegion2: CustomerRegion = {
  id: 'region-id-2',
  name: 'EHGDFWTX',
  customerId: 'test-id-2'
};
export const mockRegion3: CustomerRegion = {
  id: 'region-id-3',
  name: 'EHGHTX',
  customerId: 'test-id-3'
};

export const mockCustomer1: Customer = {
  id: 'test-id-1',
  customerId: '1111',
  branchId: '1111',
  branchName: 'Dallas, TX',
  name: 'Test Branch 1',
  billTo: true,
  lastUpdate: '2023-02-03T00:00:00.000+0000',
  contactName: 'Chris Johnson',
  contactPhone: '469-555-5555',
  regions: [],
  erpId: '1234',
  erpName: 'testErp',
  catalogs: []
};
export const mockCustomer2: Customer = {
  id: 'test-id-2',
  customerId: '2222',
  branchId: '2222',
  branchName: 'Plano, TX',
  name: 'Test Branch 2',
  billTo: true,
  lastUpdate: '2021-07-20T00:00:00.000+0000',
  contactName: 'Arthur Guo',
  contactPhone: '406-555-5555',
  regions: [],
  erpId: null,
  erpName: null,
  catalogs: []
};
export const mockCustomer3: Customer = {
  id: 'test-id-3',
  customerId: '3333',
  branchId: null,
  branchName: null,
  name: 'Test Branch 3',
  billTo: null,
  lastUpdate: null,
  contactName: null,
  contactPhone: null,
  regions: [],
  erpId: null,
  erpName: null,
  catalogs: []
};
