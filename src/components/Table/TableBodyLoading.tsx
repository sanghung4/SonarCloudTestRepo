import LoadingIcon from 'components/LoadingIcon/LoadingIcon';
import { TableProps, TemplateObj } from 'components/Table';

/**
 * Table body when loading or empty
 */
export function TableRendererBodyLoading<TData extends object = TemplateObj>(
  props: TableProps<TData>
) {
  /**
   * Props
   */
  const { 'data-testid': testid, loading, noResultsMessage } = props;

  /**
   * Render
   */
  return (
    <tbody>
      <tr>
        <td colSpan={1000} className="p-0">
          {loading ? (
            <div className="px-3 py-8 flex justify-center text-center bg-white">
              <LoadingIcon size={40} data-testid={`${testid}-loading`} />
            </div>
          ) : (
            <div className="px-3 py-8 flex justify-center text-center bg-secondary-2-20">
              <h4
                className="font-medium text-secondary-2-100"
                data-testid={`${testid}-no-results`}
              >
                {noResultsMessage}
              </h4>
            </div>
          )}
        </td>
      </tr>
    </tbody>
  );
}
