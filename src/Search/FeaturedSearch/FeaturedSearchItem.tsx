import Dotdotdot from 'react-dotdotdot';

import Image from 'components/Image';
import { FSItem } from 'Search/FeaturedSearch';
import { testIds } from 'test-utils/testIds';
import { Link } from 'react-router-dom';

/**
 * Types
 */
type Props = {
  item: FSItem;
};

/**
 * Constants
 */
const NOT_ALLOWED_CHAR = /[^a-zA-Z0-9\\ \\-]/;
const SEARCH_URL = '/search?criteria=';
const {
  item: testIdItem,
  itemImage: testIdImage,
  itemName: testIdName,
  itemUrl: testIdUrl
} = testIds.Search.FeaturedSearch;

/**
 * Component
 */
function FeaturedSearchItem(props: Props) {
  /**
   * Props
   */
  const { image, name, search, url } = props.item;
  const criteria = (search ?? name).replace(NOT_ALLOWED_CHAR, '');
  const searchQueryUrl = url ?? SEARCH_URL + criteria;
  const splitName = name.split('</br>');
  const shouldHaveTwoLines = splitName.length > 1;
  const textContainerClassName = !shouldHaveTwoLines
    ? 'featured-search__item_textcontainersinglelinepadding'
    : 'featured-search__item_textcontainer';

  /**
   * Render
   */
  return (
    <div className="featured-search__item" data-testid={testIdItem + name}>
      <div className="featured-search__item_imagecontainer">
        <Link href={searchQueryUrl} to={searchQueryUrl}>
          <Image alt={name} src={image} data-testid={testIdImage + name} />
        </Link>
      </div>
      <div className={textContainerClassName}>
        <Link
          href={searchQueryUrl}
          to={searchQueryUrl}
          data-testid={testIdUrl + name}
        >
          <Dotdotdot clamp={shouldHaveTwoLines ? 2 : 1}>
            {shouldHaveTwoLines ? (
              <p data-testid={testIdName + name}>
                {splitName[0]}
                <br data-testid={testIdName + name + '_break'} />
                {splitName[1]}
              </p>
            ) : (
              <p data-testid={testIdName + name}>{name}</p>
            )}
          </Dotdotdot>
        </Link>
      </div>
    </div>
  );
}
export default FeaturedSearchItem;
