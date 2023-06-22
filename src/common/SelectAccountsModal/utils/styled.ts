import { styled, Box } from '@dialexa/reece-component-library';

export const ModalContent = styled(Box)(({ theme }) => ({
  margin: theme.spacing(4, 11, 5),
  [theme.breakpoints.down('md')]: {
    margin: theme.spacing(8, 2)
  }
}));
