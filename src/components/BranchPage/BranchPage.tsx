import React, { useCallback, useState } from 'react';
import { FontWeight as fw, fontSize, Colors } from 'constants/style';
import { useQueryOnFocus } from 'hooks/useQueryOnFocus';
import { Icon } from 'react-native-elements';
import { FlatList, StyleSheet, TextInput, View } from 'react-native';
import { GetLocationsDocument, LocationSummary } from 'api';
import { useConfig } from 'hooks/useConfig';
import { uniqBy } from 'lodash';
import { ApolloError, QueryHookOptions } from '@apollo/client';
import { useOverlay } from 'providers/Overlay';
import { ErrorType } from 'constants/error';
import { getError } from 'utils/error';
import { orderBy, filter } from 'lodash';
import { Text } from 'components/Text';
import { ProgressBar } from 'components/ProgressBar';
import { percentage } from 'utils/numberUtils';
import { ButtonValues, getLocationData } from './utils';
import { BranchPageProps } from './types';
import { getLocationStatus } from 'utils/locationUtils';
import { NUMBER_ACCESSORY_NATIVE_ID } from 'constants/form';
import { TabFilters } from 'components/TabFilters';
import { useEffect } from 'react';
import { ScreenLayout } from 'components/ScreenLayout';
import { NumericKeyboardAccessory } from 'components/NumericKeyboardAccessory';
import { getScreenTestingIds } from 'test-utils/testIds';

const BranchPage = ({
  loading = false,
  renderItem = () => {},
  onFinishLoad = () => {},
  branchSummarieListItemHeight = null,
  testID,
}: BranchPageProps) => {
  const [{ count }] = useConfig();
  const { showAlert } = useOverlay();

  const [filterState, setFilterState] = useState<string>('Not Started');
  const [searchTerm, setSearchTerm] = useState('');
  const [totalLocation, setTotalLocations] = useState(0);
  const [totalCounted, setTotalCounted] = useState(0);
  const [locations, setLocations] = useState<LocationSummary[]>([]);
  const [showLoading, setShowLoading] = useState(false);
  const listItemHeight = branchSummarieListItemHeight ? branchSummarieListItemHeight : 85;

  const options: QueryHookOptions = {
    fetchPolicy: 'network-only',
    onCompleted: () => {
      onFinishLoad();
    },
    onError: (error: ApolloError) => {
      showAlert(getError(ErrorType.LOCATIONS, error));
    },
  };

  const onChangeText = (text: string) => {
    setSearchTerm(text);
  };
 
  const resp = useQueryOnFocus(GetLocationsDocument, options);
  useEffect(() => {
    const locationData = getLocationData(resp.data);

    const sortedLocationData = orderBy(locationData.locations, 'id');
    const searchTermLocationData = sortedLocationData.filter((item) => {
      return item.id.toLowerCase().includes(searchTerm.toLowerCase());
    });

    const filteredLocationData = filter(searchTermLocationData, (l) => {
      const locationStatus = getLocationStatus(l);
      return locationStatus === filterState;
    });

    setTotalCounted(locationData.totalCounted);
    setTotalLocations(locationData.totalLocations);
    setLocations(filteredLocationData);
    setShowLoading(false);
  }, [resp.data, filterState, searchTerm]);

  const remainingLocations =
    searchTerm.length > 0 ? locations.length : totalLocation - totalCounted;

  const handleStatus = () => {
    return searchTerm.length > 0
      ? locations
      : locations.filter((l) => {
          const locationStatus = getLocationStatus(l);
          return locationStatus === filterState;
        });
  };

  const handleFilter = (value: string) => {
    if (filterState !== value) {
      setFilterState(value);
      setShowLoading(true);
    }
  };

  const renderList = useCallback(({ item }): any => renderItem(item),[])
  const keyExtractor = useCallback((item) => `${item.id}`,[])
  const getItemLayout = useCallback((data,index) => ({
    length: listItemHeight,
    offset: listItemHeight * index,
    index
  }),[])

  const testIds = getScreenTestingIds('BranchPage');

  return (
    <ScreenLayout
      style={styles.parentContainer}
      loading={loading || resp.loading}
      testID={testIds.screenLayout}
    >
      <ScreenLayout.StaticContent
        testID={testIds.staticContent}
        banner={{ title: `Count Locations: ${count?.branch.id}-${count?.id}` }}
        dropdown={{
          title: 'Branch Count Completion',
          children: (
            <View style={styles.detailsContainerOpen}>
              <View style={styles.searchContainer}>
                <TextInput
                  testID={testIds.input}
                  inputAccessoryViewID={NUMBER_ACCESSORY_NATIVE_ID}
                  autoCapitalize="none"
                  autoCorrect={false}
                  style={styles.searchBar}
                  placeholder="Location"
                  keyboardType="default"
                  value={searchTerm}
                  onChangeText={(text) => setSearchTerm(text)}
                />
                <NumericKeyboardAccessory
                  inputAccessoryViewID={NUMBER_ACCESSORY_NATIVE_ID}
                  onPress={(num) => {
                    const currentValue = searchTerm;
                    onChangeText(currentValue.concat(num));
                  }}
                />
                <View style={styles.searchIcon}>
                  <Icon name="search" color={Colors.SECONDARY_270} />
                </View>
              </View>
              <View style={styles.locationCounterContainer}>
                <Text
                  color={Colors.WHITE}
                  fontWeight={fw.MEDIUM}
                  fontSize={fontSize.BASE}
                  style={[styles.locationCount, styles.safeTextContainer]}
                  testID={testIds.remainingLocations}
                >
                  {remainingLocations === 1
                    ? `${handleStatus().length} location`
                    : `${handleStatus().length} locations`}
                </Text>
                <ProgressBar
                  value={totalCounted}
                  total={totalLocation}
                  containerStyle={styles.percentageContainer}
                  testID={testIds.progressBar}
                />
                <Text
                  fontSize={fontSize.BASE}
                  color={Colors.WHITE}
                  style={styles.locationCount}
                  fontWeight={fw.MEDIUM}
                  testID={testIds.progressBarText}
                >
                  {`${percentage(totalCounted, totalLocation)}% OVR`}
                </Text>
              </View>
            </View>
          ),
        }}
      >
        <View style={styles.tabView}>
          <TabFilters
            active={filterState}
            onPress={handleFilter}
            filters={ButtonValues}
            testID={testIds.tabFilter}
          />
        </View>

        {!(loading || resp.loading || showLoading) && (
          <FlatList
            keyboardShouldPersistTaps={'always'}
            data={uniqBy(locations, (location) => location.id)}
            refreshing={resp.loading}
            onRefresh={resp.refetch}
            keyExtractor={keyExtractor}
            style={styles.list}
            contentContainerStyle={styles.listContent}
            renderItem={renderList}
            initialNumToRender={7}
            maxToRenderPerBatch={8}
            windowSize={10}
            getItemLayout={getItemLayout}
            testID={testIds.locationList}
          />
        )}
      </ScreenLayout.StaticContent>
    </ScreenLayout>
  );
};

const styles = StyleSheet.create({
  parentContainer: {
    backgroundColor: Colors.SECONDARY_6100,
  },
  list: {
    width: '100%',
    backgroundColor: Colors.SECONDARY_6100,
    paddingHorizontal: 12,
  },
  listContent: {
    backgroundColor: Colors.SECONDARY_6100,
  },
  percentageContainer: {
    backgroundColor: Colors.WHITE,
    width: '35%',
    height: 12,
  },
  searchContainer: {
    alignSelf: 'center',
    flexDirection: 'row',
    alignItems: 'center',
    marginTop: 26,
  },
  searchBar: {
    alignSelf: 'center',
    backgroundColor: Colors.SECONDARY_490,
    width: 294,
    height: 41,
    borderColor: Colors.PRIMARY_1100,
    borderRadius: 3,
    borderWidth: 1,
    borderBottomRightRadius: 0,
    borderTopRightRadius: 0,
    borderRightWidth: 0,
    paddingHorizontal: 10,
  },
  searchIcon: {
    width: 40,
    height: 41,
    borderRadius: 3,
    justifyContent: 'center',
    alignSelf: 'center',
    alignItems: 'center',
    borderWidth: 1,
    borderLeftWidth: 0,
    borderTopLeftRadius: 0,
    borderBottomLeftRadius: 0,
    borderColor: Colors.PRIMARY_1100,
    backgroundColor: Colors.SECONDARY_490,
  },
  banner: {
    backgroundColor: Colors.SECONDARY_6100,
    width: '100%',
  },
  bannerSubHeader: {
    flexDirection: 'row',
    marginTop: 14,
    justifyContent: 'center',
  },
  subtitle: {
    lineHeight: 24,
  },
  detailsContainerOpen: {
    backgroundColor: Colors.PRIMARY_1100,
    paddingVertical: 25,
    paddingHorizontal: 0,
    height: 135,
    justifyContent: 'center',
  },
  locationCount: {
    lineHeight: 24,
  },
  locationCounterContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    alignSelf: 'center',
    justifyContent: 'space-between',
    marginVertical: 17,
    width: 334,
  },
  safeTextContainer: {
    width: 140,
  },
  filterContainerHeader: {
    height: 48,
    alignItems: 'center',
    flexDirection: 'row',
    width: '100%',
    justifyContent: 'center',
    radius: 2,
    backgroundColor: Colors.WHITE,
    alignContent: 'center',
  },
  spacer: { color: Colors.PRIMARY_1100 },
  twentyWidth: { width: '20%' },
  locationCountAddonStyle: {
    lineHeight: 15,
    width: '60%',
    paddingTop: 3,
  },
  iconContainer: {
    width: '20%',
    paddingRight: 10,
    alignItems: 'center',
  },
  activeFilter: {
    borderBottomWidth: 3,
    minWidth: 60,
    alignItems: 'center',
  },
  loadingWrapper: {
    paddingTop: 48,
  },
  tabView:{
    height:90,
    justifyContent:'center',
    paddingVertical:0,
  }
});

export default BranchPage;
