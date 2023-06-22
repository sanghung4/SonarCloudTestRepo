package com.reece.platform.mincron.utilities;

import com.reece.platform.mincron.service.ContractService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class MincronDataFormattingTest {

    private MincronDataFormatting mincronDataFormatting;
    private SimpleDateFormat simpleDateFormat;

    public static final String MINCRON_CONTRACTS_DATE = "20220916";
    public static final String MAX_DATE = "09/16/2022";
    public static final String MAX_DATE_WITHOUT_LEADING_ZERO = "9/16/2022";

    @BeforeEach
    public void setup() throws Exception {
        mincronDataFormatting = new MincronDataFormatting();
        simpleDateFormat = mock(SimpleDateFormat.class);
    }

    @Test
    void convertDateFormat_convertsFromMincronToMaxDate() {

        String actualDate = mincronDataFormatting.convertDateFormat(
                MINCRON_CONTRACTS_DATE,
                ContractService.MINCRON_CONTRACTS_DATE_FORMAT,
                ContractService.MAX_DATE_FORMAT);

        assertTrue(MAX_DATE.equals(actualDate));
    }

    @Test
    void convertDateFormat_convertsFromMaxToMincronDate() {

        String actualDate = mincronDataFormatting.convertDateFormat(
                MAX_DATE,
                ContractService.MAX_DATE_FORMAT,
                ContractService.MINCRON_CONTRACTS_DATE_FORMAT);

        assertTrue(MINCRON_CONTRACTS_DATE.equals(actualDate));
    }

    @Test
    void convertDateFormat_addsLeadingZeroToMonthWhenSendingDateToMincron() {

        String actualDate = mincronDataFormatting.convertDateFormat(
                MAX_DATE_WITHOUT_LEADING_ZERO,
                ContractService.MAX_DATE_FORMAT,
                ContractService.MINCRON_CONTRACTS_DATE_FORMAT);

        assertTrue(MINCRON_CONTRACTS_DATE.equals(actualDate));
    }
}
