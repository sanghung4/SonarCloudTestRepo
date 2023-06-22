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

import { GetProductQuery } from 'generated/graphql';
import { ArrowDropDownIcon } from 'icons';

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

const TechnicalSpecifications = (props: Props) => {
  const { data, isSmallScreen } = props;
  const { t } = useTranslation();

  if (
    !(
      data?.product?.techSpecifications &&
      data?.product?.techSpecifications?.length > 0
    )
  ) {
    return null;
  }

  let columnOne: Array<any> = [];
  let columnTwo: Array<any> = [];

  if (!isSmallScreen) {
    const splitIndex = Math.ceil(data?.product?.techSpecifications?.length / 2);

    columnOne = data?.product?.techSpecifications.slice(0, splitIndex);
    columnTwo = data?.product?.techSpecifications.slice(splitIndex);
  }

  return isSmallScreen ? (
    <Accordion type="product">
      <AccordionSummary expandIcon={<ArrowDropDownIcon />}>
        {t('product.techSpecs')}
      </AccordionSummary>
      <AccordionDetails>
        <List>
          {data?.product?.techSpecifications?.map((spec, i) => (
            <Fragment key={spec?.name}>
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
                  >{`${spec?.name}: `}</Box>
                  <Box
                    component="span"
                    sx={(theme) => ({
                      fontSize: theme.typography.pxToRem(14),
                      lineHeight: theme.typography.pxToRem(20),
                      color: 'primary03.main'
                    })}
                  >
                    {spec?.value}
                  </Box>
                </ListItemText>
              </ListItem>
              {i < (data?.product?.techSpecifications?.length || 1) - 1 ? (
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
              {columnOne.map((spec) => (
                <TableRow key={spec?.name}>
                  <TableCellLabel>{`${spec?.name}: `}</TableCellLabel>
                  <TableCellValue>{spec?.value}</TableCellValue>
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
              {columnTwo.map((spec) => (
                <TableRow key={spec?.name}>
                  <TableCellLabel>{`${spec?.name}: `}</TableCellLabel>
                  <TableCellValue>{spec?.value}</TableCellValue>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainerStyled>
      </Grid>
    </Grid>
  );
};

export default TechnicalSpecifications;
