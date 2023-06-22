import {
  identify,
  load,
  page,
  PagePayload,
  trackApproval,
  trackLogin,
  trackRegistration,
  trackSearch,
  trackSearchResult,
  trackSubmitOrder,
  trackUploadList
} from 'utils/analytics';
import { Configuration } from 'utils/configuration';

describe('Utils - analytics', () => {
  it('expect `load` to be called', () => {
    window.analytics = {
      load: jest.fn()
    };
    Configuration.segmentApiKey = 'test key';
    const loadFn = jest.spyOn(window.analytics, 'load');
    load();
    expect(loadFn).toHaveBeenCalled();
    expect(loadFn).toHaveBeenCalledWith(Configuration.segmentApiKey);
  });

  it('expect `page` to be called with null value', () => {
    window.analytics = {
      page: jest.fn()
    };
    const pageFn = jest.spyOn(window.analytics, 'page');
    page(null);
    expect(pageFn).toHaveBeenCalled();
    expect(pageFn).toHaveBeenCalledWith();
  });

  it('expect `page` to be called with mocked value', () => {
    window.analytics = {
      page: jest.fn()
    };
    const pageFn = jest.spyOn(window.analytics, 'page');
    const mockPayload: PagePayload = { account: 'test', job: 'job' };
    page(mockPayload);
    expect(pageFn).toHaveBeenCalled();
    expect(pageFn).toHaveBeenCalledWith({ properties: mockPayload });
  });

  it('expect `identify` to be called with mocked value', () => {
    window.analytics = {
      identify: jest.fn()
    };
    const idFn = jest.spyOn(window.analytics, 'identify');
    const mockValue = 'farmermacjoy@yahoo.com';
    identify(mockValue);
    expect(idFn).toHaveBeenCalled();
    expect(idFn).toHaveBeenCalledWith(mockValue);
  });

  it('expect `trackLogin` to be called with mocked value', () => {
    window.analytics = {
      track: jest.fn()
    };
    const trackFn = jest.spyOn(window.analytics, 'track');
    const mockTrackValues = {
      billTo: '123',
      homeBranch: '456',
      authenticated: true,
      user: '789',
      firstName: 'Farmer',
      lastName: 'MacJoy',
      phoneNumber: '1-800-FM-EIEIO'
    };
    trackLogin(mockTrackValues);
    expect(trackFn).toHaveBeenCalled();
    expect(trackFn).toHaveBeenCalledWith('Log In', mockTrackValues);
  });

  it('expect `trackRegistration` to be called with mocked value', () => {
    window.analytics = {
      track: jest.fn()
    };
    const trackFn = jest.spyOn(window.analytics, 'track');
    const mockTrackValues = {
      billTo: '123',
      domain: 'fortiline',
      homeBranch: '456'
    };
    trackRegistration(mockTrackValues);
    expect(trackFn).toHaveBeenCalled();
    expect(trackFn).toHaveBeenCalledWith('Registration', mockTrackValues);
  });

  it('expect `trackSearchResult` to be called with mocked value', () => {
    window.analytics = {
      track: jest.fn()
    };
    const trackFn = jest.spyOn(window.analytics, 'track');
    const mockTrackValues = {
      authenticated: true,
      user: 'farmermacjoy',
      searchTerm: 'tractor',
      product: null,
      pageNumber: 1,
      pageIndex: 1,
      searchIndex: 1,
      timestamp: 'Oct 30, 2021'
    };
    trackSearchResult(mockTrackValues);
    expect(trackFn).toHaveBeenCalled();
    expect(trackFn).toHaveBeenCalledWith('Search Result', mockTrackValues);
  });

  it('expect `trackSearch` to be called with mocked value', () => {
    window.analytics = {
      track: jest.fn()
    };
    const trackFn = jest.spyOn(window.analytics, 'track');
    const mockTrackValues = {
      authenticated: true,
      user: 'farmermacjoy',
      searchTerm: 'tractor',
      timestamp: 'Oct 30, 2021'
    };
    trackSearch(mockTrackValues);
    expect(trackFn).toHaveBeenCalled();
    expect(trackFn).toHaveBeenCalledWith('Search', mockTrackValues);
  });

  it('expect `trackApproval` to be called with mocked value', () => {
    window.analytics = {
      track: jest.fn()
    };
    const trackFn = jest.spyOn(window.analytics, 'track');
    const mockTrackValues = {
      user: 'farmermacjoy',
      firstName: 'Farmer',
      lastName: 'MacJoy',
      phoneNumber: '1-800-FM-EIEIO'
    };
    trackApproval(mockTrackValues);
    expect(trackFn).toHaveBeenCalled();
    expect(trackFn).toHaveBeenCalledWith('Approval', mockTrackValues);
  });

  it('expect `trackSubmitOrder` to be called with mocked value', () => {
    window.analytics = {
      track: jest.fn()
    };
    const trackFn = jest.spyOn(window.analytics, 'track');
    const mockTrackValues = {
      billTo: '123',
      shipTo: '456',
      orderNumber: '789',
      shippingBranch: 'ABC'
    };
    trackSubmitOrder(mockTrackValues);
    expect(trackFn).toHaveBeenCalled();
    expect(trackFn).toHaveBeenCalledWith('SubmitOrder', mockTrackValues);
  });

  it('expect `track` NOT to be called with invalid window.analytics', () => {
    window.analytics = undefined;
    trackLogin({});
    window.analytics = {
      track: jest.fn()
    };
    const trackFn = jest.spyOn(window.analytics, 'track');
    expect(trackFn).not.toHaveBeenCalled();
  });

  it('expect `trackUploadList` to be called with mocked value', () => {
    window.analytics = {
      track: jest.fn()
    };
    const trackFn = jest.spyOn(window.analytics, 'track');
    const mockTrackValues = {
      billTo: '12345678',
      listName: 'newList',
      user: 'morscoengineer'
    };
    trackUploadList(mockTrackValues);
    expect(trackFn).toHaveBeenCalled();
    expect(trackFn).toHaveBeenCalledWith('Upload List', mockTrackValues);
  });
});
