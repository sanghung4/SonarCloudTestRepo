import React from 'react';
import { isPlainObject } from 'lodash';

export function renderNode<Props>(
  Component: React.ComponentType<Props>,
  content: any,
  defaultProps: Partial<Props>
): React.ReactNode {
  if (content == null || content === false) {
    return null;
  }
  if (React.isValidElement(content)) {
    return content;
  }
  if (typeof content === 'function') {
    return content();
  }
  if (isPlainObject(content)) {
    return <Component {...defaultProps} {...(content as Props)} />;
  }
  return <Component {...(defaultProps as Props)}>{content}</Component>;
}
