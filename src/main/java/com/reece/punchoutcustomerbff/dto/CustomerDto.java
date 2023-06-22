package com.reece.punchoutcustomerbff.dto;

import com.reece.punchoutcustomerbff.rest.CustomerRest;
import com.reece.punchoutcustomerbff.service.CustomerService;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a customer from the perspective of an endpoint.
 * @author john.valentino
 * @see CustomerRest
 * @see CustomerService
 * @see CustomerRegionDto
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CustomerDto {

    private UUID id;
    private String customerId;
    private String branchId;
    private String branchName;
    private String erpId;
    private String name;
    private String erpName;
    private Boolean isBillTo;
    private String lastUpdate;
    private String contactName;
    private String contactPhone;
    private List<CustomerRegionDto> regions;
    private List<CatalogDto> catalogs;
}
