import { useMemo } from 'react';
import { SxProps, TextField, Theme } from '@dialexa/reece-component-library';

import { StyledAutoComplete } from '../utils/styled';
import { kebabCase, uniqBy } from 'lodash-es';

/**
 * Types
 */

export type AutocompleteOption = { label: string; value: string };

type AutoCompleteProps = {
  options: AutocompleteOption[];
  testId?: string;
  disabled?: boolean;
  sx?: SxProps<Theme>;
  onChange: (value?: string) => void;
  onBlur?: () => void;
  value: string;
  id?: string;
  placeholder?: string;
};

/**
 * Component
 */
function AutoComplete(props: AutoCompleteProps) {
  /**
   * Memos
   */
  const selectedValue = useMemo(() => {
    const selected = props.options.find((opt) => opt.value === props.value);
    return selected ?? null;
  }, [props.value, props.options]);

  /**
   * Render
   */
  return (
    <StyledAutoComplete
      id={props.id}
      data-testid={props.testId}
      options={uniqBy(props.options, (option) => option.label)}
      onBlur={props.onBlur}
      onChange={(_, val) => {
        props.onChange(val?.value);
      }}
      getOptionLabel={(option) => option.label}
      value={selectedValue}
      renderInput={({ inputProps, ...rest }) => (
        <TextField
          {...rest}
          inputProps={{
            ...inputProps,
            'data-testid': kebabCase([props.testId, 'input'].join(' '))
          }}
        />
      )}
      sx={props.sx}
      disabled={props.disabled}
      placeholder={props.placeholder}
      disablePortal
    />
  );
}

export default AutoComplete;
