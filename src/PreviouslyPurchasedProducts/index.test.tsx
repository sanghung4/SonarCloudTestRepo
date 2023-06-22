import React from 'react';

import PreviouslyPurchasedProducts from 'PreviouslyPurchasedProducts';
import PreviouslyPurchasedProductsLineItem from 'PreviouslyPurchasedProducts/PreviouslyPurchasedProductsLineItem';
import { PreviouslyPurchasedProduct } from 'generated/graphql';
import { render } from 'test-utils/TestWrapper';

const mockPPPItem: PreviouslyPurchasedProduct = {
  product: {
    id: 'MSC-109342',
    imageUrls: null,
    manufacturerName: null,
    manufacturerNumber: null,
    minIncrementQty: 0,
    name: 'KOHLER MOUNTING BRACKET K-99694-BL',
    partNumber: '109342',
    price: 64.16,
    status: 'Stock',
    stock: {
      homeBranch: null,
      otherBranches: [
        {
          branchId: '2001',
          availability: 0
        }
      ]
    }
  },
  quantity: { quantity: '92', umqt: '1', uom: 'ea' },
  lastOrder: {
    lastDate: '07/20/2021',
    lastQuantity: '24'
  }
};

describe('Previously Purchased Products', () => {
  it('Renders PPP', () => {
    const { container } = render(<PreviouslyPurchasedProducts />, {
      authConfig: { authState: { isAuthenticated: true } }
    });

    expect(container).toMatchSnapshot();
  });

  it('Render PPP Line Item', () => {
    const { container } = render(
      <PreviouslyPurchasedProductsLineItem
        item={mockPPPItem}
        loading={false}
        pricingDataLoading={false}
      />,
      { authConfig: { authState: { isAuthenticated: true } } }
    );

    expect(container).toMatchSnapshot();
  });
});
