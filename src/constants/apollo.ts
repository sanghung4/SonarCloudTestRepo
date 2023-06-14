import Config from 'react-native-config';

export const linkOptions = {
  uri: `${Config.API_URL}`,
};

export const EXPIRED_TOKEN = 'Token has expired';
export const INVALID_TOKEN = ['Unable to validate token', 'Invalid token type'];

export enum eclipseSearchProductType {
  KEYWORD = 1,
  PRODUCT_ID = 2,
}
export enum kourierSearchProductType {
  KEYWORD = "0",
  PRODUCT_ID = "1",
}
