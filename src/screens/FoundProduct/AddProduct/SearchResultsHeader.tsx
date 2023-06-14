import React from 'react';
import { Colors, FontWeight as fw } from 'constants/style';
import { Section } from 'components/Section';
import { Text } from 'components/Text';
import { SearchResultsHeaderProps } from './types';
import { getScreenTestingIds } from 'test-utils/testIds';

const testIds = getScreenTestingIds('AddProduct');
export const SearchResultsHeader = ({ query }: SearchResultsHeaderProps) => (
  <Section bg={Colors.WHITE} paddingBottom={12} paddingHorizontal={24}>
    {query ? (
      <Text color={Colors.PRIMARY_1100} testID={testIds.showingSearchResults}>
        Showing Search Results for{' '}
        <Text color={Colors.PRIMARY_1100} fontWeight={fw.BOLD}>
          {query}
        </Text>
      </Text>
    ) : undefined}
  </Section>
);
