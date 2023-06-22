import { CSSProperties, useEffect, useRef } from 'react';

type VirtualizedListItemProps = {
  children: React.ReactNode;
  setHeight: (height: number | undefined) => void;
  wrapperStyle: CSSProperties;
};

function VirtualizedListItem(props: VirtualizedListItemProps) {
  /**
   * Refs
   */
  const itemRef = useRef<HTMLDivElement>(null);

  /**
   * Effects
   */
  // eslint-disable-next-line react-hooks/exhaustive-deps
  useEffect(setItemSize, []);

  /**
   * Render
   */
  return (
    <div style={props.wrapperStyle}>
      <div ref={itemRef}>{props.children}</div>
    </div>
  );

  /**
   * Effect Definitions
   */
  function setItemSize() {
    props.setHeight(itemRef.current?.getBoundingClientRect().height);
  }
}

export default VirtualizedListItem;
