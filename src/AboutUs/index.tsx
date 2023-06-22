import React from 'react';
import { useContext } from 'react';

import {
  Box,
  Button,
  Container,
  Grid,
  Image,
  Link,
  Typography
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { useQuery } from '@apollo/client';

import {
  BrandTitle,
  ContactUsButton,
  CenterContainer,
  DivisionCards,
  InfoSectionsCollection,
  MainBackground,
  MissionTitle,
  MissionText,
  NeedAssistanceImage,
  NeedAssistanceImageGrid,
  NeedAssistanceParagraph,
  NeedAssistanceTitle,
  Paragraph
} from 'AboutUs/util/styles';
import { Title } from 'AboutUs/util/styles';
import { aboutUsQuery } from 'AboutUs/util/query';
import { AuthContext } from 'AuthProvider';
import { useDomainInfo } from 'hooks/useDomainInfo';
import useDocumentTitle from 'hooks/useDocumentTitle';
import notfound from 'images/notfound.png';
import { generateCompanyUrl } from 'utils/brandList';
import { Configuration } from 'utils/configuration';
import Loader from 'common/Loader';
import { useState } from 'react';

const AboutUs = () => {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();
  const { port } = useDomainInfo();
  useDocumentTitle(t('common.about'));

  /**
   * Context
   */
  const { activeFeatures } = useContext(AuthContext);

  /**
   * State
   */
  const [showText, setShowText] = useState('');

  const { data, error, loading } = useQuery(aboutUsQuery, {
    context: {
      clientName: 'contentful'
    },
    variables: {
      aboutUsId: Configuration.contentfulAboutUsId ?? '',
      isPreview: Configuration.contentfulPreviewEnable
    }
  });

  const landerBackground = data?.aboutUs?.heroImage.url
    ? `linear-gradient(to right,
  rgba(0, 48, 87, 1) 10%, 
  rgba(0, 48, 87, 0) 100%
   ), url(${data?.aboutUs?.heroImage.url})`
    : undefined;

  /**
   * Callbacks
   */
  const handleMouseEnter = (i: any) => {
    setShowText(i);
  };

  /**
   * Render
   */
  if (loading) return <Loader />;
  if (error || !data?.aboutUs)
    return (
      <Box
        px={4}
        pt={10}
        pb={10}
        display="flex"
        flexDirection="column"
        alignItems="center"
        data-testid="error-message"
      >
        <Typography variant="body1" align="center">
          {t('aboutUs.error')}
        </Typography>
      </Box>
    );

  return (
    <Box
      display="flex"
      flexDirection="column"
      width="100%"
      bgcolor="common.white"
    >
      <MainBackground
        position="relative"
        style={{ backgroundImage: landerBackground }}
        data-testid="main-background"
      >
        <Container>
          <Box>{data.aboutUs.title}</Box>
          <Box pb={5} pr={16}>
            {data.aboutUs.title2}
          </Box>
        </Container>
      </MainBackground>
      <Container>
        <CenterContainer>
          <Title textAlign="left">{data.aboutUs.divisionsTitle?.title}</Title>
          <Paragraph
            fontSize="30px"
            fontWeight={400}
            fontFamily="Helvetica Neue"
            pb="5px"
          >
            {data.aboutUs.divisionsTitle?.paragraph}
          </Paragraph>
        </CenterContainer>
        <Box>
          <Grid container spacing={3}>
            {data.aboutUs.divisionsCarouselCollection?.items.map(
              (
                item: {
                  title: string;
                  paragraph: string;
                  paragraphColor: string;
                  linkText: string;
                  logo: { url: string };
                  backgroundImage: { url: string };
                },
                index?: React.Key | null
              ) => {
                const url = item?.linkText;
                const logo = item?.logo?.url;
                return (
                  <Grid
                    item
                    container
                    xs={12}
                    md={3}
                    key={index}
                    direction="column"
                    alignItems="center"
                  >
                    <DivisionCards
                      sx={{
                        bgcolor: item?.paragraphColor ?? 'common.white',
                        pt: '50',
                        pb: '30',
                        color: 'common.white',
                        backgroundImage: `url(${item?.backgroundImage?.url})`,
                        backgroundRepeat: 'no-repeat',
                        backgroundSize: 'cover',
                        boxShadow: '4',
                        '&:hover': {
                          backgroundImage: 'none'
                        }
                      }}
                      pt={50}
                      pb={30}
                      color="common.white"
                      boxShadow={4}
                      onMouseEnter={() => handleMouseEnter(index)}
                      onMouseLeave={() => setShowText('')}
                    >
                      {showText === index ? (
                        <Grid>
                          <Typography
                            display="block"
                            pl="30"
                            fontWeight={500}
                            fontSize="35px"
                            lineHeight="35px"
                            fontFamily="Helvetica Neue"
                            textAlign="left"
                          >
                            {item?.title}
                          </Typography>
                          <Typography
                            display="block"
                            fontWeight={400}
                            fontSize="18px"
                            lineHeight="24.5px"
                            fontFamily="Helvetica Neue"
                            pl="2px"
                            pt={2}
                            textAlign="left"
                          >
                            {item?.paragraph}
                          </Typography>

                          <Typography
                            textAlign="left"
                            pt="20px"
                            pb="20px"
                            sx={{ textDecoration: 'underline' }}
                          >
                            <Link color="common.white" href="/location-search">
                              {url}
                            </Link>
                          </Typography>
                        </Grid>
                      ) : (
                        <Grid container direction="column" alignItems="center">
                          <Grid item pb="25px">
                            <Typography display="initial">
                              <Image
                                src={logo}
                                alt="Logo"
                                fallback={notfound}
                              />
                            </Typography>
                          </Grid>
                          <Grid item>
                            <Typography
                              fontWeight={400}
                              fontSize="35px"
                              lineHeight="35px"
                              fontFamily="Helvetica Neue"
                              display="block"
                            >
                              {item?.title}
                            </Typography>
                          </Grid>
                        </Grid>
                      )}
                    </DivisionCards>
                  </Grid>
                );
              }
            )}
          </Grid>
        </Box>
      </Container>

      <CenterContainer>
        {data.aboutUs.infoSectionsCollection?.items.map(
          (
            item: { paragraph: string; text: string; title: string },
            i?: React.Key | null
          ) => {
            const paragraph = item.paragraph;
            const text = item.text;
            return (
              <InfoSectionsCollection key={`division-${i}`}>
                <MissionTitle
                  fontFamily={!i ? 'Helvetica Neue' : 'Permanent Marker'}
                  pl={10}
                >
                  {item.title}
                </MissionTitle>
                <Paragraph pb={2} pt={3}>
                  {paragraph}
                </Paragraph>
                <MissionText>{text}</MissionText>
              </InfoSectionsCollection>
            );
          }
        )}
      </CenterContainer>

      <Box
        textAlign="center"
        lineHeight="24px"
        whiteSpace="break-spaces"
        color="common.black"
        bgcolor="secondary04.main"
        padding="40px"
      >
        <Grid>
          <BrandTitle>{data.aboutUs.brands.title}</BrandTitle>
        </Grid>
        <Grid container>
          {data.aboutUs.brands?.listCollection?.items.map(
            (
              item: {
                url: string;
                logo: { url: string };
                statesList: [];
                urlText: string;
              },
              i?: React.Key | null
            ) => {
              const url = item.url;
              const statesList = item.statesList;
              const logo = item.logo.url;
              const urlText = item.urlText;
              return (
                <Grid
                  item
                  container
                  xs={12}
                  md={4}
                  key={`brand-${i}`}
                  direction="column"
                  alignItems="center"
                  justifyContent="center"
                  data-testid={`brand-company-${i}`}
                >
                  <Grid
                    item
                    container
                    direction="column"
                    alignItems="center"
                    justifyContent="center"
                    py={5}
                  >
                    <Link
                      href={generateCompanyUrl(
                        url,
                        port,
                        Configuration.environment,
                        !!activeFeatures?.includes('WATERWORKS')
                      )}
                      data-testid={`brand-url-${i}`}
                    >
                      <Image
                        src={logo}
                        alt="Logo"
                        data-testid={`brand-logo-${i}`}
                        fallback={notfound}
                      />
                    </Link>
                    <Grid
                      alignItems="center"
                      justifyContent="center"
                      data-testid={`brand-countries-${i}`}
                      pt={2}
                    >
                      <Typography>{statesList.join(', ')}</Typography>
                    </Grid>
                    <Grid item pt={3}>
                      <Link
                        href={generateCompanyUrl(
                          url,
                          port,
                          Configuration.environment,
                          !!activeFeatures?.includes('WATERWORKS')
                        )}
                      >
                        <Button
                          variant="primary"
                          data-testid={`brand-shopnow-${i}`}
                        >
                          <Typography data-testid={`brand-url-${i}`}>
                            {urlText}
                          </Typography>
                        </Button>
                      </Link>
                    </Grid>
                  </Grid>
                </Grid>
              );
            }
          )}
        </Grid>
      </Box>
      <Grid container sx={{ backgroundColor: 'primary.main' }}>
        <Grid item xs={12} md={12} lg={7} alignItems="flex-end">
          <NeedAssistanceImageGrid>
            <Image
              src={data.aboutUs.needAssistanceBlock?.backgroundImage.url}
              alt="Background"
              sx={{ maxWidth: '100%', maxHeight: '60%' }}
              fallback={notfound}
            />
          </NeedAssistanceImageGrid>
        </Grid>
        <Grid
          item
          xs={12}
          md={5}
          alignItems="flex-end"
          color="common.white"
          textAlign="center"
        >
          <Grid container justifyContent="center">
            <NeedAssistanceImage
              fallback={notfound}
              src={data.aboutUs.needAssistanceBlock?.logo.url}
              alt="Logo"
            />
          </Grid>
          <NeedAssistanceTitle>
            {data.aboutUs.needAssistanceBlock?.title}
          </NeedAssistanceTitle>
          <NeedAssistanceParagraph>
            {data.aboutUs.needAssistanceBlock?.paragraph}
          </NeedAssistanceParagraph>
          <Grid container justifyContent="center" pb="50px">
            <Link href="/support">
              <ContactUsButton
                border={2}
                borderLeft={2}
                borderRight={2}
                marginTop="20px"
                margin={5}
              >
                {data.aboutUs.needAssistanceBlock?.linkText}
              </ContactUsButton>
            </Link>
          </Grid>
        </Grid>
      </Grid>
    </Box>
  );
};

export default AboutUs;
