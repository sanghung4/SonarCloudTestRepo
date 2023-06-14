import { InMemoryCache } from '@apollo/client';

export default new InMemoryCache({
  typePolicies: {
    LocationItem: {
      keyFields: ['prodNum'],
    },
    VarianceLocationItem: {
      keyFields: ['prodNum'],
    },
  },
});
