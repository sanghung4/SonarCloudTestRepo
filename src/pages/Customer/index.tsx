import { useNavigate, useParams } from 'react-router-dom';

import { useApiCustomerDetail } from 'api/customer.api';
import Container from 'common/Container';
import { Button } from 'components/Button';
import LoadingIcon from 'components/LoadingIcon/LoadingIcon';
import CatalogStatus from 'pages/Customer/CatalogStatus';
import { ReactComponent as ArchiveIcon } from 'resources/icons/archive.svg';

/**
 * Types
 */
type CustomerRouteParams = {
  id: string;
};

/**
 * Component
 */
function Customer() {
  /**
   * Custom hooks
   */
  const { id } = useParams<CustomerRouteParams>();
  const navigate = useNavigate();

  /**
   * API
   */
  const { data, loading } = useApiCustomerDetail(id);
  const [catalog] = data?.catalogs ?? [undefined];

  /**
   * Render
   */
  if (loading) {
    return (
      <Container data-testid="customer_container">
        <div className="w-full flex justify-center">
          <LoadingIcon size="xxl" data-testid="customer_loading" />
        </div>
      </Container>
    );
  }
  return (
    <Container data-testid="customer_container">
      <h2
        className="text-primary-1-100 font-semibold text-4xl mb-8"
        data-testid="customer_detail-name"
      >
        {data?.name ?? 'N/A'}
      </h2>
      <div className="grid grid-cols-2">
        {/* LEFT COLUMN */}
        <div className="flex flex-col gap-6">
          {/* Customer Information */}
          <div className="flex flex-col">
            <span className="text-primary-3-100 text-base">
              Customer Information
            </span>
            <div className="text-primary-3-100 text-xl">
              <span>Bill-To Account: </span>
              <b data-testid="customer_detail-id">
                {data?.customerId ?? 'N/A'}
              </b>
            </div>
            <span
              className="text-primary-3-100 text-xl"
              data-testid="customer_detail-branch-id"
            >
              Branch# {data?.branchId ?? 'N/A'}
            </span>
            <span
              className="text-primary-3-100 text-xl"
              data-testid="customer_detail-branch-name"
            >
              {data?.branchName ?? ''}
            </span>
          </div>
          {/* Regions */}
          <div className="flex flex-col">
            <span className="text-primary-1-100 text-xl font-semibold">
              Regions:
            </span>
            <span
              className="text-primary-3-100 text-base"
              data-testid="customer_detail-regions"
            >
              {data?.regions?.map(({ name }) => name).join(', ') || 'N/A'}
            </span>
          </div>
          {/* Contact */}
          <div className="flex flex-col">
            <span className="text-primary-1-100 text-xl font-semibold">
              Contact:
            </span>
            <span
              className="text-primary-3-100 text-base"
              data-testid="customer_detail-contact-name"
            >
              {data?.contactName ?? 'N/A'}
            </span>
            <span
              className="text-primary-3-100 text-base"
              data-testid="customer_detail-contact-number"
            >
              {data?.contactPhone ?? ''}
            </span>
          </div>
        </div>
        {/* RIGHT COLUMN */}
        <div className="flex flex-col gap-6">
          {/* Catalog Information */}
          <div className="flex flex-col">
            <span className="text-primary-3-100 text-base">
              Catalog Information
            </span>
            <div className="text-primary-1-100 text-xl">
              <span>Catalog Status: </span>
              <CatalogStatus
                text={catalog?.status}
                data-testid="customer_detail-catalog-status"
              />
            </div>
          </div>
          {/* Catalog Name */}
          <div className="flex flex-col">
            <div className="text-primary-3-100 text-xl">
              <span>Catalog Name: </span>
              <b
                className="text-primary-1-100"
                data-testid="customer_detail-catalog-name"
              >
                {catalog?.name ?? 'N/A'}
              </b>
            </div>
            <span
              className="text-primary-3-100 text-xl"
              data-testid="customer_detail-file-name"
            >
              File Name: {catalog?.fileName ?? 'N/A'}
            </span>
          </div>
          {/* Catalog Details */}
          <div className="flex flex-col">
            <span className="text-primary-1-100 text-xl font-semibold">
              Catalog Details:
            </span>
            <span
              className="text-primary-3-100 text-base"
              data-testid="customer_detail-sku-qty"
            >
              SKU Quantity: {catalog?.skuQuantity ?? 0}
            </span>
            <span
              className="text-primary-3-100 text-base"
              data-testid="customer_detail-last-updated"
            >
              Last Updated:{' '}
              {catalog?.lastUpdate
                ? new Date(catalog.lastUpdate).toLocaleDateString('en-us')
                : '--'}
            </span>
          </div>
          {/* Buttons */}
          <div className="flex gap-4">
            <Button
              type="button"
              title="View Catalog"
              disabled={!catalog}
              className="bg-primary-1-100 text-white"
              data-testid="customer_detail-view-button"
              onClick={() => navigate(`/catalog/detail/${catalog?.id}`)}
            />
            <Button
              type="button"
              title="Catalog Archive"
              iconPosition="left"
              icon={<ArchiveIcon />}
              disabled={!catalog}
              className="bg-white text-primary-1-100 border-primary-1-100 border-2"
              data-testid="customer_detail-archive-button"
            />
          </div>
        </div>
      </div>
    </Container>
  );
}
export default Customer;
