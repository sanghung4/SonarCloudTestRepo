import { LocationSummary } from 'api';

export interface BranchPageLocation {
  id: string;
  totalCounted: number;
  totalProducts: number;
  committed: boolean;
}

export type BranchPageProps = {
  renderItem: (item: LocationSummary) => void;
  loading?: boolean;
  onFinishLoad?: () => void;
  testID?: string | undefined;
  branchSummarieListItemHeight? : null | number;
};
