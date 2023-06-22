import { useContext, useEffect, useMemo, useRef, useState } from 'react';

import {
  Box,
  Card,
  Grid,
  Hidden,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { VariableSizeList } from 'react-window';

import ContractDialogs from 'Contract/ContractDialogs';
import ContractLineItem from 'Contract/ContractLineItem';
import ContractListComment from 'Contract/ContractListComment';
import { ContractContext } from 'Contract/ContractProvider';
import ContractProductListControls from 'Contract/ProductListControls';
import ReviewRelease from 'Contract/ReviewRelease';
import { ContractProduct } from 'generated/graphql';
import trimSpaces from 'utils/trimSpaces';

export type ContractProductLineItem = {
  sequence: string;
  qty: string;
  product?: ContractProduct;
  comment?: string;
};

export default function ContractProductList() {
  /**
   * Custom hooks
   */
  const { t } = useTranslation();
  const { isSmallScreen } = useScreenSize();

  /**
   * Refs
   */
  const virtualizedRef = useRef<VariableSizeList>(null);

  /**
   * States
   */
  const [searchApplied, setSearchApplied] = useState('');
  const [smallScreenFailsafe, setSmallScreenFailsafe] = useState(false);

  /**
   * Context
   */
  const { contractData, qtyInputMap } = useContext(ContractContext);

  /**
   * Memo
   */
  const list = useMemo(listMemo, [searchApplied, contractData, qtyInputMap]);
  const productCount = useMemo(listProductCountMemo, [list]);

  /**
   * Effects
   */
  useEffect(resizeVirtualizedEffect, [
    virtualizedRef,
    isSmallScreen,
    smallScreenFailsafe
  ]);

  /**
   * Output
   */
  return (
    <Box component={Card} mb={3} bgcolor="lightestBlue.main">
      <ContractDialogs count={productCount} />
      <ReviewRelease list={list} />
      <ContractProductListControls
        hasSearch
        count={productCount}
        searchApplied={searchApplied}
        setSearchApplied={handleSearchApplied}
        testId="top"
      />
      <Box
        color="lighterBlue.contrastText"
        borderTop={1}
        borderBottom={1}
        borderColor="secondary03.main"
        bgcolor="lighterBlue.main"
      >
        <Hidden mdDown>
          <Grid
            container
            flexWrap="nowrap"
            alignItems="center"
            flexDirection="row"
            columnSpacing={2}
            px={4}
            py={1}
          >
            <Grid item xs={2}>
              <Typography fontWeight={500} component="div">
                {t('contract.seqNum')}
              </Typography>
            </Grid>
            <Grid item xs={4}>
              <Typography fontWeight={500} component="div">
                {t('contract.product')}
              </Typography>
            </Grid>
            <Grid item xs={6}>
              <Grid container columnSpacing={2} columns={5} alignItems="center">
                <Grid item xs={1}>
                  <Typography fontWeight={500} component="div" align="center">
                    {t('contract.price')}
                  </Typography>
                </Grid>
                <Grid item xs={1}>
                  <Typography fontWeight={500} component="div" align="center">
                    {t('contract.contractQty')}
                  </Typography>
                </Grid>
                <Grid item xs={1}>
                  <Typography fontWeight={500} component="div" align="center">
                    {t('contract.releasedQty')}
                  </Typography>
                </Grid>
                <Grid item xs={1}>
                  <Typography fontWeight={500} component="div" align="center">
                    {t('contract.shippedToDate')}
                  </Typography>
                </Grid>
                <Grid item xs={1}>
                  <Typography fontWeight={500} component="div" align="center">
                    {t('contract.qtyToRelease')}
                  </Typography>
                </Grid>
              </Grid>
            </Grid>
          </Grid>
        </Hidden>
      </Box>
      <Box
        mt={isSmallScreen ? 0 : 0.5}
        height={list.length ? 768 : undefined}
        id="product-search-list-container"
        data-testid="product-search-list-container"
      >
        {list.length ? (
          <VariableSizeList
            height={768}
            overscanCount={2}
            itemCount={list.length}
            itemSize={itemHeight(list)}
            itemKey={(index) => `contract-list-item${index}`}
            width="100%"
            ref={virtualizedRef}
          >
            {({ index, style }) => (
              <Box style={style}>
                {list[index].product ? (
                  <ContractLineItem
                    loading={false}
                    data={list[index].product}
                    sequence={list[index].sequence}
                  />
                ) : (
                  <ContractListComment
                    comment={list[index].comment}
                    isLast={!!list[index + 1]?.product}
                  />
                )}
              </Box>
            )}
          </VariableSizeList>
        ) : (
          <Grid
            container
            justifyContent="center"
            alignItems="center"
            width="100%"
            height="100%"
            my={6}
            mx={4}
          >
            <Typography
              fontSize={25}
              component="h5"
              fontWeight={500}
              textAlign="center"
              color="secondary03.main"
              data-testid="contract-product-list-empty"
            >
              {t('contract.noProductFound')}
            </Typography>
          </Grid>
        )}
      </Box>
      <ContractProductListControls
        count={productCount}
        searchApplied={searchApplied}
        setSearchApplied={handleSearchApplied}
        testId="bottom"
      />
    </Box>
  );

  /**
   * Handle def
   */
  function handleSearchApplied(search: string) {
    setSearchApplied(search);
    if (virtualizedRef.current) {
      virtualizedRef.current.resetAfterIndex(0, true);
    }
  }

  /**
   * Memo def
   */
  function listProductCountMemo() {
    const products = list.filter((item) => {
      const { product, comment } = item;
      const contractQty = product?.qty?.quantityOrdered ?? 0;
      const releasedQty = product?.qty?.quantityReleasedToDate ?? 0;
      const isAvailable = contractQty - releasedQty > 0;
      return product && !comment && isAvailable;
    });
    return products.length;
  }
  function listMemo() {
    const list = contractData?.contractProducts || [];
    // Create a new list for easier data handling
    let newList: ContractProductLineItem[] = list.map((item, index) =>
      // Return as a special object "ContractProductLineItem"
      ({
        comment: item?.displayOnly && item?.name ? item.name : undefined,
        product: !item?.displayOnly ? item : undefined,
        qty: qtyInputMap[`${index}`] ?? '0',
        sequence:
          !item?.displayOnly && item?.sequenceNumber ? item.sequenceNumber : ''
      })
    );
    // Filter out the blank products and comments
    newList = newList.filter((item) => item.product || item.comment);
    // Search filters
    if (searchApplied && newList.length) {
      const toSearch = trimSpaces(searchApplied.toLowerCase());
      newList = newList.filter((item, i) => {
        const mfr = trimSpaces(item.product?.mfr?.toLowerCase());
        const brand = trimSpaces(item.product?.mfr?.toLowerCase());
        const name = trimSpaces(item.product?.name?.toLowerCase());
        const partNumber = trimSpaces(item.product?.partNumber?.toLowerCase());
        return (
          mfr.includes(toSearch) ||
          brand.includes(toSearch) ||
          name.includes(toSearch) ||
          partNumber.includes(toSearch) ||
          item.sequence.toString() === toSearch
        );
      });
    }
    return newList;
  }

  /**
   * Effect def
   */
  function resizeVirtualizedEffect() {
    // Ignoring this as we cannot change breakpoint in mid-test and accurately test the render
    // istanbul ignore next
    if (isSmallScreen !== smallScreenFailsafe && virtualizedRef.current) {
      // istanbul ignore next
      setSmallScreenFailsafe(isSmallScreen);
      // istanbul ignore next
      virtualizedRef.current.resetAfterIndex(0, true);
    }
  }

  /**
   * Misc def
   */
  function itemHeight(list: ContractProductLineItem[]) {
    const productHeight = isSmallScreen ? 270 : 119;
    const commentHeight = 48;
    return (index: number) =>
      !!list[index].product ? productHeight : commentHeight;
  }
}
