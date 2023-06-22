import {
  styled,
  Grid,
  Box,
  Divider,
  Typography
} from '@dialexa/reece-component-library';
import BigDogHeroImage from 'Brands/BigDog/images/BigDogHero.jpg';

export const HeaderBannerContainer = styled(Grid)(({ theme }) => ({
  display: 'flex',
  flexDirection: 'column',
  height: '100%',
  width: '50%',
  justifyContent: 'center',
  marginTop: 0,
  marginLeft: 55,
  [theme.breakpoints.down('md')]: {
    justifyContent: 'flex-start',
    marginTop: 50,
    marginLeft: 15
  }
}));

export const HeaderBanner = styled(Box)(({ theme }) => ({
  backgroundImage: `url(${BigDogHeroImage})`,
  backgroundPosition: 'center',
  backgroundRepeat: 'no-repeat',
  backgroundSize: 'contain, cover',
  width: '100%',
  height: 334,
  [theme.breakpoints.down('md')]: {
    backgroundPosition: 'center right 15%',
    backgroundSize: 'cover',
    height: 275
  }
}));

export const HeaderTitle = styled(Typography)(({ theme }) => ({
  fontSize: 48,
  color: theme.palette.primary.contrastText,
  lineHeight: 1.2,
  fontFamily: 'Gilroy',
  fontWeight: 800,
  [theme.breakpoints.down('md')]: {
    fontSize: 30
  }
}));

export const MissionDescriptionContainer = styled(Box)(({ theme }) => ({
  width: 213,
  height: 64,
  marginLeft: 16,
  marginTop: 9,
  [theme.breakpoints.down('md')]: {
    width: 168,
    height: 56,
    marginTop: 14
  }
}));

export const MissionHeadersTypography = styled(Typography)(({ theme }) => ({
  fontFamily: 'Metropolis',
  fontWeight: 700,
  fontSize: 20,
  lineHeight: 1.2,
  color: theme.palette.common.black,
  [theme.breakpoints.down('md')]: {
    fontSize: 18
  }
}));

export const MissionDescriptionTypography = styled(Typography)(({ theme }) => ({
  fontFamily: 'Gilroy',
  fontWeight: 400,
  fontSize: 14,
  lineHeight: 1.2,
  color: theme.palette.secondary02.main,
  [theme.breakpoints.down('md')]: {
    fontSize: 12
  }
}));

export const FeaturedItemsTypography = styled(Typography)(({ theme }) => ({
  fontFamily: 'Metropolis',
  fontWeight: 800,
  fontSize: 24,
  lineHeight: 1.2,
  textAlign: 'center',
  color: theme.palette.common.black,
  [theme.breakpoints.down('md')]: {
    fontSize: 18
  }
}));

export const BigDogDivider = styled(Divider)(({ theme }) => ({
  width: '100%',
  height: '16px',
  backgroundColor: theme.palette.bigDogBrand.main
}));

export const BigDogGradientDivider = styled(Divider)(({ theme }) => ({
  width: '100%',
  height: '16px',
  backgroundColor: theme.palette.bigDogBrand.main,
  background: `linear-gradient(90deg,${theme.palette.bigDogBrand.main} 15.89%, rgba(255, 81, 0, 0.33) 92%)`
}));
