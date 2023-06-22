import React, { Fragment } from 'react';

import {
  Accordion,
  AccordionDetails,
  AccordionSummary,
  Box,
  Divider,
  Grid,
  List,
  ListItem,
  ListItemText,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableRow,
  styled
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { ArrowDropDownIcon } from '../icons';
import { GetProductQuery } from '../generated/graphql';

const TableContainerStyled = styled(TableContainer)(({ theme }) => ({
  '& .MuiTableRow-root:last-child td': {
    border: '0 none'
  },
  '& .MuiTableCell-root': {
    fontSize: theme.typography.pxToRem(16),
    color: theme.palette.primary03.main,
    padding: theme.spacing(2, 0),
    verticalAlign: 'top',
    '&:first-child': {
      paddingRight: theme.spacing(1.5)
    },
    '&:last-child': {
      paddingLeft: theme.spacing(1.5)
    }
  }
}));

const TableCellLabel = styled(TableCell)({
  fontWeight: 700,
  width: '0.1%',
  whiteSpace: 'nowrap'
});

const TableCellValue = styled(TableCell)({
  fontWeight: 400
});

interface Props {
  data?: GetProductQuery;
  isSmallScreen?: boolean;
}

const PackageDimensions = (props: Props) => {
  const { data, isSmallScreen } = props;
  const { t } = useTranslation();

  if (
    !(
      data?.product?.packageDimensions?.length ||
      data?.product?.packageDimensions?.width ||
      data?.product?.packageDimensions?.height ||
      data?.product?.packageDimensions?.volume ||
      data?.product?.packageDimensions?.weight
    )
  ) {
    return null;
  }

  let columnOne: Array<{ label: string; value: string }> = [];
  let columnTwo: Array<{ label: string; value: string }> = [];

  if (data?.product?.packageDimensions?.length) {
    columnOne.push({
      label: `${t('product.length')}: `,
      value: `${data?.product?.packageDimensions?.length}`
    });
  }

  if (data?.product?.packageDimensions?.width) {
    columnOne.push({
      label: `${t('product.width')}: `,
      value: `${data?.product?.packageDimensions?.width}`
    });
  }

  if (data?.product?.packageDimensions?.height) {
    columnOne.push({
      label: `${t('product.height')}: `,
      value: `${data?.product?.packageDimensions?.height}`
    });
  }

  if (data?.product?.packageDimensions?.volume) {
    columnOne.push({
      label: `${t('product.volume')}: `,
      value: `${data?.product?.packageDimensions?.volume} ${
        data?.product?.packageDimensions?.volumeUnitOfMeasure || ''
      }`
    });
  }

  if (data?.product?.packageDimensions?.weight) {
    columnOne.push({
      label: `${t('product.weight')}: `,
      value: `${data?.product?.packageDimensions?.weight} ${
        data?.product?.packageDimensions?.weightUnitOfMeasure || ''
      }`
    });
  }

  if (!isSmallScreen) {
    columnTwo = columnOne.splice(Math.ceil(columnOne.length / 2));
  }

  return isSmallScreen ? (
    <Accordion type="product">
      <AccordionSummary expandIcon={<ArrowDropDownIcon />}>
        {t('product.dimensions')}
      </AccordionSummary>
      <AccordionDetails>
        <List sx={{ width: 1 }}>
          {columnOne.map((item, i) => (
            <Fragment key={i}>
              <ListItem disableGutters>
                <ListItemText>
                  <Box
                    component="span"
                    sx={(theme) => ({
                      fontSize: theme.typography.pxToRem(14),
                      lineHeight: theme.typography.pxToRem(20),
                      fontWeight: 700,
                      color: 'primary03.main'
                    })}
                  >
                    {item.label}
                  </Box>
                  <Box
                    component="span"
                    sx={(theme) => ({
                      fontSize: theme.typography.pxToRem(14),
                      lineHeight: theme.typography.pxToRem(20),
                      color: 'primary03.main'
                    })}
                  >
                    {item.value}
                  </Box>
                </ListItemText>
              </ListItem>
              {i < columnOne.length - 1 ? (
                <Divider sx={{ bgcolor: 'secondary04.main' }} />
              ) : null}
            </Fragment>
          ))}
        </List>
      </AccordionDetails>
    </Accordion>
  ) : (
    <Grid container spacing={10}>
      <Grid item xs={6}>
        <TableContainerStyled>
          <Table>
            <TableBody>
              {columnOne.map((item) => (
                <TableRow key={item.label}>
                  <TableCellLabel>{item.label}</TableCellLabel>
                  <TableCellValue>{item?.value}</TableCellValue>
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
              {columnTwo.map((item) => (
                <TableRow key={item.label}>
                  <TableCellLabel>{item.label}</TableCellLabel>
                  <TableCellValue>{item?.value}</TableCellValue>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainerStyled>
      </Grid>
    </Grid>
  );
};

export default PackageDimensions;
