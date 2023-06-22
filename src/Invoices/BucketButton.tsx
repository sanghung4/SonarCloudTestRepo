import React, { MouseEvent } from 'react';

import {
  Grid,
  Skeleton,
  SxProps,
  Theme
} from '@dialexa/reece-component-library';
import { kebabCase } from 'lodash-es';

import { format } from 'utils/currency';
import {
  BucketContainer,
  BucketPricingTypography,
  BucketTitleTypography
} from './util/styled';

type Props = {
  active?: boolean;
  title: string;
  loading?: boolean;
  onClick?: (e: MouseEvent) => void;
  value?: number;
  sx?: SxProps<Theme>;
};

function BucketButton(props: Props) {
  return (
    <BucketContainer
      component="button"
      id={props.title}
      onClick={props.onClick}
      data-testid={kebabCase(`${props.title}-button`)}
      sx={{
        ...props.sx
      }}
    >
      <Grid container>
        <Grid item md={12} xs={6} alignContent="center" container>
          <BucketTitleTypography color="textPrimary">
            {props.title}
          </BucketTitleTypography>
        </Grid>
        <Grid item md={12} xs={6}>
          <BucketPricingTypography variant="h6">
            {props.loading ? (
              <Skeleton sx={{ marginLeft: 2, marginRight: 2 }} />
            ) : (
              format(props?.value ?? 0)
            )}
          </BucketPricingTypography>
        </Grid>
      </Grid>
    </BucketContainer>
  );
}

export default BucketButton;
