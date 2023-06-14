import React, {
  useCallback,
  useEffect,
  useLayoutEffect,
  useMemo,
  useRef,
  useState,
} from 'react';
import { ScreenLayout } from 'components/ScreenLayout';
import { Text } from 'components/Text';
import { AppScreenProps } from 'navigation/types';
import { BackHandler, StyleSheet, View } from 'react-native';
import { Colors, FontWeight } from 'constants/style';
import { ProgressBar } from 'components/ProgressBar';
import { CountInput } from 'components/CountInput';
import { ControlKeyboardAccessory } from 'components/ControlKeyboardAccessory';
import { INPUT_ACCESSORY_ID } from './utils';
import { getInitialVarianceInputRefs } from './utils';
import {
  compact,
  values,
  isEmpty,
  transform,
  findKey,
  noop,
  uniqBy,
  orderBy,
  groupBy,
} from 'lodash';
import { FocusedTag, QuantityMap } from './types';
import { percentage } from 'utils/numberUtils';
import {
  useCompleteVarianceCountMutation,
  useGetVarianceLocationLazyQuery,
  useGetVarianceNextLocationLazyQuery,
  useUpdateVarianceCountMutation,
  VarianceLocationItem,
} from 'api';
import { handleMutationComplete } from 'utils/apollo';
import {
  hideToast,
  showErrorToast,
  showSuccessToast,
} from 'components/ToastConfig';
import { RouteNames } from 'constants/routes';
import { HeaderBackButton } from '@react-navigation/stack';
import { useFocusEffect } from '@react-navigation/native';
import { getScreenTestingIds } from 'test-utils/testIds';
import useRenderListener from 'hooks/useRenderListener';
const VarianceLocation = ({
  navigation,
  route,
}: AppScreenProps<'VarianceLocation'>) => {
  useRenderListener();

  // ---------- NAV ----------
  const { locationId, nextLocation } = route.params;

  // ---------- API ----------
  const [
    getNextVarianceLocation,
    { loading: nextLocationLoading },
  ] = useGetVarianceNextLocationLazyQuery({
    onCompleted: ({ varianceNextLocation }) => {
      setNextVarianceLocationId(
        varianceNextLocation.locationId.toLowerCase() === 'none'
          ? ''
          : varianceNextLocation.locationId
      );
    },
    onError: () => {
      setNextVarianceLocationId('');
    },
  });

  
  const [
    getVarianceLocation,
    { data: varianceLocationData, loading: varianceLoading },
  ] = useGetVarianceLocationLazyQuery({
    onCompleted: ({ varianceLocation }) => {
      const { items } = varianceLocation;

      // Group items by tagNum to eliminate duplicates
      const groupedItems = groupBy(items, 'tagNum');

      // Generate a new QtyMap
      const newQuantityMap = transform<VarianceLocationItem[], QuantityMap>(
        groupedItems,
        (result, current) => {
          const { varianceStatus, quantity, tagNum } = current[0];

          // Only return the quantity if the item is staged
          const itemQty =
            varianceStatus === 'STAGED' && typeof quantity === 'number'
              ? `${quantity}`
              : null;

          // Set appropriate filter
          if (!itemQty) {
            setActiveFilter('togo');
          }
          // Set the value in the quantity map
          if (tagNum) {
            result[tagNum] = itemQty;
          }
        },
        {}
      );

      // Set new quantity map
      setQuantityMap(newQuantityMap);
    },
    onError: () => {
      navigateOut();
    },
    fetchPolicy: 'network-only',
  });

  const [
    updateVarianceCount,
    { loading: updateLoading },
  ] = useUpdateVarianceCountMutation({
    onCompleted: (resp) => {
      handleMutationComplete(resp.updateVarianceCount);
    },
  });

  const [complete] = useCompleteVarianceCountMutation({
    onCompleted: (resp) => {
      handleMutationComplete(resp.completeVarianceCount);
    },
  });

  // ---------- REFS ----------
  const inputRefs = useRef(
    getInitialVarianceInputRefs(
      varianceLocationData?.varianceLocation?.items || []
    )
  );

  // ---------- STATE ----------
  // Messages
  const [zeroMessageOpen, setZeroMessageOpen] = useState(true);
  const [varianceMessageOpen, setVarianceMessageOpen] = useState(true);
  // Filter
  const [activeFilter, setActiveFilter] = useState('togo');
  // Focused Input Tags
  const [prevTag, setPrevTag] = useState<FocusedTag>(undefined);
  const [currentTag, setCurrentTag] = useState<FocusedTag>(undefined);
  const [nextTag, setNextTag] = useState<FocusedTag>(undefined);
  // Inputs
  const [focusedInputValue, setFocusedInputValue] = useState<string>('');
  // Location Data
  const [quantityMap, setQuantityMap] = useState<QuantityMap>({});
  const [nextVarianceLocationId, setNextVarianceLocationId] = useState('');
  const [totalItems, setTotalItems] = useState(0);
  const [countedItems, setCountedItems] = useState(0);
  const [varianceCountItems, setVarianceCountItems] = useState<
    VarianceLocationItem[]
  >([]);
  // Loading States
  const [lastLoading, setLastLoading] = useState(false);

  // Focus on first input on page render
  const [focusFirst, setFocusFirst] = useState(true);

  // ---------- EFFECTS ----------
  // On page load fetch page data
  useEffect(() => {
    // Get variance location data
    getVarianceLocation({ variables: { id: locationId } });

    // If there is a next location in the route, set it as the next location
    if (nextLocation) {
      setNextVarianceLocationId(nextLocation);
    } else {
      getNextVarianceLocation({ variables: { id: locationId } });
    }
  }, [locationId]);
  // Create count items once data has been loaded
  useEffect(() => {
    
    if (!isEmpty(quantityMap)) {
      const locationItems = varianceLocationData?.varianceLocation?.items || [];

      const items =
        locationItems.map((item) => ({
          ...item,
          quantity: parseInt(quantityMap[item?.tagNum || ''] || '', 10),
        })) || [];

      const uniqueItems = uniqBy(items, (item) => item.prodNum);

      const sortedItems = orderBy(
        uniqueItems,
        ['sequence', 'tagNum'],
        ['asc', 'asc']
      );

      const displayedItems =
        activeFilter === 'togo'
          ? sortedItems.filter((item) => !item.quantity && item.quantity !== 0)
          : sortedItems;

      setVarianceCountItems(displayedItems);
      setTotalItems(sortedItems.length);
      setCountedItems(compact(values(quantityMap)).length);
      setActiveFilter((prev) => (displayedItems.length === 0 ? 'all' : prev));
    }
  }, [varianceLocationData, quantityMap, activeFilter]);

 // Focus on first input on page build
 useEffect(() => {
  const timer = setTimeout(() => {
    const tag = varianceCountItems[0]?.tagNum;
    if (focusFirst && tag) {
      inputRefs.current[tag]?.focus();
      setFocusFirst(false);
    }
  }, 500);
  return () => clearTimeout(timer);
}, [varianceCountItems]);

  // Changes the screens navigation handlers
  useFocusEffect(
    useCallback(() => {
      // Sets the nav bar's back button action
      navigation.setOptions({
        headerLeft: () => <HeaderBackButton onPress={navigateOut} />,
      });
      // Sets Android's hardware back button action
      const backHandler = BackHandler.addEventListener(
        'hardwareBackPress',
        () => {
          navigateOut();
          return true;
        }
      );

      return () => backHandler.remove();
    }, [])
  );

  // ---------- PAGE ACTIONS ----------
  const handleCompletePress = async () => {
    let canSubmit = true;

    if (currentTag && quantityMap[currentTag] !== focusedInputValue) {
      const variables = getUpdateVariables(currentTag, focusedInputValue);
      await updateVarianceCount(variables)
        .then(() => {
          setQuantityMap((prevMap) => ({
            ...prevMap,
            [currentTag]: focusedInputValue,
          }));
        })
        .catch(() => {
          canSubmit = false;
          setQuantityMap((prevMap) => ({ ...prevMap, [currentTag]: null }));
        });
    }

    blurInput(currentTag);

    if (canSubmit && nextVarianceLocationId) {
      completeLocation(locationId, nextVarianceLocationId);
      navigation.push(RouteNames.VARIANCE_LOCATION, {
        locationId: nextVarianceLocationId,
      });
    }

    if (canSubmit && !nextVarianceLocationId) {
      setLastLoading(true);
      await completeLocation(locationId, nextVarianceLocationId);
      navigateOut();
    }
  };

  const handleFilterPress = (filter: string) => {
    blurInput(currentTag);
    setActiveFilter(filter);
  };

  // ---------- INPUT ACTIONS ----------
  const handleFocus = (index: number) => () => {
    const prevTag = varianceCountItems[index - 1]?.tagNum;
    const currentTag = varianceCountItems[index]?.tagNum;
    const nextTag = varianceCountItems[index + 1]?.tagNum;

    if (currentTag) {
      setFocusedInputValue(inputRefs.current[currentTag]?.props.value || '');
    }

    setPrevTag(prevTag ? prevTag : undefined);
    setCurrentTag(currentTag ? currentTag : undefined);
    setNextTag(nextTag ? nextTag : undefined);
  };

  const handleChangeText = (value: string) => {
    setFocusedInputValue(value);
  };

  const handleBlur = (tagNum: string) => (newValue: string) => {
    const newFormattedValue = newValue ? newValue : null;

    if (quantityMap[tagNum] !== newFormattedValue) {
      const variables = getUpdateVariables(tagNum, newFormattedValue);

      updateVarianceCount(variables).catch(() => {
        setQuantityMap((prevMap) => ({ ...prevMap, [tagNum]: null }));
      });
    }

    setCurrentTag(undefined);
    setFocusedInputValue('');
    setQuantityMap((prevMap) => ({ ...prevMap, [tagNum]: newFormattedValue }));
  };

  const handleSubmitEditing = () => {
    if (nextTag) {
      focusInput(nextTag);
    } else if (currentTag) {
      blurInput(currentTag);
    }
  };

  // ---------- KEYBOARD ACTIONS ----------
  const handleControlDonePress = () => {
    blurInput(currentTag);
  };

  const handleControlDirectionPress = (direction: 'next' | 'prev') => () => {
    if (direction === 'next') {
      focusInput(nextTag);
    }
    if (direction === 'prev') {
      focusInput(prevTag);
    }
  };

  // ---------- HELPERS ----------
  const getUpdateVariables = (tagNum: string, newValue: string | null) => {
    const parsedQty = parseInt(newValue || '', 10);

    return {
      variables: {
        item: {
          locationId: locationId,
          quantity: isNaN(parsedQty) ? null : parsedQty,
          productId: tagNum,
        },
      },
    };
  };

  const completeLocation = (
    submittedLocation: string,
    submittedNextLocation: string
  ) => {
    return complete({ variables: { locationId: submittedLocation } })
      .then(() => {
        showSuccessToast({
          title: submittedLocation,
          text1: 'Variance updated',
        });
      })
      .catch(() => {
        showErrorToast({
          title: submittedLocation,
          text1: 'Count did not update.',
          text2: 'Tap here to navigate back.',
          onPress: () => {
            navigation.push(RouteNames.VARIANCE_LOCATION, {
              locationId: submittedLocation,
              nextLocation: submittedNextLocation,
            });
            hideToast();
          },
        });
      });
  };

  const blurInput = (tag: string | null | undefined) => {
    tag && inputRefs.current[tag]?.blur();
  };

  const focusInput = (tag: string | null | undefined) => {
    tag && inputRefs.current[tag]?.focus();
  };

  const navigateOut = () => {
    navigation.navigate(RouteNames.VARIANCE_LOCATION_LIST);
  };

  const canSubmitCount = useMemo(() => {
    const numberOfTags = Object.keys(quantityMap).length;
    const numberOfValues = compact(values(quantityMap)).length;
    const onlyOneLeft = numberOfTags - numberOfValues === 1;

    if (numberOfTags === numberOfValues) {
      return currentTag ? !!focusedInputValue : true;
    }

    if (currentTag && !focusedInputValue) {
      return false;
    }

    if (currentTag && onlyOneLeft) {
      const lastTag = findKey(quantityMap, (item) => !item);
      return currentTag === lastTag && focusedInputValue;
    }

    return false;
  }, [quantityMap, focusedInputValue, currentTag]);

  const productsLeft = useMemo(() => {
    return totalItems - countedItems;
  }, [totalItems, countedItems]);

  const testIds = getScreenTestingIds('VarianceLocation');

  return (
    <ScreenLayout
      loading={varianceLoading || lastLoading}
      pageAction={{
        title: 'Complete Recount',
        loading: updateLoading || nextLocationLoading,
        onPress: handleCompletePress,
        disabled: !canSubmitCount,
      }}
      testID={testIds.screen}
    >
      <ScreenLayout.ScrollContent
        testID={testIds.screen}
        banner={{ title: locationId }}
        dropdown={{
          title: 'Variance Location Completion',
          containerStyle: styles.dropdownContainer,
          children: (
            <View style={styles.dropdownContent}>
              <Text
                fontWeight={FontWeight.MEDIUM}
                color={Colors.WHITE}
                style={styles.progressBarText}
                centered
                testID={testIds.percentageBarText}
              >
                {percentage(countedItems, totalItems)} % Complete
              </Text>
              <View style={styles.progressBarContainer}>
                <ProgressBar
                  value={countedItems}
                  total={totalItems}
                  testID={testIds.progressBar}
                />
              </View>
            </View>
          ),
        }}
        messages={[
          {
            text1: '"0" must be input if no quantity exists',
            open: zeroMessageOpen,
            onClose: () => setZeroMessageOpen(false),
          },
          {
            text1: 'Only showing products with variance for recount',
            open: varianceMessageOpen,
            onClose: () => setVarianceMessageOpen(false),
          },
        ]}
        tabFilters={{
          onPress: handleFilterPress,
          active: activeFilter,
          filters: [
            {
              value: 'togo',
              title:
                productsLeft === 1
                  ? '1 Product to go'
                  : `${productsLeft} Products to go`,
            },
            {
              value: 'all',
              title: 'All',
            },
          ],
        }}
      >
        {varianceCountItems.map((item, index) => (
          <CountInput
            item={item}
            inputAccessoryViewID={INPUT_ACCESSORY_ID}
            disableDetails
            onPress={noop}
            onFocus={handleFocus(index)}
            onBlur={handleBlur(item.tagNum || '')}
            onChangeText={handleChangeText}
            onSubmitEditing={handleSubmitEditing}
            key={item.prodNum}
            ref={(ref) => {
              inputRefs.current[item.tagNum || ''] = ref;
            }}
            testID={testIds.countInput}
          />
        ))}
      </ScreenLayout.ScrollContent>
      <ControlKeyboardAccessory
        inputAccessoryViewID={INPUT_ACCESSORY_ID}
        nextDisabled={!nextTag}
        prevDisabled={!prevTag}
        onDonePress={handleControlDonePress}
        onNextPress={handleControlDirectionPress('next')}
        onPrevPress={handleControlDirectionPress('prev')}
      />
    </ScreenLayout>
  );
};

const styles = StyleSheet.create({
  dropdownContainer: { paddingTop: 12, paddingBottom: 12 },
  dropdownContent: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: 32,
  },
  progressBarText: { width: '50%', paddingRight: 16 },
  progressBarContainer: { paddingLeft: 16, width: '50%' },
});

export default VarianceLocation;
