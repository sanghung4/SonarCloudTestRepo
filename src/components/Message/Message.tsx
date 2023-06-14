import { CustomIcon, CustomIconNames } from 'components/CustomIcon';
import { Text } from 'components/Text';
import { Colors, FontWeight, OldColors } from 'constants/style';
import React from 'react';
import { StyleSheet, View } from 'react-native';
import { getComponentTestingIds } from 'test-utils/testIds';
import { Container } from './Container';
import { MessageProps } from './types';

const Message = ({
  text1,
  text2,
  title,
  paddingHorizontal,
  paddingVertical,
  paddingLeft,
  paddingRight,
  paddingTop,
  paddingBottom,
  padding,
  onClose,
  onPress,
  background = OldColors.BG_MESSAGE,
  color = Colors.PRIMARY_2100,
  icon = CustomIconNames.Info,
  testID,
}: MessageProps) => {
  const testIds = getComponentTestingIds('Message', testID);
  return (
    <View
      testID={testIds.component}
      style={[
        (padding || paddingHorizontal || paddingLeft) && styles.paddingLeft,
        (padding || paddingHorizontal || paddingRight) && styles.paddingRight,
        (padding || paddingVertical || paddingTop) && styles.paddingTop,
        (padding || paddingVertical || paddingBottom) && styles.paddingBottom,
      ]}
    >
      <Container
        onPress={onPress}
        style={[
          styles.containerBase,
          {
            backgroundColor: background,
            borderColor: color,
          },
        ]}
      >
        {/* Toast Icon */}
        <CustomIcon
          name={icon}
          color={color}
          containerStyle={styles.toastIconContainer}
          testID={testIds.toastIcon}
        />
        {/* Text */}
        <View style={styles.textContainer}>
          {/* Text Row 1 */}
          {text1 && (
            <Text color={color} testID={testIds.text1}>
              {title && (
                <Text color={color} fontWeight={FontWeight.BOLD}>
                  {`${title}: `}
                </Text>
              )}
              {text1}
            </Text>
          )}
          {/* Text Row 2 */}
          {text2 && (
            <Text color={color} testID={testIds.text2}>
              {text2}
            </Text>
          )}
        </View>
        {/* Close Icon */}
        {onClose && (
          <CustomIcon
            name={CustomIconNames.Delete}
            color={color}
            containerStyle={styles.toastCloseContainer}
            onPress={onClose}
            pointerEvents="none"
            testID={testIds.closeIcon}
          />
        )}
      </Container>
    </View>
  );
};

const styles = StyleSheet.create({
  paddingTop: {
    paddingTop: 8,
  },
  paddingBottom: {
    paddingBottom: 8,
  },
  paddingLeft: {
    paddingLeft: 8,
  },
  paddingRight: {
    paddingRight: 8,
  },

  containerBase: {
    paddingVertical: 10,
    paddingHorizontal: 16,
    borderWidth: 1,
    borderRadius: 5,
    width: '100%',
    flexDirection: 'row',
    alignItems: 'center',
  },
  textContainer: {
    flex: 1,
  },
  toastIconContainer: {
    marginRight: 12,
  },
  toastCloseContainer: {
    marginLeft: 12,
  },
});

export default Message;
