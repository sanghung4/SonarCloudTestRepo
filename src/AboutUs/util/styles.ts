import { Box, Grid, Image, styled } from '@dialexa/reece-component-library';

export const MainBackground = styled(Box)(({ theme }) => ({
  width: '100%',
  backgroundPosition: 'center',
  backgroundRepeat: 'no-repeat',
  backgroundSize: 'cover',
  display: 'flex',
  flexDirection: 'column',
  justifyContent: 'flex-end',
  height: '75vh',
  padding: '5px',
  color: theme.palette.primary.contrastText,
  fontSize: '125px',
  lineHeight: '129px',
  marginTop: 0,
  fontFamily: 'Helvetica Neue',
  fontWeight: 500,
  fontStyle: 'medium',
  textAlign: 'initial',
  [theme.breakpoints.down('md')]: {
    backgroundPosition: '70% 65%',
    height: '50vh',
    padding: '1px',
    fontSize: '59px',
    textAlign: 'left',
    lineHeight: '60px',
    marginRight: '70px'
  }
}));
export const CenterContainer = styled(Grid)(({ theme }) => ({
  marginTop: '40px',
  display: 'flex',
  width: '100%',
  padding: '50px 0',
  flexDirection: 'column',
  justifyContent: 'center',
  [theme.breakpoints.down('md')]: {
    marginTop: '20px',
    marginBottom: '10px',
    padding: '10px',
    alignItems: 'center'
  }
}));

export const TitleBox = styled(Box)(({ theme }) => ({
  color: 'common.white',
  fontSize: '80px',
  marginTop: -15,
  textAlign: 'initial',
  [theme.breakpoints.down('md')]: {
    fontSize: '59px',
    marginTop: -2,
    textAlign: 'left'
  }
}));
export const DivisionCards = styled(Box)(({ theme }) => ({
  display: 'flex',
  flexDirection: 'row',
  alignItems: 'center',
  justifyContent: 'center',
  height: '465px',
  width: '287px',
  textAlign: 'center',
  lineHeight: '24px',
  whiteSpace: 'break-spaces',
  padding: '15px',
  borderRadius: '3px',
  [theme.breakpoints.down('md')]: {
    flexDirection: 'column',
    height: '415px',
    width: '337px',
    paddingLeft: '21px',
    borderRadius: '4px'
  }
}));

export const InfoSectionsCollection = styled(Box)(({ theme }) => ({
  width: '950px',
  fontFamily: 'Helvetica Neue',
  textAlign: 'center',
  alignSelf: 'center',
  [theme.breakpoints.down('md')]: {
    width: '350px',
    paddingTop: 10
  }
}));

export const Title = styled(Box)(({ theme }) => ({
  fontFamily: 'Helvetica Neue',
  fontSize: '72px',
  paddingTop: 4,
  paddingBottom: 5,
  fontWeight: 500,
  lineHeight: '74px',
  color: theme.palette.primary.main,
  [theme.breakpoints.down('md')]: {
    fontSize: 40,
    lineHeight: '41.67px'
  }
}));
export const MissionTitle = styled(Box)(({ theme }) => ({
  fontSize: '72px',
  paddingTop: 4,
  paddingBottom: 35,
  fontWeight: 500,
  lineHeight: '74px',
  color: theme.palette.primary.main,
  width: '950px',
  textAlign: 'center',
  paddingLeft: 110,
  paddingRight: 110,
  [theme.breakpoints.down('md')]: {
    fontSize: '40px',
    lineHeight: '41.67px',
    width: '100%',
    paddingLeft: 3,
    paddingRight: 3,
    paddingTop: 0
  }
}));

export const Paragraph = styled(Box)(({ theme }) => ({
  fontStyle: 'normal',
  fontSize: 35,
  fontWeight: 400,
  margin: 1,
  lineHeight: '50px',
  color: theme.palette.primary.main,
  [theme.breakpoints.down('md')]: {
    fontSize: 21,
    lineHeight: '28px',
    paddingTop: '10px',
    paddingBottom: '1px'
  }
}));
export const MissionText = styled(Box)(({ theme }) => ({
  fontSize: '48px',
  fontWeight: 400,
  fontFamily: 'Permanent Marker',
  paddingBottom: '33px',
  paddingTop: '25px',
  color: theme.palette.primary.main,
  [theme.breakpoints.down('md')]: {
    fontSize: '30px'
  }
}));

export const DivisionsBox = styled(Box)(({ theme }) => ({
  marginTop: '40px',
  display: 'flex',
  width: '100%',
  padding: '50px 0',
  flexDirection: 'column',
  justifyContent: 'center',
  [theme.breakpoints.down('md')]: {
    marginTop: '40px',
    display: 'flex',
    width: '100%',
    padding: 0,
    flexDirection: 'column',
    alignItems: 'center'
  }
}));

export const BrandTitle = styled(Box)(({ theme }) => ({
  fontFamily: 'Helvetica Neue',
  fontWeight: 500,
  fontSize: '72px',
  color: theme.palette.primary.main,
  padding: 50,
  [theme.breakpoints.down('md')]: {
    fontSize: '40px',
    padding: 40
  }
}));

export const NeedAssistance = styled(Grid)(({ theme }) => ({
  color: 'common.white',
  fontSize: '80px',
  marginTop: -6,
  textAlign: 'initial',
  [theme.breakpoints.down('md')]: {
    fontSize: '59px',
    marginTop: -2,
    textAlign: 'left'
  }
}));

export const NeedAssistanceImage = styled(Image)(({ theme }) => ({
  width: '20%',
  height: '10%',
  paddingTop: '90px',
  paddingBottom: '5%',
  [theme.breakpoints.down('md')]: {
    paddingTop: '40px'
  }
}));

export const NeedAssistanceImageGrid = styled(Grid)(({ theme }) => ({
  display: 'block',
  flexDirection: 'row',
  alignItems: 'center',
  justifyContent: 'center',
  minHeight: 528,
  minWidth: 745,
  marginTop: '70px',
  [theme.breakpoints.down('md')]: {
    flexDirection: 'column',
    minHeight: 56,
    minWidth: 144,
    marginTop: 0
  }
}));

export const NeedAssistanceTitle = styled(Box)(({ theme }) => ({
  color: 'common.white',
  fontSize: 52,
  paddingTop: 4,
  paddingBottom: 2,
  fontWeight: 500,
  lineHeight: '47.5px',
  [theme.breakpoints.down('md')]: {
    fontSize: 42,
    paddingRight: 25,
    paddingLeft: 25
  }
}));

export const NeedAssistanceParagraph = styled(Box)(({ theme }) => ({
  color: 'common.white',
  fontSize: '35px',
  fontWeight: 400,
  margin: 1,
  paddingLeft: 25,
  paddingRight: 25,
  borderRadius: '3px',
  [theme.breakpoints.down('md')]: {
    paddingTop: '15px',
    fontSize: '21px'
  }
}));

export const ContactUsButton = styled(Box)(({ theme }) => ({
  color: theme.palette.primary.contrastText,
  fontSize: 38,
  fontWeight: 400,
  padding: 10,
  width: '256px',
  borderRadius: '3px',
  lineHeight: '50px',
  borderColor: 'common.white',
  [theme.breakpoints.down('md')]: {
    fontSize: 17,
    padding: 0,
    width: '176px'
  }
}));
