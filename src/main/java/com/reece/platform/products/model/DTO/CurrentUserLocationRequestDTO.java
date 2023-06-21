package com.reece.platform.products.model.DTO;

import lombok.*;

@Data
@AllArgsConstructor
public class CurrentUserLocationRequestDTO {

    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zip;
    private float latitude;
    private float longitude;
}
