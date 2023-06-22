import { Box, styled } from '@dialexa/reece-component-library';

export const BoxStyled = styled(Box)(({ theme }) => ({
  marginLeft: 30,
  [theme.breakpoints.down('md')]: {
    marginLeft: 10
  }
}));
