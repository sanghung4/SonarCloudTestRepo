package com.reece.platform.inventory.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SessionIdHolder {

    private static final ThreadLocal<String> SESSION_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> ECLIPSE_CREDENTIALS = new ThreadLocal<>();

    public String getSessionId() {
        return SESSION_ID.get();
    }

    public void setSessionId(String sessionId) {
        SESSION_ID.set(sessionId);
    }

    public void setEclipseCredentials(String eclipseCredentials) {
        ECLIPSE_CREDENTIALS.set(eclipseCredentials);
    }
}
