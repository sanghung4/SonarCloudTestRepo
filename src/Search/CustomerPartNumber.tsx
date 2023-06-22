import { useState } from 'react';
import { Collapse, Tooltip } from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { TruncateTextWithCentralEllipsis } from 'utils/strings';
import Button from 'components/Button';
import 'Search/styles/customerPartNumber.scss';

type Props = {
  partNumber?: string[] | null;
  stylingForPage: string;
};

export default function CustPartNumber(props: Props) {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();

  /**
   * State
   */
  const [viewAll, setViewAll] = useState(false);

  /**
   * Render
   */
  if (!props.partNumber) {
    return null;
  }

  return (
    <span>
      <div
        data-testid="my-customer-part-number"
        className={`custPartNumber__containerFor${props.stylingForPage}`}
      >
        <span
          className={`custPartNumber__myPartLabelFor${props.stylingForPage}`}
        >
          {t('product.partNumber')}

          <Tooltip title={props.partNumber[0]} enterTouchDelay={0}>
            <span>
              {TruncateTextWithCentralEllipsis(props.partNumber[0], 5, 5)}
            </span>
          </Tooltip>
        </span>
        {props.partNumber.length > 1 && (
          <Button
            onClick={() => setViewAll(!viewAll)}
            color="primaryLight"
            size={props.stylingForPage === 'PDP' ? 'small' : 'x-small'}
            variant="text-link"
            data-testid="view-all"
            label={viewAll ? t('common.hide') : t('common.viewAll')}
            className={`custPartNumber__viewAllBtnFor${props.stylingForPage}`}
          />
        )}
      </div>
      {props.partNumber.length > 1 && (
        <Collapse in={viewAll}>
          {props.partNumber.map((item, index) => (
            <Tooltip title={item} enterTouchDelay={0} key={`item-${index}`}>
              <div
                className={`custPartNumber__hiddenItemsFor${props.stylingForPage}`}
              >
                {Boolean(index) && TruncateTextWithCentralEllipsis(item, 5, 5)}
              </div>
            </Tooltip>
          ))}
        </Collapse>
      )}
    </span>
  );
}
