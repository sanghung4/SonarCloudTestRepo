import {
  Checkbox,
  ListItem,
  ListItemIcon,
  ListItemText,
  Skeleton,
  Typography
} from '@dialexa/reece-component-library';
import { kebabCase } from 'lodash-es';

import {
  AggregationItem,
  AggregationResults,
  ProductAttribute
} from 'generated/graphql';

/**
 * Types
 */
type SearchSubfilterProps = {
  checked: boolean;
  filter: keyof AggregationResults;
  handleToggle: (changedFilter: ProductAttribute) => void;
  subfilter: AggregationItem;
  stockText: string;
  loading: boolean;
  filterDisabled: boolean;
};

/**
 * SearchSubFilter Component
 */
export default function SearchSubfilter(props: SearchSubfilterProps) {
  /**
   * Callback
   */
  const listItemClickCb = () => {
    if (!props.filterDisabled) {
      props.handleToggle({
        attributeType: props.filter,
        attributeValue: props.subfilter.value || ''
      });
    }
  };

  const styleCheckbox = {
    cursor: props.filterDisabled ? 'not-allowed' : 'pointer',
    p: 0,
    ml: props.filterDisabled ? '2px' : 0,
    mr: props.filterDisabled ? '4px' : 0,
    backgroundColor: props.filterDisabled ? '#EEEEEE' : '',
    height: props.filterDisabled ? '1.1em' : '',
    width: props.filterDisabled ? '1.2em' : 'auto',
    borderRadius: props.filterDisabled ? '5%' : '50%',
    left: props.filterDisabled ? '0.1em' : ''
  };

  /**
   * Render
   */
  return (
    <ListItem
      key={props.subfilter.value}
      role={undefined}
      onClick={listItemClickCb}
      sx={{
        cursor: props.filterDisabled ? 'not-allowed' : 'pointer',
        px: 0,
        py: 1,
        '& .MuiListItemIcon-root, & .MuiListItemText-root': {
          mb: 0
        },
        '&:hover': {
          bgcolor: 'transparent'
        }
      }}
      data-testid={kebabCase(`filter-line-item-${props.subfilter.value}`)}
    >
      <ListItemIcon
        sx={{
          minWidth: 'auto',
          mr: 1,
          display: 'flex',
          flexDirection: 'column'
        }}
      >
        <Checkbox
          checked={props.checked}
          color="primary"
          tabIndex={-1}
          sx={styleCheckbox}
          data-testid={kebabCase(`checkbox-${props.subfilter.value}`)}
        />
      </ListItemIcon>
      <ListItemText sx={{ my: 0 }}>
        {props.filter === 'inStockLocation' && props.loading ? (
          <Skeleton variant="rectangular" width="100%" />
        ) : (
          <>
            <Typography
              variant="caption"
              color={props.filterDisabled ? 'secondary02.main' : 'textPrimary'}
            >
              {props.stockText || props.subfilter.value}
              <Typography
                variant="caption"
                component="span"
                color="textPrimary"
                fontWeight={300}
                ml={0.5}
              >
                ({props.subfilter.count})
              </Typography>
            </Typography>
          </>
        )}
      </ListItemText>
    </ListItem>
  );
}
