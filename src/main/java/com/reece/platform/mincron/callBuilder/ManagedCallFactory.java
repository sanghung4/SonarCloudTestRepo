package com.reece.platform.mincron.callBuilder;

import com.ibm.as400.access.AS400JDBCConnectionPool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManagedCallFactory {
    @Value("${host_url}")
    protected String hostUrl;

    @Value("${security_token}")
    protected String securityToken;

    @Value("${language}")
    protected String language;

    @Value("${version}")
    protected String version;

    @Value("${application_type}")
    protected String applicationType;

    @Value("${library}")
    protected String library;

    private final AS400JDBCConnectionPool pool;

    public CallBuilderConfig makeManagedCall(String programNumber, Integer numParams, Boolean withDefaultParams) throws Exception {
        Connection connection = getConnection();
        try {
            CallBuilderConfig cbc = new CallBuilderConfig(makeCallString(numParams, programNumber), connection, pool);
            if (withDefaultParams) {
                setDefaultParams(cbc);
            }

            return cbc;
        } catch (Exception e) {
            connection.close();
            logConnectionClosed();
            throw e;
        }
    }

    private void setDefaultParams(CallBuilderConfig cbc) {
        cbc.setInputString(hostUrl);
        cbc.setInputString(version);
        cbc.setInputString(securityToken);
        cbc.setInputString(language);
        cbc.setInputString(applicationType);
    }

    private String makeCallString(Integer numParams, String programNumber) {
        String[] qArray = new String[numParams];
        Arrays.fill(qArray, "?");
        String paramString = String.join(",", qArray);
        return String.format("call %s.%s(%s)", library, programNumber, paramString);
    }

    private synchronized Connection getConnection() throws Exception {
        Connection conn = pool.getConnection();
        conn.setReadOnly(true);

        logConnectionOpened();

        return conn;
    }

    private void logConnectionOpened() {
        var activeConnections = pool.getActiveConnectionCount();
        var availableConnections = pool.getAvailableConnectionCount();

        log.info("[AS400JDBCConnectionPool] - Connections Report: " + activeConnections + " Active | " + availableConnections + " Available");
    }

    private void logConnectionClosed() {
        var activeConnections = pool.getActiveConnectionCount();
        var availableConnections = pool.getAvailableConnectionCount();

        log.info("[AS400JDBCConnectionPool] - Connections Report: " + activeConnections + " Active | " + availableConnections + " Available");
    }
}
