import {
  Button,
  Grid,
  Link,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { ReactComponent as PoshIcon } from 'images/posh/poshIcon.svg';
import brochureImg from 'images/posh/poshBrochure.jpg';
import {
  PoshTypography as Typography,
  usePoshStyles
} from 'Brands/Posh/util/styles';
import { links } from 'utils/links';

export default function PoshFooter() {
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
      {/********************* BUILT TO LAST *********************/}
      <Grid container color="common.white" bgcolor="darkGray.main">
        <Grid
          xs={12}
          item
          mx={isSmallScreen ? 4 : 26}
          my={isSmallScreen ? 8 : 10}
          flexDirection="column"
          justifyContent="center"
          alignItems="center"
          textAlign="center"
        >
          <PoshIcon />
          <Grid mx={isSmallScreen ? 0 : 28} my={4}>
            <Typography
              textAlign="center"
              fontSize={isSmallScreen ? 30 : 40}
              fontWeight={700}
              lineHeight={1.2}
            >
              {t('poshMarketing.builtToLast')}
            </Typography>
          </Grid>
          <Link href={links.posh.mainShop}>
            <Button variant="secondary" sx={sx.button2}>
              {t('poshMarketing.shopTheCollection')}
            </Button>
          </Link>
        </Grid>
      </Grid>

      {/********************* BROCHURE *********************/}
      <Grid container bgcolor="lightGray.main">
        <Grid
          item
          xs={12}
          mx={isSmallScreen ? 3.25 : 4}
          my={isSmallScreen ? 8 : 10}
          flexDirection="column"
          justifyContent="center"
          alignItems="center"
          textAlign="center"
        >
          <Grid mb={4}>
            <img
              src={brochureImg}
              alt="brochure"
              style={{ boxShadow: '3px 5px 6px 0px #00000080' }}
            />
          </Grid>
          <Link href={links.posh.brochure} download>
            <Button variant="secondary" sx={sx.button}>
              {t('poshMarketing.downloadBrochure')}
            </Button>
          </Link>
        </Grid>
      </Grid>
    </>
  );
}
