import mocks from 'pages/Catalog/tests/catalog.mocks';
import { expectTestIdsTexts } from 'test-util';

export const expectToMatchTestIdTextsWhenNull = () =>
  expectTestIdsTexts([
    {
      id: 'catalog-name',
      expect: 'N/A'
    },
    {
      id: 'catalog-product-count',
      expect: '(0 Products)'
    },
    {
      id: 'catalog-customer',
      expect: 'N/A'
    },
    {
      id: 'catalog-customer-id',
      expect: 'N/A'
    }
  ]);

export const expectToMatchTestIdTextsWhenSuccess = () =>
  expectTestIdsTexts([
    {
      id: 'catalog-name',
      expect: mocks.success.catalog!.name!
    },
    {
      id: 'catalog-product-count',
      expect: `(${mocks.success.totalItems} Products)`
    },
    {
      id: 'catalog-customer',
      expect: mocks.success.customer!.name
    },
    {
      id: 'catalog-customer-id',
      expect: mocks.success.customer!.customerId
    }
  ]);

export const expectToMatchTestIdTextsWhenEmpty = () =>
  expectTestIdsTexts([
    {
      id: 'catalog-name',
      expect: mocks.empty.catalog!.name!
    },
    {
      id: 'catalog-product-count',
      expect: `(${mocks.empty.totalItems} Products)`
    },
    {
      id: 'catalog-customer',
      expect: mocks.empty.customer!.name
    },
    {
      id: 'catalog-customer-id',
      expect: mocks.empty.customer!.customerId
    }
  ]);
