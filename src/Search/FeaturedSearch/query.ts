import { gql } from '@apollo/client';
import { Maybe } from 'graphql/jsutils/Maybe';

const featuredSearchQuery = gql`
  query ($match: String) {
    featuredSearchItemCollection(
      where: { searchMatch_contains_some: [$match] }
      order: [order_ASC, name_ASC]
    ) {
      items {
        name
        image {
          url
        }
        search
      }
    }
  }
`;

export type FeaturedSearchItem = {
  image: {
    url?: string;
    __typename?: 'Asset';
  };
  name: string;
  search: Maybe<string>;
  __typename?: 'FeaturedSearchItem';
};

export type FeaturedSearchQuery = {
  featuredSearchItemCollection: {
    items: FeaturedSearchItem[];
    __typename?: 'FeaturedSearchItemCollection';
  };
};

export default featuredSearchQuery;
