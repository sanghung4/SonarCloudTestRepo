import { useCallback, useState } from 'react';

import { Maybe } from 'yup';

import { useApiCatalogUpload } from 'api/catalog.api';
import { Button } from 'components/Button';
import { FileUpload } from 'components/FileUpload';
import { Modal } from 'components/Modal';
import UploadedFileItem, { UploadedFile } from 'pages/Catalog/UploadedFileItem';
import { ReactComponent as UploadIcon } from 'resources/icons/upload.svg';
import { convertFileList } from 'util/fileProcessor';

export type UploadButtonProps = {
  disabled: boolean;
  customerId: string;
  name: Maybe<string>;
  onChangeValue?: (uploads: UploadedFile[]) => void;
};

function UploadButton(props: UploadButtonProps) {
  /**
   * State
   */
  const [open, setOpen] = useState(false);
  const [uploads, setUploads] = useState<UploadedFile[]>([]);

  /**
   * API
   */
  const { loading, call: upload } = useApiCatalogUpload(props.customerId);

  /**
   * Callbacks
   */
  // 🟤 Cb - Closing modal
  const onModalSubmit = useCallback(() => {
    setOpen(false);
    const successUploads = uploads.filter(({ id }) => Boolean(id));
    if (successUploads.length) {
      props.onChangeValue?.(successUploads);
    }
    setUploads([]);
  }, [props, uploads]);
  // 🟤 Cb - File selected then upload
  const onSelectingFiles = async (files: Maybe<FileList>) => {
    const [file] = await convertFileList(files);
    const res = await upload(file);
    // NOT SURE IF THE ERROR VALUE WILL BE USED
    const error =
      res?.status !== 200 || res?.data?.error
        ? res?.data?.error ?? "There's an issue uploading the file"
        : '';
    const newFile: UploadedFile = {
      id: res?.data?.uploadId,
      name: file.fileName,
      error
    };
    setUploads([newFile, ...uploads]);
  };
  // 🟤 Cb - Delete upload based on the n-th position (index)
  const deleteNthUploads = (index: number) => {
    const mutableUploads = [...uploads];
    mutableUploads.splice(index, 1);
    setUploads(mutableUploads);
  };
  // 🟤 Cb - Apply error to existing upload at n-th position (index)
  const errorNthUploads = (index: number, error: string) => {
    const mutableUploads = [...uploads];
    mutableUploads[index].error = error;
    setUploads(mutableUploads);
  };

  /**
   * Render
   */
  return (
    <>
      <Button
        type="button"
        title="Upload CSV File"
        className="ml-6 mt-8 mb-5 bg-white text-primary-1-100 border-primary-1-100 border-2"
        disabled={props.disabled}
        iconPosition="left"
        icon={<UploadIcon />}
        onClick={() => setOpen(true)}
        data-testid="catalog_upload-button"
      />
      <Modal
        className="max-w-[592px]"
        open={open}
        onClose={onModalSubmit}
        title={`Upload file to ${props.name ?? '--'}`}
        data-testid="catalog_upload"
      >
        <div className="flex flex-col gap-4 mt-4">
          <span className="text-secondary-2-100 text-base">
            You can upload several .csv files but one at a time to a catalog.
          </span>
          <FileUpload
            loading={loading}
            accepts="text/csv"
            displayAccepts=".csv"
            data-testid="catalog_upload-uploader"
            onSelectingFiles={onSelectingFiles}
          />
        </div>
        <div className="my-8 max-h-[180px] overflow-y-auto overflow-x-hidden">
          {uploads.map((item, i) => (
            <UploadedFileItem
              {...item}
              index={i}
              key={`item-${i}`}
              onDelete={deleteNthUploads}
              onError={errorNthUploads}
            />
          ))}
        </div>
        <div className="flex justify-end">
          <Button
            type="button"
            title="Submit"
            className="bg-primary-1-100 text-white"
            disabled={loading}
            onClick={onModalSubmit}
            data-testid="catalog_upload-save-button"
          />
        </div>
      </Modal>
    </>
  );
}
export default UploadButton;
