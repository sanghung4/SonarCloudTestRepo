import { NewPriceType, StagedItem } from "../types";

export type ConfirmModalProps = {
  confirmDisabled: boolean;
  onConfirm: () => void;
};

export interface ReviewTableProps {
  type: NewPriceType;
  headers: string[];
  data: StagedItem[];
}
