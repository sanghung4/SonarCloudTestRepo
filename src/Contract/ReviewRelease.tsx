import { useContext, useMemo } from 'react';

import { Box, Button, Grid } from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { ContractContext } from 'Contract/ContractProvider';
import { ContractProductLineItem } from 'Contract/ProductList';
import Slider from 'common/Sidebar';
import ReviewReleaseItem from 'Contract/ReviewReleaseItem';
import { VALUE_OVER_10MIL } from 'Cart/util';
import { valueOfReleasingContract } from './util';
import { useCartContext } from 'providers/CartProvider';

type Props = {
  list: ContractProductLineItem[];
};

export default function ReviewRelease(props: Props) {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();

  /**
   * Context
   */
  const {
    contractData,
    goToCart,
    isReviewReady,
    qtyInputMap,
    setIsReviewReady,
    handleReleaseOver10mil
  } = useContext(ContractContext);
  const { releaseContractToCart } = useCartContext();

  /**
   * Memo
   */
  const summaryList = useMemo(itemsMemo, [props.list, qtyInputMap]);

  /**
   * Render
   */
  return (
    <Slider
      on={isReviewReady}
      close={handleClose}
      title={t('contract.reviewItems')}
      widthOverride={450}
      containerSx={{ position: 'fixed', height: '100vh', maxHeight: '100vh' }}
      backdropSx={{ top: 0, position: 'fixed' }}
    >
      <Box flex="1 1 auto">
        <Box height="calc(100vh - 120px)" sx={{ overflowY: 'auto' }}>
          {summaryList.map((item, index) => (
            <ReviewReleaseItem
              data={item.product}
              idx={index}
              key={index}
              sequence={item.sequence}
            />
          ))}
        </Box>
        <Grid container justifyContent="flex-end" pr={2} pt={2}>
          <Button data-testid="review-release-button" onClick={handleRelease}>
            {`${t('contract.release')} (${Object.keys(qtyInputMap).length})`}
          </Button>
        </Grid>
      </Box>
    </Slider>
  );

  /**
   * Handles
   */
  function handleClose() {
    setIsReviewReady(false);
  }

  function handleRelease() {
    const value = valueOfReleasingContract(contractData, qtyInputMap);
    if (value > VALUE_OVER_10MIL) {
      handleClose();
      handleReleaseOver10mil();
      return;
    }
    releaseContractToCart(contractData, qtyInputMap);
    goToCart();
  }

  /**
   * Memo Def
   */
  function itemsMemo() {
    return Object.entries(qtyInputMap)
      .map(([key, value]) => {
        const foundItem = props.list.find((i) => i.sequence === key);
        if (!foundItem) {
          return undefined;
        }
        foundItem.qty = value;
        return foundItem;
      })
      .filter((i) => !!i) as ContractProductLineItem[];
  }
}
