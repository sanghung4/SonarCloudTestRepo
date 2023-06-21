package com.reece.platform.accounts.model.DTO;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SMFCResponseDTO {

    private Boolean operationInitiated;
    private int operationID;
    private String requestServiceMessageID;
    private List<String> resultMessages;
    private String serviceMessageID;
}
