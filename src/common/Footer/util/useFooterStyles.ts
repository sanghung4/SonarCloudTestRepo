import { useScreenSize, useTheme } from '@dialexa/reece-component-library';

export default function useFooterStyles() {
  const { isSmallScreen } = useScreenSize();
  const { breakpoints, spacing } = useTheme();

  return {
    appBarSx: {
      displayPrint: 'none',
      border: 1,
      borderColor: 'lighterGray.main',
      '& .MuiToolbar-root': { minHeight: 'auto', py: isSmallScreen ? 3 : 2 }
    },
    footerColumnGridProp: { md: 4, xs: 12, sm: 6, mx: isSmallScreen ? 6 : 0 },
    toolbarSx: {
      borderTop: '1px solid lightgrey',
      borderColor: 'lighterGray.main'
    },
    bottomBarSx: {
      '& hr': { my: 0, mx: 0.5, height: spacing(1.5) },
      '& nav': { [breakpoints.down('md')]: { mb: 2 } }
    },
    bottomBarUrlWrapperSx: {
      '& a': {
        [breakpoints.down('md')]: {
          fontSize: '0.625rem',
          lineHeight: '1.0125rem'
        }
      }
    }
  };
}
