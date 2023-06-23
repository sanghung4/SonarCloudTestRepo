import clsx from 'clsx';

import { ButtonProps, ButtonSizes } from 'components/Button';
import ConditionalWrapper from 'components/ConditionalWrapper';
import { LoadingIcon, LoadingIconSize } from 'components/LoadingIcon';

/**
 * Constants
 */
const iconSizeMap: { [k in ButtonSizes]: LoadingIconSize } = {
  base: 'lg',
  sm: 'md',
  lg: 28
};

/**
 * Component
 */
function Button(props: ButtonProps) {
  /**
   * props
   */
  const {
    align,
    className,
    'data-testid': testid,
    disabled,
    icon,
    iconPosition,
    loading,
    onClick,
    size: propsSize,
    title,
    ...rest
  } = props;
  const size = propsSize || 'base';

  /**
   * Render
   */
  return (
    <button
      className={clsx(
        'py-3 px-5 rounded font-medium',
        `text-${size}`,
        className,
        { noHover: disabled },
        'disabled:bg-secondary-2-30 disabled:text-secondary-2-100 disabled:border-0'
      )}
      onClick={onClick}
      disabled={disabled || loading}
      data-testid={testid}
      {...rest}
    >
      {loading ? (
        <LoadingIcon size={iconSizeMap[size]} />
      ) : (
        <ConditionalWrapper
          condition={Boolean(iconPosition)}
          wrapper={(children) => (
            <div
              className={clsx('flex items-center', align ?? 'justify-center')}
            >
              <span className="mr-2">{iconPosition === 'left' && icon}</span>
              {children}
              <span className="ml-2">{iconPosition === 'right' && icon}</span>
            </div>
          )}
        >
          {title ?? props.children}
        </ConditionalWrapper>
      )}
    </button>
  );
}

export default Button;
