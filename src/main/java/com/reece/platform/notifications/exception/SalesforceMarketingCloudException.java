package com.reece.platform.notifications.exception;

public class SalesforceMarketingCloudException extends Exception{
    private static final String BASE_MESSAGE = "{\"error\":\" An error occurred with your Salesforce Marketing Cloud request: %s\"}";

    public SalesforceMarketingCloudException(String message) {
        super(String.format(BASE_MESSAGE, message));
    }
}
