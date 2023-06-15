import { SpecialPrice } from "../../graphql";

export type NewPriceItem = {
  customerId: string;
  productId: string;
  branch: string;
  priceCategory: PriceCategory;
  cmpPrice: number;
  newPrice: number;
  standardCost: number;
  displayName: string;
  customerDisplayName: string;
  currentSpecial?: number;
  territory?: string | null;
  rateCardName?:string | null | undefined;
};

export type StagedItem = NewPriceItem & {
  changeWriterDisplayName: string;
  changeWriterId: string;
};

export type NewPriceType = "created" | "modified";
export type ModalNames = "confirm" | "create" | "varianceConfirmation" | "help";
export type PriceTypes =
  | "standardCost"
  | "typical"
  | "currentSpecial"
  | "competitiveMarket"
  | "rateCard"
  | "recommended"
  | "customerSales";

export type PriceCategory = "" | "Typical Price" | "Rate Card" | "Recommended" | "User Defined";

export type AddNewPrice = (type: NewPriceType, item: NewPriceItem) => void;

export type RemoveNewPrice = (type: NewPriceType, item: SpecialPrice | StagedItem) => void;

export type ToggleModal = (open: boolean, modal?: ModalNames) => void;

export interface PricingContextParams {
  openModal: ModalNames | undefined;
  createdPrices: StagedItem[];
  modifiedPrices: StagedItem[];
  addNewPrice: AddNewPrice;
  removeNewPrice: RemoveNewPrice;
  toggleModal: ToggleModal;
  resetSpecialPrices: () => void;
}

export interface PricingProviderProps {
  children: React.ReactNode;
}
