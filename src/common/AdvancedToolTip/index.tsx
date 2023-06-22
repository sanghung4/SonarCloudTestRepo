import { StyledToolTip, MobileToolTip } from './utils/styled';
import { TooltipProps } from '@mui/material';
import React, { ReactNode } from 'react';
import './styles.scss';
import { useScreenSize } from '@dialexa/reece-component-library';

/**
 * Types
 */
type AdvancedToolTipProps = {
  title: string;
  text: string;
  icon: JSX.Element;
  placement?: TooltipProps['placement'];
  children: ReactNode;
  disabled?: boolean;
};

/**
 * Component
 */
function AdvancedToolTip(props: AdvancedToolTipProps) {
  /**
   * Props
   */
  const { title, icon, text, placement, children, disabled } = props;

  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();

  // CSS Util
  const cn = (name: string) => 'advanced-tooltip' + name;

  /**
   * Render
   */

  return isSmallScreen ? (
    <MobileToolTip
      title={
        disabled ? (
          <>
            <div className={cn('__grid mobile')}>
              <div className={cn('__grid__icon')}>{icon}</div>
              <div className={cn('__grid__text')}>
                <div className={cn('__typography')}>
                  <span className={cn('__typography__bold')}>{title}: </span>
                  <span className={cn('__typography__default')}>{text}</span>
                </div>
              </div>
            </div>
          </>
        ) : (
          ''
        )
      }
      open={disabled}
      placement={placement}
      data-testid="tipMobile"
    >
      <div>{children}</div>
    </MobileToolTip>
  ) : (
    <StyledToolTip
      arrow
      enterTouchDelay={0}
      title={
        disabled ? (
          <>
            <div className={cn('__grid desktop')}>
              <div className={cn('__grid__icon')}>
                {React.cloneElement(icon, { width: 16, height: 16 })}
              </div>
              <div className={cn('__grid__text')}>
                <div className={cn('__typography')}>
                  <span className={cn('__typography__bold')}>{title}: </span>
                  <span className={cn('__typography__default')}>{text}</span>
                </div>
              </div>
            </div>
          </>
        ) : (
          ''
        )
      }
      placement={placement}
      data-testid="tipDesktop"
    >
      <div>{children}</div>
    </StyledToolTip>
  );
}

export default AdvancedToolTip;
