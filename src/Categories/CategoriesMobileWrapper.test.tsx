import { ReactNode } from 'react';
import userEvent from '@testing-library/user-event';
import { useHistory } from 'react-router-dom';
import useResizeObserver from 'use-resize-observer';

import CategoriesMobileWrapper from 'Categories/CategoriesMobileWrapper';
import { CategoriesContext } from 'Categories/CategoriesProvider';
import { success } from 'Categories/mocks';
import { Category } from 'generated/graphql';
import { render } from 'test-utils/TestWrapper';
import { setBreakpoint } from 'test-utils/mockMediaQuery';

jest.mock('use-resize-observer', () => ({
  __esModule: true,
  default: jest.fn()
}));

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useHistory: jest.fn()
}));

type MockCategoryContextProps = {
  children: ReactNode;
  data?: Category[];
  loading: boolean;
};

const MockCategoriesContext = ({
  children,
  data,
  loading
}: MockCategoryContextProps) => {
  const mockValues = {
    categories: data,
    categoriesLoading: loading
  };
  return (
    <CategoriesContext.Provider value={mockValues}>
      {children}
    </CategoriesContext.Provider>
  );
};

const mockCategoryData = success.result.data?.productCategories
  ?.categories as Category[];

describe('CategoriesButtonWrapper tests', () => {
  beforeEach(() => {
    setBreakpoint('mobile');
    (useResizeObserver as jest.Mock).mockReturnValue({
      ref: null,
      width: 1024
    });
  });

  it('should render correctly', () => {
    const { container, getByTestId } = render(
      <MockCategoriesContext data={mockCategoryData} loading={false}>
        <CategoriesMobileWrapper in={true} handleClose={jest.fn()} />
      </MockCategoriesContext>
    );
    expect(getByTestId('category-header')).toBeInTheDocument();
    expect(container).toMatchSnapshot();
  });

  it('should render correctly on loading', () => {
    const { container } = render(
      <MockCategoriesContext data={undefined} loading={true}>
        <CategoriesMobileWrapper in={true} handleClose={jest.fn()} />
      </MockCategoriesContext>
    );
    expect(container).toMatchSnapshot();
  });

  it('should navigate through the categories', () => {
    const { getByTestId } = render(
      <MockCategoriesContext data={mockCategoryData} loading={false}>
        <CategoriesMobileWrapper in={true} handleClose={jest.fn()} />
      </MockCategoriesContext>
    );
    const headerSpan = getByTestId('category-header');
    userEvent.click(getByTestId('category1-2'));
    expect(headerSpan).toHaveTextContent('Pipe & Fittings (5)');

    userEvent.click(getByTestId('category2-1'));
    expect(headerSpan).toHaveTextContent(
      'Plastic Pipe & Fittings - Supply (2)'
    );
  });

  it('should navigate back through the categories', () => {
    const handleClose = jest.fn();

    const { getByTestId } = render(
      <MockCategoriesContext data={mockCategoryData} loading={false}>
        <CategoriesMobileWrapper in={true} handleClose={handleClose} />
      </MockCategoriesContext>
    );
    const headerSpan = getByTestId('category-header');
    const backButton = getByTestId('category-back');

    userEvent.click(getByTestId('category1-2'));
    userEvent.click(getByTestId('category2-1'));
    userEvent.click(backButton);
    expect(headerSpan).toHaveTextContent('Pipe & Fittings (5)');

    userEvent.click(backButton);
    expect(headerSpan).toHaveTextContent('Product Categories (8)');

    userEvent.click(backButton);
    expect(handleClose).toHaveBeenCalledTimes(1);
  });

  it('should link to the category from the view all button', () => {
    const handleClose = jest.fn();

    const { getByTestId } = render(
      <MockCategoriesContext data={mockCategoryData} loading={false}>
        <CategoriesMobileWrapper in={true} handleClose={handleClose} />
      </MockCategoriesContext>
    );

    // Navigate to bottom category
    userEvent.click(getByTestId('category1-2'));
    const viewAllButton = getByTestId('view-all-categories');
    expect(viewAllButton.getAttribute('href')).toEqual(
      '/search?categories=Pipe%20%26%20Fittings'
    );

    userEvent.click(getByTestId('category2-0'));
    expect(viewAllButton.getAttribute('href')).toEqual(
      '/search?categories=Pipe%20%26%20Fittings&categories=Metal%20Pipe%20%26%20Fittings'
    );

    userEvent.click(viewAllButton);
    expect(handleClose).toHaveBeenCalled();
  });

  it('should navigate and close the sidebar if a category three is selected', () => {
    const push = jest.fn();
    const handleClose = jest.fn();

    (useHistory as jest.Mock).mockReturnValue({
      push
    });

    const { getByTestId } = render(
      <MockCategoriesContext data={mockCategoryData} loading={false}>
        <CategoriesMobileWrapper in={true} handleClose={handleClose} />
      </MockCategoriesContext>
    );

    // Navigate to bottom category
    userEvent.click(getByTestId('category1-0'));
    userEvent.click(getByTestId('category2-2'));
    userEvent.click(getByTestId('category3-2'));

    expect(handleClose).toHaveBeenCalled();
    expect(push).toHaveBeenCalledWith({
      pathname: '/search',
      search:
        'categories=Faucets%2C%20Fixtures%20%26%20Appliances&categories=Appliances&categories=Outdoor%20Grills%20and%20BBQ'
    });
  });

  it('should navigate and close the sidebar if a category two is selected that has no children', () => {
    const push = jest.fn();
    const handleClose = jest.fn();

    (useHistory as jest.Mock).mockReturnValue({
      push
    });

    const { getByTestId } = render(
      <MockCategoriesContext data={mockCategoryData} loading={false}>
        <CategoriesMobileWrapper in={true} handleClose={handleClose} />
      </MockCategoriesContext>
    );

    // Navigate to bottom category
    userEvent.click(getByTestId('category1-0'));
    userEvent.click(getByTestId('category2-5'));

    expect(handleClose).toHaveBeenCalled();
    expect(push).toHaveBeenCalledWith({
      pathname: '/search',
      search:
        'categories=Faucets%2C%20Fixtures%20%26%20Appliances&categories=Tools'
    });
  });
});
