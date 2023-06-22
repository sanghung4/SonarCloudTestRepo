import React, { ChangeEvent, Fragment, useMemo } from 'react';

import {
  Box,
  Divider,
  Grid,
  MenuItem,
  Radio,
  Select,
  Skeleton,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { useGetRolesQuery, useGetApproversQuery } from 'generated/graphql';
import ViewTableListItem from 'User/ViewUserListItem';
import Overlay from 'common/Overlay';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';

export type RoleInfo = {
  id?: string;
  approverId?: string;
};

type Props = {
  disabled?: boolean;
  onChange?: (info: RoleInfo) => void;
  userId?: String;
  value?: RoleInfo; // id of role
  customerApproval?: boolean;
};

function Roles(props: Props) {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * Context
   */
  const { selectedAccounts } = useSelectedAccountsContext();

  /**
   * Data Fetching
   */
  const { data: rolesQuery, loading: rolesLoading } = useGetRolesQuery();
  const { data: approversQuery } = useGetApproversQuery({
    variables: {
      billToAccountId: selectedAccounts.billTo?.id ?? ''
    }
  });

  /**
   * Memo
   */
  const requiresApprover = useMemo(() => {
    return (
      rolesQuery?.roles?.find((r) => r?.name === 'Purchase with Approval')
        ?.id === props?.value?.id
    );
  }, [props, rolesQuery]);

  const selectedApprover = useMemo(() => {
    return approversQuery?.approvers.find(
      (a) => a?.id === props.value?.approverId
    );
  }, [approversQuery, props.value]);

  /**
   * Callbacks
   */
  const handleRoleChange = (e: ChangeEvent<HTMLInputElement>) => {
    if (props.onChange) {
      props.onChange({
        ...props.value,
        id: e.target.value
      });
    }
  };

  const handleApproverChange = (e: any) => {
    if (props.onChange) {
      props.onChange({
        ...props.value,
        approverId: e.target.value
      });
    }
  };

  const messageArray = [
    t('user.userRoleMustBeAdminMessage'),
    t('user.ensureTradesAccountOwner')
  ];

  return (
    <>
      <Typography variant="h4">{t('common.roles')}</Typography>
      <Box pt={4}>
        {rolesLoading ? (
          <Grid
            container
            direction={isSmallScreen ? 'column' : 'row'}
            spacing={isSmallScreen ? 0 : 2}
          >
            {[...new Array(5)].map((_, i) => (
              <Box key={i} pt={isSmallScreen && i > 0 ? 1.5 : 0}>
                <Grid item xs>
                  <Skeleton variant="rectangular" height={44} />
                </Grid>
              </Box>
            ))}
          </Grid>
        ) : (
          <>
            <Box>
              <Overlay
                infoMessages={messageArray}
                show={props.customerApproval}
              >
                <Grid
                  container
                  direction={isSmallScreen ? 'column' : 'row'}
                  spacing={isSmallScreen ? 0 : 2}
                >
                  {rolesQuery?.roles
                    ? rolesQuery.roles.map((role, i) => {
                        return role !== null ? (
                          <Fragment key={role.id}>
                            <Grid
                              container
                              item
                              xs
                              alignItems="flex-start"
                              wrap="nowrap"
                              sx={{
                                pt: isSmallScreen && i > 0 ? 1.5 : 0
                              }}
                            >
                              <Radio
                                checked={props.value?.id === role.id}
                                disabled={
                                  props.customerApproval ? true : props.disabled
                                }
                                onChange={handleRoleChange}
                                value={role.id}
                                name="role"
                                inputProps={{
                                  'aria-label': role.name || ''
                                }}
                                data-testid={`${role.name}-check-box`}
                              />
                              <Box
                                display="flex"
                                flexDirection="column"
                                my={0.25}
                              >
                                <Typography variant="body1">
                                  {role.name}
                                </Typography>
                                <Typography variant="body2" color="primary">
                                  {i > 0
                                    ? role.description
                                    : String.fromCharCode(8203)}
                                </Typography>
                              </Box>
                            </Grid>
                          </Fragment>
                        ) : null;
                      })
                    : null}
                </Grid>
              </Overlay>
            </Box>
          </>
        )}
      </Box>
      {requiresApprover && !rolesLoading ? (
        <Box pb={isSmallScreen ? 3 : 2} pt={isSmallScreen ? 5 : 3}>
          <Typography variant="h4">{t('user.selectApprover')}</Typography>
          <Box pt={2}>
            <Grid container>
              <Grid item xs={12} md={5}>
                {props.disabled ? (
                  <>
                    <ViewTableListItem
                      label={t('user.approverLabel')}
                      value={`${selectedApprover?.firstName} ${selectedApprover?.lastName}`}
                    />
                    <Divider />
                  </>
                ) : (
                  <Select
                    label="Approver"
                    name="approver"
                    disabled={props.disabled}
                    placeholder="Select approver"
                    value={props.value?.approverId || ''}
                    onChange={handleApproverChange}
                    renderValue={(id) => {
                      const approver = approversQuery?.approvers.find(
                        (a) => a.id === id
                      );
                      return approver
                        ? `${approver?.firstName} ${approver?.lastName}`
                        : '';
                    }}
                    fullWidth
                  >
                    {approversQuery?.approvers
                      .filter((a) => a.id !== props.userId)
                      .map((approver, i) => (
                        <MenuItem value={approver.id} key={approver.id ?? i}>
                          {approver?.firstName} {approver?.lastName}
                        </MenuItem>
                      ))}
                  </Select>
                )}
              </Grid>
            </Grid>
          </Box>
        </Box>
      ) : null}
    </>
  );
}

export default Roles;
