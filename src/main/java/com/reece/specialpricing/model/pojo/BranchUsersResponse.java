package com.reece.specialpricing.model.pojo;

import com.reece.specialpricing.postgres.UserBranch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchUsersResponse {
    private String branchId;
    private String userName;
}
