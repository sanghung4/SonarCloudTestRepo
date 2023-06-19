import React from 'react';
import { StyleSheet } from 'react-native';
import * as RNE from 'react-native-elements';
import { borderRadius, Colors, fontSize } from 'constants/style';
import { getComponentTestingIds } from 'test-utils/testIds';

const SearchBar = ({
  containerStyle,
  inputStyle,
  inputContainerStyle,
  testID,
  ...rest
}: RNE.SearchBarProps) => {
  const testIds = getComponentTestingIds('SearchBar', testID);
  return (
    <RNE.SearchBar
      testID={testIds.component}
      containerStyle={StyleSheet.flatten([styles.container, containerStyle])}
      inputContainerStyle={StyleSheet.flatten([
        styles.inputContainer,
        inputContainerStyle,
      ])}
      inputStyle={[styles.input, inputStyle]}
      {...rest}
    />
  );
};

SearchBar.defaultProps = {
  searchIcon: {
    type: 'material',
    name: 'search',
    size: 24,
    color: Colors.PRIMARY_1100,
  },
  clearIcon: {
    type: 'material',
    name: 'clear',
    size: 24,
    color: Colors.PRIMARY_2100,
  },
};

const styles = StyleSheet.create({
  container: {
    borderTopColor: 'transparent',
    borderBottomColor: 'transparent',
    borderRadius: borderRadius.SMALL,
    backgroundColor: Colors.SECONDARY_460,
    paddingVertical: 0,
    paddingHorizontal: 8,
  },
  inputContainer: {
    height: 48,
  },
  input: {
    marginLeft: 0,
    fontSize: fontSize.BASE,
    color: Colors.PRIMARY_3100,
  },
});

export default SearchBar;
