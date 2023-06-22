import { act, fireEvent } from '@testing-library/react';

import { render } from 'test-utils/TestWrapper';
import { useQueryParams } from 'hooks/useSearchParam';

type SearchParamMock = {
  searchTerm: string;
  categories: string;
  filters: string;
};
type TestComponentType = {
  usePath?: boolean;
  path?: string;
};
const searchParamMock: SearchParamMock = {
  searchTerm: 'test',
  categories: 'cat',
  filters: 'filter'
};

const MockComponent = (props: TestComponentType) => {
  const [params, setParams] = useQueryParams<SearchParamMock>(
    {
      arrayKeys: []
    },
    props.usePath
  );
  const handleClick = () => setParams(searchParamMock, props.path);
  return (
    <>
      <span data-testid="search-term">{params.searchTerm}</span>
      <span data-testid="category">{params.categories}</span>
      <span data-testid="filters">{params.filters}</span>
      <button data-testid="button" onClick={handleClick} />
    </>
  );
};

describe('Utils - useSearchParam Implementations', () => {
  it('should get a search param', () => {
    const { getByTestId } = render(<MockComponent />);
    expect(getByTestId('search-term')).toBeEmptyDOMElement();
    expect(getByTestId('category')).toBeEmptyDOMElement();
    expect(getByTestId('filters')).toBeEmptyDOMElement();

    act(() => {
      fireEvent.click(getByTestId('button'));
    });

    expect(getByTestId('search-term')).toHaveTextContent(
      searchParamMock.searchTerm
    );
    expect(getByTestId('category')).toHaveTextContent(
      searchParamMock.categories
    );
    expect(getByTestId('filters')).toHaveTextContent(searchParamMock.filters);
  });

  it('should get a search param with path', () => {
    const { getByTestId } = render(<MockComponent usePath />);
    expect(getByTestId('search-term')).toBeEmptyDOMElement();
    expect(getByTestId('category')).toBeEmptyDOMElement();
    expect(getByTestId('filters')).toBeEmptyDOMElement();

    act(() => {
      fireEvent.click(getByTestId('button'));
    });

    expect(getByTestId('search-term')).toHaveTextContent(
      searchParamMock.searchTerm
    );
    expect(getByTestId('category')).toHaveTextContent(
      searchParamMock.categories
    );
    expect(getByTestId('filters')).toHaveTextContent(searchParamMock.filters);
  });

  it('should get a search param with toReplaceHistory', () => {
    const { getByTestId } = render(<MockComponent path="localhost:1234" />);
    expect(getByTestId('search-term')).toBeEmptyDOMElement();
    expect(getByTestId('category')).toBeEmptyDOMElement();
    expect(getByTestId('filters')).toBeEmptyDOMElement();

    act(() => {
      fireEvent.click(getByTestId('button'));
    });

    expect(getByTestId('search-term')).toHaveTextContent(
      searchParamMock.searchTerm
    );
    expect(getByTestId('category')).toHaveTextContent(
      searchParamMock.categories
    );
    expect(getByTestId('filters')).toHaveTextContent(searchParamMock.filters);
  });
});
