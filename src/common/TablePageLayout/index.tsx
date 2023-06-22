import React, { ReactNode } from 'react';

import {
  Box,
  Card,
  Container,
  Grid,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { kebabCase } from 'lodash-es';

import Breadcrumbs, { BreadcrumbConfig } from 'common/Breadcrumbs';

export type TablePageLayoutProps = {
  breadcrumbConfig?: BreadcrumbConfig[];
  customContent?: ReactNode;
  filters?: ReactNode;
  headerAction?: ReactNode;
  invoicesPage?: boolean;
  loading?: boolean;
  pageTitle: string;
  // TODO: rename
  table?: ReactNode;
  flatCards?: boolean;
};

function TablePageLayout(props: TablePageLayoutProps) {
  /**
   * Custom hooks
   */
  const { isSmallScreen } = useScreenSize();

  return (
    <>
      <Breadcrumbs
        pageTitle={props.pageTitle}
        config={props.breadcrumbConfig}
      />
      {/* Header Section */}
      <Container disableGutters={props?.flatCards && isSmallScreen}>
        {/* Filters Section */}
        {!props.invoicesPage && (
          <Card
            square={props?.flatCards && isSmallScreen}
            raised={!(props?.flatCards && isSmallScreen)}
            sx={{
              px: isSmallScreen ? 3 : 4,
              py: isSmallScreen ? 2 : 3,
              mb: 3
            }}
          >
            <Grid container spacing={2}>
              <Grid
                container
                item
                xs={12}
                md={10}
                alignContent="center"
                justifyContent={isSmallScreen ? 'center' : 'flex-start'}
              >
                <Typography
                  variant="h5"
                  data-testid={kebabCase(`${props?.pageTitle}-title`)}
                >
                  {props.pageTitle}
                </Typography>
              </Grid>
              {!!props.headerAction && (
                <Grid
                  item
                  xs={12}
                  md={2}
                  justifyContent={isSmallScreen ? 'flex-start' : 'flex-end'}
                  container
                >
                  {props.headerAction}
                </Grid>
              )}
            </Grid>
          </Card>
        )}
        {props.customContent}
        {/* Table Section */}
        <Card
          square={props?.flatCards && isSmallScreen}
          raised={!(props?.flatCards && isSmallScreen)}
          sx={{
            pb: props.loading ? 2 : 0,
            mb: isSmallScreen && props?.flatCards ? 0 : 3
          }}
        >
          {!!props.filters && (
            <Box p={isSmallScreen ? 3 : 4}>{props.filters}</Box>
          )}
          {props.table}
        </Card>
      </Container>
    </>
  );
}

export default TablePageLayout;
