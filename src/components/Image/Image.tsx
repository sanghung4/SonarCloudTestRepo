import { SyntheticEvent } from 'react';

import { ImageProps } from 'components/Image';
import notfound from 'resources/images/notfound.png';

function Image(props: ImageProps) {
  /**
   * Props
   */
  const { 'data-testid': testId, alt, ...rest } = props;

  /**
   * Callback
   */
  const setDefaultSrc = (e: SyntheticEvent<HTMLImageElement, Event>) => {
    e.currentTarget.src = notfound;
  };

  /**
   * Render
   */
  return (
    <img data-testid={testId} alt={alt} onError={setDefaultSrc} {...rest} />
  );
}
export default Image;
