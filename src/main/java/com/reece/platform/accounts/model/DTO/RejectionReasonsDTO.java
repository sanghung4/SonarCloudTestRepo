package com.reece.platform.accounts.model.DTO;

import com.reece.platform.accounts.model.enums.RejectionReasonEnum;
import java.util.List;
import lombok.Data;

@Data
public class RejectionReasonsDTO {

    private List<RejectionReasonEnum> rejectionReasons;
}
