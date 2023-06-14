import React, { useEffect, useState } from 'react';
import { FlatList, StyleSheet, TouchableOpacity, View } from 'react-native';
import { ScreenLayout } from 'components/ScreenLayout';
import { Text } from 'components/Text';
import { Colors, FontWeight } from 'constants/style';
import { SearchInput } from 'components/Input';
import { LocationListItem } from './LocationListItem';
import { AppScreenProps } from 'navigation/types';
import { RouteNames } from 'constants/routes';
import { useGetVarianceLocationsLazyQuery, VarianceLocationSummary } from 'api';
import { getScreenTestingIds } from 'test-utils/testIds';
import { orderBy } from 'lodash';
import { useIsFocused } from '@react-navigation/native';
import { SEARCH_ACCESSORY_ID } from './utils';
import { NumericKeyboardAccessory } from 'components/NumericKeyboardAccessory';
import { useOverlay } from 'providers/Overlay';
import { SortByEnum } from './types';
import { CustomIcon, CustomIconNames } from 'components/CustomIcon';
import useRenderListener from 'hooks/useRenderListener';

const VarianceLocationList = ({
  navigation,
}: AppScreenProps<'VarianceLocationList'>) => {
  useRenderListener();

  // ---------- HOOKS ----------
  const isFocused = useIsFocused();
  const { toggleSortBy, activeSortBy, handleSortBy } = useOverlay();

  // ---------- STATE ----------
  const [searchTerm, setSearchTerm] = useState('');
  const [displayMessage, setDisplayMessage] = useState(true);
  const [filteredLocations, setFilteredLocations] = useState<
    VarianceLocationSummary[] | undefined
  >(undefined);

  // ---------- API ----------
  const [
    getVarianceLocationList,
    {
      data: locationsData,
      loading: locationsLoading,
      refetch: locationsRefetch,
    },
  ] = useGetVarianceLocationsLazyQuery({
    fetchPolicy: 'network-only',
    notifyOnNetworkStatusChange: true,
  });

  // ---------- EFFECTS ----------
  useEffect(() => {
    isFocused && getVarianceLocationList();
  }, [isFocused]);

  useEffect(() => {
    const locations = locationsData?.varianceLocations.content;
    const sortedLocations = orderBy(locations, 'id');
    const searchLocations = sortedLocations.filter((item) =>
      item.id.toLowerCase().includes(searchTerm.toLowerCase())
    );

    setFilteredLocations(searchLocations);
  }, [searchTerm, locationsData]);

  useEffect(() => {
    handleSortBy(SortByEnum.ASCENDING);
  }, []);

  // ---------- ACTIONS ----------
  const handleNumberAccessoryChange = (value: string) => {
    setSearchTerm((prev) => prev + value);
  };

  const handleSorting = () => {
    switch (activeSortBy) {
      case SortByEnum.ASCENDING:
        return orderBy(filteredLocations, 'id', 'asc');
      case SortByEnum.DESCENDING:
        return orderBy(filteredLocations, 'id', 'desc');
      case SortByEnum.GROSS_LOWTOHIGH:
        return orderBy(filteredLocations, 'grossVarianceCost', 'asc');
      case SortByEnum.GROSS_HIGHTOLOW:
        return orderBy(filteredLocations, 'grossVarianceCost', 'desc');
      case SortByEnum.NET_LOWTOHIGH:
        return orderBy(filteredLocations, 'netVarianceCost', 'asc');
      case SortByEnum.NET_HIGHTOLOW:
        return orderBy(filteredLocations, 'netVarianceCost', 'desc');
      default:
        return orderBy(filteredLocations, 'id', 'asc');
    }
  };

  const handleLocationPress = (locationId: string) => {
    navigation.navigate(RouteNames.VARIANCE_LOCATION, {
      locationId: locationId,
    });
  };

  // ---------- HELPERS ----------
  const testIds = getScreenTestingIds('VarianceLocationList');

  // ---------- JSX ----------
  return (
    <ScreenLayout testID={testIds.screen} loading={locationsLoading}>
      <ScreenLayout.StaticContent
        testID={testIds.screen}
        banner={{ title: 'Variance Recount' }}
        dropdown={{
          title: 'Variance Recount Completion',
          children: (
            <View style={styles.dropdownWrapper}>
              <SearchInput
                value={searchTerm}
                onChangeText={setSearchTerm}
                placeholder="Location"
                inputAccessoryViewID={SEARCH_ACCESSORY_ID}
                testID={testIds.searchBar}
              />
              {locationsData?.varianceLocations && (
                <Text
                  color={Colors.WHITE}
                  fontWeight={FontWeight.MEDIUM}
                  style={styles.dropdownText}
                  testID={testIds.locationsToGo}
                >
                  {filteredLocations?.length}
                  {filteredLocations?.length === 1 ? ' Location' : ' Locations'}
                </Text>
              )}
            </View>
          ),
        }}
        messages={[
          {
            text1: 'Only showing locations with variance for recount',
            open: displayMessage,
            onClose: () => setDisplayMessage(false),
          },
        ]}
      >
        <TouchableOpacity
          style={styles.filterContainer}
          onPress={() => toggleSortBy(true)}
        >
          <CustomIcon name={CustomIconNames.DoubleCarat} />
          <Text
            color={Colors.BLACK}
            fontWeight={FontWeight.REGULAR}
            style={styles.filterText}
          >
            Sort by: {activeSortBy.replace(':', '')}
          </Text>
        </TouchableOpacity>
        {!locationsLoading && (
          <FlatList
            refreshing={locationsLoading}
            onRefresh={locationsRefetch}
            data={handleSorting()}
            style={styles.list}
            keyExtractor={(item) => `${item.id}`}
            testID={testIds.locationList}
            ItemSeparatorComponent={() => (
              <View style={styles.listItemSeparator} />
            )}
            renderItem={({ item, index }) => (
              <LocationListItem
                testID={`${testIds.screen}`}
                location={item}
                key={index}
                onPress={() => handleLocationPress(item.id)}
              />
            )}
            ListEmptyComponent={
              <Text
                centered
                style={styles.emptyText}
                testID={testIds.noLocations}
              >
                There are no locations sent for recount.
              </Text>
            }
          />
        )}
        <NumericKeyboardAccessory
          inputAccessoryViewID={SEARCH_ACCESSORY_ID}
          onPress={handleNumberAccessoryChange}
        />
      </ScreenLayout.StaticContent>
    </ScreenLayout>
  );
};

const styles = StyleSheet.create({
  dropdownWrapper: {
    paddingHorizontal: 32,
    paddingTop: 16,
    paddingBottom: 8,
    width: '100%',
  },
  dropdownText: { marginTop: 24 },
  list: { width: '100%', marginTop: 8, paddingHorizontal: 24 },
  listItemSeparator: {
    borderBottomWidth: 2,
    borderColor: Colors.SECONDARY_3100,
  },
  emptyText: {
    marginTop: 112,
    marginHorizontal: 24,
    fontSize: 14,
    fontWeight: FontWeight.MEDIUM,
  },
  filterContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    alignSelf: 'flex-end',
    padding: 5,
    marginTop: 20,
  },
  filterText: {
    marginLeft: 4,
    marginRight: 8,
  },
});

export default VarianceLocationList;
