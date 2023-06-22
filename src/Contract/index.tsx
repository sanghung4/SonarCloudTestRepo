import {
  Container,
  Grid,
  Hidden,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
// import { PrintIcon } from 'icons';
import { useTranslation } from 'react-i18next';
import { useLocation, useParams } from 'react-router-dom';

import Breadcrumbs from 'common/Breadcrumbs';
import Loader from 'common/Loader';
import { ContractProvider } from 'Contract/ContractProvider';
import ContractHeaderDesktop from 'Contract/HeaderDesktop';
import ContractHeaderMobile from 'Contract/HeaderMobile';
import ContractProductList from 'Contract/ProductList';
import useContractDetailsData from 'Contract/util/data';
import useDocumentTitle from 'hooks/useDocumentTitle';

/**
 * Types
 */
type RouterState = {
  search?: string;
};

export default function Contract() {
  /**
   * Custom hooks
   */
  const { t } = useTranslation();
  const { isSmallScreen } = useScreenSize();
  const { id } = useParams<{ id: string }>();
  const location = useLocation<RouterState>();
  const breadcrumbConfig = {
    text: 'Contracts',
    to: `/contracts${location.state?.search ?? ''}`
  };

  /**
   * Data
   */
  const { data, loading, account } = useContractDetailsData(id);

  /**
   * Page Title
   */
  useDocumentTitle(
    t('dynamicPageTitles.contract', { contractId: data?.contractNumber ?? '' })
  );

  /**
   * Callbacks
   */
  // Commenting this out until we starting fixing printing.
  // const handlePrint = () => window.print();

  /**
   * Output
   */
  return (
    <ContractProvider contractData={data} account={account}>
      {loading && <Loader backdrop />}
      <Container>
        <Grid
          container
          alignItems="center"
          justifyContent="space-between"
          wrap="nowrap"
        >
          <Grid item xs="auto">
            <Breadcrumbs pageTitle={id} config={[breadcrumbConfig]} />
          </Grid>
          <Hidden mdUp>
            <Grid
              item
              container
              xs
              px={2}
              overflow="hidden"
              justifyContent="flex-end"
            >
              <Typography
                component="h3"
                noWrap
                fontSize={16}
                fontWeight={500}
                textOverflow="ellipsis"
              >
                {t('contract.title')}
              </Typography>
            </Grid>
          </Hidden>
        </Grid>
        <Hidden mdDown>
          <Grid
            container
            alignItems="center"
            justifyContent="space-between"
            spacing={2}
            pb={3}
          >
            <Grid item>
              <Typography component="h3" fontSize={31.25}>
                {t('contract.title')}
              </Typography>
            </Grid>
            {/* <Grid item>
              <Button
                startIcon={<PrintIcon color="primary" />}
                onClick={handlePrint}
                variant="inline"
                data-testid="print-button"
              >
                {t('common.print')}
              </Button>
            </Grid> */}
          </Grid>
        </Hidden>
        {isSmallScreen ? <ContractHeaderMobile /> : <ContractHeaderDesktop />}
        <ContractProductList />
      </Container>
    </ContractProvider>
  );
}
