import React, { useState } from 'react';
import { View, StyleSheet } from 'react-native';
import { Icon } from 'react-native-elements';

import { Banner } from 'components/Banner';
import { Text } from 'components/Text';

import { borderRadius, Colors } from 'constants/style';
import { getComponentTestingIds } from 'test-utils/testIds';

export type LocationItemsBannerProps = {
  title: string;
  subtitle?: string;
  isRecount?: boolean;
  PercentageBarComponent?: React.ReactNode;
  testID?: string;
};

const LocationItemsBanner = (props: LocationItemsBannerProps) => {
  const [height, setHeight] = useState<number>(0);
  const bannerStyle = getBannerStyle(!!props.isRecount, height);

  const testIds = getComponentTestingIds('LocationItemsBanner', props.testID);

  return (
    <View
      testID={testIds.component}
      style={bannerStyle.container}
      onLayout={({ nativeEvent: { layout } }) => setHeight(layout.height)}
    >
      <Banner title={props.title} testID={testIds.banner} />

      {!props.isRecount && props.PercentageBarComponent}

      {props.isRecount && (
        <View
          testID={testIds.recountContainer}
          onLayout={({ nativeEvent: { layout } }) => setHeight(layout.height)}
          style={bannerStyle.recountContainer}
        >
          <Icon
            name="refresh"
            color={Colors.SUPPORT_2100}
            size={20}
            style={bannerStyle.icon}
          />
          <Text
            color={Colors.SUPPORT_2100}
            fontSize={18}
            allowFontScaling={false}
            testID={testIds.warning}
          >
            You are recounting this location.
          </Text>
        </View>
      )}
    </View>
  );
};

const getBannerStyle = (isRecount: boolean, recountContainerHeight: number) => {
  return StyleSheet.create({
    container: {
      width: '100%',
      paddingBottom: isRecount ? 14 : 0,
      marginBottom: isRecount ? 10 : 0,
      overflow: 'visible',
      zIndex: 999,
      borderBottomColor: Colors.SUPPORT_2100,
      borderBottomWidth: isRecount ? 1.5 : 0,
      backgroundColor: Colors.SECONDARY_6100,
    },
    recountContainer: {
      borderRadius: borderRadius.ROUNDED,
      borderColor: Colors.SUPPORT_2100,
      borderWidth: 1.5,
      backgroundColor: Colors.WHITE,
      position: 'absolute',
      justifyContent: 'center',
      alignItems: 'center',
      alignSelf: 'center',
      paddingHorizontal: 18,
      paddingVertical: 4,
      bottom: -(recountContainerHeight / 2) + 1,
      flexDirection: 'row',
    },
    icon: {
      paddingRight: 10,
    },
  });
};

export default LocationItemsBanner;
