import { ViewProps } from 'react-native';

export interface SVGProps extends ViewProps {
  size?: number;
}

export interface SVGTappableProps extends SVGProps {
  onPress?: () => void;
}

export enum SVG_TEST_IDS {
  LOGO = 'logo-icon',
  SESSION_EXPIRED = 'sessionExpiredImageTest',
  SUCCESS_IMAGE = 'successImageTest',
}
