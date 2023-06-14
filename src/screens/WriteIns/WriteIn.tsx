import React, {useMemo} from 'react';
import { Icon, ListItem } from 'react-native-elements';
import {
  OldColors,
  pagePadding,
  FontWeight as fw,
  Colors,
} from 'constants/style';
import { Text } from 'components/Text';
import { getWriteInTitle } from './utils';
import { StyleSheet, View } from 'react-native';
import { RouteNames } from 'constants/routes';
import { WriteInProps } from './types';
import { useNavigation } from '@react-navigation/native';
import { AppNavigation } from 'navigation/types';
import { getScreenTestingIds } from 'test-utils/testIds';

export const WriteIn = ({ item }: WriteInProps) => {
  const title = getWriteInTitle(item);
  const navigation = useNavigation<AppNavigation<'WriteIns'>>();

  const onPress = () => {
    navigation.navigate(RouteNames.WRITE_IN_DETAIL, {
      writeIn: item,
    });
  };

  const locationId = useMemo(()=>{
    return item.locationId
  }
  ,[item.locationId])

  const quantity = useMemo(()=>{
    return item.quantity 
  }
  ,[item.quantity])
  
  const uom = useMemo(()=>{
    return item.uom
  }
  ,[item.uom])

  const createdBy = useMemo(()=>{
    return item.createdBy
  }
  ,[item.createdBy])


  const testIds = getScreenTestingIds('WriteIn');
  return (
    <ListItem
      pad={0}
      testID={title}
      containerStyle={styles.listItemContainer}
      onPress={onPress}
    >
      <View style={styles.firstTierContainer}>
        <Text
          fontWeight={fw.BOLD}
          fontSize={20}
          testID={`${testIds.locationId}-${item.description}`}
        >
          {locationId}
        </Text>
        <View style={styles.flexRow}>
          {item.resolved ? (
            <Text
              style={styles.listItemResolved}
              fontWeight={fw.MEDIUM}
              fontSize={16}
              testID={`${testIds.resolvedStatus}-${item.description}`}
            >
              Resolved
            </Text>
          ) : (
            <Text
              style={styles.listItemUnresolved}
              fontSize={16}
              testID={`${testIds.resolvedStatus}-${item.description}`}
            >
              Unresolved
            </Text>
          )}
          <Icon name="chevron-right" color={Colors.PRIMARY_3100} />
        </View>
      </View>
      <View style={styles.secondTierContainer}>
        <Text
          fontWeight={fw.BOLD}
          color={OldColors.BLUE}
          fontSize={20}
          testID={`${testIds.quantity}-${item.description}`}
        >
          {quantity} {uom}
        </Text>
        <Text
          style={styles.listItemButtonTitle}
          fontWeight={fw.MEDIUM}
          fontSize={16}
          testID={`${testIds.createdBy}-${item.description}`}
        >
          {createdBy.replace('@morsco.com', '')}
        </Text>
      </View>
      <View style={styles.thirdTierContainer}>
        <Text
          color={Colors.SECONDARY_2100}
          fontWeight={fw.LIGHT}
          fontSize={16}
          testID={`${testIds.description}-${item.description}`}
        >
          {item.description.toUpperCase()}
        </Text>
      </View>
    </ListItem>
  );
};

const styles = StyleSheet.create({
  listItemContainer: {
    height:115,
    borderBottomWidth: 2,
    borderBottomColor: Colors.SECONDARY_3100,
    marginHorizontal: pagePadding.X,
    paddingHorizontal: 5,
    flexDirection: 'column',
    backgroundColor: Colors.SECONDARY_6100,
  },
  firstTierContainer: {
    width: '100%',
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  secondTierContainer: {
    width: '100%',
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginVertical: 3,
  },
  thirdTierContainer: {
    width: '100%',
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginVertical: 3,
    paddingRight: 25,
  },
  listItemButtonTitle: {
    color: '#202020',
    alignSelf: 'flex-end',
    opacity: 0.5,
    paddingRight: 25,
  },
  listItemResolved: {
    color: Colors.SUPPORT_1100,
  },
  listItemUnresolved: {
    color: Colors.SUPPORT_2100,
  },
  flexRow: {
    flexDirection: 'row',
    alignItems: 'center',
  },
});
