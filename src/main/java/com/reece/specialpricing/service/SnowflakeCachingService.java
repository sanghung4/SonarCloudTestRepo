package com.reece.specialpricing.service;

public interface SnowflakeCachingService {
    boolean refreshCustomerCache();
    boolean refreshProductCache();
    boolean refreshSpecialPriceCache();
}
