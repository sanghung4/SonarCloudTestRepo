import { Grid, useScreenSize, Box } from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import {
  MissionDescriptionContainer,
  MissionHeadersTypography,
  MissionDescriptionTypography
} from 'Brands/BigDog/util/styles';
import Image1 from 'images/bigDog/image1.jpg';
import Image2 from 'images/bigDog/image2.jpg';
import Image3 from 'images/bigDog/image3.jpg';

export default function BigDogMission() {
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
      rowGap={isSmallScreen ? 0 : 5}
      columnGap={isSmallScreen ? 0 : 1.6}
      flexDirection={isSmallScreen ? 'column' : 'row'}
      alignItems={isSmallScreen ? 'center' : 'flex-start'}
      color="common.white"
      mt={isSmallScreen ? 8 : 9}
      ml={0}
    >
      <Grid
        item
        display="flex"
        justifyContent={isSmallScreen ? 'center' : 'flex-start'}
        width={isSmallScreen ? '100%' : '30%'}
        flexDirection="row"
        height={149}
      >
        <Box
          bgcolor="bigDogBrand.main"
          width={152}
          height={149}
          left={0}
          top={0}
        >
          <Box component="img" src={Image1} />
        </Box>
        <Grid
          width={isSmallScreen ? 197 : 273}
          height={149}
          top={0}
          bgcolor="background.default"
        >
          <Box height={isSmallScreen ? 22 : 24} ml={2} mt={2.5}>
            <MissionHeadersTypography>
              {t('bigDogMarketing.mission.designTitle')}
            </MissionHeadersTypography>
          </Box>
          <MissionDescriptionContainer>
            <MissionDescriptionTypography>
              {t('bigDogMarketing.mission.designDescription')}
            </MissionDescriptionTypography>
          </MissionDescriptionContainer>
        </Grid>
      </Grid>
      <Grid
        item
        display="flex"
        width={isSmallScreen ? '100%' : '30%'}
        justifyContent={isSmallScreen ? 'center' : 'flex-start'}
        flexDirection="row"
        height={149}
        mt={isSmallScreen ? 2.5 : 0}
      >
        <Box
          bgcolor="bigDogBrand.main"
          width={152}
          height={149}
          left={0}
          top={0}
        >
          <Box component="img" src={Image2} />
        </Box>
        <Grid
          width={isSmallScreen ? 197 : 273}
          height={149}
          top={0}
          bgcolor="background.default"
        >
          <Box height={isSmallScreen ? 22 : 24} ml={2} mt={2.5}>
            <MissionHeadersTypography>
              {t('bigDogMarketing.mission.qualityTitle')}
            </MissionHeadersTypography>
          </Box>
          <MissionDescriptionContainer>
            <MissionDescriptionTypography>
              {t('bigDogMarketing.mission.qualityDescription')}
            </MissionDescriptionTypography>
          </MissionDescriptionContainer>
        </Grid>
      </Grid>
      <Grid
        item
        display="flex"
        flexDirection="row"
        justifyContent={isSmallScreen ? 'center' : 'flex-start'}
        width={isSmallScreen ? '100%' : '30%'}
        height={149}
        mt={isSmallScreen ? 2.5 : 0}
      >
        <Box
          bgcolor="bigDogBrand.main"
          width={152}
          height={149}
          left={0}
          top={0}
        >
          <Box component="img" src={Image3} />
        </Box>
        <Grid
          width={isSmallScreen ? 197 : 273}
          height={149}
          top={0}
          bgcolor="background.default"
        >
          <Box height={isSmallScreen ? 22 : 24} ml={2} mt={2.5}>
            <MissionHeadersTypography>
              {t('bigDogMarketing.mission.availabilityTitle')}
            </MissionHeadersTypography>
          </Box>
          <MissionDescriptionContainer>
            <MissionDescriptionTypography>
              {t('bigDogMarketing.mission.availabilityDescription')}
            </MissionDescriptionTypography>
          </MissionDescriptionContainer>
        </Grid>
      </Grid>
    </Grid>
  );
}
