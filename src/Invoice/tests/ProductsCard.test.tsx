import { render } from 'test-utils/TestWrapper';
import { successMincron } from 'Invoice/mocks';
import ProductsCard from 'Invoice/ProductsCard';
import * as t from 'locales/en/translation.json';

describe('Product Card tests for invoice items', () => {
  it('Display invoice items when items are available', () => {
    const { getByTestId } = render(
      <ProductsCard
        invoice={successMincron.result.data.invoice}
        loading={false}
      />
    );
    expect(
      getByTestId('row_1738433F4-91A7-4637-8C60-B4DF59A060C1')
    ).toBeInTheDocument();
  });

  it('Display no items found when there are no invoice items', () => {
    const { getByText } = render(
      <ProductsCard
        invoice={{
          ...successMincron.result.data.invoice,
          invoiceItems: []
        }}
        loading={false}
      />
    );
    expect(getByText(t.orders.noItemsWaterworks)).toBeInTheDocument();
    expect(getByText(t.orders.noOrdersContactBranch)).toBeInTheDocument();
  });
});
