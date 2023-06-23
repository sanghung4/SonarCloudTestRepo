import {
  ChangeEvent,
  FocusEvent,
  KeyboardEvent,
  useMemo,
  useState
} from 'react';

import clsx from 'clsx';

import { Button } from 'components/Button';
import { Input } from 'components/Input';
import { paginationInputSize, PaginationProps } from 'components/Table';
import { ReactComponent as ChevronLeftIcon } from 'resources/icons/chevron-left.svg';
import { ReactComponent as ChevronRightIcon } from 'resources/icons/chevron-right.svg';
import { stringToNumber } from 'util/stringToNumber';

/**
 * Component
 */
function Pagination(props: PaginationProps) {
  /**
   * Props
   */
  const testId = props['data-testid'];
  const current = `${props.currentPage ?? 1}`;
  const total = props.totalPages ?? 1;

  /**
   * States
   */
  const [savedInput, setSavedInput] = useState(current);
  const [input, setInput] = useState(current);

  /**
   * Memo
   */
  const disable = useMemo(() => {
    const pageNumber = stringToNumber(input);
    return {
      previous: pageNumber <= 1,
      next: pageNumber >= total
    };
  }, [input, total]);

  /**
   * Callbacks
   */
  // 🟤 Cb - used by previous/next buttons
  const togglePage = (direction?: 'up' | 'down') => () => {
    const incremental = direction === 'up' ? 1 : -1;
    const pageNumber = stringToNumber(input) + incremental;
    setInput(`${pageNumber}`);
    props.onChange?.(pageNumber);
  };
  // 🟤 Cb - used by input onFocus event
  const inputOnFocus = (e: FocusEvent<HTMLInputElement>) => {
    e.currentTarget.select();
    setSavedInput(input);
  };
  // 🟤 Cb - used by input onChange event
  const inputOnChange = (e: ChangeEvent<HTMLInputElement>) => {
    const filteredString = e.currentTarget.value.replace(/[^0-9]/gm, '');
    setInput(filteredString);
  };
  // 🟤 Cb - When input blurs, re-adjust input value
  const inputOnBlur = (e: FocusEvent<HTMLInputElement>) => {
    const toNumber = stringToNumber(e.currentTarget.value);
    const pageNumber = Math.min(toNumber, total);
    setInput(`${pageNumber}`);
    if (savedInput !== `${pageNumber}`) {
      props.onChange?.(pageNumber);
    }
  };
  // 🟤 Cb - Input press "Enter" to save
  function handleEnter(e: KeyboardEvent<HTMLInputElement>) {
    if (e.key !== 'Enter') {
      return;
    }
    e.currentTarget?.blur();
    inputOnBlur({
      currentTarget: e.currentTarget
    } as FocusEvent<HTMLInputElement>);
  }

  /**
   * Render
   */
  return (
    <div
      className="flex items-center justify-center text-primary-1-100 text-xl font-normal"
      data-testid={testId}
    >
      <Button
        className="bg-primary-1-100 text-white !px-1 !py-1"
        disabled={props.disabled || disable.previous}
        onClick={togglePage('down')}
        data-testid={`${testId}-previous`}
      >
        <ChevronLeftIcon />
      </Button>
      <Input
        disabled={props.disabled}
        className="mx-4"
        inputClassName="text-primary-1-100 h-8 !px-2 !py-0 text-base text-center disabled:text-secondary-2-50"
        labelClassName="hidden"
        noHelper
        onFocus={inputOnFocus}
        onChange={inputOnChange}
        onBlur={inputOnBlur}
        onKeyDown={handleEnter}
        style={{ width: `${paginationInputSize(input.length)}px` }} // Applying to `style` since this doesn't work in Tailwind
        value={input}
        data-testid={`${testId}-input`}
      />
      <span className={clsx({ 'text-secondary-2-60': props.disabled })}>
        of{' '}
        <span className="mx-3" data-testid={`${testId}-total`}>
          {total}
        </span>
      </span>
      <Button
        className="bg-primary-1-100 text-white !px-1 !py-1"
        disabled={props.disabled || disable.next}
        onClick={togglePage('up')}
        data-testid={`${testId}-next`}
      >
        <ChevronRightIcon />
      </Button>
    </div>
  );
}
export default Pagination;
