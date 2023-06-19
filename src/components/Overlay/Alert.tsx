import React from 'react';
import { Dimensions, View } from 'react-native';
import { Text } from 'components/Text';
import * as SVG from 'components/SVG';
import { AlertOptions } from 'providers/Overlay/types';
import { useOverlay } from 'providers/Overlay';
import { Colors, FontWeight } from 'constants/style';
import { CustomIcon, CustomIconNames } from 'components/CustomIcon';
import { CustomButton } from 'components/CustomButton';
import { getScreenTestingIds } from 'test-utils/testIds';

const Alert = () => {
  const {
    alertPayload: {
      svg,
      title,
      description,
      error,
      actions,
      onClose,
      options = {
        fullScreen: false,
        centered: false,
        hideCloseButton: false,
      },
    },
    hideAlert,
  } = useOverlay();

  const handleClose = () => {
    hideAlert();
    onClose && onClose();
  };

  const testIds = getScreenTestingIds('Alert');

  const SVGComponent = svg ? SVG[svg] : (_: SVG.SVGProps) => null;

  return (
    <View
      style={[
        styles.alertContainer,
        options.fullScreen ? styles.alertFullScreen : styles.alertNotFullScreen,
      ]}
      testID={testIds.component}
    >
      <SVGComponent size={180} style={styles.svg(options)} />

      {/* Close Button */}
      {!options.fullScreen && (
        <CustomIcon
          name={CustomIconNames.Delete}
          onPress={handleClose}
          containerStyle={styles.closeButton}
          testID={testIds.closeIcon}
        />
      )}

      {/* Title */}
      <Text
        fontSize={20}
        fontWeight={FontWeight.MEDIUM}
        color={Colors.PRIMARY_1100}
        centered={options.centered}
        testID={testIds.title}
      >
        {title}
      </Text>

      {/* Description */}
      <Text
        fontSize={16}
        fontWeight={FontWeight.REGULAR}
        color={Colors.SECONDARY_2100}
        centered={options.centered}
        style={[styles.textContent, !options.fullScreen && styles.flexGrow]}
        testID={testIds.description}
      >
        {description}
      </Text>

      {/* Error */}
      <Text
        caption
        color={Colors.SUPPORT_2100}
        centered={options.centered}
        style={styles.textContent}
        testID={testIds.error}
      >
        {error}
      </Text>

      {/* Actions */}
      <View style={styles.actions}>
        {Array.isArray(actions)
          ? actions.map(({ onPress, ...action }, index) => (
              <CustomButton
                key={index}
                onPress={() => {
                  onPress();
                  hideAlert();
                }}
                {...action}
                testID={`${testIds.action}-${action.key}`}
              />
            ))
          : null}
      </View>
    </View>
  );
};
const { width } = Dimensions.get('screen');

const styles = {
  svg: ({ centered }: AlertOptions = {}) => ({
    marginBottom: 18,
    ...(centered ? ({ alignSelf: 'center' } as const) : {}),
  }),

  alertContainer: {
    padding: 32,
  },
  alertFullScreen: {
    alignItems: 'center',
    justifyContent: 'center',
    flex: 1,
  },
  alertNotFullScreen: {
    width: width - 48,
    maxWidth: 360,
  },
  textContent: {
    marginTop: 12,
  },
  flexGrow: {
    flexGrow: 1,
  },

  actions: {
    marginTop: 20,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
  },
  actionContainer: {
    flex: 1,
  },
  action: {
    padding: 8,
  },
  closeButton: { position: 'absolute', right: 12, top: 12 },
} as const;

export default Alert;
