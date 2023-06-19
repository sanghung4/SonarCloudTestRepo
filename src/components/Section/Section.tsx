import React from 'react';
import { StyleSheet, View } from 'react-native';
import { get } from 'lodash';
import { SectionProps } from './types';
import { getComponentTestingIds } from 'test-utils/testIds';

const Section = ({ children, testID, ...rest }: SectionProps) => {
  const testIds = getComponentTestingIds('Section', testID);
  return (
    <View testID={testIds.component} style={getStyles(rest).section}>
      {children}
    </View>
  );
};

const getStyles = ({ bg, ...props }: Partial<SectionProps>) =>
  StyleSheet.create({
    section: {
      ...props,
      backgroundColor: bg || props.backgroundColor,
      alignSelf: props.alignSelf || 'auto',

      // Border
      borderColor: props.borderColor || get(props, 'border.color'),
      borderStyle: props.borderStyle || get(props, 'border.style'),

      borderWidth: props.borderWidth || get(props, 'border.width'),
      borderTopWidth: props.borderTopWidth || get(props, 'border.width.top'),
      borderBottomWidth:
        props.borderBottomWidth || get(props, 'border.width.bottom'),
      borderRightWidth:
        props.borderRightWidth || get(props, 'border.width.right'),
      borderLeftWidth: props.borderLeftWidth || get(props, 'border.width.left'),

      borderRadius: props.borderRadius || get(props, 'border.radius'),
      borderBottomRightRadius:
        props.borderBottomRightRadius ||
        get(props, 'border.radius.bottomRight'),
      borderBottomLeftRadius:
        props.borderBottomLeftRadius || get(props, 'border.radius.bottomLeft'),
      borderTopRightRadius:
        props.borderTopRightRadius || get(props, 'border.radius.topRight'),
      borderTopLeftRadius:
        props.borderTopLeftRadius || get(props, 'border.radius.topLeft'),
    },
  });

export default Section;
