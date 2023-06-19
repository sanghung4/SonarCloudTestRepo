import React, {
  useCallback,
  useEffect,
  useMemo,
  useRef,
  useState,
} from 'react';
import { StyleSheet, View } from 'react-native';
import { Colors, FontWeight } from 'constants/style';
import {
  LocationItem,
  LocationItemStatus,
  useCompleteCountMutation,
  useGetNextLocationLazyQuery,
  useUpdateCountMutation,
} from 'api';
import { QuantityMap } from './types';
import { useOverlay } from 'providers/Overlay';
import { useLocation } from 'hooks/useLocation';
import { getError } from 'utils/error';
import { ErrorType } from 'constants/error';
import { CONTROL_ACCESSORY_NATIVE_ID, getInitialInputRefs } from './utils';
import { PercentageBar } from 'components/PercentageBar';
import { RouteNames } from 'constants/routes';
import { orNA } from 'utils/stringUtils';
import { TabFilters } from 'components/TabFilters';
import {
  groupBy,
  isNull,
  transform,
  orderBy,
  uniqBy,
  values,
  compact,
  isNaN,
  findKey,
  isEmpty,
} from 'lodash';
import { useFocusEffect } from '@react-navigation/native';
import { AppScreenProps } from 'navigation/types';
import { ScreenLayout } from 'components/ScreenLayout';
import { CustomIconNames } from 'components/CustomIcon';
import { CountInput } from 'components/CountInput';
import { ControlKeyboardAccessory } from 'components/ControlKeyboardAccessory';
import { CustomButton } from 'components/CustomButton';
import { handleMutationComplete } from 'utils/apollo';
import { hideToast, showErrorToast } from 'components/ToastConfig';
import { getScreenTestingIds } from 'test-utils/testIds';
import useRenderListener from 'hooks/useRenderListener';
import  { useAppState } from 'providers/AppState';

const LocationItems = ({ navigation }: AppScreenProps<'LocationItems'>) => {
  useRenderListener();

  // ------------------------ Hooks & Providers ----------------
  const { showAlert } = useOverlay();
  const {productQuantity,updateProductQuantity,updateProductQuantityOnSuccess,storeProductQuantity} = useAppState()

  const {
    location,
    loading: loadingLocation,
    getLocation,
    status,
  } = useLocation({
    onError: (e) => {
      showAlert(getError(ErrorType.LOCATION, e));
    },
  });

  const inputRefs = useRef(getInitialInputRefs(location?.items || []));

  // ------------------------------ State ----------------------
  const [countItems, setCountItems] = useState<LocationItem[]>([]);
  const [totalItems, setTotalItems] = useState(0);
  const [totalCounted, setTotalCounted] = useState(0);
  const [currentTab, setCurrentTab] = useState('togo');
  const [isRecount, setIsRecount] = useState(false);

  // Warning states
  const [showZeroMessage, setShowZeroMessage] = useState(true);
  const [showRecountMessage, setShowRecountMessage] = useState(true);

  // Focused inputs
  const [prevFocusedTag, setPrevFocusedTag] = useState<string>();
  const [currentFocusedTag, setCurrentFocusedTag] = useState<string>();
  const [nextFocusedTag, setNextFocusedTag] = useState<string>();

  // Map of all inputs and quantities
  const [quantityMap, setQuantityMap] = useState<QuantityMap>({});

  // Focus on first input on page render
  const [focusFirst, setFocusFirst] = useState(true);

  // Next location ID
  const [nextLocationID, setNextLocationID] = useState('');

  // Use error's next location instead
  const [errorNextLocationID, setErrorNextLocationID] = useState('');
  const [useErrorNextLocation, setUseErrorNextLocation] = useState(false);

  // Checks if the last input is filled
  const [currentInputValue, setCurrentInputValue] = useState('');


  // ------------------------------- API -----------------------

  const [update, { loading: loadingUpdateCount }] = useUpdateCountMutation({
    onCompleted: (resp) => {
      handleMutationComplete(resp.updateCount);
    },
  });

  const [getNextLocation] = useGetNextLocationLazyQuery({
    onCompleted: ({ nextLocation: { locationId } }) => {
      setNextLocationID(locationId.toLowerCase() === 'none' ? '' : locationId);
    },
    onError: () => {
      setNextLocationID('');
    },
  });

  const [complete] = useCompleteCountMutation({
    onCompleted: (resp) => {
      handleMutationComplete(resp.completeCount);
    },
  });

const getQuantityFromLocalState =  useCallback((tagNumValue:LocationItem["tagNum"])=>{
  const getObj =  productQuantity.filter(o=>o.tagNum === tagNumValue)
  return getObj !== null ? getObj[0]?.quantity : 0
},[productQuantity])

  // ------------------------- Effects -------------------------

  // Initialize the quantity map for the location
  useEffect(() => {
    // Group and order items
    const groupedItems = groupBy(location?.items, 'tagNum');

    const isCommitted = !!location?.committed;

    // Initialize the quantity map
    const newQuantityMap = transform(
      groupedItems,
      (acc: QuantityMap, current) => {
        const tagNum = current[0].tagNum;
        const itemCommitted =
          location?.committed &&
          current[0].status === LocationItemStatus.COMMITTED;

        let quantity = itemCommitted ? null 
        : current[0].quantity 
        ? current[0].quantity 
        : getQuantityFromLocalState(tagNum)
        quantity = !quantity ? null : quantity 
        if (tagNum) {
          acc[tagNum] = isNull(quantity) ? null : `${quantity}`;
        }
      },
      {}
    );

    // Set the appropriate filter
    if (findKey(newQuantityMap, (item) => !item)) {
      setCurrentTab('togo');
    }
    // Set quantity map
    setQuantityMap(newQuantityMap);

    // Set the next location
    getNextLocation({ variables: { id: location?.id || '' } });

    // If the location is committed, it is a recount
    if (isCommitted) {
      setCurrentTab('all');
      setIsRecount(true);
    }
  }, [location]);

  // Set all of the displayed items and initialize values
  useEffect(() => {
    if (!isEmpty(quantityMap)) {
      // Get items from location with updated quantity map values
      const items = (location?.items || []).map((item) => ({
        ...item,
        quantity: parseInt(quantityMap[item?.tagNum || ''] || '', 10),
      }));
      // Find all unique items
      const uniqueItems = uniqBy(items, (item) => item.prodNum);
      // Sort items based on sequence or tagNum
      const sortedItems = orderBy(
        uniqueItems,
        ['sequence', 'tagNum'],
        ['asc', 'asc']
      );
      // Filter items based on selected filter
      const displayedItems =
        currentTab === 'togo'
          ? sortedItems.filter((item) => isNaN(item.quantity))
          : sortedItems;
      // Set the count items to the displayed items
      setCountItems(displayedItems);
      // Set the total to the unique items length
      setTotalItems(uniqueItems.length);
      // Set the total counted with the number of items with values
      setTotalCounted(compact(values(quantityMap)).length);
      // If the number of displayed items is 0, switch to the all tab
      setCurrentTab((prev) => (displayedItems.length === 0 ? 'all' : prev));
    }
  }, [location, quantityMap, currentTab]);

  // Focus on first input on page build
  useEffect(() => {
    const timer = setTimeout(() => {
      const tag = countItems[0]?.tagNum;
      if (focusFirst && tag) {
        inputRefs.current[tag]?.focus();
        setFocusFirst(false);
      }
    }, 500);
    return () => clearTimeout(timer);
  }, [countItems]);

  // Hide the toast if navigating away from page.
  useFocusEffect(
    useCallback(() => {
      return () => {
        hideToast();
      };
    }, [])
  );

  // ------------------------- Actions -------------------------

  // Handle pressing on the product image
  const handleItemPress = (item: LocationItem) => () => {
    navigation.navigate(RouteNames.LOCATION_ITEM_DETAIL, {
      item,
    });
  };

  // Handle focusing on a count item
  const handleFocus = (index: number) => () => {
    const prevTag = countItems[index - 1]?.tagNum;
    const currentTag = countItems[index]?.tagNum;
    const nextTag = countItems[index + 1]?.tagNum;

    if (currentTag) {
      setCurrentInputValue(inputRefs.current[currentTag]?.props.value || '');
    }

    setPrevFocusedTag(prevTag ? prevTag : undefined);
    setCurrentFocusedTag(currentTag ? currentTag : undefined);
    setNextFocusedTag(nextTag ? nextTag : undefined);
  };

  // Handle pushing one of the directions on the keyboard control bar (ios only)
  const handleControlDirection = (direction: 'next' | 'prev') => () => {
    if (direction === 'next' && nextFocusedTag) {
      inputRefs.current[nextFocusedTag]?.focus();
    }
    if (direction === 'prev' && prevFocusedTag) {
      inputRefs.current[prevFocusedTag]?.focus();
    }
  };

  // On press of done, blur the current input
  const handleControlDone = () => {
    if (currentFocusedTag) {
      inputRefs.current[currentFocusedTag]?.blur();
    }
  };

  const handleChangeText = (value: string, item:LocationItem | undefined) => {
    setCurrentInputValue(value);
    updateProductQuantity(location?.id,item,Number(value))
    // setQuantityMap((prevMap) => ({ ...prevMap, [tagNum]: value }));
  };

  // Update the count on blur
  const handleBlur = (tagNum: string) => (newValue: string) => {
    const localQuantityValue = getQuantityFromLocalState(tagNum)?.toString()
    const newFormattedValue = newValue ? newValue : null;

    if ((quantityMap[tagNum] !== newFormattedValue) || (quantityMap[tagNum] === localQuantityValue)) {
      update(getUpdateVariables(tagNum, newFormattedValue))
      .then(({data})=>{
        if(data?.updateCount.success){
         updateProductQuantityOnSuccess(tagNum)
        }else{
          storeProductQuantity(productQuantity)
        }
      })
      .catch(() => {
        storeProductQuantity(productQuantity)
        setQuantityMap((prevMap) => ({ ...prevMap, [tagNum]: null }));
      });
    }
    // setCurrentFocusedTag(undefined);
    // setCurrentInputValue('');
    setQuantityMap((prevMap) => ({ ...prevMap, [tagNum]: newFormattedValue }));
  };

  // Completes the count and submits last input value
  const handleComplete = async () => {
    const localQuantityValue = getQuantityFromLocalState(currentFocusedTag)?.toString()
    if (currentFocusedTag){
        if((currentInputValue !== quantityMap[currentFocusedTag]) || (quantityMap[currentFocusedTag] === localQuantityValue)) {
            await update(
              getUpdateVariables(currentFocusedTag, currentInputValue)
            )
            .then(({data})=>{
              if(data?.updateCount.success){
                updateProductQuantityOnSuccess(currentFocusedTag)        
              }else{
                storeProductQuantity(productQuantity)
                //storeDataD(localQuantityMap)
              }
            })
            .catch(() => {
              storeProductQuantity(productQuantity)
                setQuantityMap((prevMap) => ({
                  ...prevMap,
                  [currentFocusedTag]: null,
                }));
                return;
              });
            if (location) {
              completeLocation(location.id, nextLocationID, currentFocusedTag);
            }
          } else if (location) {
            completeLocation(location.id, nextLocationID, currentFocusedTag);
          }
      } 

    if (nextLocationID) {
      const targetNextLocation = useErrorNextLocation
        ? errorNextLocationID
        : nextLocationID;
      setFocusFirst(true);
      getLocation(targetNextLocation);
      setUseErrorNextLocation(false);
    }
  };

  // Handles submission of edited data
  const handleSubmitEditing = () => {
    if (nextFocusedTag) {
      inputRefs.current[nextFocusedTag]?.focus();
    } else if (currentFocusedTag) {
      inputRefs.current[currentFocusedTag]?.blur();
    }
  };

  // ------------------------- Helpers -------------------------

  // Check if the form is ready to submit
  const isValid = useMemo(() => {
    const allTags = Object.keys(quantityMap);
    const allValues = compact(values(quantityMap));
    const onlyOneLeft = allTags.length - allValues.length === 1;

    if (location?.committed) {
      return allValues.length > 0 || currentInputValue;
    }

    // If all tags have submitted values, return true
    if (allTags.length === allValues.length) {
      return true;
    }

    if (currentFocusedTag && !currentInputValue) {
      return false;
    }

    // If an input is focused and only one value is required
    if (currentFocusedTag && onlyOneLeft) {
      const lastTag = findKey(quantityMap, (item) => !item);
      return currentFocusedTag === lastTag && currentInputValue;
    }

    return false;
  }, [quantityMap, currentInputValue, currentFocusedTag]);

  // Creates payload for update count mutation
  const getUpdateVariables = (tagNum: string, newValue: string | null) => {
    const parsedQty = parseInt(newValue || '', 10);

    return {
      variables: {
        item: {
          locationId: location?.id || '',
          quantity: isNaN(parsedQty) ? null : parsedQty,
          productId: tagNum,
        },
      },
    };
  };

  // Handles logic behind the complete count mutation
  const completeLocation = (
    submittedLocation: string,
    nextLocation: string,
    focusedTagNum:string
  ) => {
    complete({ variables: { locationId: submittedLocation } })
      .then(
        ({ data }) => {
          if (data?.completeCount.success === true) {
            updateProductQuantityOnSuccess(focusedTagNum)
            if (!nextLocationID || location?.committed) {
              navigation.goBack();
            }
          }
        }
      )
      .catch(() => {
        showErrorToast({
          title: submittedLocation,
          text1: 'Count did not update.',
          text2: 'Tap here to navigate back.',
          testID: testIds.errorToast,
          onPress: () => {
            getLocation(submittedLocation);
            setErrorNextLocationID(nextLocation);
            setUseErrorNextLocation(true);
            hideToast();
          },
        });
      });
  };

  //Handles closing keyboard upon tab actions
  const blurInput = (tag: string | null | undefined) => {
    tag && inputRefs.current[tag]?.blur();
  };

  const handleFilterPress = (filter: string) => {
    blurInput(currentFocusedTag);
    setCurrentTab(filter);
  };

  const testIds = getScreenTestingIds('LocationItems');

  return (
    <ScreenLayout
      loading={loadingLocation}
      pageAction={{
        title: isRecount ? 'Complete Recount' : 'Complete Count',
        onPress: handleComplete,
        loading: loadingUpdateCount,
        disabled: !isValid,
      }}
      testID={testIds.screen}
    >
      <ScreenLayout.ScrollContent
        testID={testIds.screen}
        banner={{ title: orNA(location?.id) }}
        dropdown={{
          title: 'Location Completion',
          open:true,
          children: (
            <View style={styles.dropdownWrapper}>
              <PercentageBar
                value={
                  status === 'Completed' ? countItems.length : totalCounted
                }
                total={totalItems}
                minLabelColor={Colors.WHITE}
                containerStyle={styles.percentageBarContainer}
                progressBarContainerStyle={styles.percentageBarContainerStyle}
                testID={testIds.percentageBar}
              />
              {status !== 'Completed' && (
                <CustomButton
                  title="Found a Product"
                  onPress={() => navigation.navigate(RouteNames.FOUND_PRODUCT)}
                  disabled={status === 'Completed'}
                  buttonStyle={{ borderColor: Colors.WHITE }}
                  containerStyle={styles.buttonContainer}
                  testID={testIds.foundAProduct}
                />
              )}
            </View>
          ),
        }}
        messages={[
          {
            text1: '"0" must be input if no quantity exists',
            onClose: () => setShowZeroMessage(false),
            open:
              showZeroMessage &&
              totalItems - totalCounted !== 0 &&
              countItems.length !== 0,
          },
          {
            text1: 'You are recounting this location.',
            icon: CustomIconNames.Warning,
            color: Colors.SUPPORT_3100,
            background: Colors.SECONDARY_110,
            open: isRecount && showRecountMessage,
            onClose: () => setShowRecountMessage(false),
          },
        ]}
      >
        {/* Filters */}
        <TabFilters
          active={currentTab}
          onPress={(value) => handleFilterPress(value)}
          filters={[
            ...(status !== 'Completed'
              ? [
                {
                  title: `${totalItems - totalCounted} ${totalItems - totalCounted !== 1
                    ? 'Products to go'
                    : 'Product to go'
                    }`,
                  value: 'togo',
                  color: Colors.PRIMARY_2100,
                },
              ]
              : []),
            {
              title: isRecount
                ? `All (${totalItems} ${totalItems === 1 ? 'Product' : 'Products'
                })`
                : 'All',
              value: 'all',
              color: Colors.PRIMARY_2100,
            },
          ]}
          testID={testIds.tabFilter}
        />
        <View style={styles.countListContainer} testID={testIds.itemsList}>
          {countItems.map((item, index) => (
            <CountInput
              ref={(ref) => {
                inputRefs.current[item.tagNum || ''] = ref;
              }}
              item={item}
              key={item.prodNum}
              inputAccessoryViewID={CONTROL_ACCESSORY_NATIVE_ID}
              onSubmitEditing={handleSubmitEditing}
              onPress={handleItemPress(item)}
              onFocus={handleFocus(index)}
              onBlur={handleBlur(item.tagNum || '')}
              onChangeText={(value)=>handleChangeText(value,item)}
              testID={testIds.countInput}
            />
          ))}
        </View>
      </ScreenLayout.ScrollContent>
      <ControlKeyboardAccessory
        inputAccessoryViewID={CONTROL_ACCESSORY_NATIVE_ID}
        prevDisabled={!prevFocusedTag}
        nextDisabled={!nextFocusedTag}
        onDonePress={handleControlDone}
        onNextPress={handleControlDirection('next')}
        onPrevPress={handleControlDirection('prev')}
        testID={testIds.keyboardController}
      />
    </ScreenLayout>
  );
};

const styles = StyleSheet.create({
  dropdownWrapper: { paddingHorizontal: 16 },
  percentageBarContainer: { paddingTop: 8 },
  percentageBarContainerStyle: {
    width: '50%',
  },
  button: {
    borderWidth: 1,
    borderColor: Colors.WHITE,
    padding: 12,
  },
  buttonContainer: {
    marginTop: 16,
    alignSelf: 'center',
  },
  buttonTitle: { fontWeight: FontWeight.MEDIUM, fontSize: 16 },
  countListContainer: {
    height: '100%',
  },
  warningBox: {
    flexDirection: 'row',
    alignItems: 'center',
    alignSelf: 'center',
    justifyContent: 'center',
    borderColor: Colors.PRIMARY_2100,
    backgroundColor: Colors.PRIMARY_25,
    borderWidth: 1,
    borderRadius: 2,
    marginHorizontal: 24,
    paddingVertical: 4,
    paddingHorizontal: 12,
    marginTop: 20,
  },
  warningClose: { paddingLeft: 12 },
});

export default LocationItems;
