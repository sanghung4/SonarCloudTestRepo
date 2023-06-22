import { SyntheticEvent } from 'react';
import notfound from 'images/notfound.png';

type Props = {
  alt?: string;
  src?: string;
} & React.DOMAttributes<HTMLImageElement>;

function Image(props: Props) {
  /**
   * Props
   */
  const { alt = '', src = '', onError: userOnError, ...remainingProps } = props;

  /**
   * Render
   */
  return <img {...remainingProps} alt={alt} onError={onError} src={src} />;

  /**
   * Error handling (fallback)
   */
  // istanbul ignore next
  function onError(e: SyntheticEvent<HTMLImageElement, Event>) {
    e.currentTarget.onerror = null;
    e.currentTarget.src = notfound;
    userOnError?.(e);
  }
}

export default Image;
