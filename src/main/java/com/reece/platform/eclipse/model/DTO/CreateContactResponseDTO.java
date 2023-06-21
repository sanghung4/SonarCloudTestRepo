package com.reece.platform.eclipse.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data model for response to create contact request
 */
@Data
@NoArgsConstructor
public class CreateContactResponseDTO {
    private String contactId;
    private String erpUsername;
    private String erpPassword;
}
