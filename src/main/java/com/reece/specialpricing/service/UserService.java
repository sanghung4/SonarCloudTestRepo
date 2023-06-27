package com.reece.specialpricing.service;

import com.reece.specialpricing.model.pojo.BranchUsersListResponse;
import com.reece.specialpricing.model.pojo.ErrorBlock;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    ErrorBlock addUser(String branchId, String userEmail);

    ErrorBlock updateUserEmail(String oldEmail,String newEmail);

    ErrorBlock removeUserBranch(String branchId, String userEmail);

    ErrorBlock deleteUser(String userEmail);

    List<String> getUserBranchIdList(String userName);

    List<BranchUsersListResponse> getBranchUsersListResponses();
}

