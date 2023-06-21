package com.reece.platform.notifications.model.DataExtensions;

import com.reece.platform.notifications.model.DTO.EmployeeVerificationDTO;

import java.util.HashMap;

public class EmployeeVerificationDE extends BaseDataExtension {
    public EmployeeVerificationDE(EmployeeVerificationDTO employeeVerificationDTO) {
        super.setTo(employeeVerificationDTO.getEmail());
        HashMap<String, String> attr = new HashMap<>();
        attr.put("FirstName", employeeVerificationDTO.getFirstName());
        attr.put("LastName", employeeVerificationDTO.getLastName());
        attr.put("Brand", employeeVerificationDTO.getBrand());
        attr.put("VerificationLink", employeeVerificationDTO.getInviteLink());

        super.setAttr(attr);
    }
}
