import {
  Accordion,
  AccordionDetails,
  AccordionSummary,
  Divider,
  List,
  ListItem,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { ErpSystemEnum, GetProductQuery } from 'generated/graphql';
import { ArrowDropDownIcon } from 'icons';
import {
  ProductCodesLabel,
  ProductCodesListItemText,
  ProductCodesValue
} from 'Product/util/styled';

type Props = {
  data?: GetProductQuery;
};

const ProductCodes = ({ data }: Props) => {
  /**
   * Custom hooks
   */
  const { t } = useTranslation();
  const { isSmallScreen } = useScreenSize();

  /**
   * Render
   */
  if (
    !(
      data?.product?.upc ||
      data?.product?.unspsc ||
      data?.product?.seriesModelFigureNumber ||
      (data?.product?.id && data?.product?.erp === ErpSystemEnum.Eclipse)
    ) &&
    !isSmallScreen
  ) {
    return null;
  }
  return (
    <Accordion type="product">
      <AccordionSummary expandIcon={<ArrowDropDownIcon />}>
        {t('product.productCodes')}
      </AccordionSummary>
      <AccordionDetails>
        <List sx={{ width: 1 }}>
          {!!data?.product?.id && data?.product?.erp === ErpSystemEnum.Eclipse && (
            <>
              <ListItem disableGutters>
                <ProductCodesListItemText disableTypography>
                  <ProductCodesLabel>
                    {t('product.itemNumber')}
                  </ProductCodesLabel>
                  <ProductCodesValue>{data.product.id}</ProductCodesValue>
                </ProductCodesListItemText>
              </ListItem>
              <Divider sx={{ bgcolor: 'secondary04.main' }} />
            </>
          )}
          {!!data?.product?.upc && (
            <>
              <ListItem disableGutters>
                <ProductCodesListItemText disableTypography>
                  <ProductCodesLabel>{t('product.upc')}</ProductCodesLabel>
                  <ProductCodesValue>{data.product.upc}</ProductCodesValue>
                </ProductCodesListItemText>
              </ListItem>
              <Divider sx={{ bgcolor: 'secondary04.main' }} />
            </>
          )}
          {!!data?.product?.unspsc && (
            <>
              <ListItem disableGutters>
                <ProductCodesListItemText disableTypography>
                  <ProductCodesLabel>{t('product.unspc')}</ProductCodesLabel>
                  <ProductCodesValue>{data.product.unspsc}</ProductCodesValue>
                </ProductCodesListItemText>
              </ListItem>
              <Divider sx={{ bgcolor: 'secondary04.main' }} />
            </>
          )}
          {!!data?.product?.seriesModelFigureNumber && (
            <ListItem disableGutters>
              <ProductCodesListItemText disableTypography>
                <ProductCodesLabel>{t('product.series')}</ProductCodesLabel>
                <ProductCodesValue>
                  {data.product.seriesModelFigureNumber}
                </ProductCodesValue>
              </ProductCodesListItemText>
            </ListItem>
          )}
        </List>
      </AccordionDetails>
    </Accordion>
  );
};

export default ProductCodes;
