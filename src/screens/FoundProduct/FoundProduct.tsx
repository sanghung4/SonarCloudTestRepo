import React, { useEffect, useState } from 'react';
import { StyleSheet, View } from 'react-native';
import { Button } from 'components/Button';
import { Colors, FontWeight } from 'constants/style';
import { AddProduct } from 'screens/FoundProduct/AddProduct';
import { CreateWriteIn } from 'screens/FoundProduct/CreateWriteIn';
import { FOUND_ACTIONS } from './utils';
import { isMincron } from 'utils/apollo';
import { useConfig } from 'hooks/useConfig';
import { ScreenLayout } from 'components/ScreenLayout';
import { getScreenTestingIds } from 'test-utils/testIds';
import useRenderListener from 'hooks/useRenderListener';

const FoundProduct = () => {
  useRenderListener();

  const [{ count }] = useConfig();

  const [activeScreen, setActiveScreen] = useState(
    FOUND_ACTIONS.CREATE_WRITE_IN
  );

  useEffect(() => {
    setActiveScreen(
      isMincron(count)
        ? FOUND_ACTIONS.CREATE_WRITE_IN
        : FOUND_ACTIONS.ADD_PRODUCT
    );
  }, []);

  const handelPress = (screen: FOUND_ACTIONS) => () => {
    if (activeScreen !== screen) {
      setActiveScreen(screen);
    }
  };

  const testIds = getScreenTestingIds('FoundProduct');

  return (
    <ScreenLayout>
      <ScreenLayout.ScrollContent>
        <View style={styles.bannerContainer}>
          {/* SCREEN SELECT */}
          {/* SELECT CREATE WRITE IN */}
          <View style={styles.bannerButtonWrapper}>
            <Button
              title="Create a Write-In"
              type="clear"
              titleStyle={styles.bannerButtonTitle}
              buttonStyle={[
                styles.bannerButton,
                activeScreen === FOUND_ACTIONS.CREATE_WRITE_IN &&
                  styles.bannerButtonActive,
              ]}
              onPress={handelPress(FOUND_ACTIONS.CREATE_WRITE_IN)}
              testID={testIds.createAWriteInbutton}
            />
          </View>
          {/* SELECT ADD PRODUCT */}
          {!isMincron(count) && (
            <View style={styles.bannerButtonWrapper}>
              <Button
                title="Add a Product"
                type="clear"
                titleStyle={styles.bannerButtonTitle}
                buttonStyle={[
                  styles.bannerButton,
                  activeScreen === FOUND_ACTIONS.ADD_PRODUCT &&
                    styles.bannerButtonActive,
                ]}
                onPress={handelPress(FOUND_ACTIONS.ADD_PRODUCT)}
                testID={testIds.addAProduct}
              />
            </View>
          )}
        </View>
        {activeScreen === FOUND_ACTIONS.CREATE_WRITE_IN && <CreateWriteIn />}
        {activeScreen === FOUND_ACTIONS.ADD_PRODUCT && !isMincron(count) && (
          <AddProduct />
        )}
      </ScreenLayout.ScrollContent>
    </ScreenLayout>
  );
};

const styles = StyleSheet.create({
  bannerContainer: {
    backgroundColor: Colors.SECONDARY_6100,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    paddingHorizontal: 24,
    paddingVertical: 12,
  },
  bannerButtonWrapper: {
    flex: 1,
  },
  bannerButton: {
    paddingVertical: 8,
  },
  bannerButtonActive: {
    borderBottomWidth: 2,
  },
  bannerButtonTitle: {
    fontSize: 16,
    fontWeight: FontWeight.MEDIUM,
  },
});

export default FoundProduct;
