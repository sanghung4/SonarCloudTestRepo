import clsx from 'clsx';
import { ModalProps } from 'components/Modal/types';
import { ReactComponent as CloseIcon } from 'resources/icons/close.svg';

function Modal(props: ModalProps) {
  /**
   * Props
   */
  const { 'data-testid': testId } = props;

  /**
   * Render
   */
  if (!props.open) {
    return null;
  }
  return (
    <div
      className={clsx(
        'absolute left-0 top-0 right-0 bottom-0 flex items-center justify-center z-50',
        { hidden: !props.open }
      )}
      data-testid={`${testId}-wrapper`}
    >
      <div
        className="absolute left-0 top-0 right-0 bottom-0 bg-black/25 -z-10"
        onClick={() => !props.disableClose && props.onClose?.()}
        data-testid={`${testId}-bg`}
      />
      <div
        className={clsx(
          'bg-white p-6 z-0 rounded-md shadow-lg',
          props.className
        )}
        data-testid={`${testId}-container`}
      >
        <div className="flex">
          <span
            className="flex-1 text-2xl font-bold pr-4 text-secondary-2-100"
            data-testid={`${testId}-title`}
          >
            {props.title ?? ''}
          </span>
          <button
            onClick={() => props.onClose?.()}
            disabled={props.disableClose}
            data-testid={`${testId}-close-button`}
            className="text-primary-1-100 disabled:text-secondary-2-30"
          >
            <CloseIcon />
          </button>
        </div>
        <div>{props.children}</div>
      </div>
    </div>
  );
}

export default Modal;
