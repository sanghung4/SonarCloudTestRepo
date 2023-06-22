import CartLineNotesModal from 'Cart/LineNotesModal';
import { actClickOnElement, actUpdateInput } from 'test-utils/commonFireEvents';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

/**
 * Types
 */
type ComponentProp = Parameters<typeof CartLineNotesModal>[0];

/**
 * Mock values
 */
const mocks: ComponentProp = {
  open: true,
  setLineNotes: jest.fn(),
  onClose: jest.fn()
};

/**
 * Setup test function
 */
function setup(props: ComponentProp) {
  return render(<CartLineNotesModal {...props} />);
}

/**
 * TEST
 */
describe('Cart - LineNotesModal', () => {
  afterEach(() => {
    mocks.open = true;
    mocks.setLineNotes = jest.fn();
    mocks.onClose = jest.fn();
    mocks.notes = undefined;
  });

  it('should match snapshot as desktop', () => {
    setBreakpoint('desktop');
    const { baseElement } = setup(mocks);
    expect(baseElement).toMatchSnapshot();
  });

  it('should match snapshot as mobile', () => {
    setBreakpoint('mobile');
    const { baseElement } = setup(mocks);
    expect(baseElement).toMatchSnapshot();
  });

  it('expect the input value to update as user inputs value to the textfield', () => {
    setBreakpoint('desktop');
    const { getByTestId } = setup(mocks);
    const input = getByTestId('line-notes-modal-textfield');
    actUpdateInput(input, 'test');
    expect(input).toHaveDisplayValue('test');
  });

  it('expect the clear button to erase all values in the textfield', () => {
    setBreakpoint('desktop');
    const { getByTestId } = setup(mocks);
    const input = getByTestId('line-notes-modal-textfield');
    actUpdateInput(input, 'test');
    expect(input).toHaveDisplayValue('test');
    actClickOnElement(getByTestId('line-notes-modal-clear'));
    expect(input).toHaveDisplayValue('');
  });

  it('expect the save button to call setLineNotes and onClose', () => {
    setBreakpoint('desktop');
    const { getByTestId } = setup(mocks);
    actClickOnElement(getByTestId('line-notes-modal-save'));
    expect(mocks.setLineNotes).toBeCalledWith('');
    expect(mocks.onClose).toBeCalled();
  });

  it('expect the text to reset when closing', () => {
    setBreakpoint('desktop');
    const { getByTestId } = setup(mocks);
    const input = getByTestId('line-notes-modal-textfield');
    actUpdateInput(input, 'test');
    expect(input).toHaveDisplayValue('test');
    actClickOnElement(getByTestId('cart-line-notes-modal-close-button'));
    expect(mocks.onClose).toBeCalled();
    expect(input).toHaveDisplayValue('');
  });
});
