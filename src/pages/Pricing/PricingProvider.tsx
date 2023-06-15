import { createContext, useContext, useEffect, useState } from "react";

import { useOktaAuth } from "@okta/okta-react";
import { UserClaims } from "@okta/okta-auth-js";

import {
  AddNewPrice,
  ModalNames,
  PricingContextParams,
  PricingProviderProps,
  RemoveNewPrice,
  StagedItem,
  ToggleModal,
} from "./types";

const PricingContext = createContext<PricingContextParams>({
  openModal: undefined,
  createdPrices: [],
  modifiedPrices: [],
  addNewPrice: () => {},
  removeNewPrice: () => {},
  toggleModal: () => {},
  resetSpecialPrices: () => {},
});

export const PricingProvider = ({ children }: PricingProviderProps) => {
  const { authState, oktaAuth } = useOktaAuth();

  // ----- STATE ----- //
  // User Information
  const [user, setUser] = useState<UserClaims>();

  // Prices
  const [createdPrices, setCreatedPrices] = useState<StagedItem[]>([]);
  const [modifiedPrices, setModifiedPrices] = useState<StagedItem[]>([]);

  // Modal
  const [openModal, setOpenModal] = useState<ModalNames>();

  // ----- EFFECTS ----- //
  useEffect(() => {
    if (!authState || !authState.isAuthenticated) {
      setUser(undefined);
    } else {
      const accessToken = oktaAuth.getAccessToken();
      localStorage.setItem("token", accessToken ? accessToken : "");
      oktaAuth
        .getUser()
        .then((response) =>{ setUser(response);})
        .catch(console.error);
    }
  }, [authState, oktaAuth]);

  // ----- METHODS ----- //
  const addNewPrice: AddNewPrice = (type, item) => {
    const newSpecialPrice: StagedItem = {
      ...item,
      changeWriterDisplayName: user?.name ?? "",
      changeWriterId: user?.email ?? "",
    };

    if (type === "created") {
      setCreatedPrices((prev) => [...prev, newSpecialPrice]);
    }

    if (type === "modified") {
      setModifiedPrices((prev) => [...prev, newSpecialPrice]);
    }
  };

  const removeNewPrice: RemoveNewPrice = (type, item) => {
    if (type === "created") {
      setCreatedPrices((prev) => {
        const index = prev.findIndex(
          ({ customerId, productId }) => customerId + productId === item.customerId + item.productId
        );

        prev.splice(index, 1);
        return [...prev];
      });
    }

    if (type === "modified") {
      setModifiedPrices((prev) => {
        const index = prev.findIndex(
          ({ customerId, productId }) => customerId + productId === item.customerId + item.productId
        );

        prev.splice(index, 1);
        return [...prev];
      });
    }
  };

  const toggleModal: ToggleModal = (open, modal) => {
    if (open) {
      setOpenModal(modal);
    } else {
      setOpenModal(undefined);
    }
  };

  const resetSpecialPrices = () => {
    setCreatedPrices([]);
    setModifiedPrices([]);
  };

  return (
    <PricingContext.Provider
      value={{
        openModal,
        createdPrices,
        modifiedPrices,
        addNewPrice,
        removeNewPrice,
        toggleModal,
        resetSpecialPrices,
      }}
    >
      {children}
    </PricingContext.Provider>
  );
};

export const usePricing = () => useContext(PricingContext);


