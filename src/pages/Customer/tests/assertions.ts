import mocks from 'pages/Customer/tests/customer.mocks';
import { expectTestIdsTexts } from 'test-util';

export const expectToMatchTestIdTextsWhenNull = () =>
  expectTestIdsTexts([
    {
      id: 'customer_detail-name',
      expect: 'N/A'
    },
    {
      id: 'customer_detail-id',
      expect: 'N/A'
    },
    {
      id: 'customer_detail-branch-id',
      expect: 'N/A'
    },
    {
      id: 'customer_detail-branch-name',
      expect: ''
    },
    {
      id: 'customer_detail-regions',
      expect: 'N/A'
    },
    {
      id: 'customer_detail-contact-name',
      expect: 'N/A'
    },
    {
      id: 'customer_detail-contact-number',
      expect: ''
    },
    {
      id: 'customer_detail-catalog-status',
      expect: 'Unknown'
    },
    {
      id: 'customer_detail-catalog-name',
      expect: 'N/A'
    },
    {
      id: 'customer_detail-file-name',
      expect: 'File Name: N/A'
    },
    {
      id: 'customer_detail-sku-qty',
      expect: 'SKU Quantity: 0'
    },
    {
      id: 'customer_detail-last-updated',
      expect: 'Last Updated: --'
    }
  ]);

export const expectToMatchTestIdTextsWhenTruthy = () =>
  expectTestIdsTexts([
    {
      id: 'customer_detail-name',
      expect: mocks.customer.name
    },
    {
      id: 'customer_detail-id',
      expect: mocks.customer.customerId
    },
    {
      id: 'customer_detail-branch-id',
      expect: mocks.customer.branchId!
    },
    {
      id: 'customer_detail-branch-name',
      expect: mocks.customer.branchName!
    },
    {
      id: 'customer_detail-regions',
      expect: mocks.region.name
    },
    {
      id: 'customer_detail-contact-name',
      expect: mocks.customer.contactName!
    },
    {
      id: 'customer_detail-contact-number',
      expect: mocks.customer.contactPhone!
    },
    {
      id: 'customer_detail-catalog-status',
      expect: mocks.catalog.status
    },
    {
      id: 'customer_detail-catalog-name',
      expect: mocks.catalog.name
    },
    {
      id: 'customer_detail-file-name',
      expect: `File Name: ${mocks.catalog.fileName}`
    },
    {
      id: 'customer_detail-sku-qty',
      expect: `SKU Quantity: ${mocks.catalog.skuQuantity}`
    },
    {
      id: 'customer_detail-last-updated',
      expect: `Last Updated: ${new Date(
        mocks.catalog.lastUpdate
      ).toLocaleDateString('en-us')}`
    }
  ]);
