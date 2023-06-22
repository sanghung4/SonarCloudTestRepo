import React, { MouseEvent } from 'react';

import {
  Grid,
  Skeleton,
  Typography,
  useScreenSize,
  SxProps,
  Theme
} from '@dialexa/reece-component-library';
import { kebabCase } from 'lodash-es';

import { format } from 'utils/currency';
import { StyledInvoiceBucketContainer } from './util/styled';

type Props = {
  active?: boolean;
  title: string;
  loading?: boolean;
  onClick?: (e: MouseEvent) => void;
  value?: number;
  sx?: SxProps<Theme>;
};

function InvoiceBucketButton(props: Props) {
  /**
   * Custom hooks
   */
  const { isSmallScreen } = useScreenSize();

  return (
    <StyledInvoiceBucketContainer
      component="button"
      id={props.title}
      onClick={props.onClick}
      data-testid={kebabCase(`${props.title}-button`)}
    >
      <Grid container>
        <Grid item md={12} xs={6} alignContent="center" container>
          <Typography
            align={isSmallScreen ? 'left' : 'center'}
            color="textPrimary"
            sx={{
              pl: isSmallScreen ? 1 : 0,
              width: 1
            }}
          >
            {props.title}
          </Typography>
        </Grid>
        <Grid item md={12} xs={6}>
          <Typography align={isSmallScreen ? 'right' : 'center'} variant="h6">
            {props.loading ? <Skeleton /> : format(props?.value ?? 0)}
          </Typography>
        </Grid>
      </Grid>
    </StyledInvoiceBucketContainer>
  );
}

export default InvoiceBucketButton;
