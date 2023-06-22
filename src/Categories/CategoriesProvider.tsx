import { ReactNode, createContext } from 'react';

import { Maybe, Category, useProductCategoriesQuery } from 'generated/graphql';
import { useDomainInfo } from 'hooks/useDomainInfo';

type Categories = Maybe<Array<Maybe<Category>>>;

type CategoriesContextType = {
  categories?: Categories;
  categoriesLoading: boolean;
};

export const CategoriesContext = createContext({
  categoriesLoading: false
} as CategoriesContextType);

type Props = {
  children: ReactNode;
};

function CategoriesProvider(props: Props) {
  /**
   * Data
   */
  const { engine } = useDomainInfo();
  const { data: categoriesData, loading: categoriesLoading } =
    useProductCategoriesQuery({
      skip: !engine,
      variables: { engine }
    });

  return (
    <CategoriesContext.Provider
      value={{
        categories: categoriesData?.productCategories?.categories as Categories,
        categoriesLoading
      }}
    >
      {props.children}
    </CategoriesContext.Provider>
  );
}

export default CategoriesProvider;
