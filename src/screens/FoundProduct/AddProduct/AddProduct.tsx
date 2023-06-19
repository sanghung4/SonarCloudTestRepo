import React, { useEffect, useState } from 'react';
import {
  SearchProductsKourierQuery,
  useSearchProductsKourierLazyQuery,
} from 'api';
import { SearchBar } from 'components/SearchBar';
import { NUMBER_ACCESSORY_NATIVE_ID } from 'constants/form';
import { Colors, fontSize, FontWeight } from 'constants/style';
import { uniqBy } from 'lodash';
import { StyleSheet, View } from 'react-native';
import { CheckBox } from 'react-native-elements';
import { SearchResultItem } from './SearchResultItem';
import { SearchResultsEmpty } from './SearchResultsEmpty';
import { SearchResultsHeader } from './SearchResultsHeader';
import { createSearchProductsKourierInput, PAGE_SIZE } from './utils';
import { Text } from 'components/Text';
import { RouteNames } from 'constants/routes';
import { useNavigation } from '@react-navigation/native';
import { AppNavigation } from 'navigation/types';
import { CustomButton } from 'components/CustomButton';
import { NumericKeyboardAccessory } from 'components/NumericKeyboardAccessory';
import { getScreenTestingIds } from 'test-utils/testIds';

const AddProduct = () => {
  //New Kourier query
  const [result2, setResult] = useState<
    SearchProductsKourierQuery['searchProductsKourier']['prodSearch']
  >([]);
  const [pageSize, setPageSize] = useState(PAGE_SIZE);

  const [filterResult, setFilterResult] = useState<any>([]);

  const navigation = useNavigation<AppNavigation<'FoundProduct'>>();
  const [searchTerm, setSearchTerm] = useState('');
  const [searchById, setSearchById] = useState(false);
  const [isRefreshing, setIsRefreshing] = useState(false);
  const [page, setPage] = useState(1);
  const [searchInitiated, setSearchInitiated] = useState(false);
  const [isLastPage, setIsLastPage] = useState(false);
  const [searchingForMore, setSearchingForMore] = useState(false);

  //New Kourier query
  const [
    searchProductsKourier,
    {
      data: searchData,
      loading: searchLoading,
      networkStatus: searchNetworkStatus,
    },
  ] = useSearchProductsKourierLazyQuery({
    fetchPolicy: 'network-only',
    notifyOnNetworkStatusChange: true,
  });

  useEffect(() => {
    if (searchData) {
      setResult((prevData) => [
        ...prevData,
        ...searchData.searchProductsKourier.prodSearch
      ]);
    }
    setSearchingForMore(searchLoading);
  }, [searchData]);


  useEffect(() => {
    if(result2.length){
      let dataResult = result2[0]?.products?.slice(0,pageSize);
      setFilterResult(dataResult ? dataResult : [] )
      
    }else{
      setFilterResult([])
    }
    setSearchingForMore(searchLoading);
  }, [result2, pageSize]);

  useEffect(() => {
    if(result2.length){
      setIsLastPage(
        filterResult.length ===
        result2[0]?.products?.length ||
        filterResult.length === 1
      );
    }
  }, [filterResult])

  useEffect(() => {
    setIsRefreshing(searchLoading || searchNetworkStatus === 4);
  }, [searchLoading, searchNetworkStatus]);

  // Helpers
  const handleNumberPress = (num: string) => {
    handleSearchChange(searchTerm + num);
  };

  const getEmptyText = () => {
    if (searchLoading) {
      return 'Searching';
    } else {
      return searchInitiated ? null : 'Search for products.';
    }
  };

  // Search and fetches
  const handleSearchChange = (text: string) => {
    if (searchInitiated) {
      setSearchInitiated(false);
    }
    if (result2.length > 0) {
      setResult([]);
      setPageSize(PAGE_SIZE);
    }
    setSearchTerm(text);
  };

  const handleSearch = () => {
    if (searchTerm) {
      setSearchInitiated(true);

      const input = createSearchProductsKourierInput(searchTerm,searchById);
      searchProductsKourier({ variables: { input } });
      setSearchingForMore(false);
      setResult([]);
    }
    setPage(1);
  };

  const handleSearchForMore = () => {
    if (!searchTerm || isLastPage) {
      return;
    }
    setPageSize(pageSize + PAGE_SIZE)
    return;
    setSearchingForMore(true);
    //searchProductsEclipse({ variables: { input } });
    setPage((prevPage) => prevPage + 1);
  };

  const testIds = getScreenTestingIds('AddProduct');

  return (
    <View style={styles.addProductContainer}>
      <View style={styles.horizontalPadding}>
        <Text
          style={styles.searchBoxLabel}
          color={Colors.PRIMARY_1100}
          fontWeight={FontWeight.MEDIUM}
        >
          Search
        </Text>
        <SearchBar
          placeholder={`Search by ${searchById ? 'ID' : 'keyword'}`}
          onChangeText={handleSearchChange}
          onSubmitEditing={handleSearch}
          value={searchTerm}
          showLoading={searchLoading}
          disabled={searchLoading}
          autoComplete="off"
          autoCorrect={false}
          inputAccessoryViewID={NUMBER_ACCESSORY_NATIVE_ID}
          inputContainerStyle={styles.searchBarInputContainer}
          containerStyle={styles.searchBarContainer}
          labelStyle={styles.searchBoxLabel}
          testID={testIds.searchBar}
        />
        <CheckBox
          title="Search by product ID"
          checked={searchById}
          onPress={() => setSearchById((prevSearchById) => !prevSearchById)}
          containerStyle={styles.checkBoxContainer}
          testID={testIds.productIdCheckbox}
        />
        <CustomButton
          title="Search"
          containerStyle={styles.searchButtonContainer}
          onPress={handleSearch}
          disabled={searchLoading}
          testID={testIds.searchButton}
        />
      </View>
      <View style={styles.horizontalPadding}>
        <SearchResultsHeader query={searchTerm} />

        {/* If there  are search results, display them, if not, show empty with appropriate text */}
        {filterResult.length > 0 ? (
          uniqBy(filterResult, (e:any) => e?.productId).map((item, index) => {
            if (item) {
              return (
                <SearchResultItem
                  key={item.productId}
                  product={item}
                  index={index}
                  onPress={() =>
                    navigation.navigate(RouteNames.PRODUCT_DETAIL, {
                      product: item,
                    })
                  }
                  testID={testIds.searchResultItem}
                />
              );
            }
          })
        ) : (
          <SearchResultsEmpty loading={searchLoading} text={getEmptyText()} />
        )}
        {/* If there is another page, show more */}
        {!(isLastPage || isRefreshing) && (
          <View style={styles.showMoreButtonWrapper}>
            <CustomButton
              title="Show More..."
              type="link"
              onPress={() => {
                handleSearchForMore();
              }}
              disabled={isRefreshing}
              loading={isRefreshing}
              testID={testIds.showMoreButton}
            />
          </View>
        )}
      </View>
      <NumericKeyboardAccessory
        inputAccessoryViewID={NUMBER_ACCESSORY_NATIVE_ID}
        onPress={handleNumberPress}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  scrollWrapper: {
    width: '100%',
    flexGrow: 1,
    flex: 1,
  },
  horizontalPadding: {
    paddingHorizontal: 24,
  },
  listContent: {
    flexGrow: 1,
  },
  addProductContainer: {
    backgroundColor: Colors.WHITE,
    flex: 1,
  },
  searchBarInputContainer: {
    backgroundColor: Colors.SECONDARY_460,
    borderTopColor: Colors.SECONDARY_3100,
    borderBottomColor: Colors.SECONDARY_3100,
    borderWidth: 1,
    borderBottomWidth: 1,
  },
  searchBarContainer: {
    paddingHorizontal: 0,
    backgroundColor: 'transparent',
    shadowColor: Colors.BLACK,
    shadowOpacity: 0.25,
    shadowRadius: 3,
    shadowOffset: { height: 0, width: 0 },
  },
  searchBoxLabel: { marginBottom: 8, marginTop: 16 },
  checkBoxContainer: {
    paddingLeft: 0,
    marginLeft: 12,
    marginRight: 0,
    backgroundColor: 'transparent',
    borderWidth: 0,
    marginBottom: 0,
  },
  bannerTitle: {
    fontWeight: FontWeight.MEDIUM,
    color: Colors.PRIMARY_1100,
    fontSize: fontSize.H4,
  },
  searchButtonContainer: {
    marginBottom: 12,
  },
  searchButtonTitle: {
    fontSize: fontSize.BASE,
    fontWeight: FontWeight.MEDIUM,
  },
  showMoreButtonWrapper: {
    paddingBottom: 48,
    borderTopColor: Colors.SECONDARY_290,
    borderTopWidth: 1,
  },
});

export default AddProduct;