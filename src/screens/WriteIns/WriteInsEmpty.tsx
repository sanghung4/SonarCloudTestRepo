import React from 'react';
import { pagePadding, FontWeight as fw, Colors } from 'constants/style';
import { Text } from 'components/Text';
import { Section } from 'components/Section';
import { SvgIcons } from 'components/SVG';
import { StyleSheet } from 'react-native';

export const WriteInsEmpty = () => (
  <Section paddingVertical={pagePadding.Y} paddingHorizontal={pagePadding.X}>
    <Section alignSelf="center">
      <SvgIcons name={'WriteInsNoResultsImage'} size={120} />
    </Section>
    <Text h5 centered fontWeight={fw.REGULAR} style={styles.listEmptyTitle}>
      Sorry! No write-in&apos;s found
    </Text>
  </Section>
);

const styles = StyleSheet.create({
  listEmptyTitle: {
    color: Colors.PRIMARY_1100,
    marginTop: 24,
  },
});
