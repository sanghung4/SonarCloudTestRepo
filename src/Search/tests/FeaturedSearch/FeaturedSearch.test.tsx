import FeaturedSearch from 'Search/FeaturedSearch';
import { FeaturedSearchQuery } from 'Search/FeaturedSearch/query';
import {
  featuredSearchResultEmpty,
  featuredSearchResultSuccess
} from 'Search/tests/FeaturedSearch/featuredSearch.mocks';
import { SearchParams } from 'Search/util/useSearchQueryParams';
import { testIds } from 'test-utils/testIds';
import { render } from 'test-utils/TestWrapper';

/**
 * Test Ids
 */
const {
  container: testIdContainer,
  loader: testIdLoader,
  item: testIdItem,
  itemImage: testIdItemImage,
  itemName: testIdItemName,
  itemUrl: testIdItemUrl
} = testIds.Search.FeaturedSearch;

/**
 * Types
 */
type Mocks = {
  queryParam: SearchParams;
  gqlData: FeaturedSearchQuery;
  gqlLoading: boolean;
};

/**
 * Mock variables
 */
const defaultMock: Mocks = {
  queryParam: {},
  gqlData: { ...featuredSearchResultEmpty },
  gqlLoading: false
};
let mocks: Mocks = { ...defaultMock };

/**
 * Mock methods
 */
jest.mock('@apollo/client', () => ({
  ...jest.requireActual('@apollo/client'),
  useQuery: () => ({ data: mocks.gqlData, loading: mocks.gqlLoading })
}));
jest.mock('Search/util/useSearchQueryParams', () => ({
  ...jest.requireActual('Search/util/useSearchQueryParams'),
  __esModule: true,
  default: () => [mocks.queryParam]
}));

/**
 * Test function
 */
function setup() {
  return render(<FeaturedSearch />);
}

/**
 * TEST
 */
describe('Search/Featured Search', () => {
  // ⚪ Reset mocks
  afterEach(() => {
    mocks = { ...defaultMock };
  });

  // 🟢 GQL - Empty
  it('Expect to render nothing when item is none', async () => {
    // Arrange
    const { container } = setup();

    // Assert
    expect(container).toBeEmptyDOMElement();
  });

  // 🟢 GQL - Loading
  it('Expect to render loader under loading condition', async () => {
    // Arrange
    mocks.gqlLoading = true;
    const { findByTestId } = setup();

    // Act
    const loader = await findByTestId(testIdLoader);

    // Assert
    expect(loader).toBeInTheDocument();
  });

  // 🟢 GQL - Has Items
  it('Expect to render content from response', async () => {
    // Arrange
    mocks.gqlData = featuredSearchResultSuccess;
    mocks.queryParam.criteria = 'test';
    const { findByTestId } = setup();

    // Act
    const content = await findByTestId(testIdContainer);

    // Assert
    expect(content).toBeInTheDocument();
  });

  // 🟢 Item content test
  it('Expect to match the content of the first item', async () => {
    // Arrange
    mocks.gqlData = featuredSearchResultSuccess;
    const { findAllByTestId } = setup();
    const [firstItemData] =
      featuredSearchResultSuccess.featuredSearchItemCollection.items;

    // Act
    const { name, image, search } = firstItemData;
    const [firstItem] = await findAllByTestId(testIdItem + name);
    const [firstItemName] = await findAllByTestId(testIdItemName + name);
    const [firstItemImage] = await findAllByTestId(testIdItemImage + name);
    const [firstItemUrl] = await findAllByTestId(testIdItemUrl + name);

    // Assert
    expect(firstItem).toBeInTheDocument();
    expect(firstItemName).toHaveTextContent(name);
    expect(firstItemImage).toHaveAttribute('src', image.url);
    expect(firstItemUrl).toHaveAttribute('href', '/search?criteria=' + search);
  });

  it('Expect to match the content of the last item with </br>', async () => {
    // Arrange
    mocks.gqlData = featuredSearchResultSuccess;
    const { findAllByTestId } = setup();
    const [lastItemData] =
      featuredSearchResultSuccess.featuredSearchItemCollection.items.reverse();

    // Act
    const { name, image, search } = lastItemData;
    const [lastItem] = (await findAllByTestId(testIdItem + name)).reverse();
    const [lastItemBreak] = (
      await findAllByTestId(testIdItemName + name + '_break')
    ).reverse();
    const [lastItemImage] = (
      await findAllByTestId(testIdItemImage + name)
    ).reverse();
    const [lastItemUrl] = (
      await findAllByTestId(testIdItemUrl + name)
    ).reverse();

    // Assert
    expect(lastItem).toBeInTheDocument();
    expect(lastItemBreak).toBeInTheDocument();
    expect(lastItemImage).toHaveAttribute('src', image.url);
    expect(lastItemUrl).toHaveAttribute('href', '/search?criteria=' + search);
  });

  // 🟢 Nothing when it is not page 1
  it('Expect to have no items when it is not page 1', async () => {
    // Arrange
    mocks.gqlData = featuredSearchResultSuccess;
    mocks.queryParam.page = '2';
    const { queryAllByTestId } = setup();

    // Act
    const res = queryAllByTestId(testIdItem);

    // Assert
    expect(res.length).toBe(0);
  });
});
