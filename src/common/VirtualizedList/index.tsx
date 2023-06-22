import { useRef, useState } from 'react';
import { VariableSizeList } from 'react-window';
import { sum } from 'lodash-es';
import VirtualizedListItem from './VirtualizedListItem';

type VirtualizedListProps<TData> = {
  defaultItemSize: number;
  maxHeight: number;
  dataArray: TData[];
  renderItem: (data: TData, itemIndex: number) => React.ReactNode;
  width?: number | string;
};

function VirtualizedList<TData>(props: VirtualizedListProps<TData>) {
  /**
   * Refs
   */
  const listRef = useRef<VariableSizeList<any>>(null);
  const sizeMapRef = useRef<Record<number, number>>({});

  /**
   * State
   */
  const [listHeight, setListHeight] = useState(0);

  /**
   * Render
   */
  return (
    <VariableSizeList
      itemData={props.dataArray}
      height={listHeight}
      width={props.width ?? '100%'}
      ref={listRef}
      itemSize={getItemHeight}
      estimatedItemSize={props.defaultItemSize}
      itemCount={props.dataArray.length}
    >
      {({ style, data, index }) => {
        const listData = data[index];

        return (
          <VirtualizedListItem
            wrapperStyle={style}
            setHeight={handleSetHeight(index)}
          >
            {props.renderItem(listData, index)}
          </VirtualizedListItem>
        );
      }}
    </VariableSizeList>
  );

  /**
   * Callback Definitions
   */
  function handleSetHeight(index: number) {
    return function (renderedHeight: number | undefined) {
      // Set the list to reset after the index of the last item is passed
      listRef.current?.resetAfterIndex(index);

      // Add the height of the rendered item to the sizeMap
      sizeMapRef.current = {
        ...sizeMapRef.current,
        [index]: renderedHeight ?? props.defaultItemSize
      };

      // Set the overall listHeight
      setListHeight((prev) =>
        prev === props.maxHeight
          ? prev
          : Math.min(sum(Object.values(sizeMapRef.current)), props.maxHeight)
      );
    };
  }

  function getItemHeight(index: number) {
    return sizeMapRef.current[index] ?? props.defaultItemSize;
  }
}

export default VirtualizedList;
