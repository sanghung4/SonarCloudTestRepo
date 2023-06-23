import { render, screen } from '@testing-library/react';

import Modal from 'components/Modal/Modal';
import { ModalProps } from 'components/Modal/types';
import { fireEvent } from 'test-util';

/**
 * Types
 */
type MockProps = Omit<ModalProps, 'children'>;

/**
 * Mocks
 */
const defaultProps: MockProps = {
  'data-testid': 'modal',
  disableClose: false,
  onClose: jest.fn(),
  open: true
};
let props: MockProps = { ...defaultProps };

/**
 * Setup
 */
function setup(p: MockProps) {
  render(
    <Modal {...p}>
      <span data-testid="child" />
    </Modal>
  );
}

/**
 * TERST
 */
describe('components/Modal', () => {
  // ⚪ Reset mocks
  afterEach(() => {
    props = { ...defaultProps };
  });

  // 🟢 1 - Closed
  it('expect modal not to be rendered when open is false', () => {
    // arrange
    props.open = false;
    // act
    setup(props);
    const container = screen.queryByTestId('modal-wrapper');
    // assert
    expect(container).not.toBeInTheDocument();
  });

  // 🟢 2 - Open
  it('expect modal to be rendered when open is true', () => {
    // act
    setup(props);
    const container = screen.queryByTestId('modal-wrapper');
    // assert
    expect(container).toBeInTheDocument();
  });

  // 🟢 3 - close button
  it('expect onClose to be called when close button is clicked', () => {
    // act
    setup(props);
    fireEvent('click', 'modal-close-button');
    // assert
    expect(props.onClose).toBeCalled();
  });

  // 🟢 4 - background click
  it('expect onClose to be called when background is clicked', () => {
    // act
    setup(props);
    fireEvent('click', 'modal-bg');
    // assert
    expect(props.onClose).toBeCalled();
  });
});
