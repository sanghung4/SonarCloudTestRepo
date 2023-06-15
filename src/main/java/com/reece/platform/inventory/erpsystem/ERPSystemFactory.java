package com.reece.platform.inventory.erpsystem;

import com.reece.platform.inventory.model.ERPSystemName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ERPSystemFactory {
    private final MincronERPSystem mincronERPSystem;
    private final EclipseERPSystem eclipseERPSystem;

    public ERPSystem getErpSystem(String branchId) {
        return getErpSystem(ERPSystemName.fromBranchNumber(branchId));
    }

    public ERPSystem getErpSystem(ERPSystemName erpSystemName) {
        switch (erpSystemName) {
            case MINCRON:
                return mincronERPSystem;
            case ECLIPSE:
                return eclipseERPSystem;
            default:
                throw new IllegalStateException("Unknown ERP System");
        }
    }
}
