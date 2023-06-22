import {
  Grid,
  Typography,
  Link,
  Box,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import {
  FeaturedItemsTypography,
  BigDogGradientDivider
} from 'Brands/BigDog/util/styles';
import { FeaturedItems } from 'Brands/BigDog/util/featuredItems';
export default function BigDogFeaturedItems() {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * Render
   */
  return (
    <Grid mt={isSmallScreen ? 10 : 14.5}>
      <BigDogGradientDivider flexItem />
      <Grid
        height={isSmallScreen ? 39 : 59}
        mt={7.625}
        ml={isSmallScreen ? 0 : 7.125}
      >
        <Typography
          fontSize={isSmallScreen ? 32 : 48}
          fontWeight={800}
          lineHeight={1.2}
          textAlign={isSmallScreen ? 'center' : 'initial'}
          color="common.black"
          fontFamily="Gilroy"
        >
          {t('bigDogMarketing.featuredItemsTitle')}
        </Typography>
      </Grid>
      <Grid
        container
        item
        justifyContent="center"
        flexDirection={isSmallScreen ? 'column' : 'row'}
        alignItems={isSmallScreen ? 'center' : 'initial'}
        gap={isSmallScreen ? 1 : 5}
        mt={isSmallScreen ? 3 : 0}
        bgcolor="common.white"
      >
        {FeaturedItems.map((featuredItem, index) => {
          return (
            <Grid
              key={index}
              item
              width={250}
              flexDirection="column"
              alignItems="center"
              gap={isSmallScreen ? 1 : 2}
            >
              <Link href={featuredItem.url}>
                <Box
                  display="flex"
                  alignItems="center"
                  justifyContent="center"
                  height={isSmallScreen ? '100%' : 250}
                >
                  <img alt={featuredItem.title} src={featuredItem.image} />
                </Box>
              </Link>
              <Link href={featuredItem.url}>
                <FeaturedItemsTypography>
                  {featuredItem.title}
                </FeaturedItemsTypography>
              </Link>
            </Grid>
          );
        })}
      </Grid>
    </Grid>
  );
}
