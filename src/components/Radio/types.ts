import { ChangeEventHandler } from "react";
export interface RadioProps {
  label: string;
  name: string;
  value?: string;
  checked: boolean;
  disabled?: boolean;
  onChange: ChangeEventHandler<HTMLInputElement>;
}
