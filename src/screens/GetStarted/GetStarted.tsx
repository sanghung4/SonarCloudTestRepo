import React from 'react';
import { StyleSheet, View } from 'react-native';
import { ScreenLayout } from 'components/ScreenLayout';
import { useAuth } from 'providers/Auth';
import { NavTile } from './NavTile';
import { controller } from './Controller';
import { useConfig } from 'hooks/useConfig';
import { isMincron } from 'utils/apollo';
import { getScreenTestingIds } from 'test-utils/testIds';
import useRenderListener from 'hooks/useRenderListener';

const GetStarted = () => {
  useRenderListener();

  const { isManager } = useAuth();
  const [{ count }] = useConfig();

  const isEclipse = !isMincron(count);

  const displayedTiles = controller.filter(
    (item) =>
      (isManager || !item.managerOnly)
  );

  const testIds = getScreenTestingIds('GetStarted');

  return (
    <ScreenLayout testID={testIds.screenLayout}>
      <ScreenLayout.ScrollContent banner={{ title: 'Get Started' }}>
        <View style={styles.tileContainer}>
          {displayedTiles.map((item, index) => (
            <NavTile item={item} key={index} />
          ))}
        </View>
      </ScreenLayout.ScrollContent>
    </ScreenLayout>
  );
};

const styles = StyleSheet.create({
  titleContainer: {
    paddingVertical: 24,
    marginBottom: 44,
    justifyContent: 'center',
    alignItems: 'center',
  },
  titleText: {
    lineHeight: 30,
  },
  tileContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    flexWrap: 'wrap',
    paddingHorizontal: 20,
    paddingVertical: 8,
  },
});

export default GetStarted;
