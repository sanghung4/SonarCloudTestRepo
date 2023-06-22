import { useContext, useRef } from 'react';

import {
  Box,
  Button,
  Card,
  ClickAwayListener,
  DayPicker,
  Grid,
  Grow,
  Hidden,
  Popper,
  TextField
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { DateRangeContext } from 'common/DateRange';
import InputMask from 'common/InputMask';
import { dateMask } from 'utils/masks';
import { statusOptions } from 'Invoices';

type Props = {
  popper?: boolean;
  dataTestId?: string;
  invoiceStatus?: string;
};

const popperModifiers = [
  {
    name: 'flip',
    options: {
      rootBoundary: 'document',
      enabled: false
    }
  }
];

function DateRangeInput(props: Props) {
  /**
   * Context
   */
  const ctx = useContext(DateRangeContext);

  /**
   * Custom hooks
   */
  const { t } = useTranslation();

  /**
   * Refs
   */
  const containerEl = useRef<HTMLDivElement | null>(null);

  /**
   * Callbacks
   */
  const onFromFocus = () => {
    ctx.setMonth(ctx.value?.from ?? ctx.month ?? new Date());
    ctx.setOpen(true);
  };
  const onToFocus = () => {
    ctx.setMonth(ctx.value?.to ?? ctx.month ?? new Date());
    ctx.setOpen(true);
  };
  const handleClear = () => {
    ctx.onClear && ctx.onClear();
    ctx.setOpen(false);
    ctx.setApplied && ctx.setApplied(false);
  };

  /**
   * Render
   */
  const RangeDayPicker = (
    <>
      <DayPicker
        mode="custom"
        month={ctx.month}
        modifiers={ctx.modifiers}
        onMonthChange={ctx.setMonth}
        onDayClick={ctx.handleDayClick}
        selected={ctx.selected}
        toDate={new Date()}
        id="date-range"
      />
      {!!ctx.onClear && (
        <Hidden mdDown>
          <Box display="flex" justifyContent="flex-end" mb={-3}>
            <Button
              variant="inline"
              onClick={handleClear}
              data-testid="clear-date-range-button"
            >
              {t('common.clear')}
            </Button>
          </Box>
        </Hidden>
      )}
    </>
  );

  const Input = (
    <>
      <Grid item xs>
        <form onSubmit={ctx.handleSubmit}>
          <TextField
            label={
              props.dataTestId === 'search-contracts-input'
                ? t('contracts.dateRange')
                : t('orders.dateRange')
            }
            placeholder="from"
            name="from"
            fullWidth
            focused={ctx.focused === 'from'}
            onFocus={onFromFocus}
            required={
              props.invoiceStatus === statusOptions.All ||
              props.invoiceStatus === statusOptions.Closed
            }
            helperText={
              (props.invoiceStatus === statusOptions.All ||
                props.invoiceStatus === statusOptions.Closed) &&
              !ctx.value?.from
                ? t('invoices.fromDateRequired')
                : ''
            }
            FormHelperTextProps={{
              sx(theme) {
                return { color: theme.palette.orangeRed.main };
              }
            }}
            inputProps={{
              'data-testid': 'range-from',
              ...dateMask,
              ...ctx.fromProps
            }}
            InputProps={{ inputComponent: InputMask as any }}
          />
        </form>
      </Grid>
      <Grid item xs>
        <form onSubmit={ctx.handleSubmit}>
          <TextField
            placeholder="to"
            name="to"
            fullWidth
            focused={ctx.focused === 'to'}
            onFocus={onToFocus}
            helperText={
              (props.invoiceStatus === statusOptions.All ||
                props.invoiceStatus === statusOptions.Closed) &&
              !ctx.value?.to
                ? t('invoices.toDateRequired')
                : ''
            }
            FormHelperTextProps={{
              sx(theme) {
                return { color: theme.palette.orangeRed.main };
              }
            }}
            inputProps={{
              'data-testid': 'range-to',
              ...dateMask,
              ...ctx.toProps
            }}
            InputProps={{ inputComponent: InputMask as any }}
          />
        </form>
      </Grid>
    </>
  );

  return props.popper ? (
    <ClickAwayListener
      onClickAway={() => {
        ctx.setOpen(false);
        ctx.setFocused(null);
      }}
    >
      <Grid container ref={containerEl} spacing={2}>
        {Input}
        <Popper
          id={ctx.open ? 'orders-range' : undefined}
          open={ctx.open}
          anchorEl={containerEl.current}
          placement="bottom"
          modifiers={popperModifiers}
          transition
        >
          {({ TransitionProps }) => (
            <Grow {...TransitionProps}>
              <Card sx={{ pb: 4 }}>{RangeDayPicker}</Card>
            </Grow>
          )}
        </Popper>
      </Grid>
    </ClickAwayListener>
  ) : (
    <>
      <Grid container spacing={2}>
        {Input}
      </Grid>
      <Grid container justifyContent="center">
        {RangeDayPicker}
      </Grid>
    </>
  );
}

export default DateRangeInput;
