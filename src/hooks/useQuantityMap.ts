import { useEffect, useState } from 'react';
import {
  Location,
  LocationItem,
  LocationItemStatus,
  useUpdateCountMutation,
} from 'api';
import {
  compact,
  groupBy,
  transform,
  values,
  uniqBy,
  orderBy,
  isNull,
} from 'lodash';

import { useOverlay } from 'providers/Overlay';
import { getError } from 'utils/error';
import { ErrorType } from 'constants/error';
import firebaseUtils from '../utils/firebaseUtils';

interface useQuantityMapResp {
  isLoading: boolean;
  isRecount: boolean;
  totalItems: number;
  totalCounted: number;
  listDataSource: LocationItem[];
  updateProduct: (args: { productTag: string; quantity: string }) => void;
  setMapLocation: (newLocation: Location | undefined) => void;
  validateMap: () => boolean;
  setFilterItems: (shouldFilter: boolean) => void;
}

export const useQuantityMap = (): useQuantityMapResp => {
  const [totalItems, setTotalItems] = useState(0);
  const [location, setLocation] = useState<Location | undefined>();
  const [quantityMap, setQuantityMap] = useState<{
    [key: string]: string | null;
  }>({});
  const [listDataSource, setListDataSource] = useState<LocationItem[]>([]);
  const [filterItems, setFilterItems] = useState(true);

  const { showAlert } = useOverlay();

  // const [previousQuantity, setPreviousQuantity] = useState<string>('');
  const [qtyToUpdate, setQtyToUpdate] = useState<
    | {
        productTag: string;
        quantity: string;
      }
    | {}
  >({});

  //initialize quantity map when location changes
  useEffect(() => {
    const grouped = groupBy(location?.items, 'tagNum');
    const map = transform(
      grouped,
      (acc: { [key: string]: string | null }, curr) => {
        const tagNum = curr[0].tagNum;

        let quantity = curr[0].quantity;
        if (
          location?.committed &&
          curr[0].status === LocationItemStatus.COMMITTED
        ) {
          quantity = null;
        }
        if (tagNum) {
          acc[tagNum] = isNull(quantity) ? null : `${quantity}`;
        }
      },
      {}
    );
    setQuantityMap(map);
  }, [location]);

  useEffect(() => {
    const items = (location?.items || []).map((item) => {
      const newQty = quantityMap[item?.tagNum || ''] || null;
      return {
        ...item,
        quantity: newQty,
      };
    });

    const sortedItems = orderBy(items, (curr) => {
      return curr?.sequence || curr.tagNum;
    });

    const uniqueItems = uniqBy(sortedItems, (e) => e.prodNum);
    setTotalItems(uniqueItems.length);

    const filteredItems = filterItems
      ? uniqueItems.filter((item) => item.status === 'UNCOUNTED')
      : uniqueItems;

    setListDataSource(filteredItems);
  }, [location, filterItems]);

  const [update, { loading }] = useUpdateCountMutation();

  const updateProduct = async ({
    productTag,
    quantity,
  }: {
    productTag: string;
    quantity: string;
  }) => {
    if (quantity !== quantityMap[productTag]) {
      setQtyToUpdate({ productTag, quantity });
      const parsedQty = parseInt(quantity, 10);

      setQuantityMap((prevState) => {
        return {
          ...prevState,
          [productTag]: quantity,
        };
      });

      update({
        variables: {
          item: {
            locationId: location?.id || '',
            quantity: isNaN(parsedQty) ? null : parseInt(quantity, 10),
            productId: productTag,
          },
        },
      }).catch((error) =>{
        firebaseUtils.crashlyticsRecordError(error);
        showAlert(getError(ErrorType.CREATE_WRITE_IN, error))}
      );
    }
  };

  const validateMap = () => {
    const quantityMapValues = values(quantityMap);
    if (location?.committed) {
      const compacted = compact(quantityMapValues);
      return compacted.length > 0;
    }

    return compact(values(quantityMap)).length === location?.totalProducts;
  };

  return {
    isLoading: loading,
    listDataSource,
    totalItems,
    totalCounted: compact(values(quantityMap)).length,
    isRecount: !!location?.committed,
    updateProduct,
    setMapLocation: setLocation,
    validateMap,
    setFilterItems,
  };
};
