import React, { useCallback } from 'react';

import { GlobalHotKeys } from 'react-hotkeys';
import { useTranslation } from 'react-i18next';

function I18N() {
  /**
   * Custom Hooks
   */
  const { i18n } = useTranslation();

  /**
   * Callbacks
   */
  const swapLanguages = useCallback(() => {
    i18n.changeLanguage(i18n.language === 'test' ? 'en' : 'test');
  }, [i18n]);

  return (
    <GlobalHotKeys
      keyMap={{ SWAP_LANGUAGES: 'esc' }}
      handlers={{ SWAP_LANGUAGES: swapLanguages }}
    />
  );
}

export default I18N;
