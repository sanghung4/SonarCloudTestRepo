import { Branch } from 'generated/graphql';
import { isNull, isUndefined } from 'lodash-es';
import { TFunction } from 'react-i18next';

export const MAX_BRANCH_DISTANCE = 400;

export function branchCardDisplayData(t: TFunction, branch?: Branch) {
  /**
   * Data processing
   */
  const branchName = branch?.name || `(${t('branch.noName')})`;

  const address1 = branch?.address1 || `(${t('branch.noAddress')})`;
  const address2 = branch?.address2 ? `, ${branch?.address2}` : '';
  const branchAddress = address1 + address2;

  const city = branch?.city ? `${branch?.city}, ` : `(${t('branch.noCity')})`;
  const state = branch?.state || `(${t('branch.noState')})`;
  const zip = branch?.zip ?? '';
  const branchCityStateZip = `${city}${state} ${zip}`;

  const branchHours = branch?.businessHours?.split(';') ?? [];

  const distance =
    !isUndefined(branch?.distance) && !isNull(branch?.distance)
      ? branch!.distance! > MAX_BRANCH_DISTANCE
        ? '\u003E MAX_BRANCH_DISTANCE'
        : branch!.distance!.toFixed(1)
      : t('common.na');
  const branchDistance = `${distance} ${t('common.miles')}`;

  /**
   * Output
   */
  return {
    branchName,
    branchAddress,
    branchCityStateZip,
    branchHours,
    branchDistance
  };
}
