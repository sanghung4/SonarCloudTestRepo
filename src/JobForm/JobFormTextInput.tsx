import { Grid } from '@dialexa/reece-component-library';
import { FieldValues } from 'react-hook-form';

import JobFormInputLabel from './JobFormInputLabel';
import { FormFieldContainer, InputLabelWrapper } from './utils/styled';
import FormTextInput, { FormTextInputProps } from 'common/FormTextInput';

interface JobFormTextInputProps<TFieldValues extends FieldValues>
  extends FormTextInputProps<TFieldValues> {
  label: string;
  tooltip?: string;
  readOnly?: boolean;
  mdLabelSpan?: number;
  mdFieldSpan?: number;
  xsLabelSpan?: number;
  xsFieldSpan?: number;
  maxLength?: number;
}

function JobFormTextInput<TFieldValues extends FieldValues>(
  props: JobFormTextInputProps<TFieldValues>
) {
  /**
   * Props
   */
  const {
    label,
    tooltip,
    required,
    readOnly,
    mdLabelSpan,
    mdFieldSpan,
    xsLabelSpan,
    xsFieldSpan,
    testId,
    placeholder,
    maxLength,
    ...rest
  } = props;

  /**
   * Render
   */
  return (
    <FormFieldContainer
      container
      columns={10}
      data-testid={`${testId}-wrapper`}
    >
      {/* Label */}
      <InputLabelWrapper
        item
        md={mdLabelSpan ?? 3}
        xs={xsLabelSpan ?? 10}
        display="flex"
        alignItems="flex-start"
      >
        <JobFormInputLabel
          htmlFor={props.name}
          title={label}
          tooltip={tooltip}
          required={required}
        />
      </InputLabelWrapper>

      {/* Input */}
      <Grid item md={mdFieldSpan ?? 7} xs={xsFieldSpan ?? 10}>
        <FormTextInput
          {...rest}
          readOnly={readOnly}
          sx={{ paddingY: 1 }}
          testId={testId}
          placeholder={placeholder}
          maxLength={maxLength}
        />
      </Grid>
    </FormFieldContainer>
  );
}

export default JobFormTextInput;
