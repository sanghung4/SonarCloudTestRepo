import {
  Checkbox,
  ListItemText,
  Typography
} from '@dialexa/reece-component-library';

import { List as ListType } from 'generated/graphql';
import { ListMode } from 'providers/ListsProvider';
import { ListDrawerItemContainer } from './util/styled';

type Props = {
  list: ListType;
  selected: boolean;
  listMode: ListMode;
  onClick: () => void;
};

function ListDrawerItem(props: Props) {
  /**
   * Render
   */
  return (
    <ListDrawerItemContainer
      data-testid={`list-item-${props.list.name}`}
      selected={props.selected}
      onClick={props.onClick}
    >
      {props.listMode === ListMode.ADD_ITEM && (
        <Checkbox
          checked={props.selected}
          inputProps={{
            'aria-labelledby': `list-checkbox-${props.list.id}`
          }}
          tabIndex={-1}
          disableRipple
        />
      )}
      <ListItemText
        id={`list-checkbox-${props.list.id}`}
        disableTypography
        primary={
          <Typography
            variant="body1"
            color={props.selected ? 'primary02.main' : undefined}
            paddingLeft={2}
          >
            {props.list.name}
          </Typography>
        }
      />
    </ListDrawerItemContainer>
  );
}

export default ListDrawerItem;
