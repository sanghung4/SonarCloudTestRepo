import {
  Button,
  Grid,
  Typography,
  Box,
  Link,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import FooterBackgroundImageMobile from 'images/bigDog/FooterBackGroundMobile.jpg';
import FooterBackgroundImageDesktop from 'images/bigDog/FooterBackGroundDesktop.jpg';
import FooterImageMobile from 'images/bigDog/footerImageMobile.jpg';
import FooterImageDesktop from 'images/bigDog/footerImageDesktop.jpg';
import { BigDogDivider } from 'Brands/BigDog/util/styles';
import { links } from 'utils/links';

export default function BigDogFooter() {
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
      height={isSmallScreen ? 709 : 845}
      left={0}
      mt={isSmallScreen ? 6.3 : 13.7}
    >
      <BigDogDivider flexItem />
      <Grid
        top={0}
        left={0}
        height={isSmallScreen ? 295 : 329}
        sx={{
          background: isSmallScreen
            ? `url(${FooterBackgroundImageMobile})`
            : `url(${FooterBackgroundImageDesktop})`
        }}
      >
        <Typography
          pt={isSmallScreen ? 5.5 : 9.8}
          fontFamily="Gilroy"
          fontSize={isSmallScreen ? 32 : 70}
          fontWeight={800}
          lineHeight={1.2}
          textAlign="center"
          color="common.white"
        >
          {t('bigDogMarketing.range.title')}
        </Typography>
        <Typography
          mt={isSmallScreen ? 5 : 3}
          px={isSmallScreen ? 4.2 : 27.5}
          fontFamily="Gilroy"
          fontWeight={400}
          fontSize={isSmallScreen ? 20 : 32}
          lineHeight={1.2}
          textAlign="center"
          color="common.white"
        >
          {t('bigDogMarketing.range.introParagraph')}
        </Typography>
        <Grid mt={isSmallScreen ? 5 : 3.2} container justifyContent="center">
          <Link href={links.bigDog.seeFullRange}>
            <Button
              variant="text"
              sx={{
                backgroundColor: 'bigDogBrand.main',
                ':hover': { backgroundColor: 'bigDogBrand.main' }
              }}
            >
              <Typography
                fontFamily="Metropolis"
                fontWeight={700}
                fontSize={14}
                lineHeight={1.2}
                textAlign="center"
                textTransform="uppercase"
                color="common.white"
              >
                {t('bigDogMarketing.range.buttonText')}
              </Typography>
            </Button>
          </Link>
        </Grid>
      </Grid>
      <Box
        width="100%"
        height={isSmallScreen ? 400 : 500}
        left={0}
        component="img"
        src={isSmallScreen ? FooterImageMobile : FooterImageDesktop}
      />
    </Grid>
  );
}
