import { useContext } from 'react';

import {
  Box,
  Switch,
  Typography,
  Container
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import Breadcrumbs from 'common/Breadcrumbs';
import { AuthContext } from 'AuthProvider';
import PermissionRequired, { Permission } from 'common/PermissionRequired';
import { useToggleFeatures } from 'FeatureToggle/util';
import useDocumentTitle from 'hooks/useDocumentTitle';

export default function FeatureToggle() {
  /**
   * Custom hooks
   */
  const { t } = useTranslation();
  const { setFeature } = useToggleFeatures();
  useDocumentTitle(t('common.features'));

  /**
   * Contest
   */
  const { features } = useContext(AuthContext);

  /**
   * Render
   */
  return (
    <PermissionRequired permissions={[Permission.TOGGLE_FEATURES]}>
      <Box>
        <Breadcrumbs pageTitle={t('featureToggle.featureToggle')} />
        <Container>
          <>
            {features
              ?.sort((a, b) => (a.name > b.name ? 1 : 0))
              .map((feature) => (
                <Box key={feature.id} display="flex" flexDirection="row">
                  <Box mt={0.5}>
                    <Typography variant="h4">{feature.name}</Typography>
                  </Box>
                  <Switch
                    checked={feature.isEnabled}
                    onChange={setFeature(feature)}
                    name="checkedA"
                    color="primary"
                    inputProps={{ 'aria-label': 'primary checkbox' }}
                  />
                </Box>
              ))}
          </>
        </Container>
      </Box>
    </PermissionRequired>
  );
}
