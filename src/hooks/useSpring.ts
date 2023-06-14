import { useEffect, useRef } from 'react';
import { Animated } from 'react-native';

export interface SpringConfig {
  useNativeDriver?: boolean;
  stiffness?: number;
  damping?: number;
  mass?: number;
}

export const useSpring = (
  toValue: number,
  config?: SpringConfig
): Animated.Value => {
  const value = useRef(new Animated.Value(toValue));

  useEffect(() => {
    const animation = Animated.spring(value.current, {
      ...config,
      toValue,
      useNativeDriver: true,
    });

    animation.start();
    return () => animation.stop();
  }, [toValue, config]);

  return value.current;
};
