package com.reece.platform.notifications.model.DataExtensions;

import lombok.Data;

import java.util.HashMap;

@Data
public class BaseDataExtension {
    private String to;
    // attrs represent the data extension that is set up in SFMC
    // the key represents the data extension attribute
    // the value represents the value you want to store in the DE
    private HashMap<String, String> attr;
}
