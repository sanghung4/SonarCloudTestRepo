import React from 'react';
import { LocationSummary } from 'api';
import { useOverlay } from 'providers/Overlay';
import { ErrorType } from 'constants/error';
import { getError } from 'utils/error';
import { useLocation } from 'hooks/useLocation';
import { RouteNames } from 'constants/routes';
import { CountLocationItem } from './CountLocationItem';
import { BranchPage } from 'components/BranchPage';
import { AppScreenProps } from 'navigation/types';
import { getScreenTestingIds } from 'test-utils/testIds';
import useRenderListener from 'hooks/useRenderListener';


const CountLocations = ({ navigation }: AppScreenProps<'CountLocations'>) => {
  useRenderListener();

  const { showAlert } = useOverlay();
  const { loading, getLocation } = useLocation({
    onCompleted: () => {
      navigation.navigate(RouteNames.LOCATION_ITEMS);
    },
    onError: (e) => {
      showAlert(getError(ErrorType.LOCATION, e));
    },
  });

  const testIds = getScreenTestingIds('CountLocations');

  return (
    <BranchPage
      renderItem={(item: LocationSummary) => (
        <CountLocationItem
          location={item}
          key={item.id}
          onPress={() => {
            getLocation(item.id);
          }}
        />
      )}
      loading={loading}
      testID={testIds.screen}
    />
  );
};

export default CountLocations;
