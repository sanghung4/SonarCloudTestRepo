import { BranchContext } from 'providers/BranchProvider';
import SearchResult from 'Search/SearchResult';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { PRICING_DATA, PRODUCT } from 'Search/SearchResult.mocks';
import { render } from 'test-utils/TestWrapper';

describe('SearchResult tests', () => {
  it('should render correctly on Desktop', () => {
    setBreakpoint('desktop');
    const { container } = render(
      <SearchResult
        product={PRODUCT}
        pricing={PRICING_DATA}
        pricingLoading={false}
        index={0}
        onClick={() => {}}
      />
    );
    expect(container).toMatchSnapshot();
  });

  it('should render correctly on mobile', () => {
    setBreakpoint('mobile');
    const { container } = render(
      <SearchResult
        product={PRODUCT}
        pricing={PRICING_DATA}
        pricingLoading={false}
        index={0}
        onClick={() => {}}
      />
    );
    expect(container).toMatchSnapshot();
  });

  it('should display the product image', async () => {
    const { findAllByRole } = render(
      <SearchResult
        product={PRODUCT}
        pricing={PRICING_DATA}
        pricingLoading={false}
        index={0}
        onClick={() => {}}
      />
    );
    const productImage = await findAllByRole('img');
    expect(productImage[0]).toBeInTheDocument();
    expect(productImage[0]).toMatchSnapshot();
  });
});
