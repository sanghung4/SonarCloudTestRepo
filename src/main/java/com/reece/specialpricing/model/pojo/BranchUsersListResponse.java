package com.reece.specialpricing.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchUsersListResponse {
    private String branchId;
    private List<String> assignedUsers;
}
