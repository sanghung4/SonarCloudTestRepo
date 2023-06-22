import { Alert } from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { WarningIcon } from 'icons';
import 'Order/utils/styles.scss';

export default function BackOrderWarning() {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();

  /**
   * Render
   */
  return (
    <Alert
      className="alert"
      icon={<WarningIcon className="alert__warning-icon" />}
      data-testid="back-order-warning-alert"
    >
      <b>{t('common.warning')}:</b>
      {t('cart.backOrderWarningText')}
      <b>{t('cart.backOrderWarningBoldText')}</b>
      {t('cart.backOrderWarningAddText')}
    </Alert>
  );
}
