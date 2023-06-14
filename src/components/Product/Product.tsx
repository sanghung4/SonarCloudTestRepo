import React from 'react';
import { View, StyleSheet } from 'react-native';
import { ProductDivider } from './ProductDivider';
import { ProductHeader } from './ProductHeader';
import { ProductImage } from './ProductImage';
import { ProductFeaturedTitle } from './ProductFeaturedTitle';
import { ProductContent } from './ProductContent';
import { ProductProps, ProductComponents } from './types';
import { Colors } from 'constants/style';

const Product = ({
  children,
  style,
  ...rest
}: ProductProps & ProductComponents) => {
  return (
    <View
      style={StyleSheet.flatten([styles.container, style && style])}
      {...rest}
    >
      {children}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    backgroundColor: Colors.WHITE,
    paddingVertical: 8,
  },
});

Product.Divider = ProductDivider;
Product.Header = ProductHeader;
Product.Image = ProductImage;
Product.FeaturedTitle = ProductFeaturedTitle;
Product.Content = ProductContent;

export default Product;
