import {
  Container,
  Grid,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';

import { useTranslation } from 'react-i18next';
import BrandCard from 'Brands/BrandCard';
import { brandList } from 'Brands/util/list';
import useDocumentTitle from 'hooks/useDocumentTitle';

export default function BrandPage() {
  /**
   * Custom hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();
  const list = brandList(t);
  useDocumentTitle(t('common.brands'));

  /**
   * Render
   */
  return (
    <Container maxWidth="lg" sx={{ marginBottom: 12.5 }}>
      <Typography
        color="primary"
        variant="h3"
        textAlign="center"
        my={isSmallScreen ? 3 : 7.5}
        fontWeight={700}
        data-testid="brands-title"
      >
        {t('brands.title')}
      </Typography>
      <Grid container ml={isSmallScreen ? 0 : 0.5} spacing={[0, 6]}>
        {list.map((brand, key) => (
          <Grid
            item
            key={`brand-${key}`}
            pt={0}
            pb={isSmallScreen ? 4 : 5}
            xs={isSmallScreen ? 12 : 4}
          >
            <BrandCard {...brand} />
          </Grid>
        ))}
      </Grid>
    </Container>
  );
}
