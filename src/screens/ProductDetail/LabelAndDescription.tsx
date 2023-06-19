import { Section } from 'components/Section';
import { Text } from 'components/Text';
import { fontSize as fs, FontWeight as fw } from 'constants/style';
import React from 'react';
import { StyleSheet } from 'react-native';
import { orNA } from 'utils/stringUtils';
import { LabelAndDescriptionProps } from './types';

export const LabelAndDescription = ({
  label,
  value,
  numberOfLines,
}: LabelAndDescriptionProps) => (
  <Section flex={1}>
    <Text style={styles.label} fontSize={fs.CAPTION}>
      {label}
    </Text>
    <Text fontWeight={fw.MEDIUM} numberOfLines={numberOfLines}>
      {orNA(value)}
    </Text>
  </Section>
);

const styles = StyleSheet.create({
  label: {
    marginBottom: 2,
  },
});
