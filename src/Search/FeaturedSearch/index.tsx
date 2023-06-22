import { useMemo } from 'react';

import { useQuery } from '@apollo/client';
import { CircularProgress, Divider } from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import FeaturedSearchItem from 'Search/FeaturedSearch/FeaturedSearchItem';
import featuredSearchQuery, {
  FeaturedSearchQuery
} from 'Search/FeaturedSearch/query';
import 'Search/styles/featuredSearch.scss';
import useSearchQueryParams from 'Search/util/useSearchQueryParams';
import { testIds } from 'test-utils/testIds';

/**
 * Types
 */
export type FSItem = {
  name: string;
  image?: string;
  search?: string;
  url?: string;
};

/**
 * Constants
 */
const { container: testIdContainer, loader: testIdLoader } =
  testIds.Search.FeaturedSearch;

/**
 * Component
 */
function FeaturedSearch() {
  /**
   * Custom Hooks
   */
  const [params] = useSearchQueryParams();
  const { criteria = '', page = '1' } = params;
  const { t } = useTranslation();

  /**
   * GQL Data
   */
  const { data, loading } = useQuery<FeaturedSearchQuery>(featuredSearchQuery, {
    skip: !criteria || page !== '1',
    context: { clientName: 'contentful' },
    variables: { match: criteria.toLowerCase() }
  });

  /**
   * Memo
   */
  const items = useMemo(searchItemsMemo, [data, page]);

  /**
   * Render
   */
  if (loading) {
    return (
      <div
        className="featured-search__loadingcontainer"
        data-testid={testIdLoader}
      >
        <CircularProgress color="primary02.main" size={28} />
      </div>
    );
  }
  if (!items.length) {
    return null;
  }
  return (
    <div data-testid={testIdContainer}>
      <p className="featured-search__title">
        {t('search.featuredSearchTitle')}
      </p>
      <div className="featured-search__container">
        {items.map((item, i) => (
          <FeaturedSearchItem item={item} key={`featured-search-${i}`} />
        ))}
      </div>
      <Divider />
    </div>
  );

  /**
   * Memo Defs
   */
  function searchItemsMemo() {
    if (page !== '1') {
      return [];
    }

    const list = data?.featuredSearchItemCollection?.items;
    if (!list?.length) {
      return [];
    }

    // Restructure the data type
    return list.map(
      ({ image: { url: image }, name, search }) =>
        ({ image, name, search } as FSItem)
    );
  }
}

export default FeaturedSearch;
