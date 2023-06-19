import { eclipseSearchProductType, kourierSearchProductType } from '../../../constants/apollo';
import { ProductSearchEclipseInput, ProductSearchKourierInput } from 'api';
import { Dimensions } from 'react-native';

export const screenHeight = Dimensions.get('window').height;
export const overlayHeight = screenHeight * 0.9;

export const PAGE_SIZE = 8;

export const createSearchProductsEclipseInput = (
  query: string,
  page: number,
  searchId: boolean
): ProductSearchEclipseInput => ({
  selectedAttributes: [],
  pageSize: PAGE_SIZE,
  searchTerm: query.replace('"', '\\"'),
  currentPage: page,
  searchInputType: searchId
    ? eclipseSearchProductType.PRODUCT_ID
    : eclipseSearchProductType.KEYWORD,
});

export const createSearchProductsKourierInput = (
  query: string,
  searchId: boolean
): ProductSearchKourierInput => ({
  keywords:query,
  searchInputType: searchId
    ? kourierSearchProductType.PRODUCT_ID
    : kourierSearchProductType.KEYWORD,
});