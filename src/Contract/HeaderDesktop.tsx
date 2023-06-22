import { useContext, useState } from 'react';

import {
  Box,
  Button,
  Card,
  Collapse,
  Divider,
  Grid,
  IconButton,
  styled,
  Tooltip,
  Typography
} from '@dialexa/reece-component-library';
import Dotdotdot from 'react-dotdotdot';
import { useTranslation } from 'react-i18next';

import { ContractContext } from 'Contract/ContractProvider';
import { noOverflowSx } from 'Contract/util/styles';
import { HelpIcon, MinusIcon, PlusIcon } from 'icons';
import { format } from 'utils/currency';
import { tooltipClasses, TooltipProps } from '@mui/material';

const CustomWidthTooltip = styled(({ className, ...props }: TooltipProps) => (
  <Tooltip {...props} classes={{ popper: className }} />
))({
  [`& .${tooltipClasses.tooltip}`]: {
    maxWidth: 250,
    marginLeft: -65
  }
});

export default function ContractHeaderDesktop() {
  /**
   * Custom hooks
   */
  const { t } = useTranslation();

  /**
   * Context
   */
  const { contractData: data, account } = useContext(ContractContext);

  /**
   * States
   */
  const [isCollapsed, setCollapsed] = useState(false);

  /**
   * Output
   */
  return (
    <Box component={Card} px={4} py={3} mb={3}>
      {/* ================== TITLE ================== */}
      <Grid container spacing={3} wrap="nowrap" alignItems="center">
        <Grid item>
          <Typography
            component="h5"
            fontSize={20}
            color="primary.main"
            noWrap
            fontWeight={500}
          >
            {t('contract.contractNum')}
            <Typography
              component="h5"
              fontSize={20}
              color="primary.main"
              fontWeight={500}
              noWrap
              display="inline"
              data-testid="contract-number-header"
            >
              {data?.contractNumber || t('common.na')}
            </Typography>
          </Typography>
        </Grid>
        <Grid item>
          <Typography
            component="h5"
            fontSize={20}
            noWrap
            fontWeight={500}
            data-testid="contract-name-header"
          >
            {data?.contractDescription || t('common.na')}
          </Typography>
        </Grid>
        {data?.purchaseOrderNumber ? (
          <Grid item>
            <Typography component="h5" fontSize={20} noWrap fontWeight={500}>
              {t('contract.poNumber')}
              <Typography
                component="h5"
                fontSize={20}
                fontWeight={500}
                noWrap
                display="inline"
                data-testid="purchase-order-number-header"
              >
                {data?.purchaseOrderNumber}
              </Typography>
            </Typography>
          </Grid>
        ) : null}
        <Grid item container justifyContent="flex-end">
          <Box>
            <IconButton
              onClick={handleToggleCollapse}
              color="primary"
              data-testid="contract-header-details-toggle-desktop"
            >
              {isCollapsed ? <PlusIcon /> : <MinusIcon />}
            </IconButton>
          </Box>
        </Grid>
      </Grid>
      {/* ================== COLLAPSABLE ================== */}
      <Collapse in={!isCollapsed}>
        <Box data-testid="contract-header-details-box-desktop">
          {/* -------------------- Section: Account Information -------------------- */}
          <Box pt={4}>
            <Typography color="primary.main" fontWeight={500}>
              {t('contract.accountInformation')}
            </Typography>
          </Box>
          <Grid container wrap="nowrap" spacing={2} mt={1} mb={2} mx={0.5}>
            {/* ---------- Customer Name ---------- */}
            <Grid item container xs={4} spacing={2} wrap="nowrap">
              <Grid item xs="auto" sx={noOverflowSx}>
                <Typography fontWeight={500} noWrap>
                  {t('contract.customerName')}
                </Typography>
              </Grid>
              <Grid item xs>
                <Dotdotdot clamp={4}>
                  <Typography data-testid="customer-name">
                    {account?.companyName || t('common.na')}
                  </Typography>
                </Dotdotdot>
              </Grid>
            </Grid>
            {/* ---------- Ship To Address ---------- */}
            <Grid item container xs={4} spacing={2} wrap="nowrap">
              <Grid item xs="auto" sx={noOverflowSx}>
                <Typography fontWeight={500} noWrap>
                  {t('contract.shipTo')}
                </Typography>
              </Grid>
              <Grid
                item
                xs
                sx={noOverflowSx}
                data-testid="ship-to-address-full"
              >
                <Typography data-testid="ship-to-address-1">
                  {data?.accountInformation?.shipToAddress?.address1 ||
                    t('common.na')}
                </Typography>
                {data?.accountInformation?.shipToAddress?.address2 ? (
                  <Typography data-testid="ship-to-address-2">
                    {data?.accountInformation?.shipToAddress?.address2}
                  </Typography>
                ) : null}
                {data?.accountInformation?.shipToAddress?.address3 ? (
                  <Typography data-testid="ship-to-address-3">
                    {data?.accountInformation?.shipToAddress?.address3}
                  </Typography>
                ) : null}
                <Typography data-testid="ship-to-address-4">
                  {data?.accountInformation?.shipToAddress?.city ?? ''}{' '}
                  {data?.accountInformation?.shipToAddress?.state ?? ''}
                  {data?.accountInformation?.branch?.state &&
                  data?.accountInformation?.branch?.zip
                    ? ', '
                    : null}
                  {data?.accountInformation?.shipToAddress?.zip ?? ''}
                </Typography>
                {data?.accountInformation?.shipToAddress?.country ? (
                  <Typography data-testid="ship-to-address-5">
                    {data?.accountInformation?.shipToAddress?.country}
                  </Typography>
                ) : null}
              </Grid>
            </Grid>
            {/* ---------- Branch Address ---------- */}
            <Grid item container xs={4} spacing={2} wrap="nowrap">
              <Grid item xs="auto" sx={noOverflowSx}>
                <Typography fontWeight={500} noWrap>
                  {t('contract.branch')}
                </Typography>
              </Grid>
              <Grid item xs sx={noOverflowSx} data-testid="branch-address-full">
                <Typography data-testid="branch-address-1">
                  {data?.accountInformation?.branch?.address1 || t('common.na')}
                </Typography>
                {data?.accountInformation?.branch?.address2 ? (
                  <Typography data-testid="branch-address-2">
                    {data?.accountInformation?.branch?.address2}
                  </Typography>
                ) : null}
                {data?.accountInformation?.branch?.address3 ? (
                  <Typography data-testid="branch-address-3">
                    {data?.accountInformation?.branch?.address3}
                  </Typography>
                ) : null}
                <Typography data-testid="branch-address-4">
                  {data?.accountInformation?.branch?.city ?? ''}{' '}
                  {data?.accountInformation?.branch?.state ?? ''}
                  {data?.accountInformation?.branch?.state &&
                  data?.accountInformation?.branch?.zip
                    ? ', '
                    : null}
                  {data?.accountInformation?.branch?.zip ?? ''}
                </Typography>
                {data?.accountInformation?.branch?.country ? (
                  <Typography data-testid="branch-address-5">
                    {data?.accountInformation?.branch?.country}
                  </Typography>
                ) : null}
              </Grid>
            </Grid>
          </Grid>
          {/* -------------------- Section: Dates -------------------- */}
          <Divider />
          <Box pt={2}>
            <Typography color="primary.main" fontWeight={500}>
              {t('contract.dates')}
            </Typography>
          </Box>
          <Grid container wrap="nowrap" spacing={2} mt={1} mb={2} mx={0.5}>
            {/* ---------- Order Date ---------- */}
            <Grid item container xs={4} spacing={2} wrap="wrap">
              <Grid item xs="auto">
                <Typography fontWeight={500}>
                  {t('contract.orderDate')}
                </Typography>
              </Grid>
              <Grid item xs>
                <Typography data-testid="contract-order-date">
                  {data?.contractDates?.contractDate || t('common.na')}
                </Typography>
              </Grid>
            </Grid>
            {/* ---------- First Release Date ---------- */}
            <Grid item container xs={4} spacing={2} wrap="wrap">
              <Grid item xs="auto">
                <Typography fontWeight={500}>
                  {t('contract.firstReleaseDate')}
                </Typography>
              </Grid>
              <Grid item xs>
                <Typography data-testid="contract-first-release-date">
                  {data?.contractDates?.firstReleaseDate || t('common.na')}
                </Typography>
              </Grid>
            </Grid>
            {/* ---------- Last Release Date ---------- */}
            <Grid item container xs={4} spacing={2} wrap="wrap">
              <Grid item xs="auto">
                <Typography fontWeight={500}>
                  {t('contract.lastReleaseDate')}
                </Typography>
              </Grid>
              <Grid item xs>
                <Typography data-testid="contract-last-release-date">
                  {data?.contractDates?.lastReleaseDate || t('common.na')}
                </Typography>
              </Grid>
            </Grid>
          </Grid>
          {/* -------------------- Section: Customer Information + Summary -------------------- */}
          <Divider />
          <Grid container wrap="nowrap" spacing={2} mt={1} mb={2}>
            {/* --------------- Sub-Section (LEFT): Customer Information --------------- */}
            <Grid
              item
              container
              spacing={2}
              xs={6}
              mr={2}
              flexDirection="column"
            >
              <Grid item xs="auto">
                <Typography color="primary.main" fontWeight={500}>
                  {t('contract.customerInformation')}
                </Typography>
              </Grid>
              <Grid item container wrap="wrap" ml={0.5} spacing={2} xs>
                {/* <==== Left ----- */}
                <Grid item container xs={6} spacing={2} flexDirection="column">
                  {/* ---------- Customer # ---------- */}
                  <Grid item container spacing={2} wrap="wrap">
                    <Grid item xs={6}>
                      <Typography fontWeight={500}>
                        {t('contract.customerNumber')}
                      </Typography>
                    </Grid>
                    <Grid item xs={'auto'}>
                      <Typography data-testid="customer-info-customer-number">
                        {data?.customerInfo?.customerNumber || t('common.na')}
                      </Typography>
                    </Grid>
                  </Grid>
                  {/* ---------- Job # ---------- */}
                  <Grid item container spacing={2} wrap="wrap">
                    <Grid item xs={6}>
                      <Typography fontWeight={500}>
                        {t('contract.jobNumber')}
                      </Typography>
                    </Grid>
                    <Grid item xs={'auto'}>
                      <Typography data-testid="customer-info-customer-job-number">
                        {data?.customerInfo?.jobNumber || t('common.na')}
                      </Typography>
                    </Grid>
                  </Grid>
                  {/* ---------- Entered By ---------- */}
                  <Grid item container spacing={2} wrap="wrap">
                    <Grid item xs={6}>
                      <Typography fontWeight={500}>
                        {t('contract.enteredBy')}
                      </Typography>
                    </Grid>
                    <Grid item xs="auto">
                      <Typography data-testid="customer-info-entered-by">
                        {data?.customerInfo?.enteredBy || t('common.na')}
                      </Typography>
                    </Grid>
                  </Grid>
                </Grid>
                {/* ----- Right ====> */}
                <Grid item container xs={6} spacing={2} flexDirection="column">
                  {/* ---------- PO # ---------- */}
                  <Grid item container spacing={2} wrap="wrap">
                    <Grid item xs={4}>
                      <Typography fontWeight={500}>
                        {t('contract.poNumber')}
                      </Typography>
                    </Grid>
                    <Grid item xs="auto">
                      <Typography data-testid="customer-info-po-number">
                        {data?.purchaseOrderNumber || t('common.na')}
                      </Typography>
                    </Grid>
                  </Grid>
                  {/* ---------- Job Name ---------- */}
                  <Grid item container spacing={2} wrap="wrap">
                    <Grid item xs={4}>
                      <Typography fontWeight={500}>
                        {t('contract.jobName')}
                      </Typography>
                    </Grid>
                    <Grid item xs="auto">
                      <Typography data-testid="customer-info-job-name">
                        {data?.jobName || t('common.na')}
                      </Typography>
                    </Grid>
                  </Grid>
                </Grid>
              </Grid>
            </Grid>
            {/* --------------- <Vertical Divider> --------------- */}
            <Box
              component={Divider}
              bgcolor="secondary04.main"
              mt={2}
              orientation="vertical"
              flexItem
            />
            {/* --------------- Sub-Section (RIGHT): Summary --------------- */}
            <Grid
              item
              container
              spacing={2}
              xs={6}
              ml={0}
              flexDirection="column"
            >
              <Grid item xs="auto">
                <Typography color="primary.main" fontWeight={500}>
                  {t('contract.summary')}
                </Typography>
              </Grid>
              <Grid item container wrap="wrap" mx={0.5} spacing={2} xs>
                {/* <==== Left ----- */}
                <Grid item container xs={5} spacing={2} flexDirection="column">
                  {/* ---------- Subtotal ---------- */}
                  <Grid item container spacing={2} wrap="nowrap">
                    <Grid item xs={6}>
                      <Typography fontWeight={500}>
                        {t('contract.subtotal')}
                      </Typography>
                    </Grid>
                    <Grid item xs="auto">
                      <Typography data-testid="summary-subtotal">
                        {format(
                          parseFloat(data?.contractSummary?.subTotal || '0')
                        )}
                      </Typography>
                    </Grid>
                  </Grid>
                  {/* ---------- Tax Amount ---------- */}
                  <Grid item container spacing={2} wrap="nowrap">
                    <Grid item xs={6}>
                      <Typography fontWeight={500}>
                        {t('contract.taxAmount')}
                      </Typography>
                    </Grid>
                    <Grid item xs="auto">
                      <Typography data-testid="summary-tax-amount">
                        {format(
                          parseFloat(data?.contractSummary?.taxAmount || '0')
                        )}
                      </Typography>
                    </Grid>
                  </Grid>
                  {/* ---------- Other Charges ---------- */}
                  <Grid item container spacing={2} wrap="wrap">
                    <Grid item xs={6}>
                      <Typography fontWeight={500}>
                        {t('contract.otherCharges')}
                      </Typography>
                    </Grid>
                    <Grid item xs="auto">
                      <Typography data-testid="summary-other-charges">
                        {format(
                          parseFloat(data?.contractSummary?.otherCharges || '0')
                        )}
                      </Typography>
                    </Grid>
                  </Grid>
                  {/* ---------- Total ---------- */}
                  <Grid item container spacing={2} wrap="wrap">
                    <Grid item xs={6}>
                      <Typography fontWeight={500} noWrap color="primary.main">
                        {t('contract.totals')}
                      </Typography>
                    </Grid>
                    <Grid item xs={6}>
                      <Typography
                        fontWeight={500}
                        sx={{ textDecoration: 'underline' }}
                        data-testid="summary-total-amount"
                      >
                        {format(
                          parseFloat(data?.contractSummary?.totalAmount || '0')
                        )}
                      </Typography>
                    </Grid>
                  </Grid>
                </Grid>
                {/* ----- Right ====> */}
                <Grid
                  item
                  container
                  xs="auto"
                  spacing={2}
                  flexDirection="column"
                >
                  {/* ---------- Invoice To-Date ---------- */}
                  <Grid
                    item
                    container
                    spacing={2}
                    wrap="wrap"
                    textAlign="center"
                  >
                    <Grid item xs={7}>
                      <Typography fontWeight={500} component="span">
                        {t('contract.invoiceToDate')}
                      </Typography>

                      <CustomWidthTooltip
                        disableFocusListener
                        enterTouchDelay={0}
                        placement="bottom-start"
                        PopperProps={{ style: { marginLeft: -60 } }}
                        title={
                          <Grid container>
                            <Grid item xs={1}>
                              <HelpIcon />
                            </Grid>
                            <Grid item xs={11}>
                              {t('contract.invoiceToDateToolTipValue')}
                            </Grid>
                          </Grid>
                        }
                      >
                        <Button
                          variant="inline"
                          size="small"
                          sx={{ minWidth: '20px', ml: 1, mt: -0.3 }}
                        >
                          <HelpIcon />
                        </Button>
                      </CustomWidthTooltip>
                    </Grid>
                    <Grid item xs={5}>
                      <Typography data-testid="summary-invoice-to-date">
                        {format(
                          parseFloat(
                            data?.contractSummary?.invoicedToDateAmount || '0'
                          )
                        )}
                      </Typography>
                    </Grid>
                  </Grid>

                  {/* ---------- Shipped To-Date ---------- */}
                  <Grid item container spacing={2} wrap="wrap">
                    {/* <Grid item xs={'auto'}>
                      <Typography fontWeight={500}>
                        {t('contract.shippedToDate')}
                      </Typography>
                    </Grid>
                    <Grid item xs="auto">
                      <Typography data-testid="summary-shipped-to-date">
                        {format(
                          parseFloat(
                            data?.contractSummary?.lastShipmentDate || '0'
                          )
                        )}
                      </Typography>
                    </Grid> */}
                  </Grid>
                </Grid>
              </Grid>
            </Grid>
          </Grid>
        </Box>
      </Collapse>
    </Box>
  );

  /**
   * Handle Event
   */
  function handleToggleCollapse() {
    setCollapsed(!isCollapsed);
  }
}
