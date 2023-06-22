import { MouseEvent, ReactNode } from 'react';

import {
  Box,
  Button,
  Divider,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';

export type CheckoutHeadingProps = {
  title: ReactNode;
  actionIcon?: ReactNode;
  actionText?: string;
  actionCb?: (e?: MouseEvent<HTMLButtonElement>) => void;
  noMargin?: boolean;
  dataTestId?: string;
};

export default function Heading(props: CheckoutHeadingProps) {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();

  /**
   * Render
   */
  return (
    <>
      <Box
        pt={isSmallScreen ? 6 : 2}
        pb={3}
        display="flex"
        justifyContent="space-between"
        alignItems="center"
      >
        <Typography variant="h5" color="primary">
          {props.title}
        </Typography>
        {!!(props.actionIcon || props.actionText) && (
          <Button
            variant="text"
            onClick={(e) => props.actionCb && props.actionCb(e)}
            startIcon={props.actionIcon}
            sx={{ p: 0 }}
            data-testid={props.dataTestId}
          >
            {props.actionText}
          </Button>
        )}
      </Box>
      <Box
        ml={isSmallScreen ? -2 : -4}
        mb={props.noMargin ? 2 : isSmallScreen ? 5 : 7}
        width={(theme) => `calc(100% + ${theme.spacing(4)})`}
      >
        <Divider />
      </Box>
    </>
  );
}
