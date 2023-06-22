import React, { CSSProperties } from 'react';

import { Paper, useTheme, Divider } from '@dialexa/reece-component-library';
import { areEqual } from 'react-window';
import {
  Draggable,
  DraggingStyle,
  NotDraggingStyle
} from 'react-beautiful-dnd';
import ListsLineItem from './ListsLineItem';

const ListRow = React.memo(({ data, index, style }: any) => {
  const theme = useTheme();

  const {
    lineItemResults,
    isDragDisabled,
    loading,
    orderedPricing,
    priceDataLoading,
    updateItem
  } = data;

  return (
    <div style={style}>
      <Draggable
        key={`line-item-drag-${lineItemResults[index].erpPartNumber}`}
        draggableId={`${lineItemResults[index].erpPartNumber}`}
        index={index}
        isDragDisabled={isDragDisabled}
      >
        {(provided, snapshot) => (
          <Paper
            key={`line-item-${lineItemResults[index].erpPartNumber}`}
            elevation={snapshot.isDragging ? 8 : 0}
            ref={provided.innerRef}
            {...provided.draggableProps}
            style={getItemStyle(
              snapshot.isDragging,
              provided.draggableProps.style
            )}
            sx={{
              outline: 'none',
              height: '100%',
              px: 3
            }}
            data-testid={`lists-line-item-${lineItemResults[index].erpPartNumber}`}
          >
            <ListsLineItem
              item={lineItemResults[index]}
              loading={loading}
              priceData={orderedPricing[index]}
              updateItem={updateItem}
              priceDataLoading={priceDataLoading}
              dragHandleProps={provided.dragHandleProps}
            />
            <Divider />
          </Paper>
        )}
      </Draggable>
    </div>
  );

  function getItemStyle(
    isDragging: boolean,
    draggableStyle: DraggingStyle | NotDraggingStyle | undefined
  ) {
    const itemStyles = {
      userSelect: 'none',
      backgroundColor: theme.palette.common.white
    } as CSSProperties;

    if ((draggableStyle as DraggingStyle).width && isDragging) {
      itemStyles.width =
        (draggableStyle as DraggingStyle).width + theme.spacing(8);
      itemStyles.marginLeft = theme.spacing(-4);
      itemStyles.paddingLeft = theme.spacing(4);
      itemStyles.paddingRight = theme.spacing(4);
    }

    return { ...draggableStyle, ...itemStyles };
  }
}, areEqual);

export default ListRow;
