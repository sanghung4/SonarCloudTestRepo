import {
  FormControlLabel,
  RadioGroup,
  styled
} from '@dialexa/reece-component-library';

export const StyledRadioGroup = styled(RadioGroup)(({ theme }) => ({
  height: theme.spacing(5)
})) as typeof RadioGroup;

export const StyledRadioControlLabel = styled(FormControlLabel)(() => ({
  marginLeft: 0,
  '.MuiRadio-root': {
    marginRight: 12
  }
}));
