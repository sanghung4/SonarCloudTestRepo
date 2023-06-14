import { Maybe } from 'api';

export interface LabelAndDescriptionProps {
  label: string;
  value: Maybe<string> | undefined;
  numberOfLines?: number;
}
