package com.reece.platform.eclipse.external.ec;

import lombok.Value;

import java.util.Date;

@Value
public class EclipseBatchDTO {
    String batchNumber;
    String branchNumber;
    String branchName;
    String batchDescription;
    String batchUserId;
    String batchUserName;
    Date batchDate;
}
