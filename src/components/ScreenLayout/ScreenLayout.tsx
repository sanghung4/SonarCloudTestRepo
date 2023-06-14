import React, { useCallback } from 'react';
import {
  StyleSheet,
  SafeAreaView,
  KeyboardAvoidingView,
  Platform,
} from 'react-native';
import { ScreenLayoutProps } from './types';
import { Colors } from 'constants/style';
import { useOverlay } from 'providers/Overlay';
import { useFocusEffect } from '@react-navigation/native';
import { useHeaderHeight } from '@react-navigation/stack';
import { StaticContent } from './StaticContent';
import { ScrollContent } from './ScrollContent';
import { CustomButton } from 'components/CustomButton';
import { getComponentTestingIds } from 'test-utils/testIds';

const ScreenLayout = ({
  children,
  pageAction,
  loading,
  style,
  keyboardVerticalOffset,
  testID,
  ...rest
}: ScreenLayoutProps) => {
  const { toggleLoading } = useOverlay();
  const headerHeight = useHeaderHeight();

  useFocusEffect(
    useCallback(() => {
      if (loading !== undefined) {
        toggleLoading(loading);
      }
    }, [loading])
  );

  const testIds = getComponentTestingIds('ScreenLayout', testID);

  return (
    <SafeAreaView style={styles.safeArea}>
      <KeyboardAvoidingView
        style={[styles.pageWrapper, style]}
        keyboardVerticalOffset={headerHeight + (keyboardVerticalOffset || 0)}
        behavior={Platform.select({ ios: 'padding' })}
        testID={testIds.component}
        {...rest}
      >
        {/* Page Content */}
        {children}
        {/* Page Action */}
        {pageAction && !pageAction.hide && (
          <CustomButton
            {...pageAction}
            buttonStyle={[styles.removeRadius, styles.button]}
            containerStyle={styles.removeRadius}
            testID={testIds.pageAction}
          />
        )}
      </KeyboardAvoidingView>
    </SafeAreaView>
  );
};

ScreenLayout.ScrollContent = ScrollContent;
ScreenLayout.StaticContent = StaticContent;

const styles = StyleSheet.create({
  safeArea: {
    backgroundColor: Colors.WHITE,
  },
  pageWrapper: {
    height: '100%',
    backgroundColor: Colors.WHITE,
  },
  button: {
    paddingVertical: 16,
  },
  removeRadius: {
    borderRadius: 0,
  },
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
});

export default ScreenLayout;
