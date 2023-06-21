package com.reece.platform.accounts.utilities;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class GetCurrentTimeUTC {
    public static Date getCurrentDateTime(){
        Date currentDateTime = Timestamp.valueOf(ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
        return currentDateTime;
    }

}
