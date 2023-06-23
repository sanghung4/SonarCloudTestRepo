import { loadingIconSizes } from 'components/LoadingIcon/LoadingIcon';

export type LoadingIconSize = keyof typeof loadingIconSizes | number;

export type LoadingIconProps = {
  className?: string;
  size?: LoadingIconSize;
  'data-testid'?: string;
};
