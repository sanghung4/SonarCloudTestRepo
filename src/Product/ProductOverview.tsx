import {
  Accordion,
  AccordionDetails,
  AccordionSummary,
  ListItemText,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { GetProductQuery } from 'generated/graphql';
import { ArrowDropDownIcon } from 'icons';
import { BulletList } from 'Product/util/styled';

interface Props {
  data?: GetProductQuery;
}

function ProductOverview({ data }: Props) {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * Render
   */
  if (!data?.product?.productOverview) {
    return null;
  }
  return isSmallScreen ? (
    <Accordion type="product">
      <AccordionSummary expandIcon={<ArrowDropDownIcon />}>
        {t('product.productOverview')}
      </AccordionSummary>
      <AccordionDetails sx={{ py: 2, px: 0 }}>
        <BulletList>
          {data?.product?.productOverview
            .split(';')
            .map((productDescription, index) => (
              <ListItemText sx={{ display: 'list-item' }} key={index}>
                <Typography variant="body1" display="block">
                  {productDescription}
                </Typography>
              </ListItemText>
            ))}
        </BulletList>
      </AccordionDetails>
    </Accordion>
  ) : (
    <BulletList>
      {data?.product?.productOverview
        .split(';')
        .map((productDescription, index) => (
          <ListItemText sx={{ display: 'list-item' }} key={index}>
            <Typography
              display="block"
              maxWidth="calc(100% * 2 / 3)"
              variant="body1"
            >
              {productDescription}
            </Typography>
          </ListItemText>
        ))}
    </BulletList>
  );
}

export default ProductOverview;
