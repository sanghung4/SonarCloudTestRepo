import { useMemo } from 'react';

import {
  Box,
  Button,
  Divider,
  Grid,
  Link,
  Table,
  TableBody,
  TableRow,
  Typography
} from '@dialexa/reece-component-library';
import { kebabCase } from 'lodash-es';
import { useTranslation } from 'react-i18next';

import { ContractProduct } from 'generated/graphql';
import { TechnicalSpecsIcon } from 'icons';
import {
  TableCellLabel,
  TableCellValue,
  TableContainerStyled
} from 'Contract/util/styles';

export type ContractMoreDetailsProps = {
  product?: ContractProduct;
};

export default function ContractMoreDetailsDesktop(
  props: ContractMoreDetailsProps
) {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();

  /**
   * Memo
   */
  const [columnOne, columnTwo] = useMemo(techSpecColumnMemo, [
    props.product?.techSpecifications
  ]);

  /**
   * Render
   */
  return (
    <Grid container px={1} mt={1} mb={3} flexDirection="column" rowSpacing={2}>
      {/* ---------- DESKTOP - TECH DOCS ---------- */}

      <Grid item>
        <Typography
          fontSize={20}
          component="h5"
          fontWeight={500}
          color="primary"
        >
          {t('product.techDocs')}
        </Typography>

        <Grid container my={3}>
          {props.product?.technicalDocuments?.length ? (
            props.product.technicalDocuments.map((doc, i) => (
              <Grid item container xs={3} key={`doc${i}`}>
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
      {/* ---------- DESKTOP - TECH SPECS ---------- */}
      <Grid item>
        <Typography
          fontSize={20}
          component="h5"
          fontWeight={500}
          color="primary"
        >
          {t('product.techSpecs')}
        </Typography>
        <Box component={Divider} my={2} />
        {props.product?.techSpecifications?.length ? (
          <Grid container spacing={2}>
            <Grid item xs={6}>
              <TableContainerStyled>
                <Table>
                  <TableBody>
                    {columnOne.map((spec, i) => (
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
            <Grid item xs={6}>
              <TableContainerStyled>
                <Table>
                  <TableBody>
                    {columnTwo.map((spec, i) => (
                      <TableRow key={`spec-r${i}`}>
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

  /**
   * Memo def
   */
  function techSpecColumnMemo() {
    if (props.product?.techSpecifications?.length) {
      const splitIndex = Math.ceil(props.product.techSpecifications.length / 2);

      return [
        props.product.techSpecifications.slice(0, splitIndex),
        props.product.techSpecifications.slice(splitIndex)
      ];
    }
    return [[], []];
  }
}
