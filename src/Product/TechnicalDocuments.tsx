import {
  Accordion,
  AccordionDetails,
  AccordionSummary,
  Box,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { GetProductQuery } from 'generated/graphql';
import { ArrowDropDownIcon, TechnicalSpecsIcon } from 'icons';

type Props = {
  data?: GetProductQuery;
};

function TechnicalDocuments({ data }: Props) {
  /**
   * Props
   */
  const technicalDocuments = data?.product?.technicalDocuments;

  /**
   * Custom Hooks
   */
  const { t } = useTranslation();
  const { isSmallScreen } = useScreenSize();

  /**
   * Render
   */
  if (!technicalDocuments?.length && !isSmallScreen) {
    return null;
  }
  return (
    <Accordion type="product">
      <AccordionSummary expandIcon={<ArrowDropDownIcon />}>
        {t('product.techDocs')}
      </AccordionSummary>
      <AccordionDetails>
        <List>
          {technicalDocuments?.map((doc) => (
            <ListItem
              button
              disableGutters
              key={doc?.name}
              onClick={() => window.open(doc?.url ?? '', '_blank')}
            >
              <ListItemIcon>
                <Box component={TechnicalSpecsIcon} color="primary.main" />
              </ListItemIcon>
              <ListItemText
                primary={doc?.name}
                disableTypography
                color="primary.main"
                sx={(theme) => ({ ...theme.typography.body1, fontWeight: 500 })}
              />
            </ListItem>
          ))}
        </List>
      </AccordionDetails>
    </Accordion>
  );
}

export default TechnicalDocuments;
