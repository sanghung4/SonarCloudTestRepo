import { useState, useMemo, useEffect, CSSProperties } from 'react';

import { Modify } from '@dialexa/reece-component-library';
import { isUndefined } from 'lodash-es';

import MinimumQuantityDialog from 'common/QtyInput/MinimumQuantityDialog';
import {
  canUpdate,
  capMaxQtyLogic,
  maxQtyLogic,
  qtyUpdateLogic
} from 'common/QtyInput/utils';
import NumberInput, {
  ButtonProps,
  InputBaseProps,
  NumberInputSizes
} from 'components/NumberInput';
import useDebouncedCallback from 'hooks/useDebouncedCallback';

/**
 * Types
 */
export type QtyInputProps = Modify<
  InputBaseProps,
  {
    allowZero?: boolean | string; // String for test purpose
    buttonContainerStyle?: CSSProperties;
    'data-testid'?: string;
    downbuttonprops?: ButtonProps;
    fullWidth?: boolean;
    increment?: number;
    index?: number;
    max?: number;
    noDebounce?: boolean;
    onUpdate?: (value: number) => void;
    productName?: string;
    size?: NumberInputSizes;
    sync?: boolean;
    value: number;
    upbuttonprops?: ButtonProps;
  }
>;
export type QtyAdjustmentModal = {
  minQty: number;
  adjustedQty: number;
  open: boolean;
  productName: string;
};

/**
 * Constants
 */
const defaultModal: QtyAdjustmentModal = {
  open: false,
  adjustedQty: 0,
  minQty: 1,
  productName: ''
};

/**
 * Component
 */
function QtyInput(props: QtyInputProps) {
  /**
   * Props
   */
  const {
    allowZero,
    fullWidth,
    value: parentValue,
    max: incomingMax,
    increment = 1,
    noDebounce,
    onUpdate,
    productName,
    sync,
    upbuttonprops = {},
    downbuttonprops = {},
    ...otherProps
  } = props;
  const min = allowZero ? 0 : increment;

  /**
   * State
   */
  const [isManualInput, setManualInput] = useState(false);
  const [modal, setModal] = useState(defaultModal);
  const [qty, setQty] = useState(parentValue || min);

  /**
   * Memo
   */
  const max = useMemo(maxQtyMemo, [incomingMax, increment]);
  const dataTestId = isUndefined(props.index) ? '' : `-${props.index}`;

  /**
   * Callbacks
   */
  const updateQty = useDebouncedCallback(updateQtyCb, 300, [parentValue]);

  /**
   * Effects
   */
  useEffect(syncParentValueEffect, [isManualInput, parentValue, qty, sync]);

  /**
   * Render
   */
  return (
    <>
      <NumberInput
        {...otherProps}
        value={qty}
        min={min}
        max={max}
        fullWidth={fullWidth}
        allowzero={allowZero}
        sync={sync}
        increment={props.increment}
        onClick={handleClickQtyChange}
        onFocus={() => setManualInput(true)}
        onBlur={handleBlurQtyChange}
        data-testid={props['data-testid'] ?? `quantity-input${dataTestId}`}
        upbuttonprops={{
          ...upbuttonprops,
          'data-testid': `quantity-input-up${dataTestId}`
        }}
        downbuttonprops={{
          ...downbuttonprops,
          'data-testid': `quantity-input-down${dataTestId}`
        }}
      />
      <MinimumQuantityDialog
        {...modal}
        onClose={handleCloseDialog}
        onConfirm={handleCloseDialog}
      />
    </>
  );

  /**
   * Handles
   */
  function handleQtyUpdate(inputQty: number) {
    const newValue = qtyUpdateLogic({
      allowZero,
      increment,
      inputQty,
      max,
      productName,
      setModal
    });
    setQty(newValue);
    onUpdate?.(newValue);
  }
  function handleBlurQtyChange(inputQty: number) {
    if (isManualInput && inputQty !== qty) {
      setManualInput(false);
      handleQtyUpdate(inputQty);
    }
  }
  function handleClickQtyChange(inputQty: number) {
    inputQty = capMaxQtyLogic(inputQty, max);
    inputQty = Math.max(inputQty, min);
    setQty(inputQty);
    if (noDebounce) {
      onUpdate?.(inputQty);
    } else {
      updateQty(inputQty);
    }
    setManualInput(false);
  }
  function handleCloseDialog() {
    setModal({ ...modal, open: false });
  }

  /**
   * Memo def
   */
  function maxQtyMemo() {
    return maxQtyLogic(increment, incomingMax);
  }

  /**
   * Effect defs
   */
  function syncParentValueEffect() {
    if (!isManualInput && qty !== parentValue && sync) {
      setQty(parentValue);
    }
  }

  /**
   * Debounce Callback def
   */
  function updateQtyCb(newqty: number) {
    // istanbul ignore next
    if (canUpdate(newqty, parentValue, allowZero)) {
      onUpdate?.(newqty);
    }
  }
}

export default QtyInput;
