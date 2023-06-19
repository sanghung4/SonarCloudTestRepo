import React from 'react';
import { Banner } from 'components/Banner';
import { DropDownBox } from 'components/DropDownBox';
import { StyleSheet, View } from 'react-native';
import { StaticContentProps } from './types';
import { Message } from 'components/Message';
import { TabFilters } from 'components/TabFilters';
import { getComponentTestingIds } from 'test-utils/testIds';

export const StaticContent = ({
  children,
  centered,
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
}: StaticContentProps) => {
  const testIds = getComponentTestingIds('StaticContent', testID);
  return (
    <View testID={testIds.component} style={[styles.flex, style]} {...rest}>
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
                testID={testIds.message}
              />
            ) : null
          )}
        </View>
      )}
      {/* Tab Filters */}
      {tabFilters && <TabFilters {...tabFilters} />}
      {/* Page Content */}
      <View
        testID={testIds.children}
        style={[
          styles.flex,
          centered && styles.centered,
          (padding || paddingHorizontal) && styles.paddingHorizontal,
          (padding || paddingVertical) && styles.paddingVertical,
        ]}
      >
        {children}
      </View>
    </View>
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
