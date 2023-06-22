import { useContext, useMemo } from 'react';

import { noop } from 'lodash-es';
import { useTranslation } from 'react-i18next';

import { ContractContext, DialogEnum } from 'Contract/ContractProvider';

import CommonAlert from 'common/Alerts';
import { useCartContext } from 'providers/CartProvider';

type Props = {
  count: number;
};

type DialogConfigMemo = {
  [key: string]: {
    title: string;
    message: string;
    confirmBtnTitle?: string;
    onConfirm?: () => void;
    onCancel?: () => void;
    cancelBtnTitle?: string;
  };
};

export default function ContractDialogs(props: Props) {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();

  /**
   * Context
   */
  const {
    contractData,
    dialogType: dialogueType = '',
    goToCart,
    resetDialog,
    setQtyInputMap,
    qtyInputMap
  } = useContext(ContractContext);
  const { releaseContractToCart } = useCartContext();

  /**
   * Memo
   */
  const { title, message, confirmBtnTitle, onConfirm, cancelBtnTitle } =
    useMemo(configMemo, [
      dialogueType,
      props.count,
      setQtyInputMap,
      t,
      qtyInputMap,
      contractData,
      goToCart,
      releaseContractToCart
    ]);

  /**
   * Render
   */
  return (
    <CommonAlert
      title={title}
      message={message}
      confirmBtnTitle={confirmBtnTitle}
      cancelBtnTitle={cancelBtnTitle}
      onConfirm={handleConfirm}
      onCancel={resetDialog}
      open={!!dialogueType}
    />
  );

  /*
  Callbacks
  */
  function handleConfirm() {
    onConfirm?.();
    resetDialog();
  }

  /**
   * Memo Def
   */
  function configMemo() {
    const configs = {
      [DialogEnum.Clear]: {
        title: t('contracts.clearQtyTitle'),
        message: t('contracts.clearQtyDesc', {
          qty: Object.keys(qtyInputMap)?.length
        }),
        confirmBtnTitle: t('contracts.clearConfirm'),
        cancelBtnTitle: t('contracts.backToContract'),
        onConfirm: () => setQtyInputMap({})
      },
      [DialogEnum.Release]: {
        title: t('contracts.releaseAllTitle'),
        message: t('contracts.releaseAllDesc', { qty: props.count }),
        confirmBtnTitle: t(`contracts.continueRelease`),
        onConfirm: () => {
          releaseContractToCart(contractData);
          goToCart();
        },
        cancelBtnTitle: t('contracts.backToContract')
      },
      [DialogEnum.SubtotalToLarge]: {
        title: t('contracts.releaseAllOver10MilTitle'),
        message: t('contracts.releaseAllOver10MilDesc'),
        cancelBtnTitle: t('contracts.backToContract')
      },
      '': {
        title: '',
        message: '',
        confirmBtnTitle: '',
        onConfirm: noop
      }
    } as DialogConfigMemo;
    return configs[dialogueType];
  }
}
