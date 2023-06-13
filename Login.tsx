import React, { useEffect, useState } from 'react';
import {
  SafeAreaView,
  TextInput,
  View,
  Button as RNButton,
} from 'react-native';
import { Section } from 'components/Section';
import { Button } from 'components/Button';
import { Text } from 'components/Text';
import { SvgIcons } from 'components/SVG';
import { useAuth } from 'providers/Auth';
import { Colors, FontWeight as fw } from 'constants/style';
import { StyleSheet } from 'react-native';
import { version } from '../../../package.json';
import { getScreenTestingIds } from 'test-utils/testIds';
import Config from 'react-native-config';
import useRenderListener from 'hooks/useRenderListener';

const Login: React.FC = () => {
  useRenderListener();

  const { login, testingLogin } = useAuth();

  // ------------- FOR AUTOMATED TESTING ONLY ------------- //
  const [testUsername, setTestUsername] = useState('');
  const [testPassword, setTestPassword] = useState('');

  const handleTestLoginPress = () => {
    testingLogin({ username: testUsername, password: testPassword });
  };

  const testIds = getScreenTestingIds('Login');
  // ------------------------------------------------------ //

  return (
    <SafeAreaView style={styles.safeArea}>
      <Section flex={1} justifyContent="center" alignItems="center">
        <Section paddingBottom={42}>
          <SvgIcons name={'Logo'} size={126} testID={testIds.screen} />
        </Section>

        <Text h4 color={Colors.PRIMARY_1100} fontWeight={fw.REGULAR}>
          Branch Inventory
        </Text>
        {/* ---------------------- FOR AUTOMATED TESTING ONLY ---------------------- */}
        {/* Components are hidden from the user but can be targeted by their testIDs */}
        {Config.ENVIRONMENT_NAME !== 'production' && (
          <View>
            <TextInput
              value={testUsername}
              onChangeText={setTestUsername}
              placeholder="username"
              testID={testIds.testingUsernameInput}
            />
            <TextInput
              value={testPassword}
              onChangeText={setTestPassword}
              placeholder="password"
              testID={testIds.testingPasswordInput}
            />
            <RNButton
              title="login"
              onPress={handleTestLoginPress}
              testID={testIds.testingLoginButton}
            />
          </View>
        )}
        {/* ------------------------------------------------------------------------ */}
      </Section>
      <Section width="100%">
        <Button
          title="Login with Okta"
          accessibilityLabel="Login with Okta"
          onPress={login}
          containerStyle={styles.loginButtonContainer}
          iconContainerStyle={styles.loginButtonIconContainer}
          icon={{
            type: 'reece',
            name: 'okta',
            color: Colors.WHITE,
          }}
        />
        <Text caption centered style={styles.version}>
          Version: {version}
        </Text>
      </Section>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  loginButtonContainer: {
    paddingVertical: 24,
    paddingHorizontal: 18,
  },
  loginButtonIconContainer: {
    marginRight: 16,
  },
  version: {
    paddingBottom: 24,
  },
  invisible: {
    height: 0,
    width: 0,
    opacity: 0,
  },
});

export default Login;
