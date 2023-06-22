import React, { Dispatch, useContext, useRef, useState } from 'react';

import { Button, Grid, Menu, MenuItem } from '@dialexa/reece-component-library';
import {
  DeleteBinIcon,
  DownloadIcon,
  EditIcon,
  FileUploadIcon,
  MoreActions
} from 'icons';
import { filenameValidation, escapeCSVString } from 'utils/strings';
import {
  GetExportListIntoCsvQuery,
  GetProductPricingQuery,
  useGetExportListIntoCsvLazyQuery
} from 'generated/graphql';
import { CSVLink } from 'react-csv';
import { useTranslation } from 'react-i18next';
import { useHistory } from 'react-router-dom';
import { BranchContext } from 'providers/BranchProvider';
import { AuthContext } from 'AuthProvider';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import { ListContext } from '../providers/ListsProvider';
import { ListActionsButtonContainer } from 'Lists/util/styled';

type Props = {
  pricingData?: GetProductPricingQuery;
  pricingLoading?: boolean;
  setDialogOpen: Dispatch<boolean>;
};

export default function ListActions(props: Props) {
  const { pricingData, pricingLoading, setDialogOpen } = props;

  const headers = [
    { label: 'Part #', key: 'partNumber' },
    { label: 'Description', key: 'description' },
    { label: 'MFR Name', key: 'mfrName' },
    { label: 'QTY', key: 'quantity' },
    { label: 'Price', key: 'price' },
    { label: 'MFR #', key: 'mfrNumber' }
  ];

  /**
   * Custom hooks
   */
  const history = useHistory();
  const { t } = useTranslation();

  /**
   * Context
   */
  const { deleteListLoading, lists, selectedList, setRenaming } =
    useContext(ListContext);
  const { shippingBranch } = useContext(BranchContext);
  const { profile } = useContext(AuthContext);
  const { selectedAccounts } = useSelectedAccountsContext();

  /**
   * State
   */
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
  const [listLineItems, setListLineItems] = useState<
    GetExportListIntoCsvQuery['exportListToCSV']['listLineItems']
  >([]);

  /**
   * Ref
   */
  const csvLinkRef = useRef<
    CSVLink & HTMLAnchorElement & { link: HTMLAnchorElement }
  >(null);

  /**
   * Data
   */
  const [getExportListIntoCSV, { loading: getExportListIntoCSVLoading }] =
    useGetExportListIntoCsvLazyQuery({
      notifyOnNetworkStatusChange: true,
      onCompleted: (data) => {
        if (data?.exportListToCSV && data.exportListToCSV.listLineItems) {
          if (csvLinkRef.current && !getExportListIntoCSVLoading) {
            const pricingMap: { [partNumber: string]: number } =
              pricingData?.productPricing.products?.reduce((obj, pricing) => {
                const { sellPrice, productId } = pricing;
                return { ...obj, [productId]: sellPrice };
              }, {}) || {};
            const escapedListLineItems = data.exportListToCSV.listLineItems.map(
              (lineItem) => ({
                ...lineItem,
                description: escapeCSVString(lineItem.description ?? ''),
                mfrName: escapeCSVString(lineItem.mfrName ?? ''),
                price: pricingMap?.[lineItem.partNumber]
              })
            );
            setListLineItems(escapedListLineItems);
            csvLinkRef.current.link.click();
          }
        } else {
          throw new Error();
        }
      }
    });
  const open = !!anchorEl;

  /**
   * Callbacks
   */
  const handleClose = () => {
    setAnchorEl(null);
  };

  const handleDownload = () => {
    const variables = {
      listId: selectedList?.id ?? '',
      userId: profile?.userId ?? '',
      shipToAccountId: selectedAccounts.shipTo?.id ?? '',
      branchId: shippingBranch?.branchId ?? ''
    };
    getExportListIntoCSV({ variables });
    setTimeout(() => setAnchorEl(null), 3000);
  };

  return (
    <ListActionsButtonContainer>
      <Button
        variant="inline"
        data-testid="list-more-actions-button"
        aria-controls={open ? 'list-actions-menu' : undefined}
        aria-haspopup="true"
        endIcon={<MoreActions style={{ backgroundColor: 'white' }} />}
        aria-expanded={open}
        onClick={(event) => setAnchorEl(event.currentTarget)}
      >
        {t('lists.moreActions')}
      </Button>
      <Menu
        id="list-actions-menu"
        anchorEl={anchorEl}
        open={open}
        onClose={handleClose}
        MenuListProps={{
          'aria-labelledby': 'list-more-actions-button'
        }}
      >
        <MenuItem>
          <Grid pt={1} item md="auto" xs={6}>
            <Button
              variant="inline"
              startIcon={<DownloadIcon />}
              disabled={!lists.length || pricingLoading}
              onClick={handleDownload}
              data-testid="download-list-button"
            >
              {getExportListIntoCSVLoading
                ? t('common.loading')
                : t('lists.downloadList')}
            </Button>
            <CSVLink
              uFEFF={false}
              headers={headers}
              filename={filenameValidation(selectedList?.name ?? 'unknown')}
              data={listLineItems}
              ref={csvLinkRef}
            />
          </Grid>
        </MenuItem>
        <MenuItem onClick={handleClose}>
          <Button
            variant="inline"
            startIcon={<FileUploadIcon />}
            onClick={() => history.push('/lists/upload')}
            data-testid="list-file-upload-mobile"
          >
            {t('lists.uploadFile')}
          </Button>
        </MenuItem>
        <MenuItem onClick={handleClose}>
          <Button
            variant="inline"
            data-testid="rename-list-button"
            onClick={() => setRenaming(true)}
            startIcon={<EditIcon />}
            disabled={!lists.length}
          >
            {t('lists.renameList')}
          </Button>
        </MenuItem>
        <MenuItem onClick={handleClose}>
          <Grid pb={1} item md="auto" xs={6}>
            <Button
              variant="inline"
              data-testid="delete-list-button"
              onClick={() => setDialogOpen(true)}
              startIcon={<DeleteBinIcon />}
              disabled={!lists.length || deleteListLoading}
            >
              {t('lists.deleteList')}
            </Button>
          </Grid>
        </MenuItem>
      </Menu>
    </ListActionsButtonContainer>
  );
}
