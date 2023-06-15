package com.reece.platform.inventory.dto;

import lombok.Value;

import java.util.List;

@Value
public class UserInfoDTO {
    private List<String> groups;
}
