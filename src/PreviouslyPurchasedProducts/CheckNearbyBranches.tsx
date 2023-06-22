import './style.scss';
import { BranchesIcon } from 'icons';
import { useTranslation } from 'react-i18next';
/**
 * Types
 */
type CheckNearbyBranchesProps = {};

/**
 * Component
 */

/**
 * Context
 */

function CheckNearbyBranches(props: CheckNearbyBranchesProps) {
  /**
   * Custom hooks
   */
  const { t } = useTranslation();

  /**
   * Props
   */

  return (
    <div className="check-nearby-branches">
      <div className="check-nearby-branches__icon">
        <BranchesIcon />
      </div>
      <div className="check-nearby-branches__text">
        {t('product.checkNearByBranches')}
      </div>
    </div>
  );
}

export default CheckNearbyBranches;
