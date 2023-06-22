import {
  createContext,
  useCallback,
  useEffect,
  useMemo,
  useRef,
  useState,
  FocusEvent,
  FormEvent,
  FormEventHandler,
  ReactNode,
  RefObject
} from 'react';

import {
  DateRange,
  DayClickEventHandler,
  Modifiers,
  MonthChangeEventHandler,
  useScreenSize
} from '@dialexa/reece-component-library';
import { noop } from 'lodash-es';
import { useLocation } from 'react-router-dom';

import {
  getPropsMemoLogic,
  handleDayClickCbLogic,
  handleSubmitLogic,
  modifiersMemoLogic
} from 'common/DateRange/util';
import { formatDate } from 'utils/dates';

/**
 * Usage
 *
 * This is a wrapper component that will contain either a
 * dialog or an input component, for example:
 *
 * const [filtersOpen, setFiltersOpen] = useState(false);
 * const [range, setRange = useState({
 *   from: subMonths(new Date(), 6),
 *   to: new Date()
 * });
 *
 * const clearRange = () => setRange({ from: undefined, to : undefined });
 *
 * return (
 *   <DateRage value={range} onChange={setRange} onClear={clearRange}>
 *     // component definition
 *
 *     // Show input on desktop
 *     <Hidden smDown>
 *       <Grid item md={3}>
 *         <DateRangeInput popper />
 *       </Grid>
 *     </Hidden>
 *
 *     // Show button to activate modal on mobile
 *     <Hidden mdUp>
 *       <Grid item xs={12}>
 *         <Button
 *           variant="secondary"
 *           onClick={() => setFiltersOpen(true)}
 *           fullWidth
 *         >
 *           {t('common.filter')}
 *           {appliedRange?.from || appliedRange?.to ? ' (1)' : ''}
 *         </Button>
 *         <DateFilterDialog
 *           open={filtersOpen}
 *           onViewResults={handleViewResults}
 *           onClose={() => setFiltersOpen(false)}
 *         />
 *       </Grid>
 *     </Hidden>
 *
 *   </DateRange>
 * )
 */

// Used to reset or garantee props
export const EMPTY_RANGE = { from: undefined, to: undefined };

type InputProps = {
  ref: RefObject<HTMLInputElement> | null;
  onBlur: (e: FocusEvent<HTMLInputElement>) => void;
  onFocus: (e: FocusEvent<HTMLInputElement>) => void;
  value?: string;
};

export type DateRangeContextType = {
  fromProps?: InputProps;
  handleDayClick: DayClickEventHandler;
  handleSubmit: FormEventHandler;
  modifiers?: Modifiers;
  month?: Date;
  selected?: Date[];
  setMonth: MonthChangeEventHandler;
  toProps?: InputProps;
  value?: Partial<DateRange>;
  onClear?: () => void;
  setApplied?: (applied: boolean) => void;
  setOpen: (open: boolean) => void;
  open: boolean;
  focused: string | null;
  setFocused: (focused: 'from' | 'to' | null) => void;
};

export const DateRangeContext = createContext<DateRangeContextType>({
  fromProps: undefined,
  handleDayClick: noop,
  handleSubmit: noop,
  modifiers: undefined,
  month: undefined,
  selected: undefined,
  setMonth: noop,
  toProps: undefined,
  value: undefined,
  onClear: undefined,
  setApplied: undefined,
  setOpen: noop,
  open: false,
  focused: '',
  setFocused: noop
});

type Props = {
  children: ReactNode;
  value?: DateRange;
  onChange: (range: DateRange) => void;
  onClear?: () => void;
  setApplied?: (applied: boolean) => void;
};

const nonAutofocusRoutes = ['/orders', '/invoices', '/contracts'];

function DateRangeWrapper(props: Props) {
  /**
   * Refs
   */
  const fromEl = useRef<HTMLInputElement>(null);
  const toEl = useRef<HTMLInputElement>(null);

  /**
   * State
   */
  const [focused, setFocused] = useState<'from' | 'to' | null>(null);
  const [month, setMonth] = useState<Date>(props.value?.from ?? new Date());
  const [open, setOpen] = useState(false);
  const [hasCheckedAutofocus, setHasCheckedAutofocus] = useState(false);

  /**
   * Custom hooks
   */
  const location = useLocation();
  const { isSmallScreen } = useScreenSize();

  /**
   * Effects
   */
  useEffect(updateInputsWhenValueChanges, [props.value]);
  useEffect(autofocusEffect, [
    focused,
    hasCheckedAutofocus,
    isSmallScreen,
    location.pathname
  ]);

  /**
   * Memos
   */
  const modifiers = useMemo(modifiersMemo, [props.value]);
  const selected = useMemo(selectedMemo, [props.value]);
  const [fromProps, toProps] = useMemo(getPropsMemo, [fromEl, setMonth, props]);

  /**
   * Callbacks
   */
  const handleDayClick = useCallback(handleDayClickCb, [
    focused,
    fromEl,
    props,
    toEl
  ]);

  return (
    <DateRangeContext.Provider
      value={{
        modifiers,
        month,
        setMonth,
        selected,
        handleDayClick,
        handleSubmit,
        fromProps,
        toProps,
        value: props.value,
        onClear: props.onClear,
        setApplied: props.setApplied,
        setOpen,
        open,
        focused,
        setFocused
      }}
    >
      {props.children}
    </DateRangeContext.Provider>
  );

  /**
   * Memo Defs
   */
  function modifiersMemo() {
    return modifiersMemoLogic(props.value);
  }

  function selectedMemo() {
    return Object.values(props.value ?? {}) as Date[];
  }

  function getPropsMemo() {
    const { onChange, value } = props;
    return getPropsMemoLogic({
      fromEl,
      onChange,
      setFocused,
      setMonth,
      toEl,
      value
    });
  }

  /**
   * Callback Defs
   */
  function handleDayClickCb(day: Date) {
    props.setApplied && props.setApplied(false);
    const { onChange, value } = props;
    handleDayClickCbLogic({
      day,
      focused,
      fromEl,
      onChange,
      setOpen,
      toEl,
      value
    });
  }

  function handleSubmit(e: FormEvent<HTMLFormElement>) {
    handleSubmitLogic({ e, focused, fromEl, handleDayClick, toEl });
  }

  /**
   * Update input values when props change
   */
  function updateInputsWhenValueChanges() {
    if (fromEl.current) {
      fromEl.current.value = props.value?.from
        ? formatDate(props.value.from)
        : '';
    }

    if (toEl.current) {
      toEl.current.value = props.value?.to ? formatDate(props.value.to) : '';
    }
  }

  function autofocusEffect() {
    if (
      isSmallScreen &&
      !focused &&
      !nonAutofocusRoutes.includes(location.pathname) &&
      !hasCheckedAutofocus
    ) {
      setHasCheckedAutofocus(true);
      setFocused('from');
      setOpen(true);
      fromEl.current?.focus();
    }
  }
}

export default DateRangeWrapper;
