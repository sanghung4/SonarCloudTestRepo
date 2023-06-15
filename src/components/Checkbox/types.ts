import { ChangeEventHandler } from "react";

export interface CheckboxProps {
  classNames?: string;
  label: string;
  checked: boolean;
  onChange: ChangeEventHandler<HTMLInputElement>;
}
