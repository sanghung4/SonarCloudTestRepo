import React, {
  useEffect,
  createContext,
  useContext,
  useState,
  FC,
} from 'react';
import { LocationItem, Location } from 'api';
import AsyncStorage from '@react-native-async-storage/async-storage';
import * as storage from 'constants/storage';
import { ProductQuantity, ProductQuantityContextType } from './types';

const initialProductContext: ProductQuantityContextType = {
  productQuantity: [],
  updateProductQuantity: () => {},
  updateProductQuantityOnSuccess: () => {},
  storeProductQuantity: () => {},
  resetProductQuantityMap: () => {},
};

const ProductQuantityContext = createContext(initialProductContext);

const AppStateProvider: FC = ({ children }) => {
  const [productQuantityMap, setProductQuantityMap] = useState<
    ProductQuantity[]
  >([]);
  const [isUpdateQuantitySuccess, setIsUpdateQuantitySuccess] = useState(false);
 
  useEffect(() => {
    getProductQuantity();
    return () => {
      storeProductQuantity(productQuantityMap);
    };
  }, []);

  useEffect(() => {
    if (isUpdateQuantitySuccess) {
      {
        storeProductQuantity(productQuantityMap);
        setIsUpdateQuantitySuccess(false);
      }
    }

    return () => {
      setIsUpdateQuantitySuccess(false);
    };
  }, [isUpdateQuantitySuccess]);

  const updateProductQuantityMap = (
    locationId: Location['id'] | undefined,
    locationItem: LocationItem | undefined,
    quantityValue: number
  ) => {
    setProductQuantityMap((productQuantityMap) => [
      ...productQuantityMap.filter(
        (qnty) => qnty.tagNum !== locationItem?.tagNum
      ),
      {
        locationId: locationId,
        tagNum: locationItem?.tagNum,
        quantity: Number(quantityValue),
      },
    ]);
  };

  const updateLocalQuantityMapOnSuccess = (tagNum: string) => {
    setProductQuantityMap(
      productQuantityMap.filter((qnty) => qnty.tagNum !== tagNum)
    );
    setIsUpdateQuantitySuccess(true);
  };

  const storeProductQuantity = async (storedItem: ProductQuantity[]) => {
    try {
      const setproductQuantity = JSON.stringify(storedItem);

      await AsyncStorage.setItem(
        storage.PRODUCT_QUANTITY_MAP,
        setproductQuantity
      );
    } catch (error) {
      throw error;
    }
  };

  const getProductQuantity = async () => {
    try {
      const getProductsMap = await AsyncStorage.getItem(
        storage.PRODUCT_QUANTITY_MAP
      );
      const getProductQuantityMap =
        getProductsMap !== null ? JSON.parse(getProductsMap) : [];
      setProductQuantityMap(getProductQuantityMap);
    } catch (error) {
      throw error;
    }
  };

  return (
    <ProductQuantityContext.Provider
      value={{
        productQuantity: productQuantityMap,
        updateProductQuantity: (
          locationId: Location['id'] | undefined,
          locationItem: LocationItem | undefined,
          quantityValue: number
        ) => updateProductQuantityMap(locationId, locationItem, quantityValue),
        updateProductQuantityOnSuccess: (tagNum: string) =>
          updateLocalQuantityMapOnSuccess(tagNum),
        storeProductQuantity: (location: ProductQuantity[]) =>
          storeProductQuantity(location),
          resetProductQuantityMap:()=>{setProductQuantityMap([])}
      }}
    >
      {children}
    </ProductQuantityContext.Provider>
  );
};

export default AppStateProvider;

export const useAppState = (): ProductQuantityContextType =>
  useContext(ProductQuantityContext);
