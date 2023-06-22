import useSearchParam, {
  objectToSearchParams,
  groupParamsByKey,
  useQueryParams
} from 'hooks/useSearchParam';

let useHistoryMock = () => jest.fn();
const useLocationMock = {
  search: ''
};

jest.mock('react-router-dom', () => ({
  useHistory: useHistoryMock,
  useLocation: () => useLocationMock
}));

describe('Utils - useSearchParam', () => {
  it('should get a search param', () => {
    useLocationMock.search =
      '?searchTerm=another&categories=stuff+%26+ok%2C+another&filters=interesting';
    const paramValue = useSearchParam('searchTerm');

    expect(paramValue).toEqual('another');
  });

  it('should return search params as object and a setter with no config parameter', () => {
    useLocationMock.search =
      '?searchTerm=another&categories=stuff+%26+ok%2C+another&filters=interesting';
    const [params] = useQueryParams();

    expect(params).toEqual({
      searchTerm: 'another',
      categories: 'stuff & ok, another',
      filters: 'interesting'
    });
  });

  it('should return search params as object and a setter with config parameter', () => {
    useLocationMock.search =
      '?searchTerm=another&categories=stuff+%26+ok%2C+another&filters=interesting';
    const config = {
      arrayKeys: ['categories', 'filters']
    };

    const [params] = useQueryParams(config);

    expect(params).toEqual({
      searchTerm: 'another',
      categories: ['stuff & ok, another'],
      filters: ['interesting']
    });
  });

  it('should convert an object to search params', () => {
    useLocationMock.search =
      '?searchTerm=another&categories=stuff+%26+ok%2C+another&filters=interesting';
    const params = {
      searchTerm: 'test',
      categories: ['one', 'two', 'Pipes & Stuff, Another']
    };

    const search = objectToSearchParams(params);

    expect(new URLSearchParams(search).toString()).toEqual(
      'searchTerm=test&categories=one&categories=two&categories=Pipes+%26+Stuff%2C+Another'
    );
  });

  it('should convert an object to search params without falsy', () => {
    useLocationMock.search =
      '?searchTerm=another&categories=stuff+%26+ok%2C+another&filters=interesting';
    const params = {
      searchTerm: 'test',
      categories: ['one', 'two', 'Pipes & Stuff, Another'],
      falsy: [null, undefined, '']
    };

    const search = objectToSearchParams(params);

    expect(new URLSearchParams(search).toString()).toEqual(
      'searchTerm=test&categories=one&categories=two&categories=Pipes+%26+Stuff%2C+Another'
    );
  });

  it('should convert params to an object', () => {
    useLocationMock.search =
      '?searchTerm=another&categories=stuff+%26+ok%2C+another&filters=interesting';
    const search =
      '?searchTerm=another&categories=stuff+%26+ok%2C+another&categories=one+more&filters=interesting';

    let params = groupParamsByKey(new URLSearchParams(search), [
      'categories',
      'filters'
    ]);

    expect(params).toEqual({
      searchTerm: 'another',
      categories: ['stuff & ok, another', 'one more'],
      filters: ['interesting']
    });
  });
});
