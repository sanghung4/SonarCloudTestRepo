import { priceStringToFloat } from 'utils/strings';

export const VALUE_OVER_10MIL = 9999999;

export function checkIfOver10Mil(subTotal: string) {
  return priceStringToFloat(subTotal) > VALUE_OVER_10MIL;
}
