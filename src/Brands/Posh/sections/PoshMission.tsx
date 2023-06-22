import { Grid, useScreenSize } from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import {
  PoshDivider,
  PoshTypography as Typography
} from 'Brands/Posh/util/styles';

export default function PoshMission() {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * Render
   */
  return (
    <Grid
      container
      justifyContent="center"
      color="common.white"
      bgcolor="poshRed.main"
    >
      {/* Left */}
      <Grid
        item
        xs={isSmallScreen ? 12 : 4}
        px={isSmallScreen ? 4.5 : 6}
        py={isSmallScreen ? 3.5 : 12}
        flexDirection="column"
        justifyContent="center"
        alignItems="center"
      >
        <Typography
          fontSize={isSmallScreen ? 25 : 64}
          fontWeight={700}
          lineHeight={1.2}
          textAlign={isSmallScreen ? 'center' : 'initial'}
          mb={5}
        >
          {t('poshMarketing.mission.choiceTitle')}
        </Typography>
        <Typography
          fontSize={isSmallScreen ? 20 : 22}
          fontWeight={isSmallScreen ? 500 : 700}
          letterSpacing={isSmallScreen ? 1 : 0.6}
          lineHeight={1.2}
        >
          {t('poshMarketing.mission.choiceDescription')}
        </Typography>
      </Grid>
      <PoshDivider mobile={isSmallScreen} />
      {/* Middle */}
      <Grid
        item
        xs={isSmallScreen ? 12 : 4}
        px={isSmallScreen ? 4.5 : 6}
        py={isSmallScreen ? 3.5 : 12}
        flexDirection="column"
        justifyContent="center"
        alignItems="center"
      >
        <Typography
          fontSize={isSmallScreen ? 25 : 64}
          fontWeight={700}
          lineHeight={1.2}
          textAlign={isSmallScreen ? 'center' : 'initial'}
          mb={5}
        >
          {t('poshMarketing.mission.qualityTitle')}
        </Typography>
        <Typography
          fontSize={isSmallScreen ? 20 : 22}
          fontWeight={isSmallScreen ? 500 : 700}
          letterSpacing={isSmallScreen ? 1 : 0.6}
          lineHeight={1.2}
        >
          {t('poshMarketing.mission.qualityDescription')}
        </Typography>
      </Grid>
      <PoshDivider mobile={isSmallScreen} />
      {/* Right */}
      <Grid
        item
        xs={isSmallScreen ? 12 : 4}
        px={isSmallScreen ? 4.5 : 6}
        py={isSmallScreen ? 3.5 : 12}
        flexDirection="column"
        justifyContent="center"
        alignItems="center"
      >
        <Typography
          fontSize={isSmallScreen ? 25 : 64}
          fontWeight={700}
          lineHeight={1.2}
          textAlign={isSmallScreen ? 'center' : 'initial'}
          mb={5}
        >
          {t('poshMarketing.mission.designTitle')}
        </Typography>
        <Typography
          fontSize={isSmallScreen ? 20 : 22}
          fontWeight={isSmallScreen ? 500 : 700}
          letterSpacing={isSmallScreen ? 1 : 0.6}
          lineHeight={1.2}
        >
          {t('poshMarketing.mission.designDescription')}
        </Typography>
      </Grid>
    </Grid>
  );
}
