import { styled, Tooltip } from '@dialexa/reece-component-library';
import { tooltipClasses, TooltipProps } from '@mui/material';

export const MobileToolTip = styled(({ className, ...props }: TooltipProps) => (
  <Tooltip {...props} classes={{ popper: className }} />
))(({ theme }) => ({
  [`& .${tooltipClasses.tooltip}`]: {
    fontSize: 15,
    maxWidth: 400,
    mb: 10,
    paddingLeft: 20,
    paddingRight: 20,
    paddingTop: 15,
    paddingBottom: 15,
    backgroundColor: theme.palette.grey[800]
  }
}));

export const StyledToolTip = styled(({ className, ...props }: TooltipProps) => (
  <Tooltip {...props} classes={{ popper: className }} />
))(({ theme }) => ({
  [`& .${tooltipClasses.tooltip}`]: {
    fontSize: 12,
    paddingTop: 10,
    paddingBottom: 10,
    backgroundColor: theme.palette.grey[800]
  }
}));
