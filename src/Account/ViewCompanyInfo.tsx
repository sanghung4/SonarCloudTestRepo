import React from 'react';

import {
  Box,
  Card,
  CardHeader,
  Divider,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { ErpAccount } from 'generated/graphql';
import UserInfoText from 'Account/UserInfoText';

interface Props {
  company: ErpAccount;
}

function ViewCompanyInfo(props: Props) {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  return (
    <Card square={isSmallScreen} raised={!isSmallScreen}>
      <Box p={1} py={1.625}>
        <CardHeader
          title={t('common.companyInformation')}
          titleTypographyProps={{ variant: isSmallScreen ? 'h5' : 'h4' }}
          data-testid="company-information-title"
        />
      </Box>
      <Box>
        <Divider />
      </Box>
      <UserInfoText
        fieldName={`${t('common.companyName')}:`}
        fieldValue={props.company.companyName!}
        color="textPrimary"
        testId="company-name-field"
      />
      <Box pr={2} pl={2}>
        <Divider />
      </Box>
      <UserInfoText
        fieldName={`${t('common.streetAddress')}:`}
        fieldValue={`${props.company.street1} ${props.company.street2}${
          props.company.street2 ? ' ' : ''
        }${props.company.city}, ${props.company.state} ${props.company.zip}`}
        testId="street-address-field"
        color="textPrimary"
      />
    </Card>
  );
}

export default ViewCompanyInfo;
