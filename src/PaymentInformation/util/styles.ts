import { Card, Grid, ListItem, styled } from '@dialexa/reece-component-library';

export const PaymentInfoContainerCard = styled(Card)(({ theme }) => ({
  paddingLeft: theme.spacing(4),
  paddingRight: theme.spacing(4),
  paddingTop: theme.spacing(3),
  paddingBottom: theme.spacing(3),
  marginBottom: theme.spacing(3),
  [theme.breakpoints.down('md')]: {
    paddingLeft: theme.spacing(3),
    paddingRight: theme.spacing(3),
    paddingTop: theme.spacing(2),
    paddingBottom: theme.spacing(2)
  }
}));

export const PaymentInfoCaptionGrid = styled(Grid)(({ theme }) => ({
  justifyContent: 'flex-start',
  alignContent: 'center',
  [theme.breakpoints.down('md')]: { justifyContent: 'center' }
}));

export const PaymentInfoAddCardGrid = styled(Grid)(({ theme }) => ({
  justifyContent: 'flex-end',
  [theme.breakpoints.down('md')]: { justifyContent: 'center' }
}));

export const PaymentInfoListItem = styled(ListItem)(() => ({
  display: 'flex',
  '&:hover': { bgcolor: 'transparent' }
}));
