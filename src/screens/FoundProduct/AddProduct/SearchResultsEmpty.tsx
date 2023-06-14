import React from 'react';
import { ActivityIndicator, StyleSheet, View } from 'react-native';

import { Colors, FontWeight as fw } from 'constants/style';

import { Section } from 'components/Section';
import { Text } from 'components/Text';
import { SvgIcons } from 'components/SVG';
import { SearchResultsEmptyProps } from './types';

export const SearchResultsEmpty = ({
  text,
  loading,
}: SearchResultsEmptyProps) => (
  <View style={styles.container}>
    <Section alignSelf="center">
      {loading ? (
        <ActivityIndicator size="large" />
      ) : (
        <SvgIcons name={'SearchNoResultsImage'} size={72} />
      )}
    </Section>
    <Text
      h5
      centered
      color={Colors.PRIMARY_1100}
      style={styles.emptyTitle}
      fontWeight={fw.REGULAR}
    >
      {text ? text : "Sorry, we couldn't find any matches."}
    </Text>
  </View>
);

const styles = StyleSheet.create({
  emptyTitle: {
    marginTop: 24,
    marginBottom: 18,
  },
  container: {
    flex: 1,
    flexGrow: 1,
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: 48,
  },
});
