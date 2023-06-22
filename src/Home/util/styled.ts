import { styled, Typography } from '@dialexa/reece-component-library';

import landingBG from 'images/home/landing_bg.jpg';
import { ReactComponent as MaxLogo } from 'images/home/max_white.svg';

export const ScheduleAppointmentTypography = styled(Typography)(
  ({ theme }) => ({
    color: theme.palette.primary.main,
    fontSize: 24,
    fontWeight: 600,
    marginBottom: theme.spacing(2),
    [theme.breakpoints.down('md')]: {
      fontSize: 18
    }
  })
);

export const MaxLogoStyled = styled(MaxLogo)(({ theme }) => ({
  display: 'inline-block',
  position: 'relative',
  [theme.breakpoints.up('md')]: {
    top: 16,
    left: 4
  },
  [theme.breakpoints.down('md')]: {
    top: 5
  }
}));

export const landerBackground = {
  backgroundImage: `linear-gradient(rgba(0, 0, 0, 0.1) 0%, rgba(0, 0, 0, 0.5) 100%), url(${landingBG})`,
  backgroundPosition: 'top',
  backgroundRepeat: 'no-repeat',
  backgroundSize: 'cover'
};
