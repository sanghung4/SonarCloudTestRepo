import { Container, useScreenSize } from '@dialexa/reece-component-library';

import PoshHeader from 'Brands/Posh/sections/PoshHeader';
import PoshMission from 'Brands/Posh/sections/PoshMission';
import PoshVimeo from 'Brands/Posh/sections/PoshVimeo';
import PoshBathKitchen from 'Brands/Posh/sections/PoshBathKitchen';
import PoshProductList from 'Brands/Posh/sections/PoshProductList';
import PoshFooter from 'Brands/Posh/sections/PoshFooter';
import { useTranslation } from 'react-i18next';
import { usePoshStyles } from 'Brands/Posh/util/styles';
import useDocumentTitle from 'hooks/useDocumentTitle';

function PoshMarketing() {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();
  const sx = usePoshStyles(isSmallScreen);
  useDocumentTitle(t('poshMarketing.posh'));

  /**
   * Render
   */
  return (
    <Container
      disableGutters
      style={{ maxWidth: 1440 }} // Has to use style to override maxWidth manually
      sx={sx.container}
    >
      <PoshHeader />
      <PoshMission />
      <PoshVimeo />
      <PoshBathKitchen />
      <PoshProductList />
      <PoshFooter />
    </Container>
  );
}

export default PoshMarketing;
