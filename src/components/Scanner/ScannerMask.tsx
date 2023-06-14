import { borderRadius, Colors } from 'constants/style';
import React, { useState } from 'react';
import { Dimensions, StyleSheet, View } from 'react-native';
import { SCANNER_TEST_IDS } from './types';
import { ScannerMaskProps } from './types';

export const ScannerMask = ({ maskStyle }: ScannerMaskProps) => {
  const [scannerWidth, setScannerWidth] = useState(0);
  const styles = getStyles(scannerWidth);

  return (
    <View
      style={styles.maskContainer}
      testID={SCANNER_TEST_IDS.SCANNER_MASK}
      pointerEvents="none"
      onLayout={(e) => {
        const { width: layoutWidth } = e.nativeEvent.layout;
        setScannerWidth(layoutWidth);
      }}
    >
      <View style={StyleSheet.flatten([styles.maskFinder, maskStyle])} />
      <View style={styles.maskOuter}>
        <View style={styles.maskY} />
        <View style={styles.maskInner}>
          <View style={styles.maskX} />
          <View style={styles.maskFocused} />
          <View style={styles.maskX} />
        </View>
        <View style={styles.maskY} />
      </View>
    </View>
  );
};

const screenWidth = Dimensions.get('window').width;

const opacity = 'rgba(0,0,0,0.3)';
const finderWidth = screenWidth - screenWidth * 0.6;
const finderHeight = finderWidth / 2;

const getStyles = (scannerWidth: number) =>
  StyleSheet.create({
    maskContainer: {
      flex: 1,
      alignItems: 'center',
      justifyContent: 'center',
    },
    maskFinder: {
      width: scannerWidth * 0.8,
      height: scannerWidth * 0.4,
      borderRadius: borderRadius.MEDIUM,
      borderColor: Colors.WHITE,
      borderWidth: 5,
    },
    maskOuter: {
      position: 'absolute',
      top: 0,
      left: 0,
      width: '100%',
      height: '100%',
      alignItems: 'center',
      justifyContent: 'space-around',
    },
    maskInner: {
      height: finderHeight,
      flexDirection: 'row',
    },
    maskY: {
      backgroundColor: opacity,
      width: '100%',
    },
    maskX: {
      backgroundColor: opacity,
    },
    maskFocused: {
      width: finderWidth,
      height: finderHeight,
      backgroundColor: 'transparent',
    },
  });
