import { Dialog } from '@headlessui/react';
import { useTranslation } from 'react-i18next';

import Button from 'components/Button';
import { useDomainInfo } from 'hooks/useDomainInfo';
import { QuestionMark, DeleteIcon } from 'icons';
import DesktopFortilineInvoice from '../images/desktop-fortiline-invoice.png';
import DesktopInvoice from '../images/desktop-invoice.png';
import MobileFortilineInvoice from '../images/mobile-fortiline-invoice.png';
import MobileInvoice from '../images/mobile-invoice.png';
import { testIds } from 'test-utils/testIds';

/**
 * Types
 */
interface ReceiptModalProps {
  open: boolean;
  onClose: () => void;
}

/**
 * Component
 */
function ReceiptModal(props: ReceiptModalProps) {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();
  const { isWaterworks } = useDomainInfo();
  /**
   * Constants
   */
  const TEST_IDS = testIds.Register.StepThree;
  return (
    <Dialog
      open={props.open}
      onClose={handleClose}
      className="register-account-info-modal"
      data-testid={TEST_IDS.receiptModal}
    >
      <div className="register-account-info-modal__overlay"></div>

      {/* Actual modal and content */}
      <Dialog.Panel className="register-account-info-modal__panel">
        <div className="register-account-info-modal__panel__header__wrapper">
          <div className="register-account-info-modal__panel__header__wrapper__text">
            <QuestionMark className="register-account-info-modal__panel__header__wrapper__question-mark" />
            {t('register.findAccountNumber')}
          </div>

          <DeleteIcon
            onClick={handleClose}
            className="register-account-info-modal__panel__header__wrapper__close-icon"
            data-testid={TEST_IDS.closeReceiptModal}
          />
        </div>
        <span className="register-account-info-modal__panel__account-number-wrapper mobile">
          <span className="register-account-info-modal__panel__account-number-wrapper mobile__description__account">
            {t('register.your')}
            <span className="register-account-info-modal__panel__account-number-wrapper mobile__description__account__bold">
              {t('register.accountNumberDescription')}
            </span>
            {t('register.accountNumberLocation')}
          </span>
          <span className="register-account-info-modal__panel__account-number-wrapper mobile__description__zipCode">
            {t('register.your')}
            <span className="register-account-info-modal__panel__account-number-wrapper mobile__description__zipCode__bold">
              {t('register.zipCodeDescription')}
            </span>
            {t('register.accountZipCodeLocation')}
          </span>
        </span>

        <img
          src={isWaterworks ? MobileFortilineInvoice : MobileInvoice}
          className="register-account-info-modal__panel__receipt mobile"
          alt="receipt"
        />
        <img
          src={isWaterworks ? DesktopFortilineInvoice : DesktopInvoice}
          className="register-account-info-modal__panel__receipt desktop"
          alt="receipt"
        />
        <span className="register-account-info-modal__panel__account-number-wrapper desktop">
          <span className="register-account-info-modal__panel__account-number-wrapper desktop__description__account">
            {t('register.your')}
            <span className="register-account-info-modal__panel__account-number-wrapper desktop__description__account__bold">
              {t('register.accountNumberDescription')}
            </span>
            {t('register.accountNumberLocation')}
          </span>
          <span className="register-account-info-modal__panel__account-number-wrapper desktop__description__zipCode">
            {t('register.your')}
            <span className="register-account-info-modal__panel__account-number-wrapper desktop__description__zipCode__bold">
              {t('register.zipCodeDescription')}
            </span>
            {t('register.accountZipCodeLocation')}
          </span>
        </span>
        <span className="register-account-info-modal__panel__subtext__wrapper">
          <span className="register-account-info-modal__panel__subtext__wrapper__account-number">
            {t('register.cantFindAccountNumber')}
          </span>
          <span className="register-account-info-modal__panel__subtext__wrapper__branch">
            <Button
              variant="text-link"
              label={t('register.contactBranch')}
              size="small"
              onClick={handleFormSubmit}
              data-testid={TEST_IDS.contactBranchButton}
            />
          </span>
        </span>
      </Dialog.Panel>
    </Dialog>
  );

  /**
   * Callback Definitions
   */
  function handleClose() {
    props.onClose();
  }
  function handleFormSubmit() {
    window.open('/location-search');
  }
}

export default ReceiptModal;
