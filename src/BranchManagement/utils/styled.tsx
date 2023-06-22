import {
  styled,
  Box as MuiBox,
  Card as MuiCard
} from '@dialexa/reece-component-library';

export const PageHeaderCard = styled(MuiBox)(({ theme }) => ({
  padding: theme.spacing(3, 4),
  marginBottom: theme.spacing(3),
  [theme.breakpoints.down('md')]: {
    padding: theme.spacing(2, 3)
  }
})).withComponent(MuiCard);

export const PageContentCard = styled(MuiBox)(({ theme }) => ({
  display: 'flex',
  flexDirection: 'column',
  padding: theme.spacing(6),
  marginBottom: theme.spacing(3),
  [theme.breakpoints.down('md')]: {
    padding: theme.spacing(2, 3)
  }
})).withComponent(MuiCard);
