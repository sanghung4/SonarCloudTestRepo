import { FormEventHandler } from 'react';
import { Box, Button, useScreenSize } from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { SearchIcon } from 'icons';
import { SearchIconButton, SearchTextField } from './styles';

type Props = {
  value: string;
  onChange: (value: string) => void;
  onSubmit: FormEventHandler<HTMLFormElement>;
  onFocus: () => void;
};

function SearchInput(props: Props) {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * Render
   */
  return (
    <Box
      flex="1 1"
      component="form"
      id="product-search"
      onSubmit={props.onSubmit}
    >
      <SearchTextField
        id="search-term"
        type="text"
        onFocus={props.onFocus}
        value={props.value}
        onChange={handleInputChange}
        inputProps={{
          'data-testid': 'search-bar-input',
          autoComplete: 'off'
        }}
        InputProps={{
          endAdornment: (
            <>
              {!isSmallScreen && !!props.value && (
                <Button
                  variant="inline"
                  color="primaryLight"
                  size="small"
                  onClick={handleClear}
                  data-testid="search-bar-clear"
                >
                  {t('common.clear').toLowerCase()}
                </Button>
              )}
              <SearchIconButton
                data-testid="search-input-submit"
                color="primary"
                type="submit"
                disabled={!props.value}
              >
                <SearchIcon />
              </SearchIconButton>
            </>
          )
        }}
        fullWidth
      />
    </Box>
  );

  /**
   * Callback defs
   */
  function handleInputChange(
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) {
    props.onChange(e.target.value);
  }

  function handleClear() {
    props.onChange('');
  }
}

export default SearchInput;
