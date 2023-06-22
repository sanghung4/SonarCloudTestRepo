import { useTranslation } from 'react-i18next';
import Breadcrumbs from 'common/Breadcrumbs';

import {
  Box,
  Card,
  Container,
  Link,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';

import useDocumentTitle from 'hooks/useDocumentTitle';
import FormLinks from './FormLinks';

function CreditForms() {
  const { t } = useTranslation();
  useDocumentTitle(t('common.creditAndForms'));
  const { isSmallScreen } = useScreenSize();

  return (
    <>
      <Breadcrumbs
        pageTitle={t('common.creditForms')}
        config={[
          {
            text: t('common.resources')
          }
        ]}
      />
      {/* Header Section */}
      <Container>
        {/* Filters Section */}
        <Card
          sx={{
            px: isSmallScreen ? 3 : 5,
            py: isSmallScreen ? 3 : 3,
            mb: 3
          }}
        >
          <Typography variant="h5">{t('common.creditForms')}</Typography>
        </Card>
        <Card
          sx={{
            px: isSmallScreen ? 2 : 5,
            py: isSmallScreen ? 2 : 3,
            mb: 3
          }}
        >
          <Box pb={3}>
            <Typography variant="body1" display="inline">
              {t('creditForms.info1')}
            </Typography>
            <Link href={`mailto:${t('creditForms.email')}`}>
              <Typography
                variant="body1"
                display="inline"
                sx={{ color: 'primary02.main' }}
              >
                {t('creditForms.email')}
              </Typography>
            </Link>
            <Typography variant="body1" display="inline">
              {t('creditForms.info2')}
            </Typography>
          </Box>
          <FormLinks />
        </Card>
      </Container>
    </>
  );
}

export default CreditForms;
