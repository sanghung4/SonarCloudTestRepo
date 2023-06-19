import React from 'react';
import { Banner } from 'components/Banner';
import { DropDownBox } from 'components/DropDownBox';
import { ScrollView, StyleSheet, View } from 'react-native';
import { ScrollContentProps } from './types';
import { Message } from 'components/Message';
import { TabFilters } from 'components/TabFilters';
import { getComponentTestingIds } from 'test-utils/testIds';

export const ScrollContent = ({
  children,
  banner,
  dropdown,
  padding,
  paddingHorizontal,
  paddingVertical,
  style,
  messages,
  tabFilters,
  testID,
  ...rest
}: ScrollContentProps) => {
  const testIds = getComponentTestingIds('ScrollContent', testID);
  return (
    <ScrollView
      style={[styles.flex, style]}
      keyboardShouldPersistTaps="always"
      testID={testIds.component}
      {...rest}
    >
      {/* Banner */}
      {banner && <Banner {...banner} testID={testIds.banner} />}
      {/* Dropdown */}
      {dropdown && <DropDownBox {...dropdown} testID={testIds.dropDownBox} />}
      {/* Messages */}
      {messages && (
        <View style={styles.messagesContainer}>
          {messages.map(({ open, ...props }, index) =>
            open ? (
              <Message
                {...props}
                paddingTop={true}
                key={index}
                testID={`${testIds.message}-${index}`}
              />
            ) : null
          )}
        </View>
      )}
      {/* Tab Filters */}
      {tabFilters && <TabFilters {...tabFilters} testID={testIds.tabFilter} />}
      {/* Page Content */}
      <View
        style={[
          (padding || paddingHorizontal) && styles.paddingHorizontal,
          (padding || paddingVertical) && styles.paddingVertical,
        ]}
        testID={testIds.list}
      >
        {children}
      </View>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  flex: {
    flex: 1,
  },
  paddingHorizontal: { paddingHorizontal: 32 },
  paddingVertical: { paddingVertical: 24 },
  centered: {
    alignItems: 'center',
    justifyContent: 'center',
    height: '100%',
  },
  messagesContainer: { paddingHorizontal: 8 },
});
