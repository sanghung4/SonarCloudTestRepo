import { Radio } from '@dialexa/reece-component-library';

export default function inputTestId<T = HTMLInputElement>(id: string) {
  return {
    'data-testid': id
  } as React.HTMLAttributes<T>;
}

export const radio = (name: string) => {
  return <Radio inputProps={inputTestId(name)} />;
};
