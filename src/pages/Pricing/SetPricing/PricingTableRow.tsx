import { useEffect, useMemo, useState } from "react";
import clsx from "clsx";
import CurrencyInput from "react-currency-input-field";
import { SvgList } from "../../../components/SvgList";
import { PricingTableRowProps } from "./types";
import { usePricing } from "../PricingProvider";
import { NewPriceItem, PriceCategory } from "../types";
import { formatInputIDValue, formatToUSD } from "../utils";
import { useGetProductPricesLazyQuery } from "../../../graphql";
import { v4 as uuidv4 } from 'uuid';
import { useAuthContext } from "../../../store/AuthProvider";

export const PricingTableRow = ({ data, getRowData, areRowsExpanded }: PricingTableRowProps) => {
  // ----- HOOKS ----- //
  const { addNewPrice, removeNewPrice, modifiedPrices, toggleModal } = usePricing();
  const [getProductPrices, { loading: productPricesLoading }] = useGetProductPricesLazyQuery({
    notifyOnNetworkStatusChange: true,
  });
  const { userInfo } = useAuthContext();

  let hidePriceEdit = false;

  // ----- STATE ----- //
  const [expanded, setExpanded] = useState(false);
  const [selectedPriceValue, setSelectedPriceValue] = useState<number>();
  const [selectedPriceCategory, setSelectedPriceCategory] = useState<PriceCategory>("");
  const [userDefinedPrice, setUserDefinedPrice] = useState<string>();
  const [rowDisabled, setRowDisabled] = useState(false);
  const [rateCardPrice, setRateCardPrice] = useState<number | null | undefined>();
  const [rateCardName, setRateCardName] = useState<string | null | undefined>();
  const [isLoading, setIsLoading] = useState<boolean>(true);
  

  // ----- EFFECTS ----- //
  useEffect(() => {
    const matchingData = modifiedPrices.find(
      ({ branch, customerId, productId }) =>
        branch + customerId + productId === data.branch + data.customerId + data.productId
    );

    setRowDisabled(!!matchingData?.priceCategory);

    if (matchingData) {
      setSelectedPriceCategory(matchingData.priceCategory);
      setUserDefinedPrice(
        matchingData.priceCategory === "User Defined" ? `${matchingData.newPrice}` : undefined
      );
    }
  }, [modifiedPrices]);


  useEffect(() => {
    getRateCardNameAndPrice()
  }, []);
  
  useEffect(() => {
    setExpanded(areRowsExpanded);
  }, [areRowsExpanded])
  

  // ----- ACTIONS ----- //

  
   // Handles ratecard price and rate card name

   const getRateCardNameAndPrice = () => {
    if (data) {
      const variables = {
        input: { 
          branch: formatInputIDValue(data.branch),
          productId: formatInputIDValue(data.productId),
          customerId: formatInputIDValue(data.customerId),
          correlationId: uuidv4(),
        },
      };
    
    getProductPrices({ variables }).then((resp) =>
     { 
        setRateCardPrice(resp.data?.productPrices.rateCardPrice)
        setRateCardName(resp.data?.productPrices.rateCardName)
        setIsLoading(false)
     }
      );
    }
  };

  const handleShowMore = () => {
    setExpanded((prev) => !prev);
  };

  const onToggle = (priceCategortType: PriceCategory) => () => {
    selectedPriceCategory === "" ? setSelectedPriceCategory(priceCategortType) : setSelectedPriceCategory("");
  }

  const handlePriceSelection = (category: PriceCategory) => () => {
    let value = 0;

    switch (category) {
      case "Rate Card":
        value = productPrices["rateCard"];
        break;
      case "Recommended":
        value = productPrices["recommended"];
        break;
      case "Typical Price":
        value = productPrices["typical"];
        break;
      case "User Defined":
        value = 0;
        break;
    }

    setSelectedPriceValue(value);
    setSelectedPriceCategory(category);
  };

  const handleAddStagedPrice = () => {

    // Close the modal
    toggleModal(false);
    
    const newPrice =
      selectedPriceCategory === "User Defined"
        ? Number(userDefinedPrice)
        : Number(selectedPriceValue);

    const newPriceItem: NewPriceItem = {
      customerId: data.customerId,
      productId: data.productId,
      branch: data.branch,
      priceCategory: selectedPriceCategory,
      newPrice: newPrice,
      cmpPrice: productPrices.competitiveMarket,
      standardCost: productPrices.standardCost,
      currentSpecial: productPrices.currentSpecial,
      displayName: data.displayName,
      customerDisplayName: data.customerDisplayName,
      territory: data.territory,
      rateCardName:rateCardName,
    };

    addNewPrice("modified", newPriceItem);
  };

  const handleOpenVarianceDialog = () => {
    getRowData(isGreaterThan25Percent, handleAddStagedPrice);
    toggleModal(true, "varianceConfirmation");
  }

  const handleRemoveStagedPrice = () => {
    removeNewPrice("modified", data);
  };

  if(userInfo?.groups?.length === 1 && userInfo?.groups?.includes('WMS Admin - Pricing Read Only')){ 
    hidePriceEdit = true;
  }

  // ----- HELPERS ----- //

  const productPrices = useMemo(
    () => ({
      standardCost: data.prices.find((item) => item.type === "standardCost")?.value || 0,
      typical: data.prices.find((item) => item.type === "typical")?.value || 0,
      currentSpecial: data.prices.find((item) => item.type === "current")?.value || 0,
      competitiveMarket: data.prices.find((item) => item.type === "competitiveMarket")?.value || 0,
      rateCard: rateCardPrice || 0,
      recommended: data.prices.find((item) => item.type === "recommended")?.value || 0,
      customerSales: data.prices.find((item) => item.type === "customerSales")?.value || 0,
    }),
    [data, rateCardPrice]
  );

  const getMargin = (price: number) => {
    const cost = productPrices.standardCost || 100;
    return price > 0 ? `GM: ${Math.round(((price - cost) / price) * 1000) / 10}%` : `GM: ${0}%`;
  };

  // ----- CONSTANTS BASED ON STATES ----- //  
  const isGreaterThan25Percent = Number(userDefinedPrice) >= productPrices?.currentSpecial * 1.25;
  const isLowerThan25Percent = Number(userDefinedPrice) <= productPrices?.currentSpecial * 0.75;

  return (
    <>
      {/* Main Row */}
      <tr className='text-gray-900 text-sm border-b last:border-b-0'>
        {/* Customer Cell */}
        <td className='pl-8 pr-4 py-6'>
          <p className='whitespace-no-wrap'>{data.customerDisplayName}</p>
          <button className='text-gray-600 whitespace-no-wrap' onClick={handleShowMore}>
            <SvgList name={expanded ? "ChevronUp" : "ChevronDown"} className='h-8 w-5 pt-3' />
          </button>
        </td>

        {/* Product Cell */}
        <td className='px-4 py-6'>
          <p className='text-gray-600'>{data.manufacturer}</p>
          <p>{data.displayName}</p>
          <p className='text-gray-600'>MFR # {data.manufacturerReferenceNumber}</p>
        </td>

        {/* Current Special Cell */}
        <td className='px-4 py-6 whitespace-nowrap'>
          <p>{formatToUSD(productPrices.currentSpecial)}</p>
          <p className='text-gray-600'>{getMargin(productPrices.currentSpecial)}</p>
        </td>

        {/* Standard Cost Cell */}
        <td className='px-4 py-6 whitespace-nowrap'>
          <p className='pb-5'>{formatToUSD(productPrices.standardCost)}</p>
        </td>

        {/* Typical Price Cell */}
        <td className='px-4 py-6 whitespace-nowrap'>
          <label className='inline-flex items-center'>
            <input
              type='radio'
              className={hidePriceEdit?'hidden':'w-5 h-5 mr-3'}
              name={`radio-${data.customerId + data.productId}`}
              checked={selectedPriceCategory === "Typical Price"}
              disabled={rowDisabled}
              onChange={handlePriceSelection("Typical Price")}
              onClick={onToggle("Typical Price")}
            />
            <p>{formatToUSD(productPrices.typical)}</p>
          </label>
          <p
            className={clsx(
              "text-gray-600",
              "ml-8",
              selectedPriceCategory === "Typical Price"
                ? "opacity-1"
                : "opacity-0 pointer-events-none"
            )}
          >
            {getMargin(productPrices.typical)}
          </p>
        </td>

        {/* Rate Card Cell */}
        <td className='px-4 py-6 whitespace-nowrap'>
          <label className='inline-flex items-center'>
            <input
              type='radio'
              className={hidePriceEdit?'hidden':'w-5 h-5 mr-3'}
              name={`radio-${data.customerId + data.productId}`}
              checked={selectedPriceCategory === "Rate Card"}
              disabled={!isLoading ? rowDisabled : true }
              onChange={handlePriceSelection("Rate Card")}
              onClick={onToggle("Rate Card")}
            />
          <p className='text-gray-900'>
            { isLoading 
               ? 'Loading' 
               : formatToUSD(productPrices.rateCard) 
            }
          </p>
          </label>
          <p
            className={clsx(
              "text-gray-600",
              "ml-8",
              selectedPriceCategory === "Rate Card" ? "opacity-1" : "opacity-0 pointer-events-none"
            )}
          >
            {getMargin(productPrices.rateCard)}
          </p>
        </td>

        {/* Recommended Cell */}
        <td className='px-4 py-6 whitespace-nowrap'>
          <label className='inline-flex items-center'>
            <input
              type='radio'
              className={hidePriceEdit?'hidden':'w-5 h-5 mr-3'}
              name={`radio-${data.customerId + data.productId}`}
              checked={selectedPriceCategory === "Recommended"}
              disabled={rowDisabled}
              onChange={handlePriceSelection("Recommended")}
              onClick={onToggle("Recommended")}
            />
            <p className='text-gray-900'>{formatToUSD(productPrices.recommended)}</p>
          </label>
          <p
            className={clsx(
              "text-gray-600",
              "ml-8",
              selectedPriceCategory === "Recommended"
                ? "opacity-1"
                : "opacity-0 pointer-events-none"
            )}
          >
            {getMargin(productPrices.recommended)}
          </p>
        </td>

        {/* Custom Special Price Cell */}
        {!hidePriceEdit &&
          <td className='px-4 py-6 whitespace-nowrap'>
            <label className='inline-flex items-center'>
              <input
                type='radio'
                className='w-5 h-5 mr-3'
                name={`radio-${data.customerId + data.productId}`}
                checked={selectedPriceCategory === "User Defined"}
                disabled={rowDisabled}
                onChange={handlePriceSelection("User Defined")}
                onClick={onToggle("User Defined")}
              />
              <CurrencyInput
                className={clsx(
                  selectedPriceCategory === "User Defined" ? "border-blue-500" : "border-gray-500",
                  "px-4 py-1 w-32 rounded border"
                )}
                prefix='$'
                value={userDefinedPrice}
                decimalsLimit={2}
                disabled={rowDisabled || selectedPriceCategory !== "User Defined"}
                onValueChange={setUserDefinedPrice}
                allowNegativeValue={false}
              />
            </label>
            <p
              className={clsx(
                "text-gray-600",
                "ml-8",
                selectedPriceCategory === "User Defined" && userDefinedPrice
                  ? "opacity-1"
                  : "opacity-0 pointer-events-none"
              )}
            >
              {getMargin(Number(userDefinedPrice))}
            </p>
          </td>
        }

        {/* Add Action Cell */}
        {!hidePriceEdit &&
        <td className='pl-4 pr-8 py-6 text-right'>
          {rowDisabled ? (
            <button
              className='bg-green-500 text-white inline-block w-20 py-1 px-4 rounded mb-5'
              onClick={handleRemoveStagedPrice}
            >
              Added
            </button>
          ) : (
            <button
              className={clsx(
                selectedPriceValue || userDefinedPrice
                  ? "border-green-500 text-green-500"
                  : "border-gray-500 text-gray-500",
                "border inline-block w-20 py-1 px-4 rounded mb-5"
              )}
              onClick={
                selectedPriceCategory === "User Defined" && userDefinedPrice && (isGreaterThan25Percent || isLowerThan25Percent)
                ? handleOpenVarianceDialog
                : handleAddStagedPrice
              }
              disabled={
                rowDisabled ||
                (selectedPriceCategory === "User Defined"
                  ? !userDefinedPrice
                  : !selectedPriceCategory)
              }
            >
              Add
            </button>
          )}
        </td>}
      </tr>
      {/* Expanded Information Row */}
      {expanded && (
        <tr className='border-b last:border-b-0 text-xs text-center'>
          <td className='px-8 py-3' colSpan={9}>
            <div className='space-x-4'>
              <span>Salesperson: {data.salesperson}</span>
              <span>Price Line: {data.priceLine}</span>
              <span>Rate Card: {rateCardName}</span>
              <span>CMP: {formatToUSD(productPrices.competitiveMarket)}</span>
              <span>Customer Sales QTY: {data.customerSalesQuantity}</span>
              <span>Customer Sales Value: {formatToUSD(productPrices.customerSales)}</span>
              <span>Territory: {data.territory ?? "DFLT"}</span>
            </div>
          </td>
        </tr>
      )}
    </>
  );
};
