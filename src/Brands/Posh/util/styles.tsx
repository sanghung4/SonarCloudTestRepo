import { CSSProperties, useMemo } from 'react';

import {
  Divider,
  SxProps,
  Theme,
  Typography,
  TypographyProps,
  BoxProps,
  Box,
  styled,
  Grid
} from '@dialexa/reece-component-library';

import landerImg from 'images/posh/poshLanding.jpg';
import bathroomImg from 'images/posh/poshBathroom.jpg';
import kitchenImg from 'images/posh/poshKitchen.jpg';

import { ReactComponent as PoshTitleSvg } from 'images/posh/posh.svg';
import { ReactComponent as PoshLogoSvg } from 'images/posh/poshLogo.svg';

type PoshThemeList = {
  bathroomBox: SxProps<Theme>;
  button: SxProps<Theme>;
  button2: SxProps<Theme>;
  container: SxProps<Theme>;
  landerbg: SxProps<Theme>;
  kitchenBox: SxProps<Theme>;
};
export function usePoshStyles(isSmallScreen: boolean): PoshThemeList {
  return useMemo(
    () => ({
      bathroomBox: {
        backgroundImage: `url(${bathroomImg})`,
        height: isSmallScreen ? 260 : 500,
        backgroundSize: 'cover'
      },
      button: {
        color: 'common.black',
        boxShadow: `inset 0 0 0 2px black`,
        borderRadius: '4px',
        fontFamily: 'TT Commons',
        fontWeight: 700,
        fontSize: 16,
        padding: '10px 36px',
        '&:hover': {
          color: 'common.black',
          boxShadow: `inset 0 0 0 2px black`,
          borderRadius: '4px'
        }
      },
      button2: {
        color: 'common.white',
        boxShadow: `inset 0 0 0 2px white`,
        borderRadius: '4px',
        fontFamily: 'TT Commons',
        fontWeight: 700,
        fontSize: 16,
        padding: '10px 36px',
        '&:hover': {
          color: 'common.white',
          boxShadow: `inset 0 0 0 2px black`,
          borderRadius: '4px'
        }
      },
      container: {
        bgcolor: 'common.white',
        fontFamily: 'TT Commons',
        maxWidth: 1440
      },
      landerbg: {
        height: isSmallScreen ? 203 : 500,
        backgroundImage: `url(${landerImg})`,
        backgroundSize: 'cover'
      },
      kitchenBox: {
        backgroundImage: `url(${kitchenImg})`,
        height: isSmallScreen ? 260 : 500,
        backgroundSize: 'cover'
      }
    }),
    [isSmallScreen]
  );
}

export function PoshTypography(props: TypographyProps) {
  return <Typography fontFamily="TT Commons" {...props} />;
}

type PoshDividerProps = {
  mobile: boolean;
  sx?: SxProps<Theme>;
  style?: CSSProperties;
};
export function PoshDivider({ mobile, sx }: PoshDividerProps) {
  return (
    <Divider
      orientation={mobile ? undefined : 'vertical'}
      flexItem
      sx={{
        width: mobile ? '100%' : 2,
        height: mobile ? 2 : undefined,
        backgroundColor: 'common.white',
        marginRight: mobile ? undefined : '-2px',
        ...sx
      }}
    />
  );
}
export function PoshProductDivider({ mobile, style, sx }: PoshDividerProps) {
  return (
    <Divider
      flexItem
      sx={{
        width: '-webkit-fill-available',
        display: 'block',
        height: '1px',
        backgroundColor: 'mediumGray.main',
        mx: mobile ? 10 : 16,
        my: mobile ? 1.25 : 2,
        ...sx
      }}
      style={style}
    />
  );
}

const svgSizes = {
  title: {
    desktop: {
      width: 249,
      height: 131
    },
    mobile: {
      width: 100,
      height: 52
    }
  },
  landing: {
    desktop: {
      width: 496,
      height: 500
    },
    mobile: {
      width: 201,
      height: 203
    }
  }
} as const;
export function PoshTitle({ isMobile }: { isMobile: boolean }) {
  const { desktop, mobile } = svgSizes.title;
  return (
    <PoshTitleSvg
      style={{
        height: isMobile ? mobile.height : desktop.height,
        width: isMobile ? mobile.width : desktop.width
      }}
    />
  );
}
export function PoshLogo({ isMobile }: { isMobile: boolean }) {
  const { desktop, mobile } = svgSizes.landing;
  return (
    <PoshLogoSvg
      style={{
        height: isMobile ? mobile.height : desktop.height,
        width: isMobile ? mobile.width : desktop.width
      }}
    />
  );
}

export function FinishTypography(props: TypographyProps) {
  return <Typography fontFamily="Gilroy" {...props} />;
}

export function FinishButton(props: BoxProps) {
  return (
    <Box component="button" border="none" bgcolor="warmGray.main" {...props} />
  );
}

export const ProductGrid = styled(Grid)(({ theme }) => ({
  paddingLeft: 96,
  paddingRight: 96,
  paddingTop: 64,
  paddingBottom: 64,
  [theme.breakpoints.down('md')]: {
    paddingLeft: 0,
    paddingRight: 0,
    paddingTop: 48,
    paddingBottom: 48
  }
}));

export const ProductImageGrid = styled(Grid)(({ theme }) => ({
  paddingLeft: 48,
  paddingRight: 48,
  paddingTop: 0,
  paddingBottom: 0,
  [theme.breakpoints.down('md')]: {
    paddingLeft: 28,
    paddingRight: 28,
    paddingTop: 28,
    paddingBottom: 28
  }
}));

export const ProductImage = styled(Grid)(({ theme }) => ({
  paddingLeft: 128,
  paddingRight: 96,
  paddingTop: 0,
  paddingBottom: 0,
  [theme.breakpoints.down('md')]: {
    paddingLeft: 56,
    paddingRight: 48,
    paddingTop: 32,
    paddingBottom: 32
  }
}));

export const FinishesGrid = styled(Grid)(({ theme }) => ({
  paddingLeft: 24,
  paddingRight: 0,
  paddingTop: 24,
  [theme.breakpoints.down('md')]: {
    paddingLeft: 24
  }
}));

export const ChromeButton = styled(Box)(({ theme }) => ({
  paddingLeft: 24,
  paddingRight: 24,
  border: 'none',
  [theme.breakpoints.down('md')]: {
    paddingLeft: 6,
    paddingRight: 6
  }
}));

export const GoldButton = styled(Box)(({ theme }) => ({
  paddingLeft: 8,
  paddingRight: 8,
  border: 'none',
  [theme.breakpoints.down('md')]: {
    paddingLeft: 0,
    paddingRight: 0
  }
}));

export const BlackButton = styled(Box)(({ theme }) => ({
  paddingLeft: 12,
  paddingRight: 12,
  border: 'none',
  [theme.breakpoints.down('md')]: {
    paddingLeft: 0,
    paddingRight: 0
  }
}));
