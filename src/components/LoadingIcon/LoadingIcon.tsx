import clsx from 'clsx';
import { LoadingIconProps } from 'components/LoadingIcon/types';

import { ReactComponent as CircularProgress } from 'resources/images/loader.svg';

/**
 * Config
 */
export const loadingIconSizes = {
  xs: 'w-3 h-3',
  sm: 'w-4 h-4',
  md: 'w-5 h-5',
  lg: 'w-6 h-6',
  xl: 'w-8 h-8',
  xxl: 'w-12 h-12'
};

/**
 * Components
 */
function LoadingIcon(props: LoadingIconProps) {
  /**
   * Props
   */
  const getSize =
    typeof props.size === 'number'
      ? `w-[${props.size}px] h-[${props.size}px]`
      : loadingIconSizes[props.size ?? 'md'];
  /**
   * Render
   */
  return (
    <CircularProgress
      className={clsx(
        'text-secondary-2-30 fill-primary-2-100 w-10 h-10 animate-spin',
        getSize,
        props.className
      )}
      data-testid={props['data-testid']}
    />
  );
}

export default LoadingIcon;
