import { FeaturedSearchQuery } from 'Search/FeaturedSearch/query';

export const featuredSearchResultSuccess: FeaturedSearchQuery = {
  featuredSearchItemCollection: {
    __typename: 'FeaturedSearchItemCollection',
    items: [
      {
        __typename: 'FeaturedSearchItem',
        name: 'Conex',
        image: {
          __typename: 'Asset',
          url: 'https://images.ctfassets.net/qllwu7j6zru5/6BuvII2z8QjCOQjSyScY6C/c90876899618de3f4f087349266d46bf/Conex-Coupling-Stop.jpg'
        },
        search: 'Conex Copper'
      },
      {
        __typename: 'FeaturedSearchItem',
        name: 'Copper Coil',
        image: {
          __typename: 'Asset',
          url: 'https://images.ctfassets.net/qllwu7j6zru5/5brZv01IZNB2fOS98FzIAO/1772f048d1ed790dd367ddf03947a832/Q3PC100X_ATF.jpg'
        },
        search: 'soft copper'
      },
      {
        __typename: 'FeaturedSearchItem',
        name: 'Copper Tees',
        image: {
          __typename: 'Asset',
          url: 'https://images.ctfassets.net/qllwu7j6zru5/6t8GGJ6sr2b9e94k9J7OuZ/a6122241bf4a0b161e95d6632260e5e4/VIEGAP_20703_SML.jpg'
        },
        search: null
      },
      {
        __typename: 'FeaturedSearchItem',
        name: 'Copper Elbows',
        image: {
          __typename: 'Asset',
          url: 'https://images.ctfassets.net/qllwu7j6zru5/2iexYLwusey24tyuFkHFo0/4eb1ecfc177a7e017abddfd19b061770/VIEGAP_77022._SML.jpg'
        },
        search: null
      },
      {
        __typename: 'FeaturedSearchItem',
        name: 'Hard Copper Pipe, </br>Type L',
        image: {
          __typename: 'Asset',
          url: 'https://images.ctfassets.net/qllwu7j6zru5/280s17F5hapCrrkj0EbvgB/c9cc683b99016a96e007342529585eb9/CERROC_TUBING_CR_SML.jpeg'
        },
        search: 'Hard Copper Pipe Type L'
      }
    ]
  }
};
export const featuredSearchResultEmpty: FeaturedSearchQuery = {
  featuredSearchItemCollection: {
    __typename: 'FeaturedSearchItemCollection',
    items: []
  }
};
