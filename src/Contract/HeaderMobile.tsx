import { useContext, useState } from 'react';

import {
  Box,
  Button,
  Card,
  Collapse,
  Divider,
  Grid,
  IconButton,
  Tooltip,
  styled,
  Typography
} from '@dialexa/reece-component-library';
import { format } from 'utils/currency';
import { useTranslation } from 'react-i18next';

import { HelpIcon, MinusIcon, PlusIcon } from 'icons';
import { ContractContext } from 'Contract/ContractProvider';
import { noOverflowSx } from 'Contract/util/styles';
import { tooltipClasses, TooltipProps } from '@mui/material';

const CustomWidthTooltip = styled(({ className, ...props }: TooltipProps) => (
  <Tooltip {...props} classes={{ popper: className }} />
))({
  [`& .${tooltipClasses.tooltip}`]: {
    maxWidth: 260,
    marginLeft: -65
  }
});

export default function ContractHeaderMobile() {
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
  const [seeMore, setSeeMore] = useState(false);
  const [isTooltipOpen, setIsTooltipOpen] = useState(false);

  /**
   * Output
   */
  return (
    <Box component={Card} px={2} py={1} mb={2}>
      {/* ================== TITLE ================== */}
      <Grid container spacing={1} wrap="nowrap" alignItems="center">
        <Grid item sx={noOverflowSx} xs>
          <Typography
            component="h5"
            noWrap
            fontSize={16}
            fontWeight={500}
            color="primary.main"
          >
            {t('contract.contractNum')}
            <Typography
              component="h5"
              noWrap
              fontSize={16}
              fontWeight={500}
              color="primary.main"
              display="inline"
              data-testid="contract-number-header"
            >
              {data?.contractNumber || t('common.na')}
            </Typography>
          </Typography>
          <Typography
            component="h5"
            noWrap
            fontSize={16}
            fontWeight={500}
            data-testid="contract-name-header"
          >
            {data?.contractDescription || t('common.na')}
          </Typography>
        </Grid>
        <Grid item container justifyContent="flex-end" xs="auto">
          <Box>
            <IconButton
              onClick={handleToggleCollapse}
              color="primary"
              data-testid="contract-header-details-toggle-mobile"
            >
              {isCollapsed ? (
                <PlusIcon width={24} height={24} />
              ) : (
                <MinusIcon width={24} height={24} />
              )}
            </IconButton>
          </Box>
        </Grid>
      </Grid>
      {/* ================== COLLAPSABLE ================== */}
      <Collapse in={!isCollapsed}>
        <Box data-testid="contract-header-details-box-mobile">
          {/* -------------------- Section: Account Information -------------------- */}
          <Box pt={2} sx={noOverflowSx}>
            <Typography
              fontSize={14}
              fontWeight={500}
              color="primary.main"
              noWrap
            >
              {t('contract.accountInformation')}
            </Typography>
          </Box>
          <Grid
            container
            wrap="nowrap"
            flexDirection="column"
            spacing={1}
            mt={0}
            mb={1}
            mx={0.25}
          >
            {/* ---------- Customer Name ---------- */}
            <Grid item container spacing={2} wrap="nowrap">
              <Grid item xs={5} sx={noOverflowSx}>
                <Typography fontSize={14} fontWeight={500} noWrap>
                  {t('contract.customerName')}
                </Typography>
              </Grid>
              <Grid item xs={7}>
                <Typography fontSize={14} data-testid="customer-name">
                  {account?.companyName || t('common.na')}
                </Typography>
              </Grid>
            </Grid>
            {/* ---------- Ship To Address ---------- */}
            <Grid item container spacing={2} wrap="nowrap">
              <Grid item xs={5} sx={noOverflowSx}>
                <Typography fontSize={14} fontWeight={500} noWrap>
                  {t('contract.shipTo')}
                </Typography>
              </Grid>
              <Grid
                item
                xs={7}
                sx={noOverflowSx}
                data-testid="ship-to-address-full"
              >
                <Typography
                  fontSize={14}
                  noWrap
                  data-testid="ship-to-address-1"
                >
                  {data?.accountInformation?.shipToAddress?.address1 ||
                    t('common.na')}
                </Typography>
                {data?.accountInformation?.shipToAddress?.address2 ? (
                  <Typography
                    fontSize={14}
                    noWrap
                    data-testid="ship-to-address-2"
                  >
                    {data?.accountInformation?.shipToAddress?.address2}
                  </Typography>
                ) : null}
                {data?.accountInformation?.shipToAddress?.address3 ? (
                  <Typography
                    fontSize={14}
                    noWrap
                    data-testid="ship-to-address-3"
                  >
                    {data?.accountInformation?.shipToAddress?.address3}
                  </Typography>
                ) : null}
                <Typography
                  fontSize={14}
                  noWrap
                  data-testid="ship-to-address-4"
                >
                  {data?.accountInformation?.shipToAddress?.city ?? null}{' '}
                  {data?.accountInformation?.shipToAddress?.state ?? null}
                  {data?.accountInformation?.branch?.state &&
                  data?.accountInformation?.branch?.zip
                    ? ', '
                    : null}
                  {data?.accountInformation?.shipToAddress?.zip ?? null}
                </Typography>
                {data?.accountInformation?.shipToAddress?.country ? (
                  <Typography
                    fontSize={14}
                    noWrap
                    data-testid="ship-to-address-5"
                  >
                    {data?.accountInformation?.shipToAddress?.country}
                  </Typography>
                ) : null}
              </Grid>
            </Grid>
            {/* ---------- Branch Address ---------- */}
            <Grid item container spacing={2} wrap="nowrap">
              <Grid item xs={5} sx={noOverflowSx}>
                <Typography fontSize={14} fontWeight={500} noWrap>
                  {t('contract.branch')}
                </Typography>
              </Grid>
              <Grid
                item
                xs={7}
                sx={noOverflowSx}
                data-testid="branch-address-full"
              >
                <Typography fontSize={14} noWrap data-testid="branch-address-1">
                  {data?.accountInformation?.branch?.address1 || t('common.na')}
                </Typography>
                {data?.accountInformation?.branch?.address2 ? (
                  <Typography
                    fontSize={14}
                    noWrap
                    data-testid="branch-address-2"
                  >
                    {data?.accountInformation?.branch?.address2}
                  </Typography>
                ) : null}
                {data?.accountInformation?.branch?.address3 ? (
                  <Typography
                    fontSize={14}
                    noWrap
                    data-testid="branch-address-3"
                  >
                    {data?.accountInformation?.branch?.address3}
                  </Typography>
                ) : null}
                <Typography fontSize={14} noWrap data-testid="branch-address-4">
                  {data?.accountInformation?.branch?.city ?? null}{' '}
                  {data?.accountInformation?.branch?.state ?? null}
                  {data?.accountInformation?.branch?.state &&
                  data?.accountInformation?.branch?.zip
                    ? ', '
                    : null}
                  {data?.accountInformation?.branch?.zip ?? null}
                </Typography>
                {data?.accountInformation?.branch?.country ? (
                  <Typography
                    fontSize={14}
                    noWrap
                    data-testid="branch-address-5"
                  >
                    {data?.accountInformation?.branch?.country}
                  </Typography>
                ) : null}
              </Grid>
            </Grid>
          </Grid>
          {/* -------------------- Section: Dates Simple -------------------- */}
          <Collapse in={!seeMore}>
            <Box data-testid="contract-header-moredetails-box-simple-mobile">
              <Box pt={2} sx={noOverflowSx}>
                <Typography
                  color="primary.main"
                  fontSize={14}
                  fontWeight={500}
                  noWrap
                >
                  {t('contract.dates')}
                </Typography>
              </Box>
              <Grid
                container
                wrap="nowrap"
                flexDirection="column"
                spacing={1}
                mt={0}
                mb={1}
                mx={0.25}
              >
                {/* ---------- Order Date ---------- */}
                <Grid item container spacing={2} wrap="nowrap">
                  <Grid item xs={5} sx={noOverflowSx}>
                    <Typography fontSize={14} fontWeight={500} noWrap>
                      {t('contract.orderDate')}
                    </Typography>
                  </Grid>
                  <Grid item xs={7} sx={noOverflowSx}>
                    <Typography
                      fontSize={14}
                      noWrap
                      data-testid="collapsed-contract-order-date"
                    >
                      {data?.contractDates?.contractDate || t('common.na')}
                    </Typography>
                  </Grid>
                </Grid>
              </Grid>
            </Box>
          </Collapse>
          <Collapse in={seeMore}>
            <Box data-testid="contract-header-moredetails-box-more-mobile">
              {/* -------------------- Section: Dates -------------------- */}
              <Divider />
              <Box pt={2} sx={noOverflowSx}>
                <Typography
                  color="primary.main"
                  fontSize={14}
                  fontWeight={500}
                  noWrap
                >
                  {t('contract.dates')}
                </Typography>
              </Box>
              <Grid
                container
                wrap="nowrap"
                flexDirection="column"
                spacing={1}
                mt={0}
                mb={1}
                mx={0.25}
              >
                {/* ---------- Order Date ---------- */}
                <Grid item container spacing={2} wrap="nowrap">
                  <Grid item xs={5} sx={noOverflowSx}>
                    <Typography fontSize={14} fontWeight={500} noWrap>
                      {t('contract.orderDate')}
                    </Typography>
                  </Grid>
                  <Grid item xs={7} sx={noOverflowSx}>
                    <Typography
                      fontSize={14}
                      noWrap
                      data-testid="contract-order-date"
                    >
                      {data?.contractDates?.contractDate || t('common.na')}
                    </Typography>
                  </Grid>
                </Grid>
                {/* ---------- First Release Date ---------- */}
                <Grid item container spacing={2} wrap="nowrap">
                  <Grid item xs={5} sx={noOverflowSx}>
                    <Typography fontSize={14} fontWeight={500} noWrap>
                      {t('contract.firstReleaseDate')}
                    </Typography>
                  </Grid>
                  <Grid item xs={7} sx={noOverflowSx}>
                    <Typography
                      fontSize={14}
                      noWrap
                      data-testid="contract-first-release-date"
                    >
                      {data?.contractDates?.firstReleaseDate || t('common.na')}
                    </Typography>
                  </Grid>
                </Grid>
                {/* ---------- Last Release Date ---------- */}
                <Grid item container spacing={2} wrap="nowrap">
                  <Grid item xs={5} sx={noOverflowSx}>
                    <Typography fontSize={14} fontWeight={500} noWrap>
                      {t('contract.lastReleaseDate')}
                    </Typography>
                  </Grid>
                  <Grid item xs={7} sx={noOverflowSx}>
                    <Typography
                      fontSize={14}
                      noWrap
                      data-testid="contract-last-release-date"
                    >
                      {data?.contractDates?.lastReleaseDate || t('common.na')}
                    </Typography>
                  </Grid>
                </Grid>
              </Grid>
              {/* -------------------- Section: Customer Information -------------------- */}
              <Divider />
              <Box pt={2} sx={noOverflowSx}>
                <Typography
                  color="primary.main"
                  fontSize={14}
                  fontWeight={500}
                  noWrap
                >
                  {t('contract.customerInformation')}
                </Typography>
              </Box>
              <Grid
                container
                wrap="nowrap"
                flexDirection="column"
                spacing={1}
                mt={0}
                mb={1}
                mx={0.25}
              >
                {/* ---------- Customer # ---------- */}
                <Grid item container spacing={2} wrap="nowrap">
                  <Grid item xs={5} sx={noOverflowSx}>
                    <Typography fontSize={14} fontWeight={500} noWrap>
                      {t('contract.customerNumber')}
                    </Typography>
                  </Grid>
                  <Grid item xs={7} sx={noOverflowSx}>
                    <Typography
                      fontSize={14}
                      noWrap
                      data-testid="customer-info-customer-number"
                    >
                      {data?.customerInfo?.customerNumber || t('common.na')}
                    </Typography>
                  </Grid>
                </Grid>
                {/* ---------- Job # ---------- */}
                <Grid item container spacing={2} wrap="nowrap">
                  <Grid item xs={5} sx={noOverflowSx}>
                    <Typography fontSize={14} fontWeight={500} noWrap>
                      {t('contract.jobNumber')}
                    </Typography>
                  </Grid>
                  <Grid item xs={7} sx={noOverflowSx}>
                    <Typography
                      fontSize={14}
                      noWrap
                      data-testid="customer-info-customer-job-number"
                    >
                      {data?.customerInfo?.jobNumber || t('common.na')}
                    </Typography>
                  </Grid>
                </Grid>
                {/* ---------- Entered By ---------- */}
                <Grid item container spacing={2} wrap="nowrap">
                  <Grid item xs={5} sx={noOverflowSx}>
                    <Typography fontSize={14} fontWeight={500} noWrap>
                      {t('contract.enteredBy')}
                    </Typography>
                  </Grid>
                  <Grid item xs={7} sx={noOverflowSx}>
                    <Typography
                      fontSize={14}
                      noWrap
                      data-testid="customer-info-entered-by"
                    >
                      {data?.customerInfo?.enteredBy || t('common.na')}
                    </Typography>
                  </Grid>
                </Grid>
                {/* ---------- PO # ---------- */}
                <Grid item container spacing={2} wrap="nowrap">
                  <Grid item xs={5} sx={noOverflowSx}>
                    <Typography fontSize={14} fontWeight={500} noWrap>
                      {t('contract.poNumber')}
                    </Typography>
                  </Grid>
                  <Grid item xs={7} sx={noOverflowSx}>
                    <Typography
                      fontSize={14}
                      noWrap
                      data-testid="customer-info-po-number"
                    >
                      {data?.purchaseOrderNumber || t('common.na')}
                    </Typography>
                  </Grid>
                </Grid>
                {/* ---------- Job Name ---------- */}
                <Grid item container spacing={2} wrap="nowrap">
                  <Grid item xs={5} sx={noOverflowSx}>
                    <Typography fontSize={14} fontWeight={500} noWrap>
                      {t('contract.jobName')}
                    </Typography>
                  </Grid>
                  <Grid item xs={7} sx={noOverflowSx}>
                    <Typography
                      fontSize={14}
                      noWrap
                      data-testid="customer-info-job-name"
                    >
                      {data?.jobName || t('common.na')}
                    </Typography>
                  </Grid>
                </Grid>
              </Grid>
              {/* -------------------- Section: Summary -------------------- */}
              <Divider />
              <Box pt={2} sx={noOverflowSx}>
                <Typography
                  color="primary.main"
                  fontSize={14}
                  fontWeight={500}
                  noWrap
                >
                  {t('contract.summary')}
                </Typography>
              </Box>
              <Grid
                container
                wrap="nowrap"
                flexDirection="column"
                spacing={1}
                mt={0}
                mb={1}
                mx={0.25}
              >
                {/* ---------- Subtotal ---------- */}
                <Grid item container spacing={2} wrap="nowrap">
                  <Grid item xs={5} sx={noOverflowSx}>
                    <Typography fontSize={14} fontWeight={500} noWrap>
                      {t('contract.subtotal')}
                    </Typography>
                  </Grid>
                  <Grid item xs={7} sx={noOverflowSx}>
                    <Typography
                      fontSize={14}
                      noWrap
                      data-testid="summary-subtotal"
                    >
                      {format(
                        parseFloat(data?.contractSummary?.subTotal || '0')
                      )}
                    </Typography>
                  </Grid>
                </Grid>
                {/* ---------- Tax Amount ---------- */}
                <Grid item container spacing={2} wrap="nowrap">
                  <Grid item xs={5} sx={noOverflowSx}>
                    <Typography fontSize={14} fontWeight={500} noWrap>
                      {t('contract.taxAmount')}
                    </Typography>
                  </Grid>
                  <Grid item xs={7} sx={noOverflowSx}>
                    <Typography
                      fontSize={14}
                      noWrap
                      data-testid="summary-tax-amount"
                    >
                      {format(
                        parseFloat(data?.contractSummary?.taxAmount || '0')
                      )}
                    </Typography>
                  </Grid>
                </Grid>
                {/* ---------- Other Charges ---------- */}
                <Grid item container spacing={2} wrap="nowrap">
                  <Grid item xs={5} sx={noOverflowSx}>
                    <Typography fontSize={14} fontWeight={500} noWrap>
                      {t('contract.otherCharges')}
                    </Typography>
                  </Grid>
                  <Grid item xs={7} sx={noOverflowSx}>
                    <Typography
                      fontSize={14}
                      noWrap
                      data-testid="summary-other-charges"
                    >
                      {format(
                        parseFloat(data?.contractSummary?.otherCharges || '0')
                      )}
                    </Typography>
                  </Grid>
                </Grid>
                {/* ---------- Totals ---------- */}
                <Grid item container spacing={2} wrap="nowrap">
                  <Grid item xs={5} sx={noOverflowSx} color="primary.main">
                    <Typography fontSize={14} fontWeight={500} noWrap>
                      {t('contract.totals')}
                    </Typography>
                  </Grid>
                  <Grid item xs={7} sx={noOverflowSx}>
                    <Typography
                      noWrap
                      fontWeight={500}
                      fontSize={14}
                      sx={{ textDecoration: 'underline' }}
                      data-testid="summary-total-amount"
                    >
                      {format(
                        parseFloat(data?.contractSummary?.totalAmount || '0')
                      )}
                    </Typography>
                  </Grid>
                </Grid>
                {/* ---------- Invoice To-Date ---------- */}
                <Grid
                  item
                  container
                  spacing={2}
                  wrap="wrap"
                  marginBottom={isTooltipOpen ? 6 : 0}
                >
                  <Grid item xs={5}>
                    <Typography fontSize={14} fontWeight={500}>
                      {t('contract.invoiceToDate')}
                      <CustomWidthTooltip
                        disableFocusListener
                        enterTouchDelay={0}
                        onOpen={() => setIsTooltipOpen(true)}
                        onClose={() => setIsTooltipOpen(false)}
                        placement="bottom-start"
                        PopperProps={{ style: { marginTop: -50 } }}
                        title={
                          <Grid container>
                            <Grid item xs={1}>
                              <HelpIcon />
                            </Grid>
                            <Grid item xs={11}>
                              {
                                t(
                                  'contract.invoiceToDateToolTipValue'
                                ) as string
                              }
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
                    </Typography>
                  </Grid>
                  <Grid item xs={7}>
                    <Typography
                      data-testid="summary-invoice-to-date"
                      fontSize={14}
                    >
                      {format(
                        parseFloat(
                          data?.contractSummary?.invoicedToDateAmount || '0'
                        )
                      )}
                    </Typography>
                  </Grid>
                </Grid>

                {/* ---------- Shipped To-Date ---------- */}
                {/* <Grid item container spacing={2} wrap="nowrap">
                  <Grid item xs={5} sx={noOverflowSx}>
                    <Typography fontSize={14} fontWeight={500} noWrap>
                      {t('contract.shippedToDate')}
                    </Typography>
                  </Grid>
                  <Grid item xs={7} sx={noOverflowSx}>
                    <Typography
                      fontSize={14}
                      noWrap
                      data-testid="summary-shipped-to-date"
                    >
                      {format(
                        parseFloat(
                          data?.contractSummary?.lastShipmentDate || '0'
                        )
                      )}
                    </Typography>
                  </Grid>
                </Grid> */}
              </Grid>
            </Box>
          </Collapse>
          <Box
            onClick={handleToggleSeeMore}
            width="100%"
            textAlign="center"
            py={2}
            sx={{ cursor: 'pointer' }}
            data-testid="contract-header-moredetails-toggle-mobile"
          >
            <Typography
              fontWeight={500}
              color="primary02.main"
              sx={{ textDecoration: 'underline' }}
            >
              {t(seeMore ? 'contract.seeLess' : 'contract.seeMore')}
            </Typography>
          </Box>
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
  function handleToggleSeeMore() {
    setSeeMore(!seeMore);
  }
}
