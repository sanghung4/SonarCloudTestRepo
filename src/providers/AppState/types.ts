import { LocationItem, Location } from 'api';

export interface ProductQuantity {
  locationId: Location['id'] | undefined;
  tagNum: LocationItem['tagNum'];
  quantity: LocationItem['quantity'];
}

export interface ProductQuantityContextType {
  productQuantity: ProductQuantity[];
  updateProductQuantity: (
    locationId: Location['id'] | undefined,
    locationItem: LocationItem | undefined,
    quantityValue: number
  ) => void;
  updateProductQuantityOnSuccess: (tagNum: string) => void;
  storeProductQuantity: (location: ProductQuantity[]) => void;
  resetProductQuantityMap: () => void;
}
