import React from 'react';
import { StyleSheet } from 'react-native';
import { Overlay } from 'react-native-elements';
import { useOverlay } from 'providers/Overlay';

import Alert from './Alert';
import Loading from './Loading';
import SortByModal from './SortByModal';
import { getScreenTestingIds } from 'test-utils/testIds';

const OverlayContainer: React.FC = () => {
  const { alertPayload, visibleOverlay } = useOverlay();

  const testIds = getScreenTestingIds('Overlay');

  const getTestId = () => {
    if (visibleOverlay === 'alert') {
      return testIds.alert;
    }
    if (visibleOverlay === 'loading') {
      return testIds.loading;
    }
    if (visibleOverlay === 'sortBy') {
      return testIds.sortyBy;
    }
  };

  return (
    <Overlay
      testID={getTestId()}
      isVisible={
        visibleOverlay === 'alert' ||
        visibleOverlay === 'loading' ||
        visibleOverlay === 'sortBy'
      }
      fullScreen={
        !!alertPayload.options?.fullScreen || visibleOverlay === 'loading'
      }
      overlayStyle={[
        styles.overlay,
        visibleOverlay === 'loading' && styles.loadingOverlay,
      ]}
    >
      {visibleOverlay === 'alert' && <Alert />}
      {visibleOverlay === 'loading' && <Loading />}
      {visibleOverlay === 'sortBy' && <SortByModal />}
    </Overlay>
  );
};

const styles = StyleSheet.create({
  overlay: { padding: 0, borderRadius: 2 },
  loadingOverlay: {
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'transparent',
  },
});

export default OverlayContainer;
