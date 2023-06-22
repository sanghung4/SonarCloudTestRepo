import React from 'react';

import {
  Box,
  ListItem,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';

export type Props = {
  label: string;
  value: string;
  dataTestId?: string;
};

function ViewTableListItem(props: Props) {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();

  return (
    <ListItem
      disableGutters={isSmallScreen}
      sx={{
        display: 'flex',
        '&:hover': {
          bgcolor: 'transparent'
        }
      }}
    >
      <Box flexGrow={1}>
        <Typography>{props.label}:</Typography>
      </Box>
      <Typography
        color="primary"
        component="span"
        sx={{ fontWeight: 'bold' }}
        data-testid={props.dataTestId}
      >
        {props.value}
      </Typography>
    </ListItem>
  );
}

export default ViewTableListItem;
