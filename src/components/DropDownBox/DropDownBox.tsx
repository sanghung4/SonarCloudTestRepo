import React, { useState } from 'react';
import { Text } from 'components/Text';
import {
  StyleProp,
  StyleSheet,
  TouchableOpacity,
  View,
  ViewStyle,
} from 'react-native';
import { Icon } from 'react-native-elements';
import { Colors, FontWeight as fw } from 'constants/style';
import { getComponentTestingIds } from 'test-utils/testIds';

export type DropDownBoxProps = {
  title: string;
  children: React.ReactNode;
  containerStyle?: StyleProp<ViewStyle>;
  testID?: string;
  open?:boolean;
};

const DropDownBox = ({
  title,
  children,
  containerStyle,
  testID,
  open = false,
}: DropDownBoxProps) => {
  const [showChildren, setShowChildren] = useState(open);

  const handleOpenToggle = () => {
    setShowChildren((prev) => !prev);
  };

  // Test IDs object
  const testIds = getComponentTestingIds('DropDownBox', testID);

  return (
    <View testID={testIds.component}>
      <View style={styles.bannerContainer}>
        <Text
          color={Colors.PRIMARY_1100}
          fontWeight={fw.MEDIUM}
          centered
          testID={testIds.text}
        >
          {title}
        </Text>
        <TouchableOpacity
          testID={testIds.openToggle}
          style={styles.arrowWrapper}
          onPress={handleOpenToggle}
        >
          <Icon
            name={showChildren ? 'expand-less' : 'expand-more'}
            color={Colors.PRIMARY_1100}
            size={24}
          />
        </TouchableOpacity>
      </View>

      {showChildren && (
        <View
          style={[styles.childrenContainer, containerStyle]}
          testID={testIds.childrenContainer}
        >
          {children}
        </View>
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  bannerContainer: {
    position: 'relative',
    width: '100%',
    paddingVertical: 12,
    shadowColor: Colors.BLACK,
    shadowOffset: { width: 0, height: 0 },
    shadowOpacity: 0.12,
    shadowRadius: 8,
    backgroundColor: Colors.WHITE,
    elevation: 2,
  },
  arrowWrapper: {
    position: 'absolute',
    right: 0,
    paddingVertical: 12,
    paddingRight: 24,
    paddingLeft: 12,
  },

  nestedContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingRight: 15,
  },
  childrenContainer: {
    width: '100%',
    flexDirection: 'column',
    alignItems: 'center',
    shadowColor: Colors.BLACK,
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.25,
    shadowRadius: 4,
    backgroundColor: Colors.PRIMARY_1100,
    paddingTop: 8,
    paddingBottom: 16,
  },
});

export default DropDownBox;
