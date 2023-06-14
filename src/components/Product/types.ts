import { PillProps } from 'components/Pill';
import { StyleProp, ViewStyle, TextStyle, ImageStyle } from 'react-native';
import { ProductContent } from './ProductContent';
import { ProductDivider } from './ProductDivider';
import { ProductFeaturedTitle } from './ProductFeaturedTitle';
import { ProductHeader } from './ProductHeader';
import { ProductImage } from './ProductImage';

export interface ProductProps {
  children: React.ReactNode;
  style?: StyleProp<ViewStyle>;
  testID?: string | undefined;
}

export interface ProductComponents {
  Divider?: typeof ProductDivider;
  Header?: typeof ProductHeader;
  Image?: typeof ProductImage;
  FeaturedTitle?: typeof ProductFeaturedTitle;
  Content?: typeof ProductContent;
}

export interface ProductContentProps {
  children: React.ReactNode;
  style?: StyleProp<ViewStyle>;
  testID?: string | undefined;
}

export interface ProductDividerProps {
  style?: StyleProp<ViewStyle>;
  testID?: string | undefined;
}

export interface ProductFeaturedTitleProps {
  style?: StyleProp<TextStyle>;
  children?: React.ReactNode;
  testID?: string | undefined;
}

export interface ProductHeaderProps {
  title: string;
  tags?: PillProps[];
  containerStyle?: StyleProp<ViewStyle>;
  tagsRowStyle?: StyleProp<ViewStyle>;
  testID?: string | undefined;
}

export interface ProductImageProps {
  src?: string | null;
  containerStyle?: StyleProp<ViewStyle>;
  imageStyle?: StyleProp<ImageStyle>;
  PlaceholderComponent?: React.ReactElement<
    any,
    string | React.JSXElementConstructor<any>
  >;
  testID?: string | undefined;
}
