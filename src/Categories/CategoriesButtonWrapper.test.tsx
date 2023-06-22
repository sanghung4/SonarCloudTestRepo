import { ReactNode } from 'react';
import { waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import useResizeObserver from 'use-resize-observer';

import CategoriesButtonWrapper from 'Categories/CategoriesButtonWrapper';
import { CategoriesContext } from 'Categories/CategoriesProvider';
import { success } from 'Categories/mocks';
import { Category } from 'generated/graphql';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

jest.mock('use-resize-observer', () => ({
  __esModule: true,
  default: jest.fn()
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
    setBreakpoint('desktop');
    (useResizeObserver as jest.Mock).mockReturnValue({
      ref: null,
      width: 1024
    });
  });

  it('should render correctly', () => {
    const { container, getByTestId } = render(
      <MockCategoriesContext data={mockCategoryData} loading={false}>
        <CategoriesButtonWrapper />
      </MockCategoriesContext>
    );
    const browseProductsButton = getByTestId('browse-products-button');
    userEvent.hover(browseProductsButton);
    expect(getByTestId('category1-0')).toBeInTheDocument();
    expect(container).toMatchSnapshot();
  });

  it('should render correctly while loading', () => {
    const { container } = render(
      <MockCategoriesContext data={undefined} loading={true}>
        <CategoriesButtonWrapper />
      </MockCategoriesContext>
    );
    expect(container).toMatchSnapshot();
  });

  it('should link correctly', async () => {
    const { getByTestId } = render(
      <MockCategoriesContext data={mockCategoryData} loading={false}>
        <CategoriesButtonWrapper />
      </MockCategoriesContext>
    );
    userEvent.hover(getByTestId('browse-products-button'));
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    const category = getByTestId('category3-4-0');
    expect(category).toHaveAttribute(
      'href',
      '/search?&categories=Faucets%2C%20Fixtures%20%26%20Appliances&categories=Fans%20%26%20Ventilation&categories=TBC'
    );
  });

  it('should update categories when hovering over a category', () => {
    const { getByTestId } = render(
      <MockCategoriesContext data={mockCategoryData} loading={false}>
        <CategoriesButtonWrapper />
      </MockCategoriesContext>
    );
    userEvent.hover(getByTestId('browse-products-button'));
    userEvent.hover(getByTestId('category1-2'));
    const pvcLink = getByTestId('category3-2-0');

    expect(pvcLink.innerHTML).toEqual('PVC Pipe &amp; Fittings');
  });
});
