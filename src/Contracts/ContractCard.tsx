import React from 'react';

import { Box, Grid, Link, Typography } from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { Link as RouterLink, useLocation } from 'react-router-dom';

import { Contract } from 'generated/graphql';

type Props = {
  contract?: Contract;
  isEven?: boolean;
  index: number;
};

function ContractCard(props: Props) {
  /**
   * Custom hooks
   */
  const { t } = useTranslation();
  const { search } = useLocation();

  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center'
      }}
      borderColor="secondary03.main"
      data-testid={`row_${props?.contract?.contractNumber}`}
    >
      <Grid
        container
        alignItems="flex-end"
        justifyContent="center"
        sx={(theme) => ({
          bgcolor: props.isEven ? 'lightestGray.main' : undefined
        })}
      >
        <Grid item sx={{ pr: '30px', pl: '30px', pt: '16px', pb: '20px' }}>
          <Box pb={1}>
            <Typography
              sx={{
                fontWeight: 700,
                lineHeight: '24px'
              }}
            >
              {t('contracts.contractNumber')}:{' '}
              <Link
                component={RouterLink}
                color="primary02.main"
                to={{
                  pathname: `/contract/${encodeURIComponent(
                    props.contract?.contractNumber ?? ''
                  ).trim()}`,
                  state: { fromContracts: true, search }
                }}
                data-testid={`contractNumber-${props?.index}`}
              >
                {`${props.contract?.contractNumber}`}
              </Link>
            </Typography>
          </Box>
          <Typography
            sx={{
              lineHeight: '24px',
              size: '14px',
              width: '335px',
              whiteSpace: 'nowrap',
              overflow: 'hidden',
              textOverflow: 'ellipsis',
              pb: 1
            }}
          >
            {t('contracts.contractName')}:{' '}
            <Typography
              sx={{ display: 'inline' }}
              data-testid={`description-${props?.index}`}
            >
              {`${props.contract?.description}`}
            </Typography>
          </Typography>
          <Typography
            sx={{
              lineHeight: '24px',
              size: '14px',
              pb: 1
            }}
          >
            {t('contracts.lastReleaseDate')}:{' '}
            <Typography
              sx={{ display: 'inline' }}
              data-testid={`lastReleaseDate-${props?.index}`}
            >
              {`${props.contract?.lastReleaseDate}`}
            </Typography>
          </Typography>

          <Typography
            sx={{
              lineHeight: '24px',
              size: '14px',
              pb: 1
            }}
          >
            {t('contracts.jobNumber')}:{' '}
            <Typography
              sx={{ display: 'inline' }}
              data-testid={`jobNumber-${props?.index}`}
            >
              {`${props.contract?.jobNumber}`}
            </Typography>
          </Typography>

          <Typography
            sx={{
              lineHeight: '24px',
              size: '14px',
              pb: 1
            }}
          >
            {t('contracts.jobName')}:{' '}
            <Typography
              sx={{ display: 'inline' }}
              data-testid={`jobName-${props?.index}`}
            >
              {`${props.contract?.jobName}`}
            </Typography>
          </Typography>

          <Typography
            sx={{
              lineHeight: '24px',
              size: '14px',
              pb: 1
            }}
          >
            {t('contracts.firstReleaseDate')}:{' '}
            <Typography
              sx={{ display: 'inline' }}
              data-testid={`firstReleaseDate-${props?.index}`}
            >
              {`${props.contract?.firstReleaseDate}`}
            </Typography>
          </Typography>
        </Grid>
      </Grid>
    </Box>
  );
}

export default ContractCard;
