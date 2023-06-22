import { AggregationItem, AggregationResults } from 'generated/graphql';

export const FILTER_ARRAY: AggregationItem[] = [
  { value: 'mock-filter-1', count: 100 },
  { value: 'mock-filter-2', count: 100 }
];

export const FILTERS: AggregationResults = {
  brands: FILTER_ARRAY,
  btu: FILTER_ARRAY,
  capacity: FILTER_ARRAY,
  category: FILTER_ARRAY,
  colorFinish: FILTER_ARRAY,
  depth: FILTER_ARRAY,
  environmentalOptions: FILTER_ARRAY,
  flowRate: FILTER_ARRAY,
  height: FILTER_ARRAY,
  inStockLocation: FILTER_ARRAY,
  inletSize: FILTER_ARRAY,
  length: FILTER_ARRAY,
  lines: FILTER_ARRAY,
  material: FILTER_ARRAY,
  pressureRating: FILTER_ARRAY,
  productTypes: FILTER_ARRAY,
  size: FILTER_ARRAY,
  temperatureRating: FILTER_ARRAY,
  tonnage: FILTER_ARRAY,
  voltage: FILTER_ARRAY,
  wattage: FILTER_ARRAY,
  width: FILTER_ARRAY
};
