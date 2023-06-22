import {
  Box,
  Button,
  Grid,
  styled,
  Theme,
  List,
  ListItem,
  BoxProps,
  TextField
} from '@dialexa/reece-component-library';
import { CSSProperties } from 'react';

type TransitionStatuses =
  | 'entering'
  | 'entered'
  | 'exiting'
  | 'exited'
  | 'unmounted';

export const cartButtonStyles = {
  cartButtonSx: {
    fontSize: 14,
    lineHeight: '18px',
    fontWeight: 400,
    py: 1,
    mr: -3
  },
  CartLoaderComponent: styled(Box)(() => ({
    display: 'flex',
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    '& svg': {
      color: 'primary02.main'
    }
  }))
};

export const CompanyListButton = styled(Button)(() => ({
  fontSize: 12,
  lineHeight: 1.5,
  fontWeight: 400,
  py: 1,
  px: 1.5,
  '& .MuiButton-startIcon > svg': {
    height: 18,
    width: 18
  }
}));

export const desktopSubheaderStyles = {
  GridStyledLinks: styled(Grid)(({ theme }) => ({
    '& a': {
      color: theme.palette.text.primary,
      textDecoration: 'none',
      padding: theme.spacing(0.5, 1.5),
      marginRight: theme.spacing(1),
      '&.active': {
        color: theme.palette.primary02.main,
        backgroundColor: theme.palette.lighterBlue.main,
        borderRadius: theme.spacing(0.5)
      }
    }
  })),
  toolbarSx: (theme: Theme) => ({
    [theme.breakpoints.up('md')]: {
      minHeight: theme.spacing(7)
    },
    '& .MuiButton-text': {
      fontWeight: 400
    }
  })
};

export const indexStyles = (searchOpen: boolean, isSmallScreen: boolean) => ({
  toolbarSx: (theme: Theme) => ({
    borderBottom: 1,
    borderBottomColor: 'lighterGray.main',
    py: 2,
    transition: theme.transitions.create(['padding'], {
      duration: theme.transitions.duration.enteringScreen,
      easing: theme.transitions.easing.easeOut
    }),
    '& .MuiIconButton-root': {
      p: 0,
      '& svg': {
        height: 18,
        width: 18
      }
    },
    [theme.breakpoints.down('md')]: {
      minHeight: 'auto',
      border: 0,
      ...(searchOpen && {
        py: 3,
        borderBottom: 1,
        borderBottomColor: 'lighterGray.main',
        transition: theme.transitions.create(['padding'], {
          duration: theme.transitions.duration.leavingScreen,
          easing: theme.transitions.easing.easeIn
        })
      })
    }
  }),
  containerSx: (theme: Theme) => ({
    display: 'flex',
    alignItems: 'center',
    visibility: 'visible',
    transition: 'visibility 300ms linear 190ms',
    [theme.breakpoints.up('md')]: {
      px: 6
    },
    ...(searchOpen &&
      isSmallScreen && {
        visibility: 'hidden',
        transition: 'visibility 0ms linear 0ms'
      })
  })
});

export const mobileDrawerStyles = {
  drawerPaperSx: {
    right: 0,
    alignItems: 'flex-start'
  },
  listSx: {
    '& .MuiListItem-root': {
      py: 2,
      px: 4,
      '& .MuiListItemIcon-root': {
        minWidth: 'auto',
        mr: 3,
        color: 'primary.main'
      },
      '& .MuiListItemText-primary': {
        color: 'text.primary'
      }
    }
  }
};

export const ReeceLogo = styled('img')(({ theme }) => ({
  height: theme.spacing(2.25),
  [theme.breakpoints.up('md')]: {
    height: theme.spacing(6),
    padding: theme.spacing(1, 0)
  }
}));

export const switchAccountsStyles = {
  ChangeButton: styled(Button)(({ theme }) => ({
    [theme.breakpoints.up('sm')]: {
      paddingLeft: theme.spacing(1.5),
      paddingRight: theme.spacing(1.5)
    },
    [theme.breakpoints.down('md')]: {
      paddingLeft: 0
    }
  })),
  tooltipComponentProps: {
    tooltip: {
      sx: {
        maxWidth: 'none'
      }
    }
  }
};

export const userButtonStyles = {
  dropDownButtonSx: {
    py: 1,
    px: 1.5,
    '& .MuiButton-startIcon > svg': {
      height: 18,
      width: 18
    },
    '& .MuiButton-endIcon': {
      ml: 0.5,
      '& > svg': {
        height: 18,
        width: 18
      }
    }
  },
  MenuButton: styled(Button)(({ theme }) => ({
    paddingLeft: 0,
    paddingRight: 0,
    fontWeight: 400,
    justifyContent: 'flex-start',
    '& .MuiButton-startIcon': {
      marginRight: theme.spacing(3),
      '& > *:first-child': {
        height: 24,
        width: 24
      }
    }
  })),
  signInButtonSx: {
    fontSize: '12px',
    lineHeight: '18px',
    fontWeight: 400,
    py: 1,
    px: 1.5,
    '& .MuiButton-startIcon > svg': {
      height: 18,
      width: 18
    }
  }
};

export const SuggestionList = styled(List)(({ theme }) => ({
  width: '100%',
  '& .MuiListItem-dense': {
    paddingTop: theme.spacing(0.5),
    paddingBottom: theme.spacing(0.5)
  }
}));

export const SuggestionListItem = styled(ListItem)(({ theme }) => ({
  cursor: 'pointer',
  color: theme.palette.text.primary
}));

export const SuggestionListSubItem = styled(ListItem)(({ theme }) => ({
  cursor: 'pointer',
  color: theme.palette.text.primary,
  paddingLeft: theme.spacing(5)
}));

export const SkeletonListItem = styled(ListItem)(({ theme }) => ({
  paddingRight: theme.spacing(6),
  '&:nth-child(even)': {
    paddingRight: theme.spacing(3)
  }
}));

export const SearchTopResultContainer = styled(Box)(({ theme }) => ({
  display: 'flex',
  padding: theme.spacing(1, 2),

  '&:hover': {
    cursor: 'pointer',
    backgroundColor: theme.palette.lighterGray.main
  }
}));

const searchDrawerTransitions = (
  theme: Theme,
  status: TransitionStatuses
): CSSProperties => {
  switch (status) {
    case 'entering':
      return {
        visibility: 'visible',
        transition: theme.transitions.create(['height'], {
          duration: theme.transitions.duration.leavingScreen,
          easing: theme.transitions.easing.easeIn
        })
      };
    case 'entered':
      return {
        visibility: 'visible',
        transition: theme.transitions.create(['height'], {
          duration: theme.transitions.duration.leavingScreen,
          easing: theme.transitions.easing.easeIn
        })
      };
    case 'exiting':
      return {
        height: 0,
        transition: theme.transitions.create(['height'], {
          duration: theme.transitions.duration.enteringScreen,
          easing: theme.transitions.easing.easeOut
        }),
        visibility: 'visible'
      };
    case 'exited':
      return {
        height: 0,
        transition: theme.transitions.create(['height'], {
          duration: theme.transitions.duration.enteringScreen,
          easing: theme.transitions.easing.easeOut
        }),
        visibility: 'hidden'
      };
    case 'unmounted':
      return { height: 0, visibility: 'hidden' };
  }
};

export const SearchDrawerContainer = styled(Box, {
  shouldForwardProp: (prop) => prop !== 'offset' && prop !== 'status'
})<{ offset: number; status: TransitionStatuses } & BoxProps>(
  ({ theme, offset, status }) => ({
    display: 'flex',
    visibility: 'hidden',
    height: `calc(100% - ${offset}px)`,
    position: 'fixed',
    top: offset,
    bottom: 0,
    left: 0,
    right: 0,
    backgroundColor: theme.palette.background.paper,
    overflow: 'hidden',
    zIndex: theme.zIndex.drawer,
    ...searchDrawerTransitions(theme, status)
  })
);

export const DrawerBackButton = styled(Button)(({ theme }) => ({
  alignSelf: 'flex-start',
  marginBottom: theme.spacing(2),
  padding: theme.spacing(2),
  '& svg': {
    height: 20,
    width: 20
  }
}));

export const SearchTextField = styled(TextField)(({ theme }) => ({
  margin: 0,
  padding: 0,
  '& .MuiInputBase-root': {
    backgroundColor: theme.palette.lightestGray.main,
    borderColor: theme.palette.lighterGray.main,
    paddingRight: 0,
    borderRadius: '2px',
    borderWidth: 1,
    borderStyle: 'solid'
  },
  '& .MuiInputBase-input': {
    padding: theme.spacing(1.8, 0, 1.8, 2)
  },
  '& fieldset': {
    border: 0
  }
}));

export const SearchIconButton = styled(Button)(({ theme }) => ({
  padding: theme.spacing(1.5),
  minWidth: 'auto',

  '&.Mui-disabled': {
    backgroundColor: 'transparent',
    '&, & svg': {
      color: theme.palette.primary02.main
    }
  }
}));

export const SelectAccountsHeaderContainer = styled(Box)(({ theme }) => ({
  lineHeight: 1,
  display: 'flex',
  [theme.breakpoints.down('md')]: { marginLeft: 'auto', marginRight: 'auto' }
}));
