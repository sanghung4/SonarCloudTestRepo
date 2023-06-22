import { DataMap } from '@reece/global-types';
import { xorWith } from 'lodash-es';

import { ProductAttribute } from 'generated/graphql';
import { SearchParams } from 'Search/util/useSearchQueryParams';

export function getSelectedFiltersArray(filters: string[]) {
  return filters.map((f) => {
    const [attributeType, attributeValue] = f.split('|');
    return {
      attributeType,
      attributeValue
    };
  });
}

export function getSelectedFiltersMap(filters: string[]) {
  const output: DataMap<string> = {};
  filters.forEach((f) => {
    const [attributeType, attributeValue] = f.split('|');
    output[attributeType + attributeValue] = '';
  });
  return output;
}

type HandleToggleLogicsProps = {
  categories: string[];
  changedFilter: ProductAttribute;
  params: SearchParams;
  selectedFilters: {
    attributeType: string;
    attributeValue: string;
  }[];
  setParams: (obj: SearchParams, path?: string | undefined) => void;
};

export function handleToggleLogics({
  categories,
  changedFilter,
  params,
  selectedFilters,
  setParams
}: HandleToggleLogicsProps) {
  const normalizedAttribute: ProductAttribute = {
    attributeType: (changedFilter.attributeType || '').replace(/s$/, ''),
    attributeValue: changedFilter.attributeValue
  };

  if (/category\d/.test(changedFilter.attributeType ?? '')) {
    let depth = parseInt(changedFilter.attributeType?.slice(-1) ?? '1');
    setParams({
      ...params,
      page: '1',
      categories: categories.slice(0, depth - 1)
    });
  } else if (normalizedAttribute.attributeType === 'category') {
    // handle category add
    if (normalizedAttribute.attributeValue) {
      setParams({
        ...params,
        page: '1',
        categories: [...categories, normalizedAttribute.attributeValue]
      });
    }
  } else {
    // Handle filter change
    let filters = xorWith(
      selectedFilters,
      [normalizedAttribute],
      (a, b) =>
        a?.attributeType === b?.attributeType &&
        a?.attributeValue === b?.attributeValue
    );
    setParams({
      ...params,
      page: '1',
      filters: filters.map((f) => `${f?.attributeType}|${f?.attributeValue}`)
    });
  }
}

export function getFilterHeight(filter: string) {
  const defaultValue = 40;
  const result: { [k: string]: number } = {
    capacity: 80,
    category: 60,
    material: 60,
    pressureRating: 60,
    size: 60,
    voltage: 80
  };
  return result[filter] || defaultValue;
}
