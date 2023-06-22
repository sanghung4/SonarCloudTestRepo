import { Product, Scalars } from 'generated/graphql';
import { Maybe } from 'graphql/jsutils/Maybe';
import { Configuration } from 'utils/configuration';

declare global {
  interface Window {
    analytics: any;
  }
}

export interface PagePayload {
  account: String;
  job: String;
}

interface TrackLoginType {
  billTo?: string | null;
  homeBranch?: string | null;
  authenticated?: boolean | null;
  user?: string | null;
  firstName?: string | null;
  lastName?: string | null;
  phoneNumber?: string | null;
}

interface TrackUploadListType {
  billTo?: string | null;
  listName?: string | null;
  user?: string | null;
}

interface TrackRegistrationType {
  billTo?: string | null;
  homeBranch?: string | null;
  user?: string | null;
  timestamp?: string | null;
}

interface TrackSearchResultType {
  authenticated: boolean | undefined;
  user: string | null | undefined;
  searchTerm: string | null | undefined;
  product: Product | null | any;
  pageNumber: number | null;
  pageIndex: number | null;
  searchIndex: number | null;
  timestamp: string | null;
}

interface TrackSearchType {
  authenticated?: boolean | undefined;
  searchTerm?: string | null;
  user?: string | null;
  timestamp?: string | null;
}

interface TrackApprovalType {
  user?: string | null;
  firstName?: string | null;
  lastName?: string | null;
  phoneNumber?: string | null;
}

interface TrackSubmitOrderProductType {
  productName?: string | undefined;
  qty?: number | undefined;
  mfr?: string | undefined;
  erp?: Maybe<Scalars['String']>;
}

interface TrackSubmitOrderType {
  billTo?: string | null | undefined;
  shipTo?: string | null | undefined;
  orderNumber?: string | null;
  shippingBranch?: string | undefined;
  products?: Array<TrackSubmitOrderProductType> | null;
  netTotal?: string | undefined;
}

export const load = () => {
  window.analytics.load(Configuration.segmentApiKey);
};

export const page = (payload: Maybe<PagePayload>) => {
  if (payload === null) window.analytics.page();
  else {
    window.analytics.page({
      properties: {
        ...payload
      }
    });
  }
};

export const identify = (email: String) => {
  window.analytics.identify(email);
};

const track = (name: string, properties: any) => {
  if (window.analytics) {
    window.analytics.track(name, properties);
  }
};

export const trackLogin = (properties: TrackLoginType) => {
  track('Log In', properties);
};

export const trackRegistration = (properties: TrackRegistrationType) => {
  track('Registration', properties);
};

export const trackSearchResult = (properties: TrackSearchResultType) => {
  track('Search Result', properties);
};

export const trackSearch = (properties: TrackSearchType) => {
  track('Search', properties);
};

export const trackApproval = (properties: TrackApprovalType) => {
  track('Approval', properties);
};

export const trackSubmitOrder = (properties: TrackSubmitOrderType) => {
  track('SubmitOrder', properties);
};

export const trackUploadList = (properties: TrackUploadListType) => {
  track('Upload List', properties);
};
