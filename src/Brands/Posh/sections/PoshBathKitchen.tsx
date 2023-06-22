import {
  Button,
  Grid,
  Link,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import {
  PoshTypography as Typography,
  usePoshStyles
} from 'Brands/Posh/util/styles';
import { links } from 'utils/links';

export default function PoshBathKitchen() {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();
  const sx = usePoshStyles(isSmallScreen);

  /**
   * Render
   */
  return (
    <Grid container color="common.white" bgcolor="poshRed.main">
      {/* Top - Bathroom */}
      <Grid container>
        <Grid item xs={isSmallScreen ? 12 : 6} sx={sx.bathroomBox} />
        <Grid
          item
          display="flex"
          xs={isSmallScreen ? 12 : 6}
          px={isSmallScreen ? 4.5 : 6}
          py={isSmallScreen ? 3.5 : 12}
          justifyContent="center"
          alignItems="center"
        >
          <Grid flexDirection="column">
            <Typography
              textAlign="center"
              fontSize={isSmallScreen ? 25 : 64}
              fontWeight={700}
              lineHeight={1}
              mb={4}
            >
              {t('poshMarketing.bathAndKitchen.bathTitle')}
            </Typography>
            <Grid
              textAlign="center"
              justifyContent="center"
              alignItems="center"
              mt={4}
            >
              <Link href={links.posh.shopBathroom}>
                <Button variant="secondary" sx={sx.button}>
                  {t('poshMarketing.bathAndKitchen.bathButton')}
                </Button>
              </Link>
            </Grid>
          </Grid>
        </Grid>
      </Grid>
      {/* Bottom - Bathroom */}
      <Grid container flexDirection={isSmallScreen ? 'row' : 'row-reverse'}>
        <Grid item xs={isSmallScreen ? 12 : 6} sx={sx.kitchenBox} />
        <Grid
          item
          display="flex"
          xs={isSmallScreen ? 12 : 6}
          px={isSmallScreen ? 4.5 : 6}
          py={isSmallScreen ? 3.5 : 12}
          justifyContent="center"
          alignItems="center"
        >
          <Grid flexDirection="column">
            <Typography
              textAlign="center"
              fontSize={isSmallScreen ? 25 : 64}
              fontWeight={700}
              lineHeight={1}
              mb={4}
            >
              {t('poshMarketing.bathAndKitchen.kitchenTitle')}
            </Typography>
            <Grid marginTop={4} justifyContent="center" textAlign="center">
              <Link href={links.posh.shopKitchen}>
                <Button variant="secondary" sx={sx.button}>
                  {t('poshMarketing.bathAndKitchen.kitchenButton')}
                </Button>
              </Link>
            </Grid>
          </Grid>
        </Grid>
      </Grid>
    </Grid>
  );
}
