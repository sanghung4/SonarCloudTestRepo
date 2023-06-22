import {
  canUpdate,
  qtyUpdateLogic,
  QtyUpdateProps
} from 'common/QtyInput/utils';

const mockQtyUpdateLogic: QtyUpdateProps = {
  increment: 0,
  inputQty: 0,
  setModal: jest.fn()
};

describe('Common - QtyInput Utils', () => {
  it('expect `qtyUpdateLogic` to resolve with default values', () => {
    const result = qtyUpdateLogic(mockQtyUpdateLogic);
    expect(result).toBe(0);
  });

  it('expect `qtyUpdateLogic` to resolve with incremental values', () => {
    mockQtyUpdateLogic.increment = 5;
    mockQtyUpdateLogic.inputQty = 14;
    const result = qtyUpdateLogic(mockQtyUpdateLogic);
    expect(result).toBe(15);
  });

  it('expect `qtyUpdateLogic` to resolve with incremental values at the max', () => {
    const setModal = jest.fn();
    mockQtyUpdateLogic.setModal = setModal;
    mockQtyUpdateLogic.increment = 5;
    mockQtyUpdateLogic.inputQty = 14;
    mockQtyUpdateLogic.max = 11;
    const result = qtyUpdateLogic(mockQtyUpdateLogic);

    expect(result).toBe(10);
    expect(setModal).toBeCalledWith({
      open: true,
      adjustedQty: 10,
      minQty: mockQtyUpdateLogic.increment,
      productName: ''
    });
  });

  it('expect `qtyUpdateLogic` to resolve with incremental values at the max with 0 allowed', () => {
    const setModal = jest.fn();
    mockQtyUpdateLogic.setModal = setModal;
    mockQtyUpdateLogic.increment = 5;
    mockQtyUpdateLogic.inputQty = 14;
    mockQtyUpdateLogic.max = 11;
    mockQtyUpdateLogic.allowZero = true;
    const result = qtyUpdateLogic(mockQtyUpdateLogic);

    expect(result).toBe(10);
    expect(setModal).toBeCalledWith({
      open: true,
      adjustedQty: 10,
      minQty: 0,
      productName: ''
    });
  });

  it('expect `canUpdate` to resolve false with values (0, 0)', () => {
    const result = canUpdate(0, 0);
    expect(result).toBe(false);
  });

  it('expect `canUpdate` to resolve false with values (1, 1)', () => {
    const result = canUpdate(1, 1);
    expect(result).toBe(false);
  });

  it('expect `canUpdate` to resolve true with values (1, 0)', () => {
    const result = canUpdate(1, 0);
    expect(result).toBe(true);
  });

  it('expect `canUpdate` to resolve false with values (0, 1)', () => {
    const result = canUpdate(0, 1);
    expect(result).toBe(false);
  });

  it('expect `canUpdate` to resolve false with values (0, 0, true)', () => {
    const result = canUpdate(0, 0, true);
    expect(result).toBe(false);
  });

  it('expect `canUpdate` to resolve false with values (1, 1, true)', () => {
    const result = canUpdate(1, 1, true);
    expect(result).toBe(false);
  });

  it('expect `canUpdate` to resolve true with values (1, 0, true)', () => {
    const result = canUpdate(1, 0, true);
    expect(result).toBe(true);
  });

  it('expect `canUpdate` to resolve true with values (0, 1, true)', () => {
    const result = canUpdate(0, 1, true);
    expect(result).toBe(true);
  });
});
