import { ImageProps as RNEImageProps } from 'react-native-elements';

export interface ImageProps extends Omit<RNEImageProps, 'source'> {
  placeholder?: JSX.Element;
  imageUri: string | undefined;
}
