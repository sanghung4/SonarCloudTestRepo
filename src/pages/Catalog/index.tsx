import { useMemo, useState } from 'react';

import { noop } from 'lodash-es';
import { useParams, useSearchParams } from 'react-router-dom';

import { useApiCatalogDetail } from 'api/catalog.api';
import Container from 'common/Container';
import { Button } from 'components/Button';
import { SearchInput } from 'components/Input';
import { Table } from 'components/Table';
import { catalogListTableConfig } from 'pages/Catalog/tableConfig';
import TitleChangeInput from 'pages/Catalog/TitleChangeInput';
import { UploadedFile } from 'pages/Catalog/UploadedFileItem';
import UploadButton from 'pages/Catalog/UploadModal';
import { ReactComponent as BackIcon } from 'resources/icons/back.svg';

/**
 * Config
 */
const ITEMS_PER_PAGE = 10;

/**
 * Component
 */
function Catalog() {
  /**
   * Custom hooks
   */
  const { id } = useParams<{ id: string }>();
  const [[[, page]], setSearchParam] = useSearchParams([['page', '1']]);
  const currentPage = parseInt(page ?? '1');

  /**
   * States
   */
  const [uploadIds, setUploadIds] = useState<string[]>([]);
  const [catalogTitle, setCatalogTitle] = useState('N/A');

  /**
   * API
   */
  const { data, loading, refetch } = useApiCatalogDetail(
    uploadIds,
    {
      catalogId: id,
      perPage: ITEMS_PER_PAGE,
      page: currentPage
    },
    {
      onCompleted: ({ data }) => setCatalogTitle(data.catalog?.name ?? 'N/A')
    }
  );

  /**
   * Memo
   */
  const tableInstance = useMemo(
    () => catalogListTableConfig(data?.results),
    [data?.results]
  );

  /**
   * Callbacks
   */
  const onChangeValue = (uploads: UploadedFile[]) => {
    const uIds = uploads.map(({ id }) => id ?? '');
    setUploadIds(uIds);
    setSearchParam([['page', '1']]);
    refetch({ catalogId: id, page: 1, perPage: ITEMS_PER_PAGE }, uIds);
  };
  const updateCatalogTitle = (catalogTitle: string) => {
    setCatalogTitle(catalogTitle);
  };
  const updatePage = async (newPage: number) => {
    setSearchParam([['page', `${newPage}`]]);
    refetch(
      { catalogId: id, page: newPage, perPage: ITEMS_PER_PAGE },
      uploadIds
    );
  };

  /**
   * Render
   */
  return (
    <Container data-testid="catalog_container">
      {/* HEADER */}
      <div className="flex mb-8">
        <div className="flex items-center grow">
          <button
            className="text-primary-1-100"
            type="button"
            data-testid="catalog_back"
            onClick={
              // istanbul ignore next
              () => console.log('BACK')
            }
          >
            <BackIcon />
          </button>

          <TitleChangeInput
            catalogTitle={catalogTitle}
            updateCatalogTitle={updateCatalogTitle}
            id={id ?? ''}
          />

          <h4
            className="text-primary-3-80 font-normal text-2xl ml-4"
            data-testid="catalog-product-count"
          >
            {`(${data?.totalItems ?? 0} Products)`}
          </h4>
        </div>
        <div className="flex flex-col text-primary-3-100 text-right">
          <h5 className="text-xl font-medium" data-testid="catalog-customer">
            {data?.customer?.name ?? 'N/A'}
          </h5>
          <span className="text-base" data-testid="catalog-customer-id">
            {data?.customer?.customerId ?? 'N/A'}
          </span>
        </div>
      </div>
      {/* Action Bar */}
      <div className="flex mb-8">
        <div className="flex-1">
          <SearchInput
            label="Search Products within Catalog:"
            type="search"
            placeholder="Input Product Name, Description, Part #, or MFR#..."
            onSearch={noop}
            data-testid="catalog-search"
            disabled
          />
        </div>
        <div className="flex-1">
          <Button
            type="button"
            title="Filters"
            disabled
            className="ml-6 mt-8 mb-5 bg-white text-primary-1-100 border-primary-1-100 border-2"
            data-testid="catalog-filters-button"
          />
        </div>
        <div className="flex">
          <UploadButton
            disabled={loading || !data?.customer?.id}
            name={data?.catalog?.name}
            customerId={data?.customer?.id ?? ''}
            onChangeValue={onChangeValue}
          />
          <Button
            type="button"
            title="Save Changes"
            className="ml-4 mt-8 mb-5 bg-primary-1-100 text-white"
            disabled={loading}
            onClick={
              // istanbul ignore next
              () => console.log('SAVE')
            }
            data-testid="catalog-save-button"
          />
        </div>
      </div>
      {/* Table */}
      <Table
        className="w-full my-6"
        noResultsMessage="No products found in catalog. Pease edit the catalog to upload file or add products."
        loading={loading}
        table={tableInstance}
        data-testid="catalog-table"
        showItemCount
        itemCount={data?.totalItems}
        showPagination
        currentPage={data?.page}
        pages={data?.totalPages}
        onPageChange={updatePage}
      />
    </Container>
  );
}
export default Catalog;
