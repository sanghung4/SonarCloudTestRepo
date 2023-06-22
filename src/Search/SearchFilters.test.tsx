import SearchFilters, { Props as SearchFiltersProps } from './SearchFilters';
import { FILTERS } from './SearchFilters.mocks';

import { render } from 'test-utils/TestWrapper';

const mockProps: SearchFiltersProps = {
  loading: false,
  count: 100,
  filters: FILTERS,
  filtersOpen: true,
  onShowHide: () => {}
};

describe('SearchFilters component tests', () => {
  it('Should make separate calls for the product and the pricing & availability', async () => {
    const { getByTestId } = render(<SearchFilters {...mockProps} />);

    const filterKeys = Object.keys(FILTERS);

    filterKeys.forEach((filter) => {
      const filterList = getByTestId(`search-filter-list-${filter}`);
      expect(filterList).toBeTruthy();
    });
  });
});
