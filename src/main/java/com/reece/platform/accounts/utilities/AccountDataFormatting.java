package com.reece.platform.accounts.utilities;

import com.reece.platform.accounts.model.enums.ErpEnum;

/**
 * Utility class for formatting account data
 */
public class AccountDataFormatting {

    public static String formatAccountName(String erpAccountId, String companyName, String address, ErpEnum erpEnum) {
        return erpEnum.equals(ErpEnum.ECLIPSE)
            ? String.format("%s - %s - %s", erpAccountId, companyName, address)
            : String.format("%s - %s", erpAccountId, address);
    }

    public static String formatAccountAddress(String street, String city, String state, String zip) {
        return String.format("%s %s, %s %s", street, city, state, zip);
    }

    public static String formatZipcode(String zipcode) {
        return zipcode.substring(0, 5);
    }
}
