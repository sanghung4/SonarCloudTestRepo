import { pagePadding } from 'constants/style';
import { isString } from 'lodash';
import React from 'react';
import { StyleSheet, View } from 'react-native';
import { Image } from 'components/Image';
import { ProductImageProps } from './types';
import { getComponentTestingIds } from 'test-utils/testIds';

export const ProductImage = ({
  src,
  containerStyle,
  imageStyle,
  PlaceholderComponent,
  testID,
  ...rest
}: ProductImageProps) => {
  const testIds = getComponentTestingIds('Product', testID);
  return (
    <View
      testID={testIds.image}
      style={[styles.container, containerStyle && containerStyle]}
    >
      <Image
        {...rest}
        imageUri={isString(src) ? src : undefined}
        placeholder={PlaceholderComponent}
        placeholderStyle={styles.placeholder}
        style={[styles.image, imageStyle && imageStyle]}
        resizeMode="contain"
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    paddingVertical: 36,
    paddingHorizontal: pagePadding.X,
    alignItems: 'center',
  },
  image: {
    minWidth: 128,
    minHeight: 128,
  },
  placeholder: {
    backgroundColor: 'transparent',
  },
});
