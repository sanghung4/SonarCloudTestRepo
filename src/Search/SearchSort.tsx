import React, { useState } from 'react';

import {
  MenuItem,
  Select,
  useScreenSize
} from '@dialexa/reece-component-library';
import { SelectChangeEvent } from '@mui/material';

interface Options {
  [key: string]: string;
}

function SearchSort() {
  const { isSmallScreen } = useScreenSize();

  const [sortBy, setSortBy] = useState('bestMatch');

  const options: Options = {
    bestMatch: 'Best Match',
    asc: 'Product: A to Z',
    desc: 'Product: Z to A'
  };

  return (
    <Select
      id="sort"
      value={sortBy}
      onChange={handleChange}
      label={isSmallScreen ? 'Sort By' : null}
      size={isSmallScreen ? 'medium' : 'small'}
      renderValue={(value) => options[value as string]}
      fullWidth={isSmallScreen ? true : undefined}
      sx={{
        mr: isSmallScreen ? 0 : 5,
        minWidth: isSmallScreen ? null : 175
      }}
    >
      {Object.keys(options).map((option) => (
        <MenuItem key={option} value={option}>
          {options[option]}
        </MenuItem>
      ))}
    </Select>
  );

  function handleChange(event: SelectChangeEvent<unknown>) {
    setSortBy(event.target.value as string);
  }
}

export default SearchSort;
