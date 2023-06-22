import { Container, styled } from '@dialexa/reece-component-library';

export const ForgetPasswordContainer = styled(Container)(({ theme }) => ({
  paddingTop: theme.spacing(10),
  paddingBottom: theme.spacing(10),
  [theme.breakpoints.down('md')]: {
    paddingTop: theme.spacing(4),
    paddingBottom: theme.spacing(10)
  }
}));
