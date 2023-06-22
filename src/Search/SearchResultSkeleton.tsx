import React from 'react';

import {
  Box,
  Grid,
  Skeleton,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';

type Props = {
  testid: string;
};

function SearchResultSkeleton(props: Props) {
  const { isSmallScreen } = useScreenSize();

  const desktop = (
    <Box p={2} data-testid={props.testid}>
      <Skeleton
        variant="rectangular"
        height={120}
        width="100%"
        sx={{ mb: 3 }}
      />
      <Typography variant="caption">
        <Skeleton variant="text" />
      </Typography>
      <Typography>
        <Skeleton variant="text" height="3.5rem" />
      </Typography>
      <Typography variant="caption">
        <Skeleton variant="text" />
      </Typography>
      <Box display="flex" alignItems="center" mt={5}>
        <Skeleton variant="rectangular" height={30} width={45} />
        <Skeleton
          variant="rectangular"
          height={30}
          width="100%"
          sx={{ ml: 1 }}
        />
      </Box>
    </Box>
  );

  const mobile = (
    <Grid container spacing={3} data-testid={props.testid}>
      <Grid item xs={4}>
        <Skeleton variant="rectangular" height={170} width="100%" />
      </Grid>
      <Grid item xs={8}>
        <Typography variant="caption">
          <Skeleton variant="text" />
        </Typography>
        <Typography>
          <Skeleton variant="text" height="3.5rem" />
        </Typography>
        <Typography variant="caption">
          <Skeleton variant="text" />
        </Typography>
        <Box display="flex" alignItems="center" mt={5}>
          <Skeleton
            variant="rectangular"
            height={41}
            width={60}
            sx={{ flexShrink: 0 }}
          />
          <Skeleton
            variant="rectangular"
            height={41}
            width="100%"
            sx={{ ml: 2 }}
          />
        </Box>
      </Grid>
    </Grid>
  );

  return isSmallScreen ? mobile : desktop;
}

export default SearchResultSkeleton;
