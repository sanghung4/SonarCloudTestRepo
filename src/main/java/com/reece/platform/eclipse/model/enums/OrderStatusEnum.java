package com.reece.platform.eclipse.model.enums;

public enum OrderStatusEnum {
    SHIP_WHEN_SPECIFIED("S", "SHIP WHEN SPECIFIED"), CALL_WHEN_AVAILABLE("L", "CALL WHEN AVAILABLE"), CALL_WHEN_COMPLETE("C", "CALL WHEN COMPLETE");

    private final String code;
    private final String content;

    OrderStatusEnum(String code, String content) {
        this.code = code;
        this.content = content;
    }

    public String getCode() {
        return code;
    }

    public String getContent() {
        return content;
    }
}
