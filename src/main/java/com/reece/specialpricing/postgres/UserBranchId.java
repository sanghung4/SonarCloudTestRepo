package com.reece.specialpricing.postgres;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBranchId implements Serializable {

    private String userId;
    private String branchId;

}
