import { Catalog, Product, ProductCatalog } from 'api/catalog.api';

export const mockCatalog1: Catalog = {
  id: 'catalog-id-1',
  status: 'Active',
  fileName: 'test.jpg',
  name: 'test name',
  lastUpdate: '2022-10-31T00:00:00.000+0000',
  dateArchived: '2023-01-04T00:00:00.000+0000',
  procSystem: 'testSys',
  skuQuantity: 1234
};
export const mockCatalog2: Catalog = {
  id: 'catalog-id-2',
  status: 'Draft',
  fileName: 'test2.jpg',
  name: 'test name 2',
  lastUpdate: '2022-12-31T00:00:00.000+0000',
  dateArchived: '2023-02-03T00:00:00.000+0000',
  procSystem: 'testSys2',
  skuQuantity: 1235
};
export const mockCatalog3: Catalog = {
  id: 'catalog-id-3',
  status: 'Unknown',
  fileName: null,
  name: 'test name 3',
  lastUpdate: '1970-01-01T00:00:00.000+0000',
  dateArchived: null,
  procSystem: null,
  skuQuantity: null
};

export const mockProduct1: Product = {
  id: '123-456-789-012',
  name: 'Test Product 1',
  description: 'For unit test',
  imageFullSize: './test.jpg',
  partNumber: '1234',
  manufacturer: 'Test Man',
  categoryLevel1Name: 'Cat1',
  categoryLevel2Name: 'Cat2',
  categoryLevel3Name: 'Cat3',
  unspsc: 'abcd',
  imageThumb: './thumb.jpg',
  manufacturerPartNumber: 'm123',
  deliveryInDays: 10,
  maxSyncDatetime: '2023-02-03T00:00:00.000+0000'
};
export const mockProduct2: Product = {
  id: '098-765-432-109',
  name: 'Test Product 2',
  description: 'For unit test alt...',
  imageFullSize: 'test2',
  partNumber: '5678',
  manufacturer: 'Test Man 2',
  categoryLevel1Name: null,
  categoryLevel2Name: null,
  categoryLevel3Name: null,
  unspsc: 'efgh',
  imageThumb: 'thumb2',
  manufacturerPartNumber: 'm456',
  deliveryInDays: 5,
  maxSyncDatetime: '2004-08-15T00:00:00.000+0000'
};
export const mockProduct3: Product = {
  id: 'null',
  name: null,
  description: null,
  imageFullSize: null,
  partNumber: null,
  manufacturer: null,
  categoryLevel1Name: null,
  categoryLevel2Name: null,
  categoryLevel3Name: null,
  unspsc: null,
  imageThumb: null,
  manufacturerPartNumber: null,
  deliveryInDays: null,
  maxSyncDatetime: null
};

export const mockProductCatalog1: ProductCatalog = {
  id: '123-456-789-012',
  lastPullDatetime: '2004-08-15T00:00:00.000+0000',
  listPrice: 11.2,
  partNumber: '1234',
  sellPrice: 10.3,
  uom: 'abc',
  skuQuantity: 10,
  product: mockProduct1
};
export const mockProductCatalog2: ProductCatalog = {
  id: '098-765-432-109',
  lastPullDatetime: '2004-08-15T00:00:00.000+0000',
  listPrice: 1.4,
  partNumber: '5678',
  sellPrice: 10.2,
  uom: 'def',
  skuQuantity: 3,
  product: mockProduct2
};
export const mockProductCatalog3: ProductCatalog = {
  id: 'null',
  lastPullDatetime: null,
  listPrice: null,
  partNumber: null,
  sellPrice: null,
  uom: null,
  skuQuantity: null,
  product: mockProduct3
};
export const mockProductCatalog4: ProductCatalog = {
  id: 'null',
  lastPullDatetime: null,
  listPrice: null,
  partNumber: null,
  sellPrice: null,
  uom: null,
  skuQuantity: null,
  product: mockProduct3
};
