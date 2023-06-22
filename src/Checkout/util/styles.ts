import {
  InputLabel,
  styled,
  TableCell,
  Theme,
  Card,
  Box
} from '@dialexa/reece-component-library';

export const HeaderCell = styled(TableCell)(({ theme }) => ({
  fontWeight: 400,
  padding: theme.spacing(3, 6)
}));

export const itemStyles = {
  tableCellSxFn: (notAvailable: boolean) => ({
    borderBottom: notAvailable ? '0 none' : undefined
  }),
  tableCellOutOfStockSx: { pt: 0, pl: 15 }
};

export const deliveryMethodFormControlSx = (theme: Theme) => ({
  [theme.breakpoints.down('md')]: { ml: '11px' }
});

export const PaymentInputLabelStyled = styled(InputLabel)(({ theme }) => ({
  marginBottom: 0,
  [theme.breakpoints.down('md')]: {
    marginBottom: theme.spacing(1)
  }
}));

export const shipmentStyles = {
  formControlSx: (theme: Theme) => ({
    [theme.breakpoints.down('md')]: {
      marginLeft: 11
    }
  }),
  formLabelOneSx: (theme: Theme) => ({
    pb: 3,
    [theme.breakpoints.down('md')]: {
      alignItems: 'flex-start'
    }
  }),
  formLabelMultipleSx: (theme: Theme) => ({
    [theme.breakpoints.down('md')]: {
      alignItems: 'flex-start'
    }
  })
};

export const ProductCardComponent = styled(Card)(({ theme }) => ({
  marginBottom: theme.spacing(3),
  padding: theme.spacing(3, 4),
  [theme.breakpoints.down('md')]: {
    padding: theme.spacing(2, 3)
  }
}));

export const BackOrderWarningBox = styled(Box)(({ theme }) => ({
  marginLeft: theme.spacing(-4),
  marginTop: theme.spacing(-4),
  width: `calc(100% + ${theme.spacing(4)})`,
  [theme.breakpoints.down('md')]: {
    marginLeft: theme.spacing(0),
    marginTop: theme.spacing(3),
    width: `calc(100% + ${theme.spacing(0)})`
  }
}));
