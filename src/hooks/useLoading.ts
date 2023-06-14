import { useEffect } from 'react';
import { useOverlay } from 'providers/Overlay';

// Turns on the overlay if any of the loading params are set to true
// Closing after complete should be used when no overlays should
// open after loading is complete.

export const useLoading = (
  loadingArray: boolean[],
  closeAfterComplete: boolean = false
) => {
  const { toggleLoading, visibleOverlay } = useOverlay();
  useEffect(() => {
    const loading = loadingArray.some((state) => state);

    if (loading && visibleOverlay !== 'loading') {
      toggleLoading(true);
    }

    if (!loading && visibleOverlay === 'loading' && closeAfterComplete) {
      toggleLoading(false);
    }
  }, [...loadingArray]);
};
