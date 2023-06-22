import { Dispatch, ReactNode } from 'react';

import {
  Autocomplete,
  Box,
  CircularProgress,
  Grid,
  TextField,
  useScreenSize
} from '@dialexa/reece-component-library';
import { FN } from '@reece/global-types';
import { isUndefined } from 'lodash-es';
import { useTranslation } from 'react-i18next';

import {
  useBranchFilterAutocomplete,
  autocompleteOnChange
} from 'CustomerApproval/util/branchFilterUtil';
import { CoercedUser } from 'CustomerApproval/util/types';
import FilterActionButtons from 'common/TablePageLayout/FilterActionButtons';

type Props = {
  userList?: CoercedUser[];
  filterValue: string;
  setFilter: Dispatch<string>;
  submitFilter: FN;
  resetFilters: FN;
};

export default function BranchFilter(props: Props) {
  /**
   * Custom hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();
  const autocompleteOptions = useBranchFilterAutocomplete(props.userList);
  const undefinedUserList = isUndefined(props.userList);

  /**
   * Special component
   */
  function inputStartAdornment(startAdornment: ReactNode) {
    return (
      <>
        {undefinedUserList && (
          <Box ml={1}>
            <CircularProgress color="primary02.main" size={20} />
          </Box>
        )}
        {startAdornment}
      </>
    );
  }

  /**
   * Render
   */
  return (
    <Grid container spacing={2} alignItems="flex-end">
      <Grid item xs={isSmallScreen ? 12 : 4}>
        <Box marginBottom="-4px">
          <Autocomplete
            fullWidth
            value={undefinedUserList ? '' : props.filterValue || '-'}
            disableClearable
            disabled={undefinedUserList}
            id="branch-filter-autocomplete"
            options={autocompleteOptions}
            onChange={autocompleteOnChange(props.setFilter)}
            renderInput={(params) => (
              <TextField
                {...params}
                label={t('common.branch')}
                data-testid="branch-filter-input"
                id="branch-filter-input"
                InputProps={{
                  ...params.InputProps,
                  startAdornment: inputStartAdornment(
                    params.InputProps.startAdornment
                  )
                }}
              />
            )}
            data-testid="branch-filter"
          />
        </Box>
      </Grid>
      <FilterActionButtons
        dirty={props.filterValue !== t('common.all')}
        hideApplyOnMobile
        onViewResults={props.submitFilter}
        onReset={props.resetFilters}
      />
    </Grid>
  );
}
