import {
  Input,
  InputLabel,
  styled,
  Theme
} from '@dialexa/reece-component-library';

const dividerBoxWidth = (theme: Theme) => `calc(100% + ${theme.spacing(4)})`;

const InputStyled = styled(Input)(({ theme }) => ({
  marginTop: theme.spacing(2),
  [theme.breakpoints.down('md')]: {
    marginTop: theme.spacing(1)
  }
}));

const InputLabelStyled = styled(InputLabel)(({ theme }) => ({
  marginBottom: 0,
  [theme.breakpoints.down('md')]: {
    marginTop: theme.spacing(1)
  }
}));

const stateSelectOpen = {
  '& .MuiSelect-select, & .MuiSelect-icon:not(.MuiSelect-iconOpen)': {
    color: 'mediumGray.main'
  }
};

export const creditCardFormStyles = {
  dividerBoxWidth,
  InputLabelStyled,
  InputStyled,
  stateSelectOpen
};
