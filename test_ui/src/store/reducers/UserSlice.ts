import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { categorizeText, searchProduct } from "./UserActionCreator";

type Filter = {
  title: string;
  data: any[];
};

interface UserState {
  categorizedResult: any;
  searchResult: any;
  isLoading: boolean;
  userError: string;
  apiFilters: any;
  filters: Filter[];
}

const initialState: UserState = {
  categorizedResult: {},
  searchResult: [],
  isLoading: false,
  userError: "",
  apiFilters: {},
  filters: []
};

export const userSlice = createSlice({
  name: "user",
  initialState,
  reducers: {
    clearApiFilters(state){
      state.apiFilters = {}
      state.filters = []
    },
    setApiFilters(state,action:PayloadAction<any>){
      const data = action.payload
      if (data?.filter in state.apiFilters) {
        const index = state.apiFilters[data?.filter].indexOf(data?.value)
        if (index === -1)
          state.apiFilters[data?.filter].push(data?.value);
        else{
          state.apiFilters[data?.filter].splice(index,1);
          if (state.apiFilters[data?.filter].length === 0)
            delete state.apiFilters[data?.filter]
        }
      } 
      else 
        state.apiFilters[data?.filter] = [data?.value];
    },
    setFilters(state,action:PayloadAction<any>){
    const filters = action.payload
    let res: any[] = [];
    for (const filter in filters) {
      if (filters[filter][0]?.data?.length > 0){
        res.push({
          'title':filter,
          'data':filters[filter][0]?.data.filter((item:any) => Object.keys(item).length > 1)
        })
      }
    }
    state.filters=res
    }
  },
  extraReducers: {
    //Categorization
    [categorizeText.fulfilled.type]: (state, action: PayloadAction<any>) => {
      state.userError = "";
      state.categorizedResult = action.payload;
    },
    [categorizeText.pending.type]: (state) => {
      state.categorizedResult = [];
      state.searchResult = [];
    },
    [categorizeText.rejected.type]: (
      state,
      action: PayloadAction<{ message: string; code: number }>
    ) => {
      state.categorizedResult = [];
    },

    //Searching
    [searchProduct.fulfilled.type]: (state, action: PayloadAction<any>) => {
      state.userError = "";
      state.isLoading = false;
      state.searchResult = action.payload;
    },
    [searchProduct.pending.type]: (state) => {
      state.isLoading = true;
      state.userError = "";
    },
    [searchProduct.rejected.type]: (
      state,
      action: PayloadAction<{ message: string; code: number }>
    ) => {
      state.searchResult = [];
      state.userError = `Internal server error, please try again later!`;
      state.isLoading = false;
    },
  },
});

export const {
  setApiFilters,
  clearApiFilters,
  setFilters
} = userSlice.actions;

export default userSlice.reducer;
