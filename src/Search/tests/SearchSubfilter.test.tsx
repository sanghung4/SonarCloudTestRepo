import SearchSubfilter from 'Search/SearchSubfilter';
import { FILTERS, FILTER_ARRAY } from 'Search/SearchFilters.mocks';
import { render } from 'test-utils/TestWrapper';

type filterType = keyof typeof FILTERS;
let filterData: filterType = 'inStockLocation';

const SearchSubfilterProps = {
  checked: false,
  filter: filterData,
  handleToggle: () => {},
  subfilter: FILTER_ARRAY[0],
  stockText: 'In-stock @ Morrison Supply',
  loading: false,
  filterDisabled: true
};

describe('Check isPricingOnly branches', () => {
  it('Check branch matches snapshot post load', async () => {
    const { container } = render(<SearchSubfilter {...SearchSubfilterProps} />);

    expect(container).toMatchSnapshot();
  });

  it('Check in-stock filter to be enabled when branch is not isPricingOnly', async () => {
    SearchSubfilterProps.filterDisabled = false;
    const { findByTestId } = render(
      <SearchSubfilter {...SearchSubfilterProps} />
    );

    const stkFilterElement = await findByTestId(
      'filter-line-item-mock-filter-1'
    );

    expect(stkFilterElement).not.toHaveClass('Mui-disabled');
  });
});
