import { useMemo, ChangeEvent } from 'react';
import {
  Grid,
  Box,
  Typography,
  Input,
  useScreenSize
} from '@dialexa/reece-component-library';
import { Button as MuiButton } from '@mui/material';
import {
  useController,
  UseControllerProps,
  FieldValues
} from 'react-hook-form';
import { useTranslation } from 'react-i18next';

import JobFormInputLabel from './JobFormInputLabel';
import {
  ChooseFileInputLabel,
  RemoveFileButton,
  FormFieldContainer,
  InputLabelWrapper
} from './utils/styled';

interface JobFormFileInputProps<TFieldValues extends FieldValues>
  extends UseControllerProps<TFieldValues> {
  label: string;
  testId: string;
  onFileReset: () => void;
  tooltip?: string;
  required?: boolean;
  mdLabelSpan?: number;
  mdFieldSpan?: number;
  xsLabelSpan?: number;
  xsFieldSpan?: number;
}

function JobFormFileInput<TFieldValues extends FieldValues>(
  props: JobFormFileInputProps<TFieldValues>
) {
  /**
   * Props
   */
  const {
    label,
    tooltip,
    required,
    onFileReset,
    mdLabelSpan,
    mdFieldSpan,
    xsLabelSpan,
    xsFieldSpan,
    testId,
    ...rest
  } = props;

  /**
   * Custom Hooks
   */
  const { t } = useTranslation();
  const { isSmallScreen } = useScreenSize();
  const {
    field: { name, value, ref, onChange },
    fieldState: { error }
  } = useController({ ...rest });

  /**
   * Memo
   */
  // eslint-disable-next-line react-hooks/exhaustive-deps
  const fileName = useMemo(fileNameMemo, [value]);

  /**
   * Render
   */
  return (
    <FormFieldContainer container columns={10} data-testid={testId}>
      {/* Label */}
      <InputLabelWrapper
        item
        md={mdLabelSpan ?? 3}
        xs={xsLabelSpan ?? 10}
        alignItems="flex-start"
      >
        <JobFormInputLabel
          title={label}
          tooltip={tooltip}
          required={required}
        />
      </InputLabelWrapper>

      {/* Input */}
      <Grid item md={mdFieldSpan ?? 7} xs={xsFieldSpan ?? 10}>
        {/* First Row */}
        {value ? (
          <Box display="flex" overflow={'hidden'} whiteSpace="nowrap">
            <Typography fontWeight={500} marginRight={0.5}>
              {t('jobForm.projectUploadFileSelected')}:
            </Typography>
            <Typography fontWeight={500} marginRight={1} overflow="hidden">
              {fileName}
            </Typography>

            <RemoveFileButton
              variant="inline"
              onClick={handleFileRemove}
              data-testid={`${testId}-remove`}
            >
              {t('jobForm.projectUploadRemove')}
            </RemoveFileButton>
          </Box>
        ) : (
          <Box display="flex" alignItems="center" marginBottom={1.5}>
            {/* Button to upload file */}
            <ChooseFileInputLabel
              htmlFor={props.name}
              data-testid={`${testId}-button`}
            >
              <MuiButton component="span" variant="outlined" size="small">
                {t('jobForm.projectUploadChooseFile')}
              </MuiButton>
            </ChooseFileInputLabel>
            {/* Empty text */}
            {!error && (
              <Typography fontSize={14}>
                {t('jobForm.projectUploadNoFile')}
              </Typography>
            )}
          </Box>
        )}

        {/* Second Row (accepted file types) */}
        <Typography fontSize={14} color="mediumGray.main">
          {t('jobForm.projectUploadFileTypes')}
        </Typography>
        {/* Hidden File Input */}
        <Box display="none">
          <Input
            name={name}
            id={name}
            type="file"
            onChange={handleChange}
            inputProps={{
              ref: ref,
              accept:
                'image/gif, image/jpeg, image/png, image/jpg, application/pdf'
            }}
          />
        </Box>
      </Grid>
    </FormFieldContainer>
  );

  /**
   * Memo Definitions
   */
  function fileNameMemo() {
    const nameLength = value?.name?.length ?? 0;
    const maxCharacters = isSmallScreen ? 20 : 32;
    const limit = isSmallScreen ? 8 : 13;

    if (nameLength > maxCharacters) {
      return nameLength > maxCharacters
        ? `${value.name.substring(0, limit)}...${value.name.slice(
            -(limit + 1)
          )}`
        : value.name;
    }

    return value?.name;
  }

  /**
   * Callback Definitions
   */
  function handleChange(e: ChangeEvent<HTMLInputElement>) {
    e.target.files && onChange(e.target.files[0]);
  }

  function handleFileRemove() {
    onFileReset();
  }
}

export default JobFormFileInput;
