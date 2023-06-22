import React from 'react';

import {
  Box,
  Button,
  Grid,
  Link,
  Typography
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { ChevronRightIcon } from 'icons';
import { CoercedUser } from 'UserManagement';

type Props = {
  user: CoercedUser | any;
  onRowClick: (row: CoercedUser) => void;
  rejectedUser?: boolean;
  index: number;
  viewUserId: string;
};

function UserCard(props: Props) {
  /**
   * Custom hooks
   */
  const { t } = useTranslation();

  return (
    <Box
      borderBottom={1}
      py={2}
      width={1}
      sx={{
        borderColor: 'secondary03.main'
      }}
    >
      <Grid
        container
        alignItems="flex-end"
        data-testid={`row_${props?.user.email}`}
      >
        <Grid item>
          <Box pb={1}>
            <Typography
              sx={{
                fontWeight: 500,
                color: 'primary.main'
              }}
            >
              {t('common.emailAddress')}
            </Typography>
            <Link
              href={`mailto:${props.user.email}`}
              sx={{ color: 'primary02.main' }}
              data-testid={`emailAddress-${props?.index}`}
            >
              {props.user.email}
            </Link>
          </Box>
          <Typography
            sx={{
              fontWeight: 500,
              color: 'primary.main'
            }}
          >
            {t('common.name')}
          </Typography>
          <div
            style={{ display: 'inline' }}
            data-testid={`firstName-${props?.index}`}
          >
            {` ${props.user.firstName} `}
          </div>
          <div
            style={{ display: 'inline' }}
            data-testid={`lastName-${props?.index}`}
          >
            {`${props.user.lastName}`}
          </div>
        </Grid>
        <Grid item display="flex" justifyContent="flex-end" flexGrow={1}>
          <Button
            onClick={() => props?.onRowClick(props.user)}
            endIcon={<ChevronRightIcon />}
            variant="text"
            sx={{ p: 0 }}
            data-testid={`viewUser-${props?.viewUserId
              .toLowerCase()
              .replaceAll(' ', '-')}-${props?.index}`}
          >
            {t('user.viewUser')}
          </Button>
        </Grid>
      </Grid>
    </Box>
  );
}

export default UserCard;
