import React, { FC, useState } from 'react';

import {
  Box,
  Container,
  Grid,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import phoneFrame from 'images/home/phone.png';
import screenBuildBusiness from 'images/home/building-business.jpg';
import screenEasyToUse from 'images/home/easy-to-use.jpg';
import screenFastFuture from 'images/home/faster-future.jpg';
import screenSaveTime from 'images/home/saves-time.jpg';
import { ReactComponent as Line0 } from 'images/home/line0.svg';
import { ReactComponent as Line1 } from 'images/home/line1.svg';
import { ReactComponent as Line2 } from 'images/home/line2.svg';
import {
  FeatureBuildBusiness,
  FeatureBuildBusinessHover,
  FeatureEasyToUse,
  FeatureEasyToUseHover,
  FeatureFastFuture,
  FeatureFastFutureHover,
  FeatureSaveTime,
  FeatureSaveTimeHover
} from 'icons';
import { MaXFeaturesContainer } from './styles';

type FeatureList = {
  [key: string]: {
    title: string;
    sub: string;
    screenshot: string;
    IconSVG: FC;
    IconSVGHover: FC;
    LineSVG: FC;
    testID?: string;
  };
};

function Features() {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * Selections
   */
  const featureList: FeatureList = {
    easyToUse: {
      title: t('home.featureEasyToUse'),
      sub: t('home.featureEasyToUseExt'),
      screenshot: screenEasyToUse,
      IconSVG: FeatureEasyToUse,
      IconSVGHover: FeatureEasyToUseHover,
      LineSVG: Line0,
      testID: 'easy-to-use'
    },
    buildBusiness: {
      title: t('home.featureBusiness'),
      sub: t('home.featureBusinessExt'),
      screenshot: screenBuildBusiness,
      IconSVG: FeatureBuildBusiness,
      IconSVGHover: FeatureBuildBusinessHover,
      LineSVG: Line1,
      testID: 'build-businesses'
    },
    savesTime: {
      title: t('home.featureSavesTime'),
      sub: t('home.featureSavesTimeExt'),
      screenshot: screenSaveTime,
      IconSVG: FeatureSaveTime,
      IconSVGHover: FeatureSaveTimeHover,
      LineSVG: Line1,
      testID: 'saves-time'
    },
    fasterFuture: {
      title: t('home.featureFastFuture'),
      sub: t('home.featureFastFutureExt'),
      screenshot: screenFastFuture,
      IconSVG: FeatureFastFuture,
      IconSVGHover: FeatureFastFutureHover,
      LineSVG: Line2,
      testID: 'faster-future'
    }
  };
  type toSelect = keyof typeof featureList;

  /**
   * States
   */
  // Only applies when it is hovering, when it is not hovered, it will be `undefined`
  const [selection, setSelection] = useState<toSelect | undefined>();
  // Has to be a separate state since we cannot leave undefined for screenshots (blank screenshot)
  const [screenshotVisible, setScreenshotVisible] =
    useState<toSelect>('easyToUse');

  /**
   * Callbacks
   */
  const hoverUpdate = (select: string) => {
    setSelection(select);
    setScreenshotVisible(select);
  };
  const unhover = () => {
    setSelection(undefined);
  };

  return (
    <Box
      display="flex"
      justifyContent="center"
      flexDirection="column"
      sx={{ bgcolor: 'background.paper' }}
    >
      <Typography
        variant={isSmallScreen ? 'h4' : 'h1'}
        color="primary"
        align="center"
        sx={{
          my: isSmallScreen ? 4 : 0,
          mb: isSmallScreen ? 4 : 10,
          fontWeight: isSmallScreen ? 500 : 700
        }}
        data-testid="home-feature-title"
      >
        {t('home.featureTitle')}
      </Typography>
      <Box
        width="100%"
        py={isSmallScreen ? 2 : 3}
        sx={{ bgcolor: 'warmGray.main' }}
      >
        <Container
          disableGutters={isSmallScreen}
          maxWidth={isSmallScreen ? 'md' : 'lg'}
        >
          <Grid
            container
            justifyContent="center"
            wrap="nowrap"
            direction={isSmallScreen ? 'column' : 'row'}
            alignItems={isSmallScreen ? 'center' : 'stretch'}
          >
            <MaXFeaturesContainer xs container>
              <Box
                mr={isSmallScreen ? 35 : undefined}
                height={isSmallScreen ? 568 : undefined}
                position="relative"
              >
                <Box
                  top={isSmallScreen ? undefined : -348}
                  position={isSmallScreen ? 'relative' : 'absolute'}
                  height={isSmallScreen ? 568 : 724}
                >
                  <Box
                    width={isSmallScreen ? 242 : 309}
                    height={isSmallScreen ? 526 : 671}
                    top={isSmallScreen ? 17 : 22}
                    left={isSmallScreen ? 18 : 24}
                    borderRadius="25px"
                    position="absolute"
                    sx={{
                      backgroundImage: `url(${featureList[screenshotVisible].screenshot})`,
                      backgroundSize: 'cover'
                    }}
                    data-testid={`home-phone-image-${featureList[screenshotVisible].testID}`}
                  />
                  <Box
                    width={isSmallScreen ? 280 : 356}
                    height={isSmallScreen ? 568 : 724}
                    position="absolute"
                    sx={{
                      backgroundImage: `url(${phoneFrame})`,
                      backgroundSize: 'cover'
                    }}
                  />
                </Box>
              </Box>
            </MaXFeaturesContainer>
            <Grid
              item
              container
              xs={8}
              direction="column"
              justifyContent="center"
              onMouseLeave={unhover}
              data-testid="features-list"
            >
              {Object.keys(featureList).map((key, index) => {
                const { title, sub, IconSVG, IconSVGHover, LineSVG, testID } =
                  featureList[key];
                return (
                  <Box
                    display="flex"
                    justifyContent="center"
                    mx={isSmallScreen ? undefined : 5}
                    my={3.5}
                    key={key}
                    onMouseEnter={() => !isSmallScreen && hoverUpdate(key)}
                    data-testid={testID}
                  >
                    <Grid item container direction="row" wrap="nowrap" xs={8}>
                      {!isSmallScreen ? (
                        <Grid item container xs={true}>
                          <Box
                            overflow="visible"
                            position="relative"
                            width={100}
                          >
                            <Box
                              position="absolute"
                              top={index === 3 ? -72 : index === 1 ? 24 : 34}
                              left={-10}
                            >
                              {selection === key && !isSmallScreen ? (
                                <LineSVG
                                  data-testid={`home-feature-svg-${index}`}
                                />
                              ) : null}
                            </Box>
                          </Box>
                        </Grid>
                      ) : null}
                      <Grid item container xs={true}>
                        <Box
                          width={isSmallScreen ? 56 : 64}
                          mr={isSmallScreen ? 2 : 3}
                          display="flex"
                        >
                          {selection === key && !isSmallScreen ? (
                            <IconSVGHover
                              data-testid={`home-feature-svg-hover-${index}`}
                            />
                          ) : (
                            <IconSVG
                              data-testid={`home-feature-svg-${index}`}
                            />
                          )}
                        </Box>
                      </Grid>
                      <Grid
                        item
                        container
                        xs={false}
                        direction="column"
                        height={!isSmallScreen ? 160 : undefined}
                      >
                        <Box mx={isSmallScreen ? 1 : 2} maxWidth={360}>
                          <Grid item>
                            <Typography
                              variant="h3"
                              color="primary"
                              sx={
                                isSmallScreen
                                  ? { fontSize: 18, lineHeight: '24px' }
                                  : { fontSize: 25, lineHeight: '30px' }
                              }
                              data-testid={`home-feature-title-${index}`}
                            >
                              {title}
                            </Typography>
                          </Grid>
                          <Grid width={isSmallScreen ? 'auto' : 180} item>
                            <Typography
                              color="primary"
                              sx={
                                isSmallScreen
                                  ? { fontSize: 14, lineHeight: '19px' }
                                  : { fontSize: 16, lineHeight: '24px' }
                              }
                              data-testid={`home-feature-subtitle-${index}`}
                            >
                              {sub}
                            </Typography>
                          </Grid>
                        </Box>
                      </Grid>
                    </Grid>
                  </Box>
                );
              })}
            </Grid>
          </Grid>
        </Container>
      </Box>
      <Box height={80} />
    </Box>
  );
}
export default Features;
