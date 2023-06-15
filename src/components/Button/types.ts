import { HTMLProps } from "react";

export interface ButtonProps {
  size?: string;
  disabled?: boolean;
  className?: string;
  loading?: boolean;
  title?: string;
  icon?: React.ReactNode;
  iconPosition?: "left" | "right";
  onClick: HTMLProps<HTMLButtonElement>["onClick"];
}
