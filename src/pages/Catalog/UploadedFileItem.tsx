import clsx from 'clsx';

import { useApiDeleteCatalogUpload } from 'api/catalog.api';
import { ReactComponent as FileIcon } from 'resources/icons/file.svg';
import { ReactComponent as TrashIcon } from 'resources/icons/trash-alt.svg';

/**
 * Types
 */
export type UploadedFile = {
  id?: string;
  name: string;
  error?: string;
};

type Props = UploadedFile & {
  index: number;
  highlighted?: boolean;
  onDelete: (index: number) => void;
  onError: (index: number, e: string) => void;
};

/**
 * Component
 */
function UploadedFileItem(props: Props) {
  /**
   * API
   */
  const { call, loading } = useApiDeleteCatalogUpload(props.id);

  /**
   * Callbacks
   */
  const handleDelete = async () => {
    if (!props.id) {
      props.onDelete(props.index);
      return;
    }
    const res = await call();
    res?.status === 200
      ? props.onDelete(props.index)
      : props.onError(props.index, "There's an issue deleting the file");
  };
  /**
   * Render
   */
  return (
    <div
      className={clsx('mt-4 p-2', {
        'rounded-lg border-4 border-primary-2-100': props.highlighted
      })}
      data-testid="catalog_upload-file-item"
    >
      <div className="flex text-primary-1-100">
        <span className="flex items-center pr-2">
          <FileIcon width={20} height={20} />
        </span>
        <span
          className="text-xl text-primary-1-100 flex-1"
          data-testid="catalog_upload-file-item-name"
        >
          {props.name}
        </span>
        <span className="flex items-center">
          <button
            disabled={loading}
            onClick={handleDelete}
            className="disabled:text-secondary-2-60"
            data-testid="catalog_upload-file-item-delete"
          >
            <TrashIcon width={24} height={24} />
          </button>
        </span>
      </div>
      {Boolean(props.error) && (
        <div
          className="text-common-error text-xs mt-2"
          data-testid="catalog_upload-file-item-error"
        >
          {props.error}
        </div>
      )}
    </div>
  );
}
export default UploadedFileItem;
