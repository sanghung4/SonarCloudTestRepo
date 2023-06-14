import React from 'react';
import { Image as RNEImage } from 'react-native-elements';
import { getComponentTestingIds } from 'test-utils/testIds';
import { ImageProps } from './types';

const Image = ({ placeholder, imageUri, testID, ...rest }: ImageProps) => {
  const testIds = getComponentTestingIds('Image', testID);

  return imageUri ? (
    <RNEImage source={{ uri: imageUri }} testID={testIds.component} {...rest} />
  ) : placeholder ? (
    placeholder
  ) : null;
};

export default Image;
