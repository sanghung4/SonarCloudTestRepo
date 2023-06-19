import React,{useMemo} from 'react';
import { Section } from 'components/Section';
import { BranchProgressBar } from './BranchProgressBar';
import { Pill } from 'components/Pill';
import { Text } from 'components/Text';
import { StyleSheet, View } from 'react-native';
import { ListItem } from 'react-native-elements';
import { BranchItemProps } from './types';
import { countStatusMap, getLocationStatus } from 'utils/locationUtils';
import { Colors } from 'constants/style';
import { getScreenTestingIds } from 'test-utils/testIds';

export const BranchItem = ({ location }: BranchItemProps) => {
  const locationStatus = getLocationStatus(location);
  const testIds = getScreenTestingIds('BranchItems');

  const locationId = useMemo(()=>{
    return location.id
  },[location.id])
  
  const status = useMemo(()=>{
    return locationStatus
  },[locationStatus])

  const totalProducts = useMemo(()=>{
    return location.totalProducts
  },[location.totalProducts])

  const totalCounted = useMemo(()=>{
    return location.totalCounted
  },[location.totalCounted])

  return (
    <ListItem
      pad={0}
      testID={location.id}
      containerStyle={styles.listItemContainer}
    >
      <ListItem.Content style={styles.listItemContent}>
        <View style={styles.sectionContainer}>
          <Section
            flexDirection="row"
            alignItems="center"
            justifyContent="space-between"
            width="100%"
            marginBottom={12}
          >
            <Text
              h5
              testID={`${testIds.countLocation}-${location.id}`.replace(
                /\s/g,
                '-'
              )}
            >
              {locationId}
            </Text>
            <Pill
              variant="outline"
              status={countStatusMap[status]}
              style={styles.pill}
              value={status}
              testID={`${testIds.pill}-${location.id}`.replace(/\s/g, '-')}
            />
          </Section>
          <BranchProgressBar
            total={totalProducts}
            value={totalCounted}
            minMaxLabelsWrapperStyle={styles.minMaxLabelWrapperStyle}
            progressBarContainerStyle={
              status === 'Started' && location.totalProducts !== 1
                ? styles.progressBarContainer
                : null
            }
            minLabelColor={Colors.PRIMARY_2100}
            testID={`${testIds.progressBar}-${location.id}`.replace(/\s/g, '-')}
          />
        </View>
      </ListItem.Content>
    </ListItem>
  );
};

const styles = StyleSheet.create({
  listItemContainer: {
    borderBottomWidth: 2,
    borderColor: Colors.SECONDARY_3100,
    backgroundColor: Colors.SECONDARY_6100,
    marginBottom: 15,
  },
  listItemContent: {
    alignItems: 'center',
    justifyContent: 'center',
  },
  minMaxLabelWrapperStyle: {
    flexDirection: 'column',
    alignItems: 'flex-start',
  },
  progressBarContainer: {
    width: '95%',
    alignSelf: 'center',
    marginTop: 19,
    marginBottom: 10,
  },
  sectionContainer: { width: '100%' },
  pill: { width: 100 },
});
