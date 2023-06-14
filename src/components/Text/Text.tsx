import React from 'react';
import { Platform, StyleSheet } from 'react-native';
import { identity, pickBy } from 'lodash';
import { FontWeight as fw } from 'constants/style';
import { TextProps } from './types';
import * as RNE from 'react-native-elements';

const pickObj = (obj: Record<string, any> = {}) => {
  return pickBy(obj, identity);
};

const Text = ({
  color,
  centered,
  fontSize,
  fontWeight,
  h1Style,
  h2Style,
  h3Style,
  h4Style,
  h5Style,
  h5,
  smallStyle,
  small,
  captionStyle,
  caption,
  style,
  ...rest
}: TextProps) => {
  const textAlign = centered ? 'center' : 'left';

  const attr = pickObj({ color, textAlign, fontWeight, fontSize });
  return (
    <RNE.Text
      h1Style={[h1Style, attr]}
      h2Style={[h2Style, attr]}
      h3Style={[h3Style, attr]}
      h4Style={[h4Style, attr]}
      style={[
        style,
        attr,
        h5 && [styles.bold, styles.h5, h5Style, attr],
        small && [smallStyle, styles.small, attr],
        caption && [captionStyle, styles.caption, attr],
      ]}
      {...rest}
    />
  );
};

const styles = StyleSheet.create({
  bold: {
    ...Platform.select({
      android: {
        fontWeight: fw.BOLD,
      },
      ios: {},
    }),
  },
  h5: { fontSize: RNE.normalize(20) },
  small: { fontSize: RNE.normalize(14) },
  caption: { fontSize: RNE.normalize(12) },
});

export default RNE.withTheme(Text, 'Text');
