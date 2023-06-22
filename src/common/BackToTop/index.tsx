import { useEffect, useRef, useState } from 'react';

import {
  Box,
  Button,
  Grow,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import useResizeObserver from 'use-resize-observer/polyfilled';

import { LargeArrowUpIcon } from 'icons';

const boxSx = { '& > button': { py: 2 } };
const growStyle = { transformOrigin: 'center bottom' };

export default function BackToTop() {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * State
   */
  const [shouldShow, setShouldShow] = useState(false);

  /**
   * Ref
   */
  const contentRef = useRef<HTMLElement>(document.getElementById('content'));
  const { height } = useResizeObserver<HTMLElement>({
    ref: contentRef
  });

  /**
   * Effect
   */
  useEffect(() => setShouldShow((height ?? 0) > window.outerHeight), [height]);

  /**
   * Render
   */
  return (
    <Grow in={isSmallScreen && shouldShow} style={growStyle}>
      <Box
        bgcolor="common.white"
        border={1}
        borderColor="lightGray.main"
        sx={boxSx}
      >
        <Button
          variant="text"
          fullWidth
          startIcon={<LargeArrowUpIcon />}
          data-testid="back-to-top-button"
          onClick={() => window.scrollTo({ top: 0, behavior: 'smooth' })}
        >
          {t('backToTop.text')}
        </Button>
      </Box>
    </Grow>
  );
}
