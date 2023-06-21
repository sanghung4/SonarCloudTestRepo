package com.reece.platform.accounts.model.DTO;

import com.reece.platform.accounts.model.enums.RejectionReasonEnum;
import lombok.Data;

@Data
public class RejectUserDTO {
    private RejectionReasonEnum rejectionReasonEnum;
    private String customRejectionReason;
}
