import React, { useState, useEffect } from 'react';
import { StyleSheet, TouchableOpacity, View } from 'react-native';
import { Text } from 'components/Text';
import { Colors, fontSize, FontWeight } from 'constants/style';
import { useOverlay } from 'providers/Overlay';
import { SortByEnum } from 'screens/VarianceLocationList/types';
import { CustomButton } from 'components/CustomButton';
import { CustomIcon, CustomIconNames } from 'components/CustomIcon';

const SortByModal = () => {
  const { toggleSortBy, handleSortBy, activeSortBy } = useOverlay();

  const [tempSortBy, setActiveSortBy] = useState<SortByEnum>(
    SortByEnum.ASCENDING
  );

  const options = [
    SortByEnum.ASCENDING,
    SortByEnum.DESCENDING,
    SortByEnum.NET_HIGHTOLOW,
    SortByEnum.NET_LOWTOHIGH,
    SortByEnum.GROSS_HIGHTOLOW,
    SortByEnum.GROSS_LOWTOHIGH,
  ];

  useEffect(() => {
    if (tempSortBy !== activeSortBy) {
      setActiveSortBy(activeSortBy);
    }
  }, []);

  return (
    <View style={styles.sortByContainer}>
      <CustomIcon
        name={CustomIconNames.Delete}
        style={styles.deleteIcon}
        onPress={() => toggleSortBy(false)}
      />
      <Text style={styles.sortByText}>Sort By</Text>
      {options.map((option: SortByEnum) => {
        return (
          <TouchableOpacity
            key={option}
            style={styles.optionContainer}
            onPress={() => {
              setActiveSortBy(option);
            }}
          >
            <CustomIcon
              name={
                option === tempSortBy
                  ? CustomIconNames.RadioActive
                  : CustomIconNames.RadioInactive
              }
            />
            <Text style={styles.optionText}>{option}</Text>
          </TouchableOpacity>
        );
      })}
      <View style={styles.actionContainer}>
        <CustomButton
          title={'Cancel'}
          onPress={() => toggleSortBy(false)}
          type={'link'}
        />
        <CustomButton
          title={'Sort'}
          buttonStyle={styles.sortButton}
          onPress={() => {
            handleSortBy(tempSortBy);
            toggleSortBy(false);
          }}
        />
      </View>
    </View>
  );
};
const styles = StyleSheet.create({
  actionContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-evenly',
    width: '100%',
  },
  cancelButton: {
    width: 137,
    height: 44,
    justifyContent: 'center',
    alignItems: 'center',
  },
  cancelButtonText: {
    fontSize: fontSize.BASE,
    fontWeight: FontWeight.MEDIUM,
    color: Colors.PRIMARY_3100,
    textDecorationLine: 'underline',
  },
  deleteIcon: {
    alignSelf: 'flex-end',
  },
  optionContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    marginLeft: 23,
    marginVertical: 14,
  },
  optionText: {
    marginLeft: 23,
  },
  sortButton: {
    width: 137,
    height: 44,
  },
  sortButtonText: {
    fontSize: fontSize.BASE,
    fontWeight: FontWeight.MEDIUM,
    color: Colors.WHITE,
  },
  sortByContainer: {
    width: 325,
    minHeight: 331,
    backgroundColor: Colors.WHITE,
    padding: 12,
  },
  sortByText: {
    marginLeft: 21,
    fontWeight: FontWeight.MEDIUM,
    color: Colors.PRIMARY_1100,
    fontSize: fontSize.H5,
  },
});

export default SortByModal;
