import { useEffect, useState } from 'react';

import { Box, Typography } from '@dialexa/reece-component-library';

export default function CreditCardCallback() {
  /**
   * State
   */
  const [loaded, setLoaded] = useState(false);

  /**
   * Effect
   */
  useEffect(() => {
    if (!loaded) {
      setLoaded(true);
      window.parent.postMessage(window.location.href, window.location.origin);
    }
  }, [loaded]);

  /**
   * Render
   */
  return (
    <Box flex={1} display="flex" justifyContent="center" alignItems="center">
      <Typography variant="h6">
        Sending you back to the credit card page...
      </Typography>
    </Box>
  );
}
