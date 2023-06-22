import {
  Autocomplete,
  Typography,
  styled
} from '@dialexa/reece-component-library';

export const FormInputErrorText = styled(Typography)(({ theme }) => ({
  paddingTop: theme.spacing(0.5),
  color: theme.palette.orangeRed.main,
  ...theme.typography.caption,
  [theme.breakpoints.down('md')]: {
    paddingTop: 0
  }
}));

export const StyledAutoComplete = styled(Autocomplete, {
  shouldForwardProp: (prop) => true
})(() => ({
  '.MuiInputBase-fullWidth.MuiInputBase-adornedEnd.MuiAutocomplete-inputRoot': {
    paddingRight: 40
  },
  '& .MuiFormControl-root': {
    marginTop: 0,
    padding: 0
  },
  '.MuiAutocomplete-clearIndicator': {
    display: 'none'
  }
})) as typeof Autocomplete;
