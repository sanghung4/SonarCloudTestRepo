import ListsLineItem from 'Lists/ListsLineItem';
import mocks, {
  COPPER_CLAMPS_ADD_TO_CART,
  LIST_PROVIDER_MOCKS
} from 'Lists/test/index.mocks';
import * as t from 'locales/en/translation.json';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

describe('ListsLineItem', () => {
  it('Should match snapshot when loading', () => {
    const { container } = render(
      <ListsLineItem
        loading
        item={LIST_PROVIDER_MOCKS.VALID_LIST.selectedList?.listLineItems[0]!}
      />
    );
    expect(container).toMatchSnapshot();
  });

  // TO-DO - FIX THIS LATER
  it.skip('Should display find at other branches', async () => {
    const { findAllByTestId, findByTestId } = render(
      <ListsLineItem
        item={LIST_PROVIDER_MOCKS.VALID_LIST.selectedList?.listLineItems[0]!}
      />
    );
    const branchButton = await findAllByTestId('find-at-other-branches-button');

    expect(branchButton[0]).toBeInTheDocument();
    expect(branchButton[0]).toHaveTextContent(t.product.checkNearByBranches);
    expect(branchButton[0]).toMatchSnapshot();
  });

  it('Should display the availability of an item in the list', async () => {
    const { findAllByTestId } = render(
      <ListsLineItem
        item={LIST_PROVIDER_MOCKS.VALID_LIST.selectedList?.listLineItems[0]!}
      />
    );

    const stockChip = await findAllByTestId('availability-stock-chip');

    expect(stockChip[0]).toHaveTextContent(t.common.availableForOrder);
    expect(stockChip[0]).toMatchSnapshot();
  });

  it('Should match snapshot for mobile view', () => {
    setBreakpoint('mobile');
    const { container } = render(
      <ListsLineItem
        loading
        priceDataLoading
        item={LIST_PROVIDER_MOCKS.VALID_LIST.selectedList?.listLineItems[0]!}
      />
    );
    expect(container).toMatchSnapshot();
  });
});
