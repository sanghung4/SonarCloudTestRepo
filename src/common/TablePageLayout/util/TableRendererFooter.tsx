import {
  Box,
  Grid,
  Pagination,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { TableRendererProps } from 'common/TablePageLayout/TableRenderer';
import { handleGoToPage } from 'utils/tableUtils';

export function TableRendererFooter<TData extends object>(
  props: TableRendererProps<TData>
) {
  /**
   * Props
   */
  const {
    resultsCount,
    resultsCountText,
    tableInstance: { gotoPage, nextPage, pageCount, previousPage, state }
  } = props;
  /**
   * Custom hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * Render
   */
  return (
    <Box px={3} py={2}>
      <Grid
        container
        item
        xs={12}
        direction={isSmallScreen ? 'column-reverse' : 'row'}
        justifyContent={isSmallScreen ? 'center' : 'space-between'}
      >
        <Grid container item xs="auto" justifyContent="center">
          <Box
            textAlign="center"
            mx={1}
            my={0.5}
            data-testid={`total-${resultsCountText?.toLowerCase()}-count`}
          >
            {!!resultsCount && `${resultsCount} ${resultsCountText}`}
          </Box>
        </Grid>
        {Boolean(pageCount && resultsCount) && (
          <Grid
            container
            item
            justifyContent={isSmallScreen ? 'center' : 'flex-end'}
            xs
          >
            <Pagination
              current={state.pageIndex + 1}
              count={pageCount}
              ofText={t('common.of')}
              onChange={handleGoToPage(gotoPage)}
              onPrev={previousPage}
              onNext={nextPage}
              data-testid="pagination-table-footer"
              dataTestIdCurrentPage="current-page-number"
              dataTestIdTotalNumberOfPages="total-number-of-pages"
            />
          </Grid>
        )}
      </Grid>
    </Box>
  );
}
