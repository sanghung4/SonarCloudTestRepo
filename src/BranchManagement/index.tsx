import { useMemo, useState } from 'react';
import {
  Column,
  useFlexLayout,
  usePagination,
  useSortBy,
  useTable
} from 'react-table';

import TablePageLayout from 'common/TablePageLayout';
import TableRenderer from 'common/TablePageLayout/TableRenderer';
import {
  BranchListItemFragment,
  useGetBranchesListQuery
} from 'generated/graphql';
import FlagCell from './FlagCell';
import PermissionRequired, { Permission } from 'common/PermissionRequired';
import EditModal from './EditModal';
import { orderBy } from 'lodash-es';
import { useTranslation } from 'react-i18next';
import { TruncatedCell } from 'utils/tableUtils';

/**
 * Component
 */
function BranchManagement() {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();
  /**
   * State
   */
  const [editOpen, setEditOpen] = useState(false);
  const [selectedBranch, setSelectedBranch] =
    useState<BranchListItemFragment>();

  /**
   * Data
   */
  const { data: branchesListData, loading: branchesListLoading } =
    useGetBranchesListQuery();

  /**
   * Memos
   */
  const displayedData = useMemo(() => {
    return (
      orderBy(branchesListData?.branches, (branch) => branch.branchId) ?? []
    );
  }, [branchesListData?.branches]);

  const columns = useMemo<Column<BranchListItemFragment>[]>(() => {
    return [
      {
        accessor: 'branchId',
        Header: `${t('branchManagement.erpId')}`,
        width: 75
      },
      {
        accessor: 'name',
        Header: `${t('branchManagement.branchName')}`,
        width: 300,
        Cell: TruncatedCell
      },
      {
        accessor: 'address1',
        Header: `${t('branchManagement.address')}`,
        Cell: ({ row }) =>
          `${row.original.address1}, ${row.original.city}, ${row.original.state} ${row.original.zip}`,
        width: 450
      },
      {
        accessor: 'isActive',
        Header: `${t('branchManagement.active')}`,
        Cell: ({ value }) => <FlagCell value={value} />
      },
      {
        accessor: 'isAvailableInStoreFinder',
        Header: `${t('branchManagement.inStoreFinder')}`,
        Cell: ({ value }) => <FlagCell value={value} />
      },
      {
        accessor: 'isPricingOnly',
        Header: `${t('branchManagement.pricingOnly')}`,
        Cell: ({ value }) => <FlagCell value={value} />
      },
      {
        accessor: 'isShoppable',
        Header: `${t('branchManagement.shoppable')}`,
        Cell: ({ value }) => <FlagCell value={value} />
      },
      {
        accessor: (data) =>
          [
            data.isBandK && 'Bath & Kitchen',
            data.isHvac && 'HVAC',
            data.isPlumbing && 'Plumbing',
            data.isWaterworks && 'Waterworks'
          ]
            .filter((i) => i)
            .join(', '),
        Header: `${t('branchManagement.division')}`,
        width: 350
      }
    ];
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  /**
   * Table
   */
  const tableInstance = useTable(
    {
      data: displayedData,
      columns
    },
    useSortBy,
    useFlexLayout,
    usePagination
  );

  /**
   * Render
   */
  return (
    <>
      <EditModal
        open={editOpen}
        selectedBranch={selectedBranch}
        onClose={handleEditClose}
      />
      <PermissionRequired
        permissions={[Permission.MANAGE_BRANCHES]}
        redirectTo="/"
      >
        <TablePageLayout
          loading={branchesListLoading}
          pageTitle={t('branchManagement.storeFinderAdmin')}
          table={
            <TableRenderer
              resultsCount={tableInstance.rows.length}
              resultsCountText={t('branchManagement.branches')}
              noResultsMessage={t('common.noResultsFound')}
              tableInstance={tableInstance}
              primaryKey="branchId"
              testId="store-finder-table"
              onRowClick={handleRowClick}
            />
          }
        />
      </PermissionRequired>
    </>
  );
  /**
   * Callback Defs
   */
  function handleRowClick(data: BranchListItemFragment) {
    setSelectedBranch(data);
    setEditOpen(true);
  }

  function handleEditClose() {
    setEditOpen(false);
    setSelectedBranch(undefined);
  }
}

export default BranchManagement;
