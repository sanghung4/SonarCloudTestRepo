import React, { ChangeEvent, FormEvent } from 'react';

import {
  Box,
  Grid,
  TextField,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import FilterActionButtons from 'common/TablePageLayout/FilterActionButtons';

type Props = {
  search: string;
  setSearch: (search: string) => void;
  onSubmit: () => void;
  onReset: () => void;
  resultsCount?: number;
};

function Filters(props: Props) {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  return (
    <form onSubmit={handleSubmit}>
      <Grid container spacing={2} alignItems="flex-end">
        <Grid item xs={isSmallScreen ? 12 : 4}>
          <TextField
            label={t('common.searchBy')}
            placeholder={t('quotes.searchPlaceholder')}
            value={props.search}
            onChange={(e: ChangeEvent<HTMLInputElement>) =>
              props.setSearch(e.target.value)
            }
            fullWidth
          />
        </Grid>
        <FilterActionButtons
          dirty={Boolean(props.search && props.search !== '')}
          hideApplyOnMobile={false}
          resultsCount={props.resultsCount}
          onViewResults={props.onSubmit}
          onReset={props.onReset}
        />
      </Grid>
      <Box component="button" type="submit" sx={{ display: 'none' }} />
    </form>
  );

  function handleSubmit(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();

    props.onSubmit();
  }
}

export default Filters;
