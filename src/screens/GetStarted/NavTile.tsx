import React from 'react';
import { Text } from 'components/Text';
import { Colors, FontWeight } from 'constants/style';
import { StyleSheet, TouchableOpacity, View } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import { AppNavigation } from 'navigation/types';
import { CustomIcon } from 'components/CustomIcon';
import { NavTileProps } from './types';

export const NavTile = ({ item }: NavTileProps) => {
  const navigation = useNavigation<AppNavigation<'GetStarted'>>();
  return (
    <View style={styles.container} testID={item.testID}>
      <TouchableOpacity
        style={styles.navTile}
        onPress={() => navigation.navigate(item.screen)}
        testID={`${item.testID}-touchable`}
      >
        <CustomIcon name={item.icon} containerStyle={styles.iconContainer} />
        <View style={styles.titleContainer}>
          <Text
            fontSize={14}
            color={Colors.PRIMARY_3100}
            fontWeight={FontWeight.MEDIUM}
            style={styles.title}
            centered
          >
            {item.text}
          </Text>
        </View>
      </TouchableOpacity>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    padding: 12,
    flexBasis: '50%',
  },
  navTile: {
    aspectRatio: 1,
    elevation: 1,
    backgroundColor: Colors.WHITE,
    borderRadius: 2,
    shadowColor: Colors.PRIMARY_3100,
    shadowOpacity: 0.15,
    shadowRadius: 10,
    shadowOffset: { height: 2, width: 2 },
    alignItems: 'center',
    padding: 16,
  },
  iconContainer: {
    marginBottom: 8,
  },
  titleContainer: { flex: 1, justifyContent: 'center' },
  title: { textAlign: 'center' },
});
