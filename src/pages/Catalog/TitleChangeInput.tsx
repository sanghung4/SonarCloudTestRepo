import { useState } from 'react';

import { useFormik } from 'formik';
import * as Yup from 'yup';

import {
  PostCatalogTitleRequest,
  useApiPostCatalogTitle
} from 'api/catalog.api';
import Input from 'components/Input/Input';
import { ReactComponent as EditIcon } from 'resources/icons/edit.svg';

/**
 * Types
 */
export type TitleChangeInputProps = {
  updateCatalogTitle: (newCatalogTitle: string) => void;
  catalogTitle: string;
  id: string;
};

/**
 * Component
 */
function TitleChangeInput(props: TitleChangeInputProps) {
  /**
   * Props
   */
  const { id, catalogTitle, updateCatalogTitle } = props;

  /**
   * State
   */
  const [isEditing, setIsEditing] = useState(false);

  /**
   * API
   */
  const { call: titleChange } = useApiPostCatalogTitle(id);

  /**
   * Callbacks
   */
  // 🟤 Cb - Switch edit mode
  const toggleEditingMode = (e: React.MouseEvent) => {
    e.preventDefault();
    if (isEditing) {
      formik.handleSubmit();
    } else {
      formik.setFieldValue('name', catalogTitle);
      setIsEditing(true);
    }
  };
  // 🟤 Cb - Form Submit
  const onSubmit = (values: PostCatalogTitleRequest) => {
    titleChange(values);
    updateCatalogTitle(values.name as string);
    setIsEditing(false);
  };
  // 🟤 Cb - Input Blur
  const onBlur = (e: React.FocusEvent) => {
    formik.handleBlur(e);
    formik.handleSubmit();
  };

  /**
   * Form
   */
  const formik = useFormik({
    initialValues: { name: catalogTitle },
    validationSchema: Yup.object({
      name: Yup.string()
        .required('Catalog name must contain more than one character')
        .min(2, 'Catalog name must contain more than one character')
        .trim()
        .matches(/\S+/, 'Catalog name must contain more than one character')
    }),
    onSubmit
  });

  /**
   * Render
   */
  return (
    <form
      data-testid="catalog-form"
      onSubmit={formik.handleSubmit}
      className="flex flex-row"
    >
      {isEditing ? (
        <Input
          id="name"
          name="name"
          data-testid="catalog-name-input"
          value={formik.values.name}
          status={formik.errors.name ? 'error' : 'neutral'}
          helperText={
            formik.touched.name && formik.errors.name ? formik.errors.name : ''
          }
          onChange={formik.handleChange}
          onBlur={onBlur}
          className="text-black font-semibold text-3xl ml-5"
        />
      ) : (
        <h3
          className="text-black font-semibold text-3xl ml-5 cursor-pointer"
          data-testid="catalog-name"
          onClick={toggleEditingMode}
        >
          {catalogTitle}
        </h3>
      )}
      <button
        type={!isEditing ? 'submit' : 'button'}
        className="text-primary-1-100 ml-4"
        data-testid="catalog-edit"
        onClick={toggleEditingMode}
      >
        <EditIcon />
      </button>
    </form>
  );
}

export default TitleChangeInput;
