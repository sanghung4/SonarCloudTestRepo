import { StackNavigationProp } from '@react-navigation/stack';
import { KourierProduct, Product } from 'api';
import { AppStackParamList } from 'navigation/types';

export interface SearchResultProps {
  product: KourierProduct;
  onPress: () => void;
  index: number;
  testID: string;
}

export interface SearchResultsEmptyProps {
  text?: string | null;
  loading: boolean;
}

export interface SearchResultsHeaderProps {
  query: string;
}

export type AddProductNav = StackNavigationProp<
  AppStackParamList,
  'FoundProduct'
>;
