import {
  Button,
  Grid,
  Link,
  Table,
  TableBody,
  TableRow,
  Typography
} from '@dialexa/reece-component-library';
import { kebabCase } from 'lodash-es';
import { useTranslation } from 'react-i18next';

import { ContractMoreDetailsProps } from 'Contract/ProductMoreDetailsDesktop';
import {
  TableCellLabel,
  TableCellValue,
  TableContainerStyled
} from 'Contract/util/styles';
import { TechnicalSpecsIcon } from 'icons';

export default function ContractMoreDetailsMobile(
  props: ContractMoreDetailsProps
) {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();

  /**
   * Render
   */
  return (
    <Grid container px={1} mt={3} mb={5} flexDirection="column" rowSpacing={2}>
      {/* ---------- MOBILE - TECH DOCS ---------- */}

      <Grid item>
        <Typography
          fontSize={20}
          component="h5"
          fontWeight={500}
          color="primary"
        >
          {t('product.techDocs')}
        </Typography>

        <Grid container mt={1} mb={4} flexDirection="column" rowSpacing={2}>
          {props.product?.technicalDocuments?.length ? (
            props.product.technicalDocuments.map((doc, i) => (
              <Grid item container xs={12} key={`doc${i}`}>
                <Link
                  href={doc?.url ?? ''}
                  target="_blank"
                  data-testid={kebabCase(`${doc?.name}`)}
                >
                  <Button variant="inline" startIcon={<TechnicalSpecsIcon />}>
                    {doc?.name ?? ''}
                  </Button>
                </Link>
              </Grid>
            ))
          ) : (
            <Grid item xs={12}>
              <Typography
                component="h5"
                fontWeight={500}
                color="secondary03.main"
              >
                {t('product.techDocsNotFound')}
              </Typography>
            </Grid>
          )}
        </Grid>
      </Grid>
      {/* ---------- MOBILE - TECH SPECS ---------- */}
      <Grid item>
        <Typography
          fontSize={20}
          component="h5"
          fontWeight={500}
          color="primary"
        >
          {t('product.techSpecs')}
        </Typography>
        {props.product?.techSpecifications?.length ? (
          <Grid container spacing={2}>
            <Grid item xs={12}>
              <TableContainerStyled>
                <Table>
                  <TableBody>
                    {props.product?.techSpecifications.map((spec, i) => (
                      <TableRow key={`spec-l${i}`}>
                        <TableCellLabel>{`${spec?.name}: `}</TableCellLabel>
                        <TableCellValue
                          data-testid={kebabCase(`${spec?.name}`)}
                        >
                          {spec?.value}
                        </TableCellValue>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </TableContainerStyled>
            </Grid>
          </Grid>
        ) : (
          <Typography component="h5" fontWeight={500} color="secondary03.main">
            {t('product.techSpecsNotFound')}
          </Typography>
        )}
      </Grid>
    </Grid>
  );
}
