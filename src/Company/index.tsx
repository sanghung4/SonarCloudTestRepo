import { useContext } from 'react';

import {
  Grid,
  Link,
  Typography,
  useScreenSize,
  Image
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { AuthContext } from 'AuthProvider';
import brands, { generateCompanyUrl } from 'utils/brandList';
import { Configuration } from 'utils/configuration';
import { useDomainInfo } from 'hooks/useDomainInfo';
import notfound from 'images/notfound.png';

export default function Company() {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();
  const { port } = useDomainInfo();

  /**
   * Context
   */
  const { activeFeatures } = useContext(AuthContext);

  return (
    <Grid textAlign="center" bgcolor="warmGray.main" p={8}>
      <Typography
        lineHeight="39px"
        fontSize={39}
        mb={6}
        textAlign="left"
        color="primary.main"
        fontWeight={500}
      >
        {t('aboutUs.ourCompanies')}
      </Typography>
      <Grid container spacing={isSmallScreen ? 8 : 0}>
        {brands.map((item, i) => {
          const { sub, Logo, states, canUseLogo = true, svgPath = '' } = item;
          return (
            <Grid
              item
              container
              key={`brand-${i}`}
              direction="column"
              alignItems="center"
              justifyContent="center"
              xs={isSmallScreen ? 12 : 4}
              py={isSmallScreen ? 2 : 5}
            >
              <Link
                href={generateCompanyUrl(
                  sub,
                  port,
                  Configuration.environment,
                  !!activeFeatures?.includes('WATERWORKS')
                )}
                data-testid={`brand-logo-${sub}`}
              >
                {canUseLogo ? (<Logo />) :
                (
                  <Image
                    alt={t('common.productPicture')}
                    fallback={notfound}
                    src={svgPath}
                  />
                )}
              </Link>
              <Grid
                alignItems="center"
                justifyContent="center"
                py={isSmallScreen ? 1 : 2}
              >
                <Typography lineHeight="24px" whiteSpace="break-spaces">
                  {states.join(', ')}
                </Typography>
              </Grid>
            </Grid>
          );
        })}
      </Grid>
    </Grid>
  );
}
