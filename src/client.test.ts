import { ApolloLink, concat, execute, gql } from '@apollo/client';
import {
  apolloLink,
  applyIncomingIfValid,
  authLink,
  authLinkContentful,
  cache,
  invalidateListCache,
  onlyTakeIncoming,
  overwriteIfNotNull,
  readSelectedAccountsVar
} from 'client';
import { Configuration } from 'utils/configuration';

const TOKEN_KEY = 'okta-token-storage';
const MOCK_ACCESS_TOKEN = '{ accessToken: { accessToken: mock-access-token } }';

const testQuery = gql`
  query {
    foo
  }
`;

const mockListCacheData = {
  'ListLineItem:A': '',
  'ListLineItem:B': {},
  'List:A': '',
  'List:B': {}
};

describe('Apollo Client tests', () => {
  it('Expect `overwriteIfNotNull` to return previous data', () => {
    expect(overwriteIfNotNull('foo', 'bar')).toBe('foo');
  });
  it('Expect `overwriteIfNotNull` to return incoming data', () => {
    expect(overwriteIfNotNull(null, 'bar')).toBe('bar');
  });
  it('Expect `applyIncomingIfValid` to return incoming data', () => {
    expect(applyIncomingIfValid('foo', ['bar'])![0]).toBe('bar');
  });
  it('Expect `onlyTakeIncoming` to return incoming data', () => {
    expect(onlyTakeIncoming('foo', 'bar')).toBe('bar');
  });
  it('Expect `readSelectedAccountsVar` to return incoming data', () => {
    const result = readSelectedAccountsVar();
    expect(result.billTo).toBe(undefined);
    expect(result.shipTo).toBe(undefined);
    expect(result.shippingBranchId).toBe(undefined);
    expect(result.erpSystemName).toBe(undefined);
  });

  it('Should set the Authorization header to the correct value - Logged Out', () => {
    localStorage.removeItem(TOKEN_KEY);

    const testLink = new ApolloLink((operation) => {
      const headers = operation.getContext().headers;
      expect(headers.Authorization).toEqual('');

      return null;
    });

    const links = ApolloLink.from([authLink, testLink]);
    execute(links, { query: testQuery }).subscribe(() => {});
  });

  it('Should set the Authorization header to the correct value - Logged In', () => {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.setItem(TOKEN_KEY, MOCK_ACCESS_TOKEN);

    const testLink = new ApolloLink((operation) => {
      const headers = operation.getContext().headers;
      expect(headers.Authorization).toEqual(MOCK_ACCESS_TOKEN);

      return null;
    });

    const links = ApolloLink.from([authLink, testLink]);
    execute(links, { query: testQuery }).subscribe(() => {});
  });

  it('Invalidate List cache test', () => {
    //@ts-ignore
    cache.data.data = mockListCacheData;
    invalidateListCache();
    const result = [];
    //@ts-ignore
    const cacheDataHelper = cache.data.data;
    Object.keys(cacheDataHelper).forEach((key) => {
      result.push(cacheDataHelper[key]);
    });
    expect(result.length).toEqual(0);
  });

  it('Should set the Apollolink to the correct value for contentful', () => {
    Configuration.contentfulPreviewEnable = false;
    Configuration.contentfulApiKey = '1234';
    Configuration.contentfulPreviewApiKey = '5678';

    const testLink = new ApolloLink((operation, forward) => {
      const clientName = operation.getContext().clientName;
      expect(clientName).toEqual('contentful');

      return forward(operation);
    });

    const links = concat(
      testLink,
      ApolloLink.from([authLinkContentful, apolloLink])
    );
    execute(links, {
      query: testQuery,
      context: {
        clientName: 'contentful'
      }
    }).subscribe(() => {});
  });

  it('Should set the Apollolink to the correct value for contentful with preview', () => {
    Configuration.contentfulPreviewEnable = true;
    Configuration.contentfulApiKey = '1234';
    Configuration.contentfulPreviewApiKey = '5678';

    const testLink = new ApolloLink((operation, forward) => {
      const clientName = operation.getContext().clientName;
      expect(clientName).toEqual('contentful');

      return forward(operation);
    });

    const links = concat(
      testLink,
      ApolloLink.from([authLinkContentful, apolloLink])
    );
    execute(links, {
      query: testQuery,
      context: {
        clientName: 'contentful'
      }
    }).subscribe(() => {});
  });

  it('Should set the Apollolink to the correct value for public api', () => {
    const testLink = new ApolloLink((operation, forward) => {
      const clientName = operation.getContext()?.clientName;
      expect(clientName).toBe(undefined);

      return forward(operation);
    });

    const links = concat(testLink, ApolloLink.from([authLink, apolloLink]));
    execute(links, {
      query: testQuery
    }).subscribe(() => {});
  });
});
