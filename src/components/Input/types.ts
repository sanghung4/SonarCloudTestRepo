import { ChangeEventHandler } from "react";

export type InputChangeHandler = ChangeEventHandler<HTMLInputElement>;

export interface InputProps extends React.ComponentPropsWithoutRef<"input"> {
  label?: string;
  status?: "error" | "success" | string;
  helperText?: string;
  endIcon?: React.ReactNode;
  onChange?: InputChangeHandler;
  className?: React.HTMLAttributes<HTMLDivElement>["className"];
  loading?: boolean;
}
