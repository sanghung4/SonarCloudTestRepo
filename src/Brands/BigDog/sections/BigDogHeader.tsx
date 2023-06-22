import {
  Grid,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import {
  HeaderBanner,
  HeaderBannerContainer,
  HeaderTitle,
  BigDogDivider
} from 'Brands/BigDog/util/styles';
import { ReactComponent as BigDogLogoSvg } from 'images/bigDog/BigDogLogo.svg';

export default function BigDogHeader() {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * Render
   */
  return (
    <>
      <Grid maxWidth="1440px" container>
        <HeaderBanner>
          <HeaderBannerContainer>
            <BigDogLogoSvg
              style={{
                width: isSmallScreen ? 160 : 238,
                height: isSmallScreen ? 34 : 50
              }}
            />
            <Grid>
              <HeaderTitle>{t('bigDogMarketing.title')}</HeaderTitle>
            </Grid>
          </HeaderBannerContainer>
        </HeaderBanner>
        <BigDogDivider flexItem />
      </Grid>
      <Grid>
        <Typography
          mx={isSmallScreen ? 1.7 : 24}
          my={isSmallScreen ? 8 : 9}
          mb={0}
          color="common.black"
          textAlign="center"
          fontSize={isSmallScreen ? 24 : 36}
          fontFamily="Metropolis"
          fontWeight={800}
          lineHeight={1.2}
        >
          {t('bigDogMarketing.introParagraph')}
        </Typography>
        <Typography
          mx={isSmallScreen ? 1.7 : 27.5}
          mb={0}
          color="bigDogBrand.main"
          textAlign="center"
          fontSize={isSmallScreen ? 24 : 36}
          fontFamily="Metropolis"
          fontWeight={800}
          lineHeight={1.2}
        >
          {t('bigDogMarketing.paragraphOne')}
        </Typography>
      </Grid>
    </>
  );
}
