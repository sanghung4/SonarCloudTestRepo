import React from 'react';

import {
  Grid,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';

interface Props {
  fieldName: String;
  fieldValue: String;
  color: any;
  testId: string;
}

function UserInfoText(props: Props) {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();

  return (
    <Grid
      container
      direction="row"
      spacing={2}
      justifyContent="space-between"
      wrap="nowrap"
      sx={{
        width: 1,
        px: isSmallScreen ? 3 : 5,
        py: 2.5
      }}
    >
      <Grid item>
        <Typography noWrap variant={isSmallScreen ? 'body2' : 'body1'}>
          {props.fieldName}
        </Typography>
      </Grid>
      <Grid item zeroMinWidth>
        <Typography
          noWrap
          align="right"
          variant={isSmallScreen ? 'body2' : 'body1'}
          color={props.color}
          sx={{ fontWeight: 700 }}
          data-testid={props.testId}
        >
          {props.fieldValue}
        </Typography>
      </Grid>
    </Grid>
  );
}

export default UserInfoText;
