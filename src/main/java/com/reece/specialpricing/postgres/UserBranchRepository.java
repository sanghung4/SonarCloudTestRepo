package com.reece.specialpricing.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserBranchRepository extends JpaRepository<UserBranch, UserBranchId> {

    @Transactional
    int deleteByUserId(String userId);
    List<UserBranch> findByBranchId(String branchId);
    UserBranch findByBranchIdAndUserId(String branchId,String userId);

}
