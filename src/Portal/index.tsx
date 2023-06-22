import React from 'react';
import { useTranslation } from 'react-i18next';

function Portal() {
  const { t } = useTranslation();

  return <h1>{t('common.portal')}</h1>;
}

export default Portal;
