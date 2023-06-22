import { useContext } from 'react';
import {
  Box,
  Container,
  DropdownButton,
  Grid,
  Hidden,
  Link,
  MenuItem
} from '@dialexa/reece-component-library';
import { useHistory, useLocation } from 'react-router-dom';
import { useTranslation } from 'react-i18next';

import UserButton from 'common/Header/UserButton';
import { CompanyListButton } from 'common/Header/util';
import { HelpIcon, LocationIcon } from 'icons';
import { Configuration } from 'utils/configuration';
import { useDomainInfo } from 'hooks/useDomainInfo';
import { generateCompanyUrl } from 'utils/brandList';
import { AuthContext } from 'AuthProvider';
import {
  CompanyDomain,
  expressionsHomeGallery,
  isWaterworksEnabled,
  isExpressionsHomeGalleryEnabled,
  waterworksCompany
} from 'hooks/utils/useDomainInfo';

function CompanyListHeader() {
  /**
   * Custom Hooks
   */
  const { companyList, port } = useDomainInfo();
  const history = useHistory();
  const location = useLocation();
  const { t } = useTranslation();

  /**
   * Context
   */
  const { features } = useContext(AuthContext);

  /**
   * Logic
   */
  let filteredCompanyList: CompanyDomain[] = [];
  if (!isWaterworksEnabled(features)) {
    filteredCompanyList = companyList.filter(
      (c) => c.sub !== waterworksCompany.sub
    );
  } else {
    filteredCompanyList = companyList;
  }

  if (!isExpressionsHomeGalleryEnabled(features)) {
    filteredCompanyList = filteredCompanyList.filter(
      (c) => c.sub !== expressionsHomeGallery.sub
    );
  }

  // Hide if on the select accounts page
  if (
    location.pathname.includes('/select-accounts') ||
    location.pathname.includes('/register')
  ) {
    return null;
  }

  /**
   * Render
   */
  return (
    <Hidden mdDown>
      <Box
        displayPrint="none"
        bgcolor="common.white"
        borderBottom={1}
        borderColor="lighterGray.main"
      >
        <Box component={Container} maxWidth="lg" px={2}>
          <Grid
            py={1}
            container
            justifyContent="space-between"
            alignItems="center"
          >
            <Grid item>
              <DropdownButton
                anchorOrigin={{
                  vertical: 'bottom',
                  horizontal: 'left'
                }}
                data-testid="company-list"
                transformOrigin={{
                  vertical: 'top',
                  horizontal: 'left'
                }}
                content={() => (
                  <Box py={2}>
                    {filteredCompanyList.map((company, i) => (
                      <Link
                        key={i}
                        data-testid={`${company.sub}-link`}
                        underline="none"
                        href={generateCompanyUrl(
                          company.sub,
                          port,
                          Configuration.environment,
                          isWaterworksEnabled(features) ?? false
                        )}
                      >
                        <MenuItem>{company.link}</MenuItem>
                      </Link>
                    ))}
                  </Box>
                )}
              >
                {t('common.reeceCompanies')}
              </DropdownButton>
            </Grid>
            <Grid item>
              <UserButton />
              <CompanyListButton
                color="gray"
                iconColor="primary"
                startIcon={<LocationIcon height={12} width={12} />}
                variant="text"
                onClick={() => {
                  history.push('/location-search');
                }}
                data-testid="location-search-button"
              >
                {t('common.storeFinder')}
              </CompanyListButton>
              <CompanyListButton
                color="gray"
                iconColor="primary"
                startIcon={<HelpIcon />}
                variant="text"
                onClick={() => {
                  history.push('/support');
                }}
                data-testid="support-button"
              >
                {t('support.help')}
              </CompanyListButton>
            </Grid>
          </Grid>
        </Box>
      </Box>
    </Hidden>
  );
}

export default CompanyListHeader;
