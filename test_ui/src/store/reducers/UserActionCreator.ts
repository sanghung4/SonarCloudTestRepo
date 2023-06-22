import axios from "axios";
import { createAsyncThunk } from "@reduxjs/toolkit";

export const DEFAULT_PAGE_SIZE = process.env.DEFAULT_PAGE_SIZE ? process.env.DEFAULT_PAGE_SIZE : 24;

export const categorizeText = createAsyncThunk(
  "user/categorizeText",
  async (searchData: string, thunkAPI) => {
    try {
      const data = axios
        .get(`${process.env.REACT_APP_API_URL}/categorize_text`, {
          params: {
            text: searchData,
          },
        })
        .then((res) => res.data);

      return data;
    } catch (err: any) {
      console.error(err);

      return thunkAPI.rejectWithValue({
        message: err.message,
        code: err.response.status,
      });
    }
  }
);

export const searchProduct = createAsyncThunk(
  "user/searchProduct",
  async ({ searchData, page, apiFilters }: { searchData: string; page: number,apiFilters?: {} }, thunkAPI) => {
    try {
      const filters = apiFilters && Object.keys(apiFilters).length === 0 ? [] : [apiFilters]
      const request_body = {
        query: searchData,
        search_meta: {
          SOURCE: process.env.REACT_APP_SEARCH_SRC,
          DEST: process.env.REACT_APP_SEARCH_DEST,
        },
        page: {
          size: DEFAULT_PAGE_SIZE,
          current: page,
        },
        filters: {
          all: filters,
          none: [],
        },
      };
      const data = await axios.post(`${process.env.REACT_APP_API_URL}/search`, request_body, {
          headers: {
            "Content-Type": "application/json",
          },
        })
      return data;
    } catch (err: any) {
      console.error(err);

      return thunkAPI.rejectWithValue({
        message: err.message,
        code: err.response.status,
      });
    }
  }
);

