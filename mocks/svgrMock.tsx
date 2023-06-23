/**
 * NOTE: This mock will treat SVGs in Jest unit tests as a DIV element since SVGs in unit test doesn't store certain attributes
 */
import React from 'react';

const SvgrMock = React.forwardRef<HTMLDivElement>((props, ref) => (
  <div ref={ref} {...props} />
));

SvgrMock.displayName = 'SvgrMock';

export const ReactComponent = SvgrMock;
export default SvgrMock;