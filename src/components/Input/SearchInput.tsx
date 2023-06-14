import { CustomIcon, CustomIconNames } from 'components/CustomIcon';
import { Colors, FontWeight } from 'constants/style';
import React from 'react';
import { StyleSheet, TextInput, View } from 'react-native';
import { getComponentTestingIds } from 'test-utils/testIds';
import { SearchInputProps } from './types';

export const SearchInput = ({
  containerStyle,
  style,
  testID,
  ...rest
}: SearchInputProps) => {
  const testIds = getComponentTestingIds('SearchInput', testID);
  return (
    <View
      testID={testIds.component}
      style={[styles.inputContainer, containerStyle]}
    >
      <TextInput
        testID={testIds.input}
        autoFocus={false}
        autoCorrect={false}
        autoCapitalize="none"
        autoComplete="off"
        allowFontScaling={false}
        style={[styles.textInput, style]}
        placeholderTextColor={Colors.SECONDARY_3100}
        {...rest}
      />
      <CustomIcon name={CustomIconNames.Search} />
    </View>
  );
};

const styles = StyleSheet.create({
  inputContainer: {
    flexDirection: 'row',
    backgroundColor: Colors.WHITE,
    borderRadius: 3,
    paddingVertical: 8,
    paddingRight: 8,
    paddingLeft: 12,
    borderWidth: 1,
    borderColor: Colors.WHITE,
  },
  textInput: {
    flex: 1,
    fontSize: 16,
    fontWeight: FontWeight.MEDIUM,
    paddingVertical: 0,
  },
});
