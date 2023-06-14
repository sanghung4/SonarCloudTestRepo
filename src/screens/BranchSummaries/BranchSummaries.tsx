import React from 'react';
import { LocationSummary } from 'api';
import { BranchItem } from './BranchItem';
import { BranchPage } from 'components/BranchPage';
import useRenderListener from 'hooks/useRenderListener';

const BranchSummaries = () => {
  useRenderListener();

 const listItemHeight= 110
  return (
    <BranchPage
      renderItem={(item: LocationSummary) => <BranchItem location={item} />}
      branchSummarieListItemHeight={listItemHeight}
    />
  );
};

export default BranchSummaries;
