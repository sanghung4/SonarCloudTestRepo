import React, { useMemo, useState, useCallback } from 'react';
import {
  FlatList,
  View,
  TouchableWithoutFeedback,
  TextInput,
  Keyboard,
  StyleSheet,
  TouchableOpacity,
} from 'react-native';
import * as API from 'api';
import { Colors, FontWeight as fw } from 'constants/style';
import { useConfig } from 'hooks/useConfig';
import { Text } from 'components/Text';
import { useOverlay } from 'providers/Overlay';
import { useLoading } from 'hooks/useLoading';
import { useQueryOnFocus } from 'hooks/useQueryOnFocus';
import { WriteInFilter } from './types';
import { getWriteInData, getQueryOptions } from './utils';
import { WriteIn } from './WriteIn';
import { WriteInsEmpty } from './WriteInsEmpty';
import { SvgIcons } from 'components/SVG';
import { ScreenLayout } from 'components/ScreenLayout';
import { getScreenTestingIds } from 'test-utils/testIds';
import useRenderListener from '../../hooks/useRenderListener';

const WriteIns = () => {
  useRenderListener();
  const alert = useOverlay();
  const [{ count }] = useConfig();
  const [filter, setFilter] = useState(WriteInFilter.UNRESOLVED);
  const [searchTerm, setSearchTerm] = useState('');
  const options = getQueryOptions(alert);
  const resp = useQueryOnFocus(API.GetWriteInsDocument, options);
  const data = useMemo(() => getWriteInData(resp.data!, filter), [
    resp.data,
    filter,
  ]);
  const [filteredData, setFilteredData] = useState<API.WriteIn[]>();
  const [isSearching, setIsSearching] = useState(false);
  const [isResolvedActive, setResolvedActive] = useState(false);
  const [isUnresolvedActive, setUnresolvedActive] = useState(true);

  const writeinItemHeight = 115;

  useLoading([resp.loading]);

  const handleFilterTabChange = () => {
    setSearchTerm('');
    Keyboard.dismiss();
    setIsSearching(false);
  };

  const handleSearch = (value: string) => {
    setSearchTerm(value);
    if (value.length !== 0) {
      setIsSearching(true);
      const filtered = data.filter(
        (item: API.WriteIn) =>
          item.locationId.toLowerCase().includes(value.toLowerCase()) ||
          item.description.toLowerCase().includes(value.toLowerCase())
      );
      setFilteredData(filtered);
    } else {
      setFilteredData(data);
      setIsSearching(false);
    }
  };

  const renderList = useCallback(({ item }) => (item ? <WriteIn item={item} /> : null),[])
  const keyExtractor = useCallback((item, idx) => item ? `${item.id}` : `${idx}`,[])
  const getItemLayout = useCallback((data,index) => ({
    length: writeinItemHeight,
    offset: writeinItemHeight * index,
    index
  }),[])

  const testIds = getScreenTestingIds('WriteIns');

  return (
    <TouchableWithoutFeedback
      onPress={() => Keyboard.dismiss()}
      style={styles.container}
    >
      <ScreenLayout style={styles.background} testID={testIds.screenLayout}>
        <ScreenLayout.StaticContent
          banner={{
            title: 'View Write-Ins',
          }}
        >
          <View style={styles.searchContainer}>
            <TextInput
              style={styles.searchBar}
              onChangeText={(val) => handleSearch(val)}
              value={searchTerm}
              autoCapitalize="none"
              clearButtonMode="always"
              placeholder="Location / Item"
              maxLength={50}
              autoComplete="off"
              autoCorrect={false}
              clearTextOnFocus={false}
              testID={testIds.searchInput}
            />
            <View style={styles.searchIcon}>
              <SvgIcons name={'SearchIcon'} testID={testIds.screen} />
            </View>
          </View>
          <View style={styles.filterContainer}>
            <TouchableOpacity
              onPress={() => {
                setFilter(WriteInFilter.UNRESOLVED);
                setUnresolvedActive(true);
                setResolvedActive(false);
                handleFilterTabChange();
              }}
              testID={testIds.unresolvedTab}
            >
              <View
                style={
                  isUnresolvedActive ? styles.redUnderline : styles.noUnderline
                }
              >
                <Text
                  fontWeight={fw.MEDIUM}
                  color={
                    isUnresolvedActive
                      ? Colors.SUPPORT_2100
                      : Colors.SECONDARY_2100
                  }
                  fontSize={16}
                  adjustsFontSizeToFit
                  allowFontScaling={false}
                  centered
                >
                  Unresolved
                </Text>
              </View>
            </TouchableOpacity>
            <TouchableOpacity
              onPress={() => {
                setFilter(WriteInFilter.RESOLVED);
                setResolvedActive(true);
                setUnresolvedActive(false);
                handleFilterTabChange();
              }}
              testID={testIds.resolvedTab}
            >
              <View
                style={
                  isResolvedActive ? styles.greenUnderline : styles.noUnderline
                }
              >
                <Text
                  fontWeight={fw.MEDIUM}
                  color={
                    isResolvedActive
                      ? Colors.SUPPORT_1100
                      : Colors.SECONDARY_2100
                  }
                  fontSize={16}
                  adjustsFontSizeToFit
                  allowFontScaling={false}
                  centered
                >
                  Resolved
                </Text>
              </View>
            </TouchableOpacity>
          </View>

          {!resp.loading && (
            <FlatList
              data={isSearching ? filteredData : data}
              extraData={isSearching}
              refreshing={resp.loading}
              onRefresh={resp.refetch}
              keyExtractor={keyExtractor}
              style={styles.list}
              contentContainerStyle={styles.listContent}
              ListEmptyComponent={<WriteInsEmpty />}
              renderItem={renderList}
              initialNumToRender={7}
              maxToRenderPerBatch={8}
              windowSize={10}
              getItemLayout={getItemLayout}
            />
          )}
        </ScreenLayout.StaticContent>
      </ScreenLayout>
    </TouchableWithoutFeedback>
  );
};

const styles = StyleSheet.create({
  banner: {
    backgroundColor: Colors.SECONDARY_6100,
  },
  bannerWrapper: { width: '100%' },
  subtitle: {
    marginTop: 24,
    lineHeight: 24,
  },
  list: {
    width: '100%',
    alignSelf: 'center',
  },
  listContent: {
    flexGrow: 1,
  },
  searchBar: {
    alignSelf: 'center',
    width: 274,
    height: 40,
    borderWidth: 1,
    borderRadius: 3,
    borderColor: Colors.PRIMARY_1100,
    borderBottomRightRadius: 0,
    borderTopRightRadius: 0,
    backgroundColor: Colors.WHITE,
    borderRightWidth: 0,
    paddingHorizontal: 10,
  },
  searchIcon: {
    width: 40,
    height: 40,
    borderRadius: 3,
    justifyContent: 'center',
    borderWidth: 1,
    borderLeftWidth: 0,
    borderTopLeftRadius: 0,
    borderBottomLeftRadius: 0,
    borderColor: Colors.PRIMARY_1100,
    backgroundColor: Colors.WHITE,
  },
  searchContainer: {
    alignSelf: 'center',
    flexDirection: 'row',
    marginTop: 12,
  },
  filterContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-evenly',
    width: '100%',
    paddingHorizontal: 38,
    marginTop: 28,
    marginBottom: 14,
  },
  redUnderline: {
    alignSelf: 'center',
    borderBottomColor: Colors.SUPPORT_2100,
    borderBottomWidth: 2,
    width: 106,
  },
  noUnderline: {
    alignSelf: 'center',
    width: 106,
  },
  greenUnderline: {
    alignSelf: 'center',
    borderBottomColor: Colors.SUPPORT_1100,
    borderBottomWidth: 2,
    width: 106,
  },
  container: {
    width: '100%',
    height: '100%',
    backgroundColor: Colors.SECONDARY_6100,
  },
  wrapper: { flex: 1 },
  background: { backgroundColor: Colors.SECONDARY_6100 },
});

export default WriteIns;
