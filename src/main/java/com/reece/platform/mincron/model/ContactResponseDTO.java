package com.reece.platform.mincron.model;

import lombok.Data;

import java.util.List;

@Data
public class ContactResponseDTO
{
    private String firstName;
    private String lastName;
    private List<String> telephone;
    private List<String> email;
}
