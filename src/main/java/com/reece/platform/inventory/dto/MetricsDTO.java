package com.reece.platform.inventory.dto;

import lombok.Value;

@Value
public class MetricsDTO {

        String status;
        Integer total;
        Integer needToBeCounted;
        Integer counted;
        String timeStarted;
        String timeEnded;
        String location;

}

