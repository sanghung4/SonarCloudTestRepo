import {
  Box,
  Button,
  Grid,
  MenuItem,
  Select,
  TextField,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { FormHelperTextProps } from '@mui/material';
import { camelCase, isNull } from 'lodash-es';
import { useTranslation } from 'react-i18next';

import { InviteUserFormInput } from 'Invite';
import useInviteUserForm from 'Invite/util/useInviteUserForm';

export type InviteUserFormValues = Omit<InviteUserFormInput, 'erp'>;
export type InviteUserProps = {
  onSubmitInvite: (invite: InviteUserFormValues) => void;
};

export default function InviteUser(props: InviteUserProps) {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * Form
   */
  const { approvers, formik, requiresApprover, roles } =
    useInviteUserForm(props);
  const { errors, touched, values, handleBlur, handleChange, handleSubmit } =
    formik;

  /**
   * Render
   */
  return (
    <Box
      display="flex"
      flexDirection="column"
      alignItems="center"
      pt={isSmallScreen ? 3 : 6}
    >
      <Typography
        variant={isSmallScreen ? 'h4' : 'h3'}
        component="h1"
        color="primary"
        fontWeight={700}
      >
        {t('user.inviteNewUser')}
      </Typography>
      <Box
        component="form"
        onSubmit={handleSubmit}
        noValidate
        pt={isSmallScreen ? 4 : 5}
        pb={isSmallScreen ? 6 : 0}
      >
        <Grid container spacing={isSmallScreen ? 1 : 2}>
          <Grid item xs={12}>
            <TextField
              id="invite-email"
              data-testid="emailInput"
              name="email"
              label={t('common.emailAddress')}
              placeholder={t('common.enterEmailAddress')}
              value={values.email}
              onChange={handleChange}
              onBlur={handleBlur}
              error={!!(touched.email && errors.email)}
              helperText={touched.email && errors.email ? errors.email : ' '}
              inputProps={{ 'data-testid': 'invite-user-email-input' }}
              FormHelperTextProps={
                {
                  'data-testid': 'email-msg'
                } as FormHelperTextProps
              }
              fullWidth
              required
            />
          </Grid>
          <Grid item xs={12}>
            <TextField
              id="invite-first-name"
              data-testid="firstNameInput"
              name="firstName"
              label={t('common.firstName')}
              placeholder={t('common.firstName')}
              value={values.firstName}
              onChange={handleChange}
              onBlur={handleBlur}
              error={!!(touched.firstName && errors.firstName)}
              helperText={
                touched.firstName && errors.firstName ? errors.firstName : ' '
              }
              inputProps={{ 'data-testid': 'invite-user-firstname-input' }}
              FormHelperTextProps={
                {
                  'data-testid': 'firstname-msg'
                } as FormHelperTextProps
              }
              fullWidth
              required
            />
          </Grid>
          <Grid item xs={12}>
            <TextField
              id="invite-last-name"
              data-testid="lastNameInput"
              name="lastName"
              label={t('common.lastName')}
              placeholder={t('common.lastName')}
              value={values.lastName}
              onChange={handleChange}
              onBlur={handleBlur}
              error={!!(touched.lastName && errors.lastName)}
              helperText={
                touched.lastName && errors.lastName ? errors.lastName : ' '
              }
              inputProps={{ 'data-testid': 'invite-user-lastname-input' }}
              FormHelperTextProps={
                {
                  'data-testid': 'lastname-msg'
                } as FormHelperTextProps
              }
              fullWidth
              required
            />
          </Grid>
          <Grid item xs={12} data-testid="invite-user-role-grid">
            <Select
              id="invite-role"
              name="userRoleId"
              label={t('common.role')}
              placeholder={t('common.select')}
              value={values.userRoleId}
              onChange={handleChange}
              onBlur={handleBlur}
              error={!!(errors.userRoleId && touched.userRoleId)}
              helperText={
                touched.userRoleId && errors.userRoleId
                  ? errors.userRoleId
                  : ' '
              }
              renderValue={(id) => {
                if (roles.length) {
                  const selectedType = roles
                    .find((pt) => pt?.id === id)
                    ?.name?.toLowerCase();

                  return selectedType
                    ? t(`roles.${camelCase(selectedType)}`)
                    : t('common.select');
                }
              }}
              inputProps={{ 'data-testid': 'invite-user-roles-input' }}
              fullWidth
              required
              sx={{ mt: '0 !important' }}
            >
              {!!roles &&
                roles.map(
                  (role) =>
                    !isNull(role.id) &&
                    !isNull(role.name) && (
                      <MenuItem value={role?.id} key={role.id}>
                        {t(`roles.${camelCase(role.name)}`)}
                      </MenuItem>
                    )
                )}
            </Select>
          </Grid>
          <Grid item xs={12} data-testid="invite-user-approver-grid">
            {requiresApprover && (
              <Select
                id="invite-role-approver"
                data-testid="roleInput"
                name="approverId"
                label={t('common.approver')}
                placeholder="Select"
                value={values.approverId}
                onChange={handleChange}
                onBlur={handleBlur}
                required
                error={!!(touched.approverId && errors.approverId)}
                helperText={
                  touched.approverId && errors.approverId
                    ? errors.approverId
                    : ' '
                }
                renderValue={(id) => {
                  const approver = approvers.find((a) => a.id === id);
                  return approver
                    ? `${approver?.firstName} ${approver?.lastName}`
                    : '';
                }}
                fullWidth
                inputProps={{ 'data-testid': 'invite-user-approver-input' }}
                sx={{ mt: '0 !important' }}
              >
                {approvers.map((approver) => (
                  <MenuItem value={approver.id} key={approver.id}>
                    {approver?.firstName} {approver?.lastName}
                  </MenuItem>
                ))}
              </Select>
            )}
          </Grid>
          <Grid container item xs={12} justifyContent="center">
            <Box width={isSmallScreen ? 0.75 : 0.5} mt={isSmallScreen ? 3 : 8}>
              <Button type="submit" fullWidth data-testid="inviteUserButton">
                {t('user.inviteUser')}
              </Button>
            </Box>
          </Grid>
        </Grid>
      </Box>
    </Box>
  );
}
