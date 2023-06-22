import { ReactNode } from 'react';

import { Alert, Box, Grid } from '@dialexa/reece-component-library';

type Props = {
  show?: boolean;
  infoMessages?: string[];
  children: ReactNode;
};

function Overlay(props: Props) {
  return (
    <Box position="relative">
      <Box
        position="absolute"
        width={1}
        height={1}
        bgcolor={props.show ? 'common.white' : 'transparent'}
        sx={{ opacity: props.show ? 0.6 : 0 }}
      />
      {!!props.show && (
        <Box position="absolute" width={1} height={1} zIndex={999}>
          <Grid container alignItems="center" justifyContent="center">
            <Grid item md={6} xs={12}>
              <Alert
                severity="info"
                variant="filled"
                sx={{ color: 'common.white' }}
              >
                {props.infoMessages?.map((m: string) => (
                  <Box key={m}>{m} </Box>
                ))}
              </Alert>
            </Grid>
          </Grid>
        </Box>
      )}
      {props.children}
    </Box>
  );
}

export default Overlay;
