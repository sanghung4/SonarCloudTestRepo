package com.reece.platform.accounts.model.DTO;

import java.util.List;
import lombok.Data;

@Data
public class SFMCDeleteContactsRequestDTO {

    List<String> values;
    String delete_operation_type;
}
