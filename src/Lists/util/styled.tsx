import {
  Alert,
  alpha,
  Box,
  Button,
  Card,
  Grid,
  ListItemButton,
  styled,
  TextField,
  Typography
} from '@dialexa/reece-component-library';

/**
 * Lists (INDEX)
 */
export const AddToCartButtonContainer = styled(Grid)(({ theme }) => ({
  paddingLeft: 0,
  width: 182,
  [theme.breakpoints.down('md')]: {
    paddingLeft: theme.spacing(2),
    width: '100%'
  }
}));

export const CartButtonContainer = styled(Grid)(({ theme }) => ({
  marginTop: theme.spacing(2),
  justifyContent: 'flex-end',
  alignItems: 'center',
  [theme.breakpoints.down('md')]: {
    justifyContent: 'flex-start'
  }
}));

export const CreateListButton = styled(Button)(({ theme }) => ({
  width: theme.spacing(18.5),
  height: theme.spacing(5),
  borderRadius: 2,
  backgroundColor: theme.palette.primary02.main,
  ':hover': { backgroundColor: theme.palette.primary02.main }
}));

export const ListInfoCard = styled(Card)(({ theme }) => ({
  paddingLeft: theme.spacing(5),
  paddingRight: theme.spacing(5),
  paddingTop: theme.spacing(3),
  paddingBottom: theme.spacing(3),
  marginTop: theme.spacing(4),
  displayPrint: 'none',
  borderRadius: '10px 10px 0px 0px',
  [theme.breakpoints.down('md')]: {
    paddingLeft: theme.spacing(2),
    paddingRight: theme.spacing(2),
    paddingTop: theme.spacing(2),
    paddingBotton: theme.spacing(2)
  }
}));

export const ListItemsInfoCard = styled(Card)(({ theme }) => ({
  minHeight: 300,
  marginBottom: theme.spacing(3),
  '@media print': { boxShadow: 'none !important' },
  borderRadius: '0px 0px 10px 10px',
  [theme.breakpoints.down('md')]: { minHeight: 175 }
}));

export const ListsHeaderTypography = styled(Typography)(({ theme }) => ({
  fontSize: 39.1,
  fontFamily: 'Roboto',
  fontStyle: 'normal',
  fontWeight: 500,
  lineHeight: '46px',
  letterSpacing: '0.0075em',
  color: theme.palette.primary.main,
  align: 'left',
  [theme.breakpoints.down('md')]: { fontSize: 31.25, align: 'center' }
}));

export const MyListsHeaderContainer = styled(Grid)(({ theme }) => ({
  width: 'auto',
  [theme.breakpoints.down('md')]: { width: 186 }
}));

/**
 * ListActions
 */
export const ListActionsButtonContainer = styled(Grid)(({ theme }) => ({
  marginTop: 0,
  [theme.breakpoints.down('md')]: { marginTop: theme.spacing(2.5) }
}));

/**
 * ListDrawer
 */
export const ListDrawerContainer = styled(Box)(({ theme }) => ({
  display: 'flex',
  flexDirection: 'column',
  height: '100%',
  width: 380,
  [theme.breakpoints.down('md')]: {
    width: '100vw'
  }
}));

export const ListHardLimitAlert = styled(Alert)(({ theme }) => ({
  color: theme.palette.orangeRed.main,
  alignItems: 'center',
  border: `1px solid ${theme.palette.orangeRed.main}`,
  backgroundColor: theme.palette.waterWorksWarning.main,
  ...theme.typography.body2,
  '& .MuiAlert-icon': { color: theme.palette.error.dark }
}));

export const ListDrawerStickyBox = styled(Box)(({ theme }) => ({
  justifyContent: 'center',
  [theme.breakpoints.down('md')]: { position: 'sticky' }
}));

/**
 * ListDrawerItem
 */
export const ListDrawerItemContainer = styled(ListItemButton)(({ theme }) => ({
  padding: 0,
  margin: theme.spacing(0, 1),

  '&.Mui-selected, &:hover': {
    backgroundColor: alpha(theme.palette.primary02.main, 0.1)
  },
  '.MuiCheckbox-root': {
    paddingLeft: theme.spacing(1.5),
    paddingRight: 0
  },
  '.MuiListItemText-root': {
    marginY: theme.spacing(1.5)
  }
}));

/**
 * ListRenameDialog
 */
export const ListNameInput = styled(TextField)(({ theme }) => ({
  marginTop: 0,
  '& .Mui-disabled': {
    bgcolor: theme.palette.lighterGray.main,
    '& .MuiOutlinedInput-notchedOutline': {
      borderColor: theme.palette.lightGray.main
    }
  }
}));

/**
 * Upload
 */
export const ListsInfoTypography = styled(Typography)(({ theme }) => ({
  width: 526,
  height: theme.spacing(5),
  fontFamily: 'Roboto',
  fontWeight: 400,
  fontSize: 12,
  lineHeight: '20px',
  letterSpacing: '0.005em',
  marginTop: theme.spacing(1),
  color: theme.palette.text.secondary,
  [theme.breakpoints.down('md')]: { width: 'auto' }
}));
