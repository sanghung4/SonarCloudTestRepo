import { useHistory } from "react-router-dom";
import { toast } from "react-toastify";
import { Button } from "../../../components/Button";
import { useSetSpecialPricesMutation, PriceSuggestion } from "../../../graphql";
import { ConfirmModal } from "./ConfirmModal";
import { usePricing } from "../PricingProvider";
import { ReviewTable } from "./ReviewTable";
import {
  createdPricingColumns,
  createPriceChangeSuggestion,
  modifiedPricingColumns,
} from "../utils";
import { Banner } from "../../../components/Banner";

export const ReviewPricing = () => {
  // ----- HOOKS ----- //
  const history = useHistory();
  const { toggleModal, createdPrices, modifiedPrices, resetSpecialPrices } = usePricing();

  // ----- API ----- //
  const [setSpecialPrices, { loading: setSpecialPricesLoading }] = useSetSpecialPricesMutation();

  // ----- ACTIONS ----- //
  const handleSubmitClick = () => {
    toggleModal(true, "confirm");
  };

  const handleSubmitStaging = () => {
    // Close the modal
    toggleModal(false);

    // Create array of created prices
    const priceCreateSuggestions: PriceSuggestion[] = createdPrices.map(
      createPriceChangeSuggestion
    );

    // Create array of modified prices
    const priceChangeSuggestions: PriceSuggestion[] = modifiedPrices.map(
      createPriceChangeSuggestion
    );

    setSpecialPrices({ variables: { input: { priceChangeSuggestions, priceCreateSuggestions } } })
      .then(() => {
        // Reset state values
        resetSpecialPrices();
        goBack();
        // Create toast message
        toast.success("Special Pricing Changes Staged");
      })
      .catch(() => {
        toast.error("Special Pricing Changes Failed to Stage");
      });
  };

  // ----- HELPERS ----- //
  const goBack = () => {
    history.push("/pricing");
  };

  return (
    <>
      <Banner header='Review Changes' />
      <ConfirmModal confirmDisabled={setSpecialPricesLoading} onConfirm={handleSubmitStaging} />
      <div className='flex flex-col items-center justify-center p-8'>
        <div className='w-full space-y-8'>
          {/* CREATED */}
          {createdPrices.length > 0 && (
            <div>
              {/* Header */}
              <h1 className='font-medium text-3xl mb-4'>Created Prices</h1>
              {/* Table */}
              <div className='overflow-auto bg-white rounded-t shadow-md'>
                <ReviewTable headers={createdPricingColumns} data={createdPrices} type='created' />
              </div>
            </div>
          )}
          {/* MODIFIED */}
          {modifiedPrices.length > 0 && (
            <div>
              {/* Header */}
              <h1 className='font-medium text-3xl mb-4'>Modified Prices</h1>
              {/* Table */}
              <div className='overflow-auto bg-white rounded-t shadow-md'>
                <ReviewTable
                  headers={modifiedPricingColumns}
                  data={modifiedPrices}
                  type='modified'
                />
              </div>
            </div>
          )}
          {/* ACTIONS */}
          <div className='text-right space-x-2'>
            <Button className='text-reece-500 underline' onClick={goBack} title='Back' />
            <Button
              className='bg-reece-500 text-white'
              onClick={handleSubmitClick}
              title='Submit Pricing'
            />
          </div>
        </div>
      </div>
    </>
  );
};
