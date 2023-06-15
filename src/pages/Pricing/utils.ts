import { KourierProduct, PriceSuggestion, SpecialPrice } from "../../graphql";
import { StagedItem } from "./types";

export const formatSearchValue = (value: string) => {
  const prefix = "MSC-";

  if (value.startsWith(prefix)) {
    return value;
  }

  return value ? prefix + value : value;
};
export const formatInputIDValue = (value: string) => {
  const prefix = "MSC-";

  if (value.startsWith(prefix)) {
    return value.split('-')[1];
  }

  return value;
};

export const isBranchExists = (arrayValues: string[], searchValue : string[] | undefined) => {
  if(arrayValues && searchValue)
  {
    return arrayValues.some(item => searchValue.includes(item))
  }
};

export const checkHomeBranchProduct = (userBranchList: string[], productsList : SpecialPrice[] | undefined) => {
  if(userBranchList && productsList)
  {
    return productsList.filter(i=> userBranchList.indexOf(i.branch.split('-')[1]) !== -1)
  }
};

export const defaultValues = {
  priceLine: { value: "", display: "Filter by Price Line" },
  priceLineOptions: [{ value: "", display: "All" }],
  tablePageSize: 10,
};

export const pricingColumns = [
  {title: "Customer", description: "", isSortable: false, sortingColumnName: ""},
  {title: "Product", description: "", isSortable: true, sortingColumnName: "manufacturer"},
  {title: "Current Special", description: "Current active Customer Special Price", isSortable: true, sortingColumnName: "currentPrice"},
  {title: "Standard Cost", description: "", isSortable: true, sortingColumnName: "standardCost"},
  {title: "Typical Price", description: "Average Price paid by other Customers on the same Rate Card at your Branch, Market, Region, or Division.", isSortable: true, sortingColumnName: "typicalPrice"},
  {title: "Rate Card", description: "Current Rate Card Price assigned to the Customer", isSortable: true, sortingColumnName: "rateCardPrice"},
  {title: "Recommended", description: "Halfway between the Typical Price and the Rate Card Price", isSortable: true, sortingColumnName: "recommendedPrice"},
  {title: "Custom Special Price", description: "You may use this field to enter your own Price of choice", isSortable: false, sortingColumnName: ""},
  {title: "", description: "", isSortable: false, sortingColumnName: ""},
];

export const createdPricingColumns = [
  "Customer",
  "Product",
  "CMP",
  "Updated Price",
  "Price Type",
  "",
];

export const modifiedPricingColumns = [
  "Customer",
  "Product",
  "Current Special",
  "Updated Price",
  "Price Type",
  "",
];

const SEARCH_RESULT_PAGE_SIZE = 10;

export enum EclipseSearchProductType {
  ProductId = 2,
  Keyword = 1,
}

export const createSearchVariables = (
  query: string,
  searchById: boolean,
  currentPage?: number
) => ({
  input: {
    selectedAttributes: [],
    currentPage: currentPage || 1,
    pageSize: SEARCH_RESULT_PAGE_SIZE,
    searchTerm: query.replace('"', '\\"'),
    searchInputType: searchById
      ? EclipseSearchProductType.ProductId
      : EclipseSearchProductType.Keyword,
  },
});

export const customPagination = (items:KourierProduct[] ,page:number,per_page:number)=>{

  const offset = (page - 1) * per_page
  const paginatedItems = items.slice(offset).slice(0,per_page)
  const total_pages = Math.ceil(items.length / per_page)

  return {
    page: page,
    per_page: per_page,
    pre_page: per_page ? page - 1 : null ,
    next_page: (total_pages > page) ? page + 1 : null,
    total: items.length,
    total_pages: total_pages,
    data: paginatedItems
  }
}

export const customerSearchHelpText = {
  success: "Valid Customer ID",
  errorInvalidCustId: "Please enter a valid BILL TO Customer ID",
  errorValidBillToCustId: "Please Enter Valid BILL TO Customer",
  errorBranchMismatch: `Customer's home branch is not the same as user's branch`,
  errorUnAuthUser: "User is not authorized",
  errorNonNumeric: "Invalid Customer ID: Entry allows numeric digits only",
  errorInvalidBillToCustId: "Please enter a valid BILL TO Customer ID",
  empty: undefined,
};

export const createPriceChangeSuggestion = (item: StagedItem): PriceSuggestion => {
  return {
    customerId: item.customerId,
    productId: item.productId,
    branch: item.branch,
    cmpPrice: item.cmpPrice,
    priceCategory: item.priceCategory,
    newPrice: item.newPrice,
    changeWriterDisplayName: item.changeWriterDisplayName,
    changeWriterId: item.changeWriterId,
    territory: item.territory,
  };
};

export const formatToUSD = (value: number) => {
  return value.toLocaleString("en-US", {
    style: "currency",
    currency: "USD",
  });
};
