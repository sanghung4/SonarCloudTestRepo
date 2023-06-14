import React from 'react';
import {
  DrawerContentComponentProps,
  DrawerContentScrollView,
  DrawerItem,
} from '@react-navigation/drawer';
import { Colors, FontWeight as fw } from 'constants/style';
import { StyleSheet, View } from 'react-native';
import { Text } from 'components/Text';
import { useAuth } from 'providers/Auth';
import { Icon } from 'react-native-elements';
import { useConfig } from 'hooks/useConfig';
import { orNA } from 'utils/stringUtils';
import { logError } from 'utils/error';
import { version } from '../../../package.json';
import { useLocation } from 'hooks/useLocation';
import { RouteNames } from 'constants/routes';
import { getComponentTestingIds } from 'test-utils/testIds';

interface DrawerContentType extends DrawerContentComponentProps {
  testID?: string | undefined;
}

const DrawerContent = ({ testID, ...props }: DrawerContentType) => {
  const [{ count }] = useConfig();
  const { user, logout } = useAuth();

  const { location } = useLocation();
  const testIds = getComponentTestingIds('DrawerContent', testID);
  return (
    <DrawerContentScrollView
      testID={testIds.component}
      contentContainerStyle={styles.content}
      {...props}
    >
      <View style={styles.userInfoSection}>
        <Text h4 color={Colors.PRIMARY_1100} fontWeight={fw.BOLD}>
          {orNA(user?.name)}
        </Text>
        <Text small color={Colors.PRIMARY_3100}>
          {orNA(user?.preferred_username)}
        </Text>
      </View>

      {count ? (
        <View style={styles.configInfoSection}>
          <View>
            <Text small color={Colors.PRIMARY_1100}>
              Count ID
            </Text>
            <Text fontWeight={fw.BOLD}>{count.id}</Text>
          </View>

          <View style={styles.subsection}>
            <Text small color={Colors.PRIMARY_1100}>
              Branch Name
            </Text>
            <Text fontWeight={fw.BOLD}>{count.branch.name}</Text>
          </View>

          {location ? (
            <View style={styles.subsection}>
              <Text small color={Colors.PRIMARY_1100}>
                Location ID
              </Text>
              <Text fontWeight={fw.BOLD}>{location.id}</Text>
            </View>
          ) : null}
        </View>
      ) : null}

      <View testID={testIds.drawerContentFooter} style={styles.footer}>
        {logError() ? (
          <DrawerItem
            icon={() => <Icon name="bug-report" color={Colors.PRIMARY_2100} />}
            label="Error Log"
            onPress={() => props.navigation.navigate(RouteNames.ERROR_LOG)}
          />
        ) : null}
        <DrawerItem
          icon={() => (
            <Icon
              name="logout"
              color={Colors.PRIMARY_2100}
              testID={testIds.logoutButton}
            />
          )}
          label="Logout"
          onPress={logout}
        />
        <Text caption style={styles.version}>
          Version: {version}
        </Text>
      </View>
    </DrawerContentScrollView>
  );
};

const drawerSpacing = { X: 20, Y: 15 };

const styles = StyleSheet.create({
  content: {
    flex: 1,
    paddingTop: drawerSpacing.Y * 2,
    backgroundColor: Colors.SECONDARY_460,
  },
  userInfoSection: {
    paddingHorizontal: drawerSpacing.X,
    paddingVertical: drawerSpacing.Y,
  },
  configInfoSection: {
    backgroundColor: Colors.WHITE,
    paddingHorizontal: drawerSpacing.X,
    paddingVertical: drawerSpacing.Y,
    borderTopWidth: 1,
    borderColor: Colors.SECONDARY_3100,
  },
  subsection: {
    marginTop: drawerSpacing.Y,
  },
  footer: {
    flex: 1,
    borderTopWidth: 1,
    borderColor: Colors.SECONDARY_3100,
    backgroundColor: Colors.SECONDARY_460,
  },
  version: {
    paddingTop: 25,
    paddingLeft: 16,
  },
});

export default DrawerContent;
