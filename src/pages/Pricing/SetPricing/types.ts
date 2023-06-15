import { SpecialPrice } from "../../../graphql";
import { StagedItem } from "../types";

export type VarianceConfirmationModalProps = {
  onConfirm: () => void;
  isGreaterThan25Percent: boolean;
};

export type CreateModalProps = {
  customerId: string;
  userBranch: (string | null)[] | null | undefined;
};

export interface PricingTableRowProps {
  data: SpecialPrice;
  getRowData: (isGreaterThan25Percent: boolean, handleAddStagedPrice: () => void) => void;
  areRowsExpanded: boolean;
  stagedData?: StagedItem;
}
