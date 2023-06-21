package com.reece.platform.products.model.repository;

import com.reece.platform.products.model.ApprovalFlowStateEnum;
import com.reece.platform.products.model.entity.ApprovalFlowState;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalFlowStateDAO extends JpaRepository<ApprovalFlowState, UUID> {
    ApprovalFlowState findByDisplayName(String displayName);
}
