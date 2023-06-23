import { DetailedHTMLProps, ImgHTMLAttributes } from 'react';

type DefaultImgProps = DetailedHTMLProps<
  ImgHTMLAttributes<HTMLImageElement>,
  HTMLImageElement
>;

export type ImageProps = Omit<DefaultImgProps, 'alt' | 'onError'> & {
  'data-testid'?: string;
  alt: string;
};
