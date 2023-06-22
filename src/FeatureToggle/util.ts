import { useSnackbar } from '@dialexa/reece-component-library';
import { AuthContext } from 'AuthProvider';
import { Feature } from 'generated/graphql';
import { ChangeEvent, useContext } from 'react';
import { useTranslation } from 'react-i18next';

export function useToggleFeatures() {
  /**
   * Custom hooks
   */
  const { t } = useTranslation();
  const { pushAlert } = useSnackbar();

  /**
   * Contest
   */
  const { setFeature: setFeatureAuth } = useContext(AuthContext);

  /**
   * Output
   */
  const setFeature =
    (feature: Feature) => (e: ChangeEvent<HTMLInputElement>) => {
      setFeatureAuth?.(feature.id, e.target.checked);
      pushAlert(t('featureToggle.changeSaved'), { variant: 'success' });
    };
  return { setFeature };
}
