import { HTMLAttributes, useEffect, useMemo, useRef, useState } from 'react';

import {
  Box,
  IconButton,
  Modal,
  Slide,
  Typography,
  alpha,
  useScreenSize,
  SxProps
} from '@dialexa/reece-component-library';
import useResizeObserver from 'use-resize-observer/polyfilled';

import Backdrop from 'common/Sidebar/Backdrop';
import SidebarError from 'common/Sidebar/SidebarError';
import ConditionalWrapper from 'common/ConditionalWrapper';
import { CloseIcon } from 'icons';

type ElementProps = HTMLAttributes<HTMLElement>;
interface Props extends ElementProps {
  close: () => void;
  error?: boolean;
  on: boolean;
  title?: string;
  isContentLoading?: boolean;
  widthOverride?: number;
  containerSx?: SxProps;
  backdropSx?: SxProps;
}

function Sidebar(props: Props) {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();

  /**
   * State
   */
  const [sidebarHeight, setSidebarHeight] = useState(0);

  /**
   * Refs
   */
  const contentRef = useRef<HTMLDivElement>(null);
  const { height: contentHeight = 0 } = useResizeObserver<HTMLDivElement>({
    ref: contentRef
  });

  /**
   * Memo
   */
  const availableHeight = useMemo(availableHeightMemo, [
    contentHeight,
    contentRef
  ]);

  /**
   * Effect
   */
  useEffect(sidebarHeightEffect, [
    availableHeight,
    contentHeight,
    props.isContentLoading,
    props.on,
    isSmallScreen
  ]);
  useEffect(disableScrolling, [props.on]);

  /**
   * Render
   */
  return (
    <>
      <Backdrop
        on={props.on}
        onClick={props.close}
        sx={{ backdropFilter: 'none', ...props.backdropSx }}
      />
      <Box
        width="1"
        height={1}
        position="fixed"
        zIndex={-1}
        component="div"
        ref={contentRef}
      />
      <ConditionalWrapper
        condition={isSmallScreen}
        wrapper={(children: JSX.Element) => (
          <Modal open={props.on} disablePortal>
            {children}
          </Modal>
        )}
      >
        <Slide
          direction="left"
          in={props.on}
          mountOnEnter
          unmountOnExit
          children={
            <Box
              position="absolute"
              top={0}
              right={0}
              zIndex={3}
              overflow="hidden"
              bgcolor="common.white"
              boxShadow={(theme) =>
                `0 5px 20px 0 ${alpha(theme.palette.common.black, 0.1)}`
              }
              sx={{
                height: isSmallScreen ? '100vh' : undefined,
                maxHeight: isSmallScreen
                  ? undefined
                  : availableHeight || '100%',
                width: isSmallScreen ? '100vw' : props.widthOverride ?? 380,
                ...props.containerSx
              }}
            >
              <Box
                data-testid="branch-list"
                display="flex"
                flexDirection="column"
              >
                <Box
                  id="sidebar-header"
                  py={1}
                  pl={3}
                  pr={2}
                  display="flex"
                  justifyContent="space-between"
                  alignItems="center"
                  sx={(theme) => ({
                    boxShadow: `0 2px 10px 0 ${alpha(
                      theme.palette.primary03.main,
                      0.15
                    )}`
                  })}
                >
                  {!!props.title && (
                    <Typography variant="h5" color="primary">
                      {props.title}
                    </Typography>
                  )}
                  <IconButton
                    onClick={props.close}
                    size="large"
                    sx={{ p: 1 }}
                    data-testid="slider-close"
                  >
                    <CloseIcon />
                  </IconButton>
                </Box>
                {props.error ? (
                  <SidebarError />
                ) : (
                  <Box
                    sx={{
                      [isSmallScreen || props.isContentLoading
                        ? 'height'
                        : 'maxHeight']: sidebarHeight
                    }}
                    display="flex"
                    flexDirection="column"
                  >
                    {props.children}
                  </Box>
                )}
              </Box>
            </Box>
          }
        />
      </ConditionalWrapper>
    </>
  );

  /**
   * Memo defs
   */
  function availableHeightMemo() {
    return Math.min(
      contentHeight,
      window.innerHeight - (contentRef.current?.getBoundingClientRect().y ?? 0)
    );
  }

  /**
   * Effect defs
   */
  function sidebarHeightEffect() {
    if (!props.on || props.isContentLoading) {
      // 56 is estimated header height
      setSidebarHeight(
        (isSmallScreen ? window.innerHeight : contentHeight) - 56
      );
    } else {
      // recalculate the header height after it mounts
      (async () => {
        // (a)wait for sidebar to mount
        await new Promise((res) => setTimeout(res, 0));
        setSidebarHeight(
          (isSmallScreen ? window.innerHeight : availableHeight) -
            // Ignoreing since this will never occur in unit test
            // istanbul ignore next
            (document.getElementById('sidebar-header')?.getBoundingClientRect()
              .height ?? 0)
        );
      })();
    }
  }
  function disableScrolling() {
    const root = document.getElementById('root');
    // istanbul ignore next
    if (root) {
      root.style.overflow = props.on ? 'hidden' : 'visible';
    }
    // istanbul ignore next
    return () => {
      if (root) {
        root.style.overflow = 'visible';
      }
    };
  }
}

export default Sidebar;
