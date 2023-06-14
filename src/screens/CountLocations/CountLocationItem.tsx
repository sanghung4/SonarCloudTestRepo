import React, {useMemo} from 'react';
import { Section } from 'components/Section';
import { Text } from 'components/Text';
import { Icon, ListItem } from 'react-native-elements';
import { Pill } from 'components/Pill';
import { StyleSheet } from 'react-native';
import { countStatusMap, getLocationStatus } from 'utils/locationUtils';
import { CountLocationItemProps } from './types';
import {
  fontSize as fs,
  FontWeight as fw,
  OldColors,
  pagePadding,
  Colors,
} from 'constants/style';
import { getScreenTestingIds } from 'test-utils/testIds';

export const CountLocationItem = ({
  location,
  onPress,
}: CountLocationItemProps) => {
  const locationStatus = useMemo(()=>{ return getLocationStatus(location); },[location])
  const testIds = getScreenTestingIds('CountLocationItems');

  const locationId = useMemo(()=>{
    return location.id
  },[location.id])
  const totalProducts = useMemo(()=>{
    return location.totalProducts
  },[location.totalProducts])

  return (
    <ListItem
      pad={0}
      containerStyle={styles.listItemContainer}
      onPress={() => onPress()}
    >
      <ListItem.Content style={styles.listItemContent}>
        <Section
          flexDirection="row"
          alignItems="center"
          justifyContent="space-between"
          width="100%"
          testID={location.id}
        >
          <Section flex={1}>
            <Text
              h5
              testID={`${testIds.countLocation}-${location.id}`.replace(
                /\s/g,
                '-'
              )}
            >
              {locationId}
            </Text>
            <Text
              fontSize={fs.BASE}
              fontWeight={fw.REGULAR}
              color={Colors.SECONDARY_2100}
              testID={`${testIds.totalProducts}-${location.id}`.replace(
                /\s/g,
                '-'
              )}
            >{`${totalProducts} products`}</Text>
          </Section>
          <Section paddingRight={10}>
            <Pill
              style={styles.baseWidth}
              variant="outline"
              status={countStatusMap[locationStatus]}
              value={locationStatus}
              testID={`${testIds.pill}-${location.id}`.replace(/\s/g, '-')}
            />
          </Section>
          <Icon name="chevron-right" color={Colors.SECONDARY_2100} />
        </Section>
      </ListItem.Content>
    </ListItem>
  );
};

const styles = StyleSheet.create({
  listItemContainer: {
    height:85,
    paddingHorizontal: pagePadding.X,
    borderBottomWidth: 2,
    borderColor: Colors.SECONDARY_3100,
    backgroundColor: OldColors.BACKGROUND,
  },
  listItemContent: {
    alignItems: 'center',
    justifyContent: 'center',
  },
  baseWidth: {
    width: 100,
  },
});
