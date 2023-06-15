export type SvgName =
  | "Logo"
  | "NegativeChevron"
  | "PositiveChevron"
  | "ChevronUp"
  | "ChevronDown"
  | "SearchIcon"
  | "userTesting";

export interface SvgListProps {
  name: SvgName;
  background?: string;
  fill?: string;
  className?: string;
}
