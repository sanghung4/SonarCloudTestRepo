import { styled, Box, BoxProps } from '@dialexa/reece-component-library';

interface StyledInfoChipContainerProps extends BoxProps {
  contained?: boolean;
  backgroundColor?: string;
}

export const InfoChipContainer = styled(Box, {
  shouldForwardProp: (prop) =>
    prop !== 'contained' && prop !== 'backgroundColor'
})<StyledInfoChipContainerProps>(({ backgroundColor, contained, theme }) => ({
  padding: theme.spacing(0, contained ? 1.5 : 0),
  minHeight: theme.spacing(3),
  borderRadius: contained ? theme.spacing(3) : undefined,
  backgroundColor: contained ? backgroundColor : undefined,
  display: 'flex',
  alignItems: 'center',
  width: 'fit-content',
  fontWeight: '500',
  svg: {
    height: 20,
    width: 20,
    marginRight: theme.spacing(0.5)
  }
}));
