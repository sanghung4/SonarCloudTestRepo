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

type Props = {
  data?: GetProductQuery;
};

function FeaturesAndBenefits({ data }: Props) {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();
  const { isSmallScreen } = useScreenSize();

  /**
   * Render
   */
  if (!data?.product?.featuresAndBenefits) {
    return null;
  }
  return isSmallScreen ? (
    <Accordion type="product">
      <AccordionSummary expandIcon={<ArrowDropDownIcon />}>
        {t('product.featuresBenefits')}
      </AccordionSummary>
      <AccordionDetails sx={{ py: 2, px: 0 }}>
        <BulletList>
          {data?.product?.featuresAndBenefits
            .split(';')
            .map((feature, index) => (
              <ListItemText sx={{ display: 'list-item' }} key={index}>
                <Typography variant="body1" display="block">
                  {feature}
                </Typography>
              </ListItemText>
            ))}
        </BulletList>
      </AccordionDetails>
    </Accordion>
  ) : (
    <BulletList>
      {data?.product?.featuresAndBenefits.split(';').map((feature, index) => (
        <ListItemText sx={{ display: 'list-item' }} key={index}>
          <Typography
            display="block"
            maxWidth="calc(100% * 2 / 3)"
            variant="body1"
          >
            {feature}
          </Typography>
        </ListItemText>
      ))}
    </BulletList>
  );
}

export default FeaturesAndBenefits;
