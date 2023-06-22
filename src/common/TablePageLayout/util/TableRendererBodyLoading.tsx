import {
  Box,
  CircularProgress,
  Typography
} from '@dialexa/reece-component-library';

import Link from 'common/Link';
import { TableRendererProps } from 'common/TablePageLayout/TableRenderer';

export function TableRendererBodyLoading<TData extends object>(
  props: TableRendererProps<TData>
) {
  /**
   * Props
   */
  const {
    isWaterworks,
    loading,
    noResultsContactBranch,
    noResultsContactMessage,
    noResultsMessage
  } = props;

  /**
   * Render
   */
  return (
    <tbody>
      <tr>
        <td colSpan={6}>
          <Box
            px={3}
            py={8}
            display="flex"
            justifyContent="center"
            textAlign="center"
          >
            {loading ? (
              <CircularProgress size={50} color="primary02.main" />
            ) : isWaterworks ? (
              <Box>
                <Typography variant="h4" color="secondary03.main">
                  {noResultsMessage}
                </Typography>
                <Typography
                  variant="h5"
                  color="secondary03.main"
                  textAlign="center"
                >
                  {noResultsContactMessage}{' '}
                  <Link to="/support" underline="always">
                    {noResultsContactBranch}
                  </Link>
                </Typography>
              </Box>
            ) : (
              <Typography variant="h4" color="text.secondary" fontWeight={700}>
                {noResultsMessage}
              </Typography>
            )}
          </Box>
        </td>
      </tr>
    </tbody>
  );
}
