import { useEffect, useRef, useState } from 'react';

import {
  Button,
  ButtonProps,
  Modify,
  PropTypes
} from '@dialexa/reece-component-library';

type Props = Modify<
  ButtonProps,
  {
    color?: PropTypes.Color;
    disabled?: boolean;
    fullWidth?: boolean;
    value: string;
    valueDone: string;
    onClick?: () => void;
    size?: 'small' | 'medium' | 'large';
    testId?: string;
    timeout?: number;
  }
>;

export default function FeedbackButton(props: Props) {
  /**
   * Props
   */
  const {
    color,
    disabled,
    fullWidth,
    value,
    valueDone,
    size,
    testId,
    timeout,
    startIcon,
    sx
  } = props;

  /**
   * Refs
   */
  const timerRef = useRef<NodeJS.Timeout | null>(null);

  /**
   * State
   */
  const [applied, setApplied] = useState(false);

  /**
   * Effects
   */
  useEffect(timerEffect, []);

  /**
   * Render
   */
  return (
    <Button
      startIcon={startIcon}
      color={applied ? 'success' : color}
      disabled={disabled}
      fullWidth={fullWidth}
      onClick={applied ? undefined : handleClick}
      size={size}
      sx={[
        ...(sx ? (Array.isArray(sx) ? sx : [sx]) : []),
        {
          whiteSpace: 'nowrap'
        }
      ]}
      data-testid={testId}
    >
      {applied ? valueDone : value}
    </Button>
  );

  /**
   * Defs
   */
  function handleClick() {
    if (props.onClick) {
      setApplied(true);
      props.onClick();
      timerRef.current = setTimeout(() => setApplied(false), timeout || 3000);
    }
  }

  function timerEffect() {
    return () => {
      timerRef.current && clearTimeout(timerRef.current);
    };
  }
}
