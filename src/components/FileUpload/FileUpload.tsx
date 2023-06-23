import { useEffect, useRef, useState } from 'react';

import clsx from 'clsx';

import { FileUploadProps } from 'components/FileUpload';
import { LoadingIcon } from 'components/LoadingIcon';

/**
 * Component
 */
function FileUpload(props: FileUploadProps) {
  /**
   * Props
   */
  const disabled = props.loading || props.disabled;
  /**
   * States
   */
  const [dragging, setDragging] = useState(false);

  /**
   * Refs
   */
  const inputRef = useRef<HTMLInputElement>(null);
  const dropzoneRef = useRef<HTMLDivElement>(null);

  /**
   * Effects
   */
  // Listeners
  useEffect(() => {
    const div = dropzoneRef.current;
    div?.addEventListener('dragenter', dragInEvent);
    div?.addEventListener('dragleave', dragOutEvent);
    div?.addEventListener('dragover', dragEvent);
    div?.addEventListener('drop', dropEvent);
    return () => {
      div?.removeEventListener('dragenter', dragInEvent);
      div?.removeEventListener('dragleave', dragOutEvent);
      div?.removeEventListener('dragover', dragEvent);
      div?.removeEventListener('drop', dropEvent);
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  /**
   * Render
   */
  return (
    <div
      ref={dropzoneRef}
      onClick={() => !disabled && inputRef.current?.click()}
      data-testid={`${props['data-testid']}-container`}
      className={clsx(
        'bg-white border-primary-1-100 border rounded-sm',
        'px-32 py-9 flex justify-center items-center',
        'text-primary-1-100 text-base font-medium',
        'hover:bg-primary-1-10 cursor-pointer select-none',
        {
          'border-secondary-2-50 text-secondary-2-50': props.disabled,
          'hover:bg-white cursor-default': disabled,
          'bg-primary-1-30': dragging
        },
        props.className
      )}
    >
      {props.loading ? (
        <LoadingIcon
          size="xxl"
          data-testid={`${props['data-testid']}-loading`}
        />
      ) : (
        `Click to browse desktop or drag ${
          props.displayAccepts ?? props.accepts ?? 'any'
        } file here to upload`
      )}
      <input
        type="file"
        accept={props.accepts}
        ref={inputRef}
        className="hidden"
        multiple={props.multiple}
        disabled={disabled}
        data-testid={`${props['data-testid']}-input`}
        onChange={(e) => {
          props.onSelectingFiles(e.target.files);
          // Reset to prevent the same file not selecting (intended by vanilla JS)
          e.currentTarget.value = '';
        }}
      />
    </div>
  );

  /**
   * Listeners
   */
  function dragInEvent(this: HTMLDivElement, e: DragEvent) {
    dndPreventDefault(e);
    if (e.dataTransfer?.files.length && !disabled) {
      setDragging(true);
    }
  }
  function dragOutEvent(this: HTMLDivElement, e: DragEvent) {
    dndPreventDefault(e);
    setDragging(false);
  }
  function dragEvent(this: HTMLDivElement, e: DragEvent) {
    dndPreventDefault(e);
  }
  function dropEvent(this: HTMLDivElement, e: DragEvent) {
    dndPreventDefault(e);
    const fileCount = e.dataTransfer?.files.length;
    const isSingleFile = !props.multiple && fileCount === 1;
    if (fileCount && (props.multiple || isSingleFile) && !disabled) {
      props.onSelectingFiles(e.dataTransfer.files);
      e.dataTransfer.clearData();
    }
    setDragging(false);
  }

  /**
   * Util
   */
  function dndPreventDefault(e: Event) {
    e.preventDefault();
    e.stopPropagation();
  }
}
export default FileUpload;
