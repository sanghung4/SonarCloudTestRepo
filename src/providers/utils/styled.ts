import { Drawer, styled } from '@dialexa/reece-component-library';

export const ListDrawerStyled = styled(Drawer)(({ theme }) => ({
  zIndex: 1200,
  backdropFilter: 'none',
  [theme.breakpoints.down('md')]: {
    zIndex: 1600
  }
}));
