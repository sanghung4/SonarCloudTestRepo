import { ChangeEventHandler, KeyboardEventHandler, Fragment, useEffect, useMemo, useState } from "react";
import { Dialog, Transition } from "@headlessui/react";
import clsx from "clsx";
import CurrencyInput from "react-currency-input-field";
import { MdClose, MdSearch } from "react-icons/md";
import { toast } from "react-toastify";
import { useDebouncedCallback } from "use-debounce";
import { AutocompleteInput, AutocompleteOption } from "../../../components/AutocompleteInput";
import { Button } from "../../../components/Button";
import { Input } from "../../../components/Input";
import { Radio } from "../../../components/Radio";
import {
  CustomerResponse,
  KourierProduct,
  ProductPriceResponse,
  SearchProductsEclipseQuery,
  useGetCustomerByIdLazyQuery,
  useGetProductPricesLazyQuery,
  useSearchProductsEclipseLazyQuery,
  useSearchProductsKourierLazyQuery
} from "../../../graphql";
import { usePricing } from "../PricingProvider";
import { NewPriceItem } from "../types";
import { createSearchVariables, customPagination, customerSearchHelpText, formatToUSD, isBranchExists } from "../utils";
import { CreateModalProps } from "./types";

export const CreateModal = ({ customerId, userBranch }: CreateModalProps) => {
  // ----- HOOKS ----- //
  const { addNewPrice, toggleModal, openModal } = usePricing();

  // ----- STATE ----- //
  const [customerSearchValue, setCustomerSearchValue] = useState("");
  const [customerSearchStatus, setCustomerSearchStatus] = useState<"errorInvalidCustId" | "errorValidBillToCustId" | "empty" | "errorBranchMismatch" | "errorUnAuthUser">("empty");
  const [customerSearchData, setCustomerData] = useState<CustomerResponse>();

  const [searchPage, setSearchPage] = useState(1);
  const [searchByProductId, setSearchByProductId] = useState(false);
  const [showMoreLoading, setShowMoreLoading] = useState(false);
  const [canSearch, setCanSearch] = useState(true);
  const [productSearchTerm, setProductSearchTerm] = useState("");
  const [productSearchOptions, setProductSearchOptions] = useState<AutocompleteOption[]>([]);
  const [kourierProductSearch, setKourierProductSearch] = useState<KourierProduct[]>([]);
  const [selectedProduct, setSelectedProduct] = useState<AutocompleteOption>();
  const [selectedProductPrices, setSelectedProductPrices] = useState<ProductPriceResponse>();
  const [newPriceValue, setNewPriceValue] = useState<string>();

  // ----- Constants ----- //

  const pageSize = 10

  // ----- API ----- //

  const [getCustomerById, { loading: getCustomerByIdLoading }] = useGetCustomerByIdLazyQuery({
    notifyOnNetworkStatusChange: true,
  });

  // Debounced version of getCustomerById
  const debouncedGetCustomerById = useDebouncedCallback(() => {
    if (customerSearchValue && !isNaN(Number(customerSearchValue))) {
      const variables = { input: customerSearchValue };
     
      getCustomerById({ variables })
        .then(({ data }) => {
          if((data?.customer.isBillTo === false && data?.customer.isShipTo === true) || 
            (data?.customer.isBillTo === false && data?.customer.isShipTo === false)){
            setCustomerSearchStatus("errorValidBillToCustId");
            return
          }
          if(userBranch && !userBranch.includes(null)) {
            if (isBranchExists(userBranch as string[], data?.customer.homeBranch.split(" "))){
              if (data?.customer.isBillTo) {
                setCustomerData(data.customer);
              } else {
                throw new Error();
              }
            }else {
              customerSearchValue && setCustomerSearchStatus("errorBranchMismatch");
            }
          }else {
            customerSearchValue && setCustomerSearchStatus("errorUnAuthUser");
          }
        
        })
        .catch(() => {
          setCustomerSearchStatus("errorInvalidCustId");
        });
    } else {
      setCustomerSearchStatus("errorInvalidCustId");
    }
  }, customerId.length > 0 ? 1000 : 0);

  const [searchProducts, { data: searchProductsData, loading: searchProductsLoading }] =
    useSearchProductsEclipseLazyQuery({
      notifyOnNetworkStatusChange: true,
    });

  const [searchProductsKourier, { data: searchProductsKourierData, loading: searchProductsKourierLoading }] =
    useSearchProductsKourierLazyQuery({
      notifyOnNetworkStatusChange: true,
    });

  //  product search using Eclipse
  const searchProductsByEclipse = () => {
    if (productSearchTerm && canSearch) {
      setCanSearch(false)
      if(searchByProductId){
        const variables = createSearchVariables(productSearchTerm, searchByProductId);
  
        searchProducts({ variables }).then(({ data }) => {
          data && createProductOptions(data);
        });
      }else {
        searchProductsByKourier()
      }
  }
  }

  // product search using Kourier
  const searchProductsByKourier = () => {
    if (productSearchTerm && canSearch) {
      setCanSearch(false);
      const variables = {input : {keywords: productSearchTerm, displayName:"", searchInputType: searchByProductId ? "1": "0"}}
      searchProductsKourier({variables}).then(({ data }) => {
       
        if(data){
          const kourierProductList = data?.searchProductsKourier.prodSearch[0]?.products as KourierProduct[] ?? []
          setKourierProductSearch(kourierProductList)
          if(kourierProductList.length > 0){
            const paginatedData =  customPagination(kourierProductList, searchPage, pageSize)
            setProductSearchOptions((prev) => {
              const options: AutocompleteOption[] = [];
              paginatedData.data?.forEach((item) => {
                if (item && item.productId && item.displayField ) {
                  options.push({ value: item.productId, display: item.displayField });
                }
              });
        
              if(options.length === 1){
                const exactmatchedOption = options[0];
                if(exactmatchedOption.display.replace(/\n/g, " ") === productSearchTerm) 
                {
                  handleProductSelectChange(exactmatchedOption);
                return []
                }
              }
        
              return prev.concat(options);
            });
          }
        } 
      }).catch(error =>{
        throw(error)
      })
    }
  }

  const [getProductPrices, { loading: productPricesLoading }] = useGetProductPricesLazyQuery({
    notifyOnNetworkStatusChange: true,
  });

  // ----- EFFECTS ----- //
  useEffect(() => {
    if(customerId.length > 0) {
      setCustomerSearchValue(customerId)
      debouncedGetCustomerById();
    } else {
      setCustomerSearchValue("");
    }
  }, [customerId]);


  useEffect(() => {
  
    // Reset customer search state
    setCustomerSearchStatus("empty");
    // Reset customer data
    setCustomerData(undefined);
    // Reset product search type
    setSearchByProductId(false);
  
  }, [customerSearchValue]);

  // Resets
  useEffect(() => {
    // Reset product search
    setProductSearchTerm("");
  }, [searchByProductId]);

  useEffect(() => {
    // Reset search page
    setSearchPage(1);
    // Reset search options
    setProductSearchOptions([]);
    // Reset selected product
    setSelectedProduct(undefined);
    //Set allow search flag to true
    setCanSearch(true);
  }, [productSearchTerm]);

  useEffect(() => {
    // Reset selected prices
    setSelectedProductPrices(undefined);
    // Reset new price
    setNewPriceValue("");
  }, [selectedProduct]);

  // ----- ACTIONS ----- //
  // Handle the change of the customer search input from user input
  const handleCustomerSearchChange: ChangeEventHandler<HTMLInputElement> = (e) => {
    // Set search value
    setCustomerSearchValue(e.target.value);
  };

  // Handle the change of the product search input
  const handleProductSearchChange = (value: string) => {
    // Set the search term
    setProductSearchTerm(value);
  
  };

// Handle the on enter of the product search input
  const handleEnterKey = (value:string ) => { 
    if(value === 'Enter') {
       // Initiate a search
       searchProductsByKourier();
    }
  }

  const handleEnterKeyForCustomerId : KeyboardEventHandler<HTMLInputElement> =(e) => { 
    if(e.key === 'Enter') {
       // Initiate a search
       debouncedGetCustomerById();
    }
  }

  // Handles the change of the search type radios
  const handleSearchTypeChange: ChangeEventHandler<HTMLInputElement> = (e) => {
    // Set searchByProductId value
    setSearchByProductId(e.target.value === "productID");
    setProductSearchTerm("");
    const input:any = document.getElementsByName("autocompleteInput")
    const idInput = input[0].id
    const Autoinput:any = document.getElementById(idInput)
    Autoinput.value="";
  };

  // Handles when a product is selected from the autocomplete list
  const handleProductSelectChange = (option: AutocompleteOption) => {
    // Set the selected product
    setSelectedProduct(option);

    if (customerSearchData) {
      const variables = {
        input: { branch: customerSearchData.homeBranch, productId: option.value },
      };
     
      getProductPrices({ variables }).then((resp) =>
        setSelectedProductPrices(resp.data?.productPrices)
      );
    }
  };



  const handleCreatePrice = () => {
    // Create new price object
    if (selectedProduct && customerSearchData) {
      const newPriceItem: NewPriceItem = {
        priceCategory: "User Defined",
        newPrice: Number(newPriceValue),
        productId: selectedProduct.value,
        displayName: selectedProduct.display,
        cmpPrice: selectedProductPrices?.cmp ?? 0,
        standardCost: selectedProductPrices?.stdCost ?? 0,
        customerId: customerSearchData.id,
        branch: customerSearchData.homeBranch,
        customerDisplayName: customerSearchData.name,
      };

      addNewPrice("created", newPriceItem);

      // Reset product search (triggers all other resets)
      setProductSearchTerm("");

      toast.success("Success: You created a price ready for review!");
    }
  };

  // Handles showing more options if a search is paginated
  const handleShowMore = () => {
    setShowMoreLoading(true)
    const paginatedData =  customPagination(kourierProductSearch, searchPage + 1, pageSize)
      
      setProductSearchOptions((prev) => {
        const options: AutocompleteOption[] = [];
        paginatedData.data?.forEach((item) => {
          if (item && item.productId && item.displayField  ) {
            options.push({ value: item.productId, display: item.displayField });
          }
        });
        setSearchPage((prev) => prev + 1);
        setShowMoreLoading(false)
        return prev.concat(options);
      });
  };

  // Handles closing the modal
  const handleCloseModal = () => {
    toggleModal(false);
  };

  // ----- HELPERS ----- //
  const createProductOptions = (data: SearchProductsEclipseQuery) => {
    const products = data.searchProductsEclipse.products;

    setProductSearchOptions((prev) => {
      const options: AutocompleteOption[] = [];

      products.forEach((item) => {
        if (item && item.id && item.name) {
          options.push({ value: item.id, display: item.name });
        }
      });

      if(options.length === 1){
        const exactmatchedOption = options[0];
        if(exactmatchedOption.display.replace(/\n/g, " ") === productSearchTerm) 
        {
          handleProductSelectChange(exactmatchedOption);
        return []
        }
      }

      return prev.concat(options);
    });
  };
  
  const canShowMore = useMemo(() => {
    const totalItems = searchProductsKourierData?.searchProductsKourier.prodSearch[0]?.products?.length ?? 0;
    return (totalItems > productSearchOptions.length) && !searchByProductId;
  }, [productSearchOptions]);

  const showProductSearch = useMemo(() => !!customerSearchData, [customerSearchData]);
  
  const showPricingInputs = useMemo(
    () => showProductSearch && selectedProductPrices,
    [showProductSearch, selectedProductPrices]
  );

  const newPriceHelperText = useMemo(() => {
    const newPriceNumber = Number(newPriceValue);

    const standardCost = selectedProductPrices?.stdCost ?? 0;
    const margin = Math.round(((newPriceNumber - standardCost) / newPriceNumber) * 1000) / 10;
    return newPriceNumber ? `GM: ${margin}%` : "";
  }, [newPriceValue, selectedProductPrices]);

  // ----- JSX ----- //
  return (
    <Transition.Root show={openModal === "create"} as={Fragment}>
      <Dialog onClose={handleCloseModal} className='relative z-50' as='div'>
        {/* Overlay */}
        <Transition.Child
          enter='ease-out duration-300'
          enterFrom='opacity-0'
          enterTo='opacity-100'
          leave='ease-in duration-200'
          leaveFrom='opacity-100'
          leaveTo='opacity-0'
          className='fixed inset-0 bg-gray-700 bg-opacity-75 transition-opacity'
        />
        {/* Modal */}
        <Transition.Child
          className='fixed inset-0 flex items-center justify-center p-4'
          enter='ease-out duration-300'
          enterFrom='opacity-0 translate-y-4 md:translate-y-0 md:scale-95'
          enterTo='opacity-100 translate-y-0 md:scale-100'
          leave='ease-in duration-200'
          leaveFrom='opacity-100 translate-y-0 md:scale-100'
          leaveTo='opacity-0 translate-y-4 md:translate-y-0 md:scale-95'
        >
          <Dialog.Panel className='rounded bg-white py-10 px-14 relative w-[571px]'>
            {/* Close Button */}
            <button
              className='absolute right-14 top-10 text-2xl text-primary-1-100'
              onClick={handleCloseModal}
            >
              <MdClose />
            </button>

            {/* Title */}
            <Dialog.Title className='text-3xl font-semibold'>Create Pricing</Dialog.Title>

            {/* Inputs */}
            <div className='space-y-2 my-4 w-[232px]'>
              {/* Customer ID */}
              <Input
                label='BILL TO Customer ID'
                value={customerSearchValue}
                onKeyDown={handleEnterKeyForCustomerId}
                onChange={handleCustomerSearchChange}
                status={customerSearchStatus}
                helperText={customerSearchHelpText[customerSearchStatus]}
                loading={getCustomerByIdLoading}
                placeholder='Search by Customer ID'
                endIcon={<MdSearch onClick={()=>debouncedGetCustomerById()} className="cursor-pointer"  />}
              />

              {/* Product Search w/ Search by ID Checkbox */}
              <div
                className={clsx(
                  "transition-all",
                  showProductSearch && customerSearchStatus !== 'errorInvalidCustId' ? "opacity-100" : "opacity-0 cursor-none"
                )}
              >
             {/* Customer Name - (READ ONLY) */}
              <div className='mt-4 mb-6 w-[379px]'>
                 <Input
                  label='Customer Name'
                  value={customerSearchData?.name}
                  readOnly
                />
              </div>
                {/* Label */}
                <p className='mb-2 text-neutral-700 font-bold'>Product</p>
                {/* Search By Radio */}
                <div className='mb-2 flex items-center space-x-4 w-[472px]'>
                  <p>Search by:</p>
                  <Radio
                    name='search-type'
                    label='Keywords'
                    value='keyword'
                    disabled={!showProductSearch}
                    checked={!searchByProductId}
                    onChange={handleSearchTypeChange}
                  />
                  <Radio
                    name='search-type'
                    label='Product ID'
                    value='productID'
                    disabled={!showProductSearch}
                    checked={searchByProductId}
                    onChange={handleSearchTypeChange}
                  />
                </div>
                {/* Autocomplete */}
                <AutocompleteInput
                  placeholder="Search input"
                  filterOptions={false}
                  disabled={!showProductSearch}
                  selectedOption={selectedProduct}
                  inputValue={productSearchTerm}
                  onSelectChange={handleProductSelectChange}
                  onInputValueChange={handleProductSearchChange}
                  onKeyDown={handleEnterKey}
                  options={productSearchOptions}
                  endIcon={<MdSearch onClick={()=>searchProductsByKourier()} />}
                  loading={searchProductsLoading || productPricesLoading || searchProductsKourierLoading}
                  renderOptionsEnd={
                    (canShowMore || searchProductsKourierLoading) && (
                      <div className='w-full text-center'>
                        <Button
                          className='disabled:bg-transparent text-reece-500'
                          onClick={handleShowMore}
                          disabled={showMoreLoading}
                          title={showMoreLoading ? "Loading" : "Show More..."}
                        />
                      </div>
                    )
                  }
                />
              </div>
        

              {/* Price Inputs */}
              <div
                className={clsx(
                  "transition-all space-y-4 w-[379px]",
                  showPricingInputs ? "opacity-100" : "opacity-0 cursor-none"
                )}
              >
              <div className='my-4 flex items-center justify-between space-x-4'>
                {/* CMP - (READ ONLY) */}
                <Input
                  label='CMP'
                  value={formatToUSD(selectedProductPrices?.cmp ?? 0)}
                  readOnly
                />
                {/* Standard Cost - (READ ONLY) */}
                <Input
                  label='Standard Cost'
                  value={formatToUSD(selectedProductPrices?.stdCost ?? 0)}
                  readOnly
                />
              </div>
                {/* New Price */}
                <div>
                  <p className='mb-2 text-neutral-700 font-bold'>New Price</p>
                  <CurrencyInput
                    className='border border-[#D6D7D9] focus:outline-reece-500 bg-white w-[182px] px-4 py-3 rounded'
                    prefix='$'
                    placeholder='Enter price here'
                    decimalsLimit={2}
                    allowNegativeValue={false}
                    disabled={!showPricingInputs}
                    value={newPriceValue}
                    onValueChange={setNewPriceValue}
                  />
                  <p className='pl-2 mt-1 text-xs text-neutral-600'>{newPriceHelperText}</p>
                </div>
              </div>
            </div>
            {/* Create Button */}
            <Button
              disabled={
                !Number(newPriceValue) ||
                !selectedProduct ||
                !customerSearchData ||
                !selectedProductPrices
              }
              className='w-[182px] mt-2 text-white bg-reece-800'
              onClick={handleCreatePrice}
              title='Create'
            />
          </Dialog.Panel>
        </Transition.Child>
      </Dialog>
    </Transition.Root>
  );
};
