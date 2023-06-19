import Config from 'react-native-config';

export const isBeta = (): boolean => Config.RELEASE_TYPE === 'BETA';
export const isAlpha = (): boolean => Config.RELEASE_TYPE === 'ALPHA';
