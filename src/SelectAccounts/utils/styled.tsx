import { styled, Container, Button } from '@dialexa/reece-component-library';

export const PageContainer = styled(Container)(() => ({
  alignItems: 'center',
  height: '100%'
}));

export const LogoutButton = styled(Button)(({ theme }) => ({
  marginTop: theme.spacing(3),
  position: 'absolute'
}));

export const SelectAccountsContainer = styled(Container)(({ theme }) => ({
  minHeight: theme.spacing(25),
  display: 'flex',
  flexDirection: 'column',
  justifyContent: 'center',
  height: '100%',
  [theme.breakpoints.down('md')]: {
    marginBottom: theme.spacing(9),
    marginTop: theme.spacing(4)
  }
}));
