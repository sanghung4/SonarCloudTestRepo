import React, { forwardRef } from 'react';

import { IMaskInput } from 'react-imask';

// /**
//  * Custom intermediate Component to handle `ref`
//  *
//  * https://mui.com/components/text-fields/#integration-with-3rd-party-input-libraries
//  */
interface InputMaskProps {
  onChange: (event: { target: { name: string; value: string } }) => void;
  name: string;
}

const InputMask = forwardRef<HTMLElement, InputMaskProps>(function InputMask(
  props,
  ref
) {
  const { onChange, ...other } = props;

  return (
    <IMaskInput
      {...other}
      //@ts-ignore
      inputRef={ref}
      onAccept={(value: any) => {
        onChange({ target: { name: props.name, value } });
      }}
      overwrite
    />
  );
});

export default InputMask;
