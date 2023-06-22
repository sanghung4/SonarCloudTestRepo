import {
  styled,
  Autocomplete as MuiAutocomplete,
  Box as MuiBox,
  Button as MuiButton,
  Card as MuiCard,
  FormControlLabel as MuiFormControlLabel,
  Grid as MuiGrid,
  Input as MuiInput,
  InputLabel as MuiInputLabel,
  RadioGroup as MuiRadioGroup,
  Select as MuiSelect,
  Typography as MuiTypography
} from '@dialexa/reece-component-library';

/******************************/
/* Styled Components          */
/******************************/
export const InputLabelWrapper = styled(MuiGrid)(({ theme }) => ({
  display: 'flex',
  justifyContent: 'flex-end',
  [theme.breakpoints.down('md')]: {
    justifyContent: 'flex-start'
  }
}));

export const PageHeaderCard = styled(MuiBox)(({ theme }) => ({
  padding: theme.spacing(3, 4),
  marginBottom: theme.spacing(3),
  [theme.breakpoints.down('md')]: {
    padding: theme.spacing(2, 3)
  }
})).withComponent(MuiCard);

export const JobFormContainer = styled(MuiBox)(({ theme }) => ({
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  padding: theme.spacing(6, 0),
  marginBottom: theme.spacing(3),
  [theme.breakpoints.down('md')]: {
    padding: theme.spacing(3, 2.5, 4)
  }
})).withComponent(MuiCard);

export const JobFormLinkContainer = styled(MuiBox)(({ theme }) => ({
  padding: theme.spacing(3, 5),
  marginBottom: theme.spacing(3),
  [theme.breakpoints.down('md')]: {
    padding: theme.spacing(2)
  }
})).withComponent(MuiCard);

export const JobStepperWrapper = styled(MuiBox)(({ theme }) => ({
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  maxWidth: theme.spacing(86),
  width: '100%',
  marginBottom: theme.spacing(4),
  '.MuiStepper-horizontal': {
    width: '148%'
  }
}));

export const FormSectionLabel = styled(MuiTypography)(({ theme }) => ({
  color: theme.palette.primary.light,
  fontSize: 22,
  fontWeight: theme.typography.h5.fontWeight
}));

export const FormSectionDetails = styled(MuiTypography)(({ theme }) => ({
  color: theme.palette.primary.light,
  fontSize: 14,
  marginTop: theme.spacing(1.25)
}));

export const FormRequiredText = styled(MuiTypography)(({ theme }) => ({
  color: theme.palette.orangeRed.main,
  fontSize: 14,
  marginTop: theme.spacing(1.25)
}));

export const FormInputLabelContainer = styled(MuiInputLabel)(({ theme }) => ({
  marginBottom: 0,
  textAlign: 'right',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'flex-end',
  marginRight: theme.spacing(3),
  whiteSpace: 'normal',
  minHeight: theme.spacing(5),
  [theme.breakpoints.down('md')]: {
    textAlign: 'left',
    justifyContent: 'flex-start'
  }
}));

export const FormFieldContainer = styled(MuiGrid)(({ theme }) => ({
  padding: theme.spacing(1.5, 0),
  [theme.breakpoints.down('md')]: {
    padding: theme.spacing(1, 0)
  }
}));

export const FormTextField = styled(MuiInput)(({ theme }) => ({
  padding: theme.spacing(1, 2),
  textOverflow: 'ellipsis'
}));

export const FormSection = styled(MuiBox)(({ theme }) => ({
  padding: theme.spacing(0, 0, 2.5, 0)
}));

export const FormSectionFields = styled(MuiBox)(() => ({
  paddingTop: 12,
  paddingBottom: 12
}));

export const FormActionContainer = styled(MuiBox)(({ theme }) => ({
  display: 'flex',
  justifyContent: 'center',
  marginTop: theme.spacing(3),
  [theme.breakpoints.down('md')]: {
    marginTop: 0
  }
}));

export const FormSelect = styled(MuiSelect)(({ theme }) => ({
  color: theme.palette.primary03.main,
  '.MuiSelect-select': {
    padding: theme.spacing(1, 7, 1, 2)
  },

  '& .MuiSelect-icon': {
    color: theme.palette.primary03.main,
    right: theme.spacing(2)
  },
  '& .MuiSelect-select': {
    '&:focus': {
      backgroundColor: 'initial'
    }
  }
}));

export const FormSelectInnerInput = styled(MuiInput)(() => ({
  padding: 100
}));

export const FormRadioInputDetails = styled(MuiTypography)(({ theme }) => ({
  fontSize: 14,
  marginRight: theme.spacing(3),
  color: theme.palette.mediumGray.main,
  textAlign: 'right',
  [theme.breakpoints.down('md')]: {
    textAlign: 'left'
  }
}));

export const FormRadioControlLabel = styled(MuiFormControlLabel)(() => ({
  marginLeft: 0,
  '.MuiRadio-root': {
    marginRight: 12
  }
}));

export const FormRadioGroup = styled(MuiRadioGroup)(({ theme }) => ({
  height: theme.spacing(5)
}));

export const ChooseFileInputLabel = styled(MuiInputLabel)(({ theme }) => ({
  margin: theme.spacing(0, 4, 0, 0)
}));

export const RemoveFileButton = styled(MuiButton)(({ theme }) => ({
  textDecoration: 'underline',
  color: theme.palette.primary02.main,
  fontWeight: 500
}));

export const FormInputErrorText = styled(MuiTypography)(({ theme }) => ({
  paddingTop: theme.spacing(0.5),
  color: theme.palette.orangeRed.main,
  ...theme.typography.caption,
  [theme.breakpoints.down('md')]: {
    paddingTop: 0
  }
}));

export const FormAutocomplete = styled(MuiAutocomplete)(({ theme }) => ({
  '& .MuiFormControl-root': {
    marginTop: 0,
    padding: 0
  },
  '& .MuiOutlinedInput-root .MuiAutocomplete-input': {
    padding: 0,
    height: theme.spacing(3)
  },
  '& .MuiAutocomplete-inputRoot': {
    paddingTop: theme.spacing(1),
    paddingBottom: theme.spacing(1),
    paddingLeft: theme.spacing(2)
  }
}));

export const FormBackButton = styled(MuiButton)(({ theme }) => ({
  textDecoration: 'underline',
  color: theme.palette.primary02.main,
  fontWeight: 500,
  marginRight: theme.spacing(2)
}));

export const FormFileInputErrorText = styled(MuiTypography)(({ theme }) => ({
  display: 'flex',
  alignItems: 'center',
  fontSize: 14,
  color: theme.palette.orangeRed.main,
  svg: {
    marginRight: theme.spacing(1.5)
  }
}));

/******************************/
/* Style Objects (sx)         */
/******************************/
export const formSelectMenuStyles = {
  maxHeight: 240,
  '& .MuiMenu-paper': {
    border: 1,
    borderColor: 'lightGray.main',
    boxShadow: 'none',
    mt: 1
  },
  '& .MuiMenu-list': {
    py: 2,
    '& .MuiMenuItem-root': {
      py: 1,
      px: 2
    }
  }
};
