import { render } from 'test-utils/TestWrapper';
import ProductsCard from 'Order/ProductsCard';
import { mincronMocks, pricingDataMocks } from './mocks';
import * as t from 'locales/en/translation.json';

describe('Product Card tests for invoice items', () => {
  it('Display invoice items when items are available', () => {
    const { getByTestId } = render(
      <ProductsCard
        order={mincronMocks[0].result?.data?.order}
        pricingData={pricingDataMocks}
        loading={false}
      />
    );
    expect(getByTestId('row_3613945')).toBeInTheDocument();
  });

  it('Display no items found when there are no invoice items', () => {
    const { getByText } = render(
      <ProductsCard
        order={{
          ...mincronMocks[0].result.data.order,
          lineItems: []
        }}
        pricingData={pricingDataMocks}
        loading={false}
      />
    );
    expect(getByText(t.orders.noItemsWaterworks)).toBeInTheDocument();
    expect(getByText(t.orders.noOrdersContactBranch)).toBeInTheDocument();
  });
});
