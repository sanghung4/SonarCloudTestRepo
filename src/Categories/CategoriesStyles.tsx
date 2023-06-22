import {
  List,
  ListItemButton,
  ListItemText,
  alpha,
  styled
} from '@dialexa/reece-component-library';

export const ListStyled = styled(List)(({ theme }) => ({
  overflowY: 'scroll',
  [theme.breakpoints.up('sm')]: {
    padding: theme.spacing(1.5, 0),
    borderRight: '1px solid #e4e4e4',
    width: '25%',
    flexShrink: 0
  }
}));

export const ListItemButtonStyled = styled(ListItemButton)(({ theme }) => ({
  [theme.breakpoints.up('sm')]: {
    marginTop: 6,
    marginBottom: 6,
    paddingTop: 4,
    paddingBottom: 4,
    '&.Mui-focusVisible': {
      backgroundColor: alpha(theme.palette.primary02.main, 0.1)
    },
    '&.Mui-selected': {
      backgroundColor: alpha(theme.palette.primary02.main, 0.1),
      borderRight: `5px solid ${theme.palette.primary02.main}`,
      '&:hover': {
        backgroundColor: alpha(theme.palette.primary02.main, 0.1)
      }
    }
  },
  '&.MuiListItemButton-gutters': {
    [theme.breakpoints.up('md')]: {
      paddingLeft: 12,
      paddingRight: 12
    },
    [theme.breakpoints.down('md')]: {
      paddingLeft: 32,
      paddingRight: 32
    }
  }
}));

export const ListItemTextStyled = styled(ListItemText)(({ theme }) => ({
  '& .MuiListItemText-primary': {
    [theme.breakpoints.down('md')]: {
      color: theme.palette.text.primary
    }
  }
}));
