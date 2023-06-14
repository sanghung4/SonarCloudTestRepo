import { Text } from 'components/Text';
import { Colors, FontWeight } from 'constants/style';
import React from 'react';
import { StyleSheet, View } from 'react-native';
import { Icon } from 'react-native-elements';

export const IsRecountBanner = () => {
  return (
    <View style={styles.recountNotice}>
      <Icon name="refresh" color={Colors.SUPPORT_2100} size={18} />
      <Text
        style={styles.recountText}
        color={Colors.SUPPORT_2100}
        fontWeight={FontWeight.BOLD}
      >
        YOU ARE RECOUNTING THIS LOCATION
      </Text>
    </View>
  );
};

const styles = StyleSheet.create({
  recountNotice: {
    justifyContent: 'center',
    width: '100%',
    flexDirection: 'row',
    alignItems: 'center',
    marginTop: 20,
  },
  recountText: {
    alignItems: 'center',
    marginLeft: 4,
    textAlignVertical: 'center',
  },
});
