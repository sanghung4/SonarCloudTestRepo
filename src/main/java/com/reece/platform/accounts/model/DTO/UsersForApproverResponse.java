package com.reece.platform.accounts.model.DTO;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsersForApproverResponse {

    private List<UsersForApproverDTO> users;
}
