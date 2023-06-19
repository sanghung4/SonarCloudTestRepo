import { Text } from 'components/Text';
import { Colors } from 'constants/style';
import { useSpring } from 'hooks/useSpring';
import React from 'react';
import { Animated, Dimensions, Platform, StyleSheet, View } from 'react-native';
import { Icon } from 'react-native-elements';
import { getComponentTestingIds } from 'test-utils/testIds';
import {
  BottomSheetDescriptionProps,
  BottomSheetProps,
  BottomSheetSubtitleProps,
  BottomSheetTitleProps,
} from './types';

const BottomSheet = ({
  minHeight = 131,
  isVisible,
  onClose,
  onBackdropPress,
  children,
  containerStyle,
  contentStyle,
  closeIcon,
  testID,
}: BottomSheetProps): JSX.Element => {
  const spring = useSpring(isVisible ? 1 : 0, { damping: 67 });
  const screenHeight = Dimensions.get('window').height;
  const testIds = getComponentTestingIds('BottomSheet', testID);
  return (
    <Animated.View
      testID={testIds.component}
      onTouchEnd={onBackdropPress}
      style={[
        styles.container,
        containerStyle && containerStyle,
        {
          transform: [
            {
              translateY: spring.interpolate({
                inputRange: [0.01, 1],
                outputRange: [screenHeight, 0],
                extrapolate: 'clamp',
              }),
            },
          ],
        },
      ]}
    >
      <View
        testID={testIds.contentContainer}
        accessible={false}
        style={StyleSheet.flatten([
          styles.content,
          contentStyle,
          { minHeight },
        ])}
      >
        {closeIcon && (
          <Icon
            type="material"
            name="close"
            color={Colors.PRIMARY_1100}
            onPress={onClose}
            containerStyle={styles.icon}
            testID="close-button"
            accessible={true}
            accessibilityLabel="close"
          />
        )}
        {children}
      </View>
    </Animated.View>
  );
};

BottomSheet.defaultProps = {
  closeIcon: true,
};

BottomSheet.Title = ({ children, testID }: BottomSheetTitleProps) => {
  const testIds = getComponentTestingIds('BottomSheet', testID);

  return (
    <Text
      testID={testIds.title}
      h4
      color={Colors.PRIMARY_1100}
      style={styles.title}
    >
      {children}
    </Text>
  );
};

BottomSheet.Subtitle = ({ children, testID }: BottomSheetSubtitleProps) => {
  const testIds = getComponentTestingIds('BottomSheet', testID);
  return (
    <Text
      testID={testIds.subtitle}
      color={Colors.SECONDARY_2100}
      style={styles.subtitle}
    >
      {children}
    </Text>
  );
};

BottomSheet.Description = ({
  children,
  testID,
}: BottomSheetDescriptionProps) => {
  const testIds = getComponentTestingIds('BottomSheet', testID);
  return (
    <Text
      testID={testIds.description}
      color={Colors.SECONDARY_270}
      style={styles.description}
    >
      {children}
    </Text>
  );
};

const styles = StyleSheet.create({
  container: {
    backgroundColor: 'transparent',
    justifyContent: 'flex-end',
    ...StyleSheet.absoluteFillObject,
    ...Platform.select({
      android: {
        elevation: 2,
      },
      default: {
        shadowColor: 'rgba(0,0,0, .4)',
        shadowOffset: { height: 1, width: 1 },
        shadowOpacity: 1,
        shadowRadius: 1,
      },
    }),
  },
  icon: {
    position: 'absolute',
    top: 16,
    right: 16,
  },
  content: {
    backgroundColor: Colors.SECONDARY_460,
    paddingVertical: 18,
    paddingHorizontal: 24,
  },
  title: {
    paddingBottom: 8,
  },
  subtitle: {
    paddingBottom: 8,
  },
  description: {
    flexGrow: 1,
  },
});

export default BottomSheet;
