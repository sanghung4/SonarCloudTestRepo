import {
  Button,
  Grid,
  Link,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import {
  PoshLogo,
  PoshTitle,
  PoshTypography as Typography,
  usePoshStyles
} from 'Brands/Posh/util/styles';
import { links } from 'utils/links';

export default function PoshHeader() {
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
    <>
      {/********************* TITLE *********************/}
      <Grid
        container
        justifyContent="center"
        alignItems="center"
        height={isSmallScreen ? 84 : 238}
      >
        <PoshTitle isMobile={isSmallScreen} />
      </Grid>

      {/********************* LANDING *********************/}
      <Grid container sx={sx.landerbg}>
        <Grid item ml={25}>
          <PoshLogo isMobile={isSmallScreen} />
        </Grid>
      </Grid>

      {/********************* INTRO *********************/}
      <Grid container justifyContent="center" bgcolor="lightGray.main">
        <Grid
          container
          flexDirection="column"
          justifyContent="center"
          alignItems="center"
          maxWidth={isSmallScreen ? 480 : 851}
          mx={isSmallScreen ? 3 : 0}
          my={isSmallScreen ? 4 : 7}
        >
          <Typography
            color="textSecondary"
            textAlign="center"
            fontSize={isSmallScreen ? 36 : 64}
            fontWeight={700}
            lineHeight={1}
            mb={4}
          >
            {t('poshMarketing.introTitle')}
          </Typography>
          <Typography
            color="textPrimary"
            textAlign="center"
            fontSize={isSmallScreen ? 20 : 22}
            fontWeight={isSmallScreen ? 500 : 700}
            lineHeight={1.2}
          >
            {t('poshMarketing.introParagraph')}
          </Typography>
          <Grid marginTop={5}>
            <Link href={links.posh.mainShop}>
              <Button sx={sx.button} variant="secondary">
                {t('poshMarketing.shopPosh')}
              </Button>
            </Link>
          </Grid>
        </Grid>
      </Grid>
    </>
  );
}
