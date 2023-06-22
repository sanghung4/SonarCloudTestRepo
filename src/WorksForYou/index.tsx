import React from 'react';

import {
  Container,
  Grid,
  useScreenSize,
  List,
  ListItemText,
  Typography
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import useDocumentTitle from 'hooks/useDocumentTitle';

import BackgroundImage from 'images/WorksForYou_hero.png';

const WorksForYou = () => {
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();
  useDocumentTitle(t('common.worksForYou'));

  const background = {
    backgroundImage: `url(${BackgroundImage})`,
    backgroundColor: '#002a4e',
    backgroundPosition: isSmallScreen ? 'right' : 'top right',
    backgroundRepeat: 'no-repeat',
    backgroundSize: isSmallScreen ? 'cover' : 'contain, cover',
    height: isSmallScreen ? 275 : 533,
    width: '100%'
  };

  const txt = [
    t('worksForYou.paragraph1'),
    t('worksForYou.paragraph2'),
    t('worksForYou.paragraph3'),
    t('worksForYou.paragraph4'),
    t('worksForYou.paragraph5')
  ];

  return (
    <Grid sx={{ w: '100%', background: 'white' }}>
      <Grid item container sx={background}>
        <Container>
          <Grid
            item
            container
            direction="row"
            wrap="nowrap"
            sx={{ width: '100%', height: '100%', py: 2, position: 'relative' }}
          >
            <Grid
              item
              container
              sx={{
                height: '100%',
                py: isSmallScreen ? '40px' : '60px',
                px: isSmallScreen ? '15px' : 0,
                mt: isSmallScreen ? '25px' : 0
              }}
              direction="column"
              justifyContent="flex-end"
              alignItems="flex-start"
            >
              <Typography
                color={'primary.contrastText'}
                variant={isSmallScreen ? 'h4' : 'h1'}
                data-testid={`works-for-you-title-0`}
              >
                {t('worksForYou.title')}
              </Typography>
              <Typography
                color={'primary.contrastText'}
                variant={isSmallScreen ? 'h4' : 'h1'}
                data-testid={`works-for-you-title-1`}
              >
                {t('worksForYou.titleContinued')}
              </Typography>
            </Grid>
          </Grid>
        </Container>
      </Grid>
      <Grid
        item
        container
        sx={{ p: isSmallScreen ? '20px' : '60px', py: isSmallScreen ? 6 : 10 }}
      >
        <Container>
          <Grid item container direction={isSmallScreen ? 'column' : 'row'}>
            <Grid
              item
              container
              direction="row"
              justifyContent="center"
              sx={{
                width: isSmallScreen ? '100%' : '30%',
                px: isSmallScreen ? 5 : 0
              }}
            >
              <Typography
                variant={isSmallScreen ? 'h3' : 'h1'}
                color={'primary.dark'}
                textAlign={isSmallScreen ? 'center' : 'initial'}
                data-testid={`works-for-you-building-a-future`}
              >
                {t('worksForYou.buildingAFuture')}
              </Typography>
            </Grid>
            <Grid
              item
              container
              sx={{
                width: isSmallScreen ? '100%' : '70%',
                px: isSmallScreen ? 0 : 6
              }}
            >
              <List>
                {txt.map((text, index) => (
                  <ListItemText
                    sx={{ padding: '10px' }}
                    key={`list-item-txt-${index}`}
                  >
                    <Typography
                      variant="body1"
                      color={'primary.main'}
                      textAlign={isSmallScreen ? 'center' : 'initial'}
                      data-testid={`works-for-you-paragraph-${index}`}
                    >
                      {text}
                    </Typography>
                  </ListItemText>
                ))}
              </List>
            </Grid>
          </Grid>
        </Container>
      </Grid>
    </Grid>
  );
};

export default WorksForYou;
