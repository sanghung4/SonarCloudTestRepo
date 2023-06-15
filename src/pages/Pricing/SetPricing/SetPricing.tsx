import { ChangeEventHandler, useEffect, useMemo, useState, KeyboardEventHandler } from "react";
import {
  SpecialPrice,
  useGetCustomerByIdLazyQuery,
  useGetPaginatedSpecialPricesLazyQuery,
  useGetPriceLinesLazyQuery,
} from "../../../graphql";
import { Pagination } from "../../../components/Pagination";
import { PricingTableRow } from "./PricingTableRow";
import { Input } from "../../../components/Input";
import { Button } from "../../../components/Button";
import { Dropdown, DropdownOption } from "../../../components/Dropdown";
import { customerSearchHelpText, defaultValues, formatSearchValue, isBranchExists, pricingColumns, checkHomeBranchProduct } from "../utils";
import { MdAddCircle, MdSearch, MdExpand } from "react-icons/md";
import { FaSortDown, FaSortUp } from "react-icons/fa";
import { usePricing } from "../PricingProvider";
import { useDebouncedCallback } from "use-debounce";
import { useHistory } from "react-router-dom";
import { CreateModal } from "./CreateModal";
import { Banner } from "../../../components/Banner";
import { VarianceConfirmationModal } from "./VarianceConfirmationModal";
import { InformationCircleIcon as InformationCircleIconSolid } from "@heroicons/react/solid";
import { InformationCircleIcon } from "@heroicons/react/outline";
import HelpModal from "./HelpModal";
import clsx from "clsx";
import { useAuthContext } from "../../../store/AuthProvider";

export const SetPricing = () => {
  const { createdPrices, modifiedPrices, toggleModal } = usePricing();
  const history = useHistory();
  const { userInfo, userBranchId } = useAuthContext()

  let hidePriceEdit = false;

  // ----- STATE ----- //
  const [customerSearchValue, setCustomerSearchValue] = useState("");
  const [customerSearchStatus, setCustomerSearchStatus] = useState<"errorInvalidCustId" | "errorValidBillToCustId" | "success" | "errorBranchMismatch" | "errorUnAuthUser" | "errorNonNumeric" | "errorInvalidBillToCustId" |"empty">(
    "empty"
  );
  const [productSearchValue, setProductSearchValue] = useState("");
  const [selectedPriceLine, setSelectedPriceLine] = useState<DropdownOption>(
    defaultValues.priceLine
  );
  const [priceLineOptions, setPriceLineOptions] = useState<DropdownOption[]>(
    defaultValues.priceLineOptions
  );
  const [searchResults, setSearchResults] = useState<SpecialPrice[]>([]);
  const [isActiveLoader, setIsActiveLoader] = useState<"customer_id" |"product_id" | "">("");
  const [hasProductsNotReturned, setHasProductsNotReturned] = useState<boolean>(false);
  
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const [totalCount, setTotalCount] = useState(0);
  
  const [isGreaterThan25Percent, setIsGreaterThan25Percent] = useState<boolean>(false);
  const [handleAddStagedPrice, setHandleAddStagedPrice] = useState<() => void>(() => () => {});
  const [areRowsExpanded, setAreRowsExpanded] = useState<boolean>(false);

  const [sortingDirection, setSortingDirection] = useState<string>("desc");
  const [sortingColumn, setSortingColumn] = useState<string>("manufacturer");

  // ----- API ----- //
  // Get special pricing
  const [getSpecialPricing,{loading:queryLoader}] = useGetPaginatedSpecialPricesLazyQuery();
  const [getCustomerById, { loading: getCustomerByIdLoading }] = useGetCustomerByIdLazyQuery({
    notifyOnNetworkStatusChange: true,
  });

  const isBillToCustomerId = () =>{
    if (customerSearchValue) {
      const variables = { input: customerSearchValue };
     
      getCustomerById({ variables })
        .then(({ data }) => {
          if(data === null ||
            (data?.customer.isBillTo === false && data?.customer.isShipTo === true) || 
            (data?.customer.isBillTo === false && data?.customer.isShipTo === false)){
            setCustomerSearchStatus("errorInvalidBillToCustId");
           return
          }
          specialPricingQueryCall()
        })
        .catch(() => {
          setCustomerSearchStatus("errorInvalidCustId");
        });
    }
  }
  

  const specialPricingQueryCall = (priceLine?: string, page?: number) => {
    
    if (customerSearchValue || productSearchValue) {
      
      const variables = {
        input: {
          customerId: formatSearchValue(customerSearchValue),
          productId: formatSearchValue(productSearchValue),
          priceLine: priceLine || "",
        },
        pagingContext: {
          pageSize: defaultValues.tablePageSize,
          orderBy: sortingColumn,
          orderDirection: sortingDirection,
          page: page || 1,
        },
      };

      getSpecialPricing({ variables })
        .then(({ data }) => {
          const hasProducts = data?.paginatedSpecialPrices.results ?? []
          if(hasProducts.length === 0){
            setHasProductsNotReturned(true);
            return
          }
          const productBranchIdstoFilter = data?.paginatedSpecialPrices.results?.map((i)=>i.branch.split('-')[1])
          if(userBranchId && !userBranchId.includes(null)) {
            if(productSearchValue.length >0 && customerSearchValue === "" ){
              const matchedBranchProduct = checkHomeBranchProduct(userBranchId as string[], data?.paginatedSpecialPrices.results)
              setTotalPages(matchedBranchProduct?.length || 0);
              setTotalCount(matchedBranchProduct?.length || 0);
              setSearchResults(matchedBranchProduct || []);
            }else {
              if(isBranchExists(userBranchId as string[], productBranchIdstoFilter)){
                setTotalPages(data?.paginatedSpecialPrices.meta.pageCount || 0);
                setTotalCount(data?.paginatedSpecialPrices.meta.resultCount || 0);
                setSearchResults(data?.paginatedSpecialPrices.results || []);
              }else {
                customerSearchValue && setCustomerSearchStatus("errorBranchMismatch");
                customerSearchValue && productSearchValue && setCustomerSearchStatus("empty");
              }
            }
          }else {
            customerSearchValue && setCustomerSearchStatus("errorUnAuthUser");
          }
        })
        .catch(() => {
          customerSearchValue && setCustomerSearchStatus("errorInvalidCustId");
          customerSearchValue && productSearchValue && setCustomerSearchStatus("empty");
        });
    }
  }

 
  // Get price lines
  const [getPriceLines] = useGetPriceLinesLazyQuery();

  const debouncedGetPriceLines = useDebouncedCallback(() => {
   
    if (customerSearchValue) {
      getPriceLines({
        variables: {
          input: {
            customerId: formatSearchValue(customerSearchValue),
          },
        },
      }).then(({ data }) => {
        const formattedOptions =
          data?.priceLines.map((item) => ({
            value: item,
            display: item,
          })) ?? [];

        setPriceLineOptions([...defaultValues.priceLineOptions, ...formattedOptions]);
      })
    }
  }, 1000);

  const debouncedNonNumericCheck = () => {
   if(!isNaN(Number(customerSearchValue))){
    setIsActiveLoader('customer_id');
    isBillToCustomerId()
  }else {
    setIsActiveLoader('');
    setCustomerSearchStatus("errorNonNumeric");
  }
  }

  // ----- EFFECTS ----- //

  useEffect(() => {
    // Reset price line options
    setPriceLineOptions(defaultValues.priceLineOptions);
      debouncedGetPriceLines();
  }, [customerSearchValue]);

  useEffect(() => {
    // Reset table page
    setCurrentPage(1);
     // Reset total pages
    setTotalPages(0);
     // Reset total count
    setTotalCount(0);
    // Reset search results
    setSearchResults([]);
    // Reset zero product error state
    setHasProductsNotReturned(false);
    // Reset selected price line
    setSelectedPriceLine(defaultValues.priceLine);
    // Reset customer search status
    setCustomerSearchStatus("empty");
    //reset loaders to empty state
    setIsActiveLoader('');
    //reset sorting values
    setSortingColumn('manufacturer');
    setSortingDirection('desc');

  }, [customerSearchValue,productSearchValue]);

  useEffect(() => {
    specialPricingQueryCall(selectedPriceLine.value, currentPage);
  }, [sortingColumn, sortingDirection])

  // ----- ACTIONS ----- //
  const handleCustomerSearchChange: ChangeEventHandler<HTMLInputElement> = (e) => {
    setCustomerSearchValue(e.target.value) 
  };

  const handleEnterKeyOnCustomerId: KeyboardEventHandler<HTMLInputElement> = (e) => { 
    if(e.key === 'Enter') {
      debouncedNonNumericCheck();
    }
  }
  const handleEnterKeyOnProductId: KeyboardEventHandler<HTMLInputElement> = (e) => { 
    if(e.key === 'Enter') {
        setIsActiveLoader('product_id');
        specialPricingQueryCall(); 
    }
  }

  const handleProductSearchChange: ChangeEventHandler<HTMLInputElement> = (e) => {
    setProductSearchValue(e.target.value);
  };

  const handleSelectPriceLine = (selection: DropdownOption) => {
    setSelectedPriceLine(selection);
    specialPricingQueryCall(selection.value);
  };

  const handlePageUpdate = (page: number) => {
    setCurrentPage(page);
    specialPricingQueryCall(selectedPriceLine.value, page);
  };

  const handleCreateClick = () => {
    toggleModal(true, "create");
  };

  const handleExpandClick = () => {
    setAreRowsExpanded((prevState) => !prevState) 
  }

  const handleReviewClick = () => {
    history.push("pricing/review");
  };

  const handleSortingClick = (sortingColumnName: string) => {
    if (sortingColumnName !== sortingColumn) {
      setSortingColumn(sortingColumnName);
    }
    if (sortingColumnName === sortingColumn) {
      setSortingDirection((prevValue) => prevValue === 'desc' ? 'asc' : 'desc');
    }
  }

  // ----- HELPERS ----- //
  const getStagedData = (data: SpecialPrice) => {
    const matchingData = modifiedPrices.find(
      (staged) =>
        staged.branch + staged.customerId + staged.productId ===
        data.branch + data.customerId + data.productId
    );

    return matchingData;
  };

  const getRowData = (isGreaterThan25Percent: boolean, handleAddStagedPrice: () => void) => {
    setHandleAddStagedPrice(() => handleAddStagedPrice)
    setIsGreaterThan25Percent(isGreaterThan25Percent);
  }

  if(userInfo?.groups?.length === 1 && userInfo?.groups?.includes('WMS Admin - Pricing Read Only')){ 
    hidePriceEdit = true;
  }

  const getReviewButtonText = useMemo(() => {
    const total = modifiedPrices.length + createdPrices.length;

    return total > 0 ? `Review Changes (${total})` : "Review Changes";
  }, [modifiedPrices, createdPrices]);

  // - JSX - //
  return (
    <>
      <Banner header='Special Pricing' />
      <VarianceConfirmationModal
        onConfirm={handleAddStagedPrice}
        isGreaterThan25Percent={isGreaterThan25Percent}
      />
      <CreateModal customerId={customerSearchValue} userBranch={userBranchId}/>
      <HelpModal />
      <div className='flex flex-col items-center justify-center p-8'>
        <div className='w-full grid grid-cols-8 gap-8'>
          <div className='col-span-8'>
            {/* Inputs */}
            <div className='grid grid-cols-3 gap-x-8 gap-y-4 mb-4'>
              {/* Company Input */}
              <Input
                label='BILL TO Customer ID'
                placeholder='Search by Customer ID'
                onKeyDown={handleEnterKeyOnCustomerId}
                endIcon={<MdSearch onClick={()=>{
                  setIsActiveLoader('customer_id');
                      debouncedNonNumericCheck();
                    }}
                className="cursor-pointer"  />}
                loading={isActiveLoader === 'customer_id' ? queryLoader || getCustomerByIdLoading  : false}
                onChange={handleCustomerSearchChange}
                status={customerSearchStatus}
                helperText={customerSearchHelpText[customerSearchStatus]}
              />
              {/* Product Input */}
              <Input
                label='Product ID'
                placeholder='Search by Product ID'
                onKeyDown={handleEnterKeyOnProductId}
                endIcon={<MdSearch onClick={()=>{
                  setIsActiveLoader('product_id');
                      specialPricingQueryCall();
                    }}
                className="cursor-pointer" />}
                loading={isActiveLoader === 'product_id' ? queryLoader : false}
                onChange={handleProductSearchChange}
              />
              {/* Price Line Select */}
              <Dropdown
                label='Price Line'
                textSize='large'
                options={priceLineOptions}
                selected={selectedPriceLine}
                setSelected={handleSelectPriceLine}
                disabled={!customerSearchValue || !!productSearchValue}
              />
              {/* Actions */}
              <div className='flex col-span-3 justify-between'>
                {/* Open Special Price Modal */}
                <Button
                  className={hidePriceEdit?'hidden':'bg-primary-1-100 text-white'}
                  onClick={handleCreateClick}
                  title='Create'
                  icon={<MdAddCircle />}
                />
              </div>
              <div className='flex col-span-3 justify-between'>
                {/* Expand or Collapse information rows */}
                <Button
                  className='text-primary-1-100 border-primary-1-100 border'
                  onClick={handleExpandClick}
                  title={areRowsExpanded ? 'Collapse Rows' : 'Expand Rows'}
                  icon={<MdExpand />}
                  iconPosition="left"
                />
                {/* Review Changes */}
                <Button
                  className={hidePriceEdit?'hidden':'bg-reece-500 text-white'}
                  disabled={createdPrices.length < 1 && modifiedPrices.length < 1}
                  onClick={handleReviewClick}
                  title={getReviewButtonText}
                />
              </div>
            </div>
            {/* Table */}
            <div className='overflow-auto bg-white rounded-t shadow-md'>
              <table className='min-w-full max-w-full leading-normal table-auto text-left'>
                {/* Table Header */}
                <thead className='bg-primary-1-100 text-white font-bold text-xs uppercase whitespace-nowrap'>
                  <tr>
                    {pricingColumns.map((column, index) =>
                    (
                      ((hidePriceEdit && index < 7) || (!hidePriceEdit)) &&
                      <th key={index} className='px-2 py-3 first:pl-8 last:pr-8'>
                        <span className='flex'>
                          {searchResults.length > 0 && column.isSortable && (
                            <button className='-mt-[0.05rem] mr-0.5' onClick={() => handleSortingClick(column.sortingColumnName)}>
                              <FaSortUp
                                className={clsx(
                                  column.sortingColumnName === sortingColumn && sortingDirection === "asc"
                                    ? "text-white"
                                    : "text-[#C8C8C8]/25"
                                )}
                              />
                              <FaSortDown className={clsx(
                                "-mt-3",
                                column.sortingColumnName === sortingColumn && sortingDirection === "desc"
                                  ? "text-white"
                                  : "text-[#C8C8C8]/25"
                              )} />
                            </button>
                          )}
                          {column.title}
                          {
                            column.description !== "" &&
                            <div className='group ml-2' >
                              <InformationCircleIcon className='w-4 h-4' />
                              <span className='tooltip-text bg-primary-2-10 text-primary-2-100 p-3 -mt-24 -ml-28 rounded hidden group-hover:flex absolute text-left z-50 whitespace-normal max-w-xs normal-case' >
                                <InformationCircleIconSolid className='w-[10%] text-primary-2-100 mr-4' />
                                <p className="w-[90%]">
                                  {column.title}: <span className="font-normal">{column.description}</span>
                                </p>
                              </span>
                            </div>
                          }
                        </span>
                      </th>
                    ))}
                  </tr>
                </thead>
                <tbody>
                  {/* Price Rows */}
                  {searchResults.map((data) => (
                    <PricingTableRow
                      data={data}
                      getRowData={getRowData}
                      areRowsExpanded={areRowsExpanded}
                      stagedData={getStagedData(data)}
                      key={`${data.branch}-${data.productId}-${data.customerId}`}
                    />
                  ))}
                  {/* Empty Results Row */}
                  {searchResults.length < 1 && (
                    <tr>
                      <td className='py-6 text-sm text-center' colSpan={9}>
                        {hasProductsNotReturned
                      ? 'No products with specials found' 
                      : 'Search by customer or product to find customer specials'
                      }
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
            {/* Pagination */}
            <Pagination
              currentPage={currentPage}
              totalPages={totalPages}
              totalCount={totalCount}
              updatePage={handlePageUpdate}
            />
            <Button
              className={hidePriceEdit?'hidden':'bg-reece-500 text-white block ml-auto mt-4'}
              disabled={createdPrices.length < 1 && modifiedPrices.length < 1}
              onClick={handleReviewClick}
              title={getReviewButtonText}
            />
          </div>
        </div>
      </div>
    </>
  );
};
