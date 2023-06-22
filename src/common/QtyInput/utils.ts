import { QtyAdjustmentModal } from 'common/QtyInput';

export type QtyUpdateProps = {
  allowZero?: boolean | string;
  increment: number;
  inputQty: number;
  max?: number;
  productName?: string;
  setModal: (value: QtyAdjustmentModal) => void;
};

export function qtyUpdateLogic({
  allowZero,
  increment,
  inputQty,
  max,
  productName,
  setModal
}: QtyUpdateProps) {
  const remainder = increment ? inputQty % increment : 0;

  // When there's remainder, it needs to be adjusted to be divisible by `increment`
  if (remainder) {
    const matchIncrement = inputQty - remainder + increment;
    const toAdjustTo = max
      ? Math.min(matchIncrement, maxQtyLogic(increment, max)!)
      : matchIncrement;
    setModal({
      open: true,
      adjustedQty: toAdjustTo,
      minQty: allowZero ? 0 : increment,
      productName: productName ?? ''
    });
    return toAdjustTo;
  }
  return capMaxQtyLogic(inputQty, max);
}

export function maxQtyLogic(increment: number, incomingMax?: number) {
  return incomingMax ? incomingMax - (incomingMax % increment) : undefined;
}

export function capMaxQtyLogic(inputQty: number, max?: number) {
  return max ? Math.min(inputQty, max) : inputQty;
}

export function canUpdate(
  newqty: number,
  oldqty: number,
  allowZero?: boolean | string
) {
  const notAtZero = !allowZero && newqty > 0;
  const isPositive = allowZero && newqty >= 0;
  const notAtLowest = notAtZero || isPositive;
  return Boolean(newqty !== oldqty && notAtLowest);
}
