import React from 'react';
import { StyleSheet, View } from 'react-native';
import { Text } from 'components/Text';
import { OldColors, Colors, FontWeight, pagePadding } from 'constants/style';
import { BannerProps } from './types';
import { getComponentTestingIds } from 'test-utils/testIds';

const Banner = ({ title, titleStyle, rightComponent, testID }: BannerProps) => {
  const testIds = getComponentTestingIds('Banner', testID);

  return (
    <View testID={testIds.component} style={styles.container}>
      <Text
        testID={testIds.title}
        style={[styles.bannerTitle, titleStyle && titleStyle]}
        centered
      >
        {title}
      </Text>

      <View style={styles.rightComponent} testID={testIds.rightComponent}>
        {rightComponent}
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    position: 'relative',
    paddingVertical: 20,
  },
  bannerTitle: {
    fontSize: 24,
    fontWeight: FontWeight.BOLD,
    lineHeight: 30,
    color: Colors.PRIMARY_1100,
  },
  rightComponent: {
    justifyContent: 'center',
    position: 'absolute',
    right: 0,
    bottom: 0,
    top: 0,
    marginRight: 24,
  },
  containerStyle: {
    width: '100%',
    minHeight: 67,
    alignItems: 'flex-start',
    justifyContent: 'center',
    backgroundColor: OldColors.BEIGE,
    paddingHorizontal: pagePadding.X,
    paddingVertical: 16,
  },
  centered: { alignItems: 'center' },
});

Banner.defaultProps = {
  centered: true,
};

export default Banner;
