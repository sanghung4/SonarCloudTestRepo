import NavigationPrompt, { ChildData } from 'react-router-navigation-prompt';
import { TFunction, useTranslation } from 'react-i18next';

import { Location, Action } from 'history';

import CommonAlert from 'common/Alerts';

type WhenFn = (
  pl: Location,
  nl: Location | undefined,
  action?: Action
) => boolean;
export type NavigationAlertProps = {
  when: boolean | WhenFn;
  title?: string;
  message?: string;
  confirmBtnTitle?: string;
  onConfirm?: () => void;
  onCancel?: () => void;
  cancelBtnTitle?: string;
};
export type SubNavigationAlertProps = NavigationAlertProps & {
  t: TFunction;
};

export default function NavigationAlert(props: NavigationAlertProps) {
  const { t } = useTranslation();
  return (
    <NavigationPrompt when={props.when} allowGoBack>
      {subNavigationAlert({ ...props, t })}
    </NavigationPrompt>
  );
}
export function subNavigationAlert(props: SubNavigationAlertProps) {
  return (navPrompt: ChildData) => {
    return (
      <CommonAlert
        open
        title={props.t('common.navPromptTitle')}
        message={props.t('common.navPromptMsg')}
        confirmBtnTitle={props.t('common.navPromptConfirmBtn')}
        cancelBtnTitle={props.t(`common.cancel`)}
        {...props}
        onConfirm={() => {
          props.onConfirm?.();
          navPrompt.onConfirm();
        }}
        onCancel={() => {
          props.onCancel?.();
          navPrompt.onCancel();
        }}
      />
    );
  };
}
