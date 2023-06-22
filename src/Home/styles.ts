import { Grid, styled } from '@dialexa/reece-component-library';

export const MaXFeaturesContainer = styled(Grid)(({ theme }) => ({
  alignItems: 'center',
  justifyContent: 'center',
  [theme.breakpoints.down(1045)]: {
    marginLeft: -140
  },
  [theme.breakpoints.down('md')]: {
    marginLeft: 0
  }
}));
