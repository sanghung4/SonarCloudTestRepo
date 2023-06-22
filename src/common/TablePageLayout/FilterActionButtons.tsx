import {
  Button,
  Grid,
  Typography,
  DateRange as DateRangeType,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { isUndefined } from 'lodash-es';
import { statusOptions } from 'Invoices';
import {
  FilterActionButtonContainer,
  ResultsInfoContainer
} from './util/styles';

type Props = {
  dirty?: boolean;
  resultsCount?: number;
  onViewResults: () => void;
  applied?: boolean;
  onReset: () => void;
  range?: DateRangeType;
  invoiceStatus?: string;
  hideApplyOnMobile: boolean;
  warningMessage?: string;
};

function FilterActionButtons(props: Props) {
  /**
   * Custom hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  return (
    <>
      {(!isSmallScreen || (isSmallScreen && !props.hideApplyOnMobile)) && (
        <FilterActionButtonContainer item xs={12} md="auto">
          <Button
            data-testid="branch-filter-set-filter"
            onClick={props.onViewResults}
            color={props.applied ? 'success' : undefined}
            disabled={
              props.dirty === false ||
              props.warningMessage === 'error' ||
              ((props.invoiceStatus === statusOptions.All ||
                props.invoiceStatus === statusOptions.Closed) &&
                (!props.range?.from || !props.range?.to))
            }
            fullWidth
            sx={{ mb: 1.5 }}
          >
            {props.applied
              ? t('invoices.filterApplied')
              : t('common.viewResults')}
          </Button>
        </FilterActionButtonContainer>
      )}
      {!!(props.dirty && props.onReset) && (
        <FilterActionButtonContainer item xs={12} md="auto">
          <Button
            data-testid="reset-button"
            variant="secondary"
            onClick={props.onReset}
            fullWidth
            sx={{ mb: isSmallScreen ? 0 : 1.5 }}
          >
            {t('common.reset')}
          </Button>
        </FilterActionButtonContainer>
      )}
      {!isUndefined(props.resultsCount) && (
        <ResultsInfoContainer item xs={12} md="auto">
          <Grid
            container
            item
            md
            justifyContent={isSmallScreen ? 'center' : 'flex-start'}
            alignContent="center"
            pb={isSmallScreen ? 0 : 2.75}
          >
            <Typography variant="subtitle2" data-testid="results">
              {`${props.resultsCount} ${t('common.result', {
                count: props.resultsCount
              })}`}
            </Typography>
          </Grid>
        </ResultsInfoContainer>
      )}
    </>
  );
}

export default FilterActionButtons;
