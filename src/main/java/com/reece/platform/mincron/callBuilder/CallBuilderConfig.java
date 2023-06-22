package com.reece.platform.mincron.callBuilder;

import java.sql.*;

import com.ibm.as400.access.AS400JDBCConnectionPool;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CallBuilderConfig implements AutoCloseable {

    private Integer paramNumber = 1; // system is 1-indexed
    private final Connection connection;
    private final AS400JDBCConnectionPool pool;

    @Getter
    private final CallableStatement cs;

    private ResponseBuilderConfig rbc;

    public CallBuilderConfig(String callString, Connection connection, AS400JDBCConnectionPool pool) throws Exception {
        this.connection = connection;
        this.pool = pool;
        cs = connection.prepareCall(callString);
    }

    public void setInputString(String value) {
        try {
            cs.setString(paramNumber++, value.toUpperCase());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void setInputInt(Integer value) {
        try {
            cs.setInt(paramNumber++, value);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void setOutputChar() {
        try {
            cs.registerOutParameter(paramNumber++, java.sql.Types.CHAR);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void setOutputDecimal() {
        try {
            cs.registerOutParameter(paramNumber++, java.sql.Types.DECIMAL);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void setOutputNumeric(int scale) {
        try {
            cs.registerOutParameter(paramNumber++, Types.NUMERIC, scale);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public ResponseBuilderConfig getResultSet(int startingRowIndex) throws SQLException {
        cs.execute();
        rbc = new ResponseBuilderConfig(cs.getResultSet(), startingRowIndex);
        return rbc;
    }

    public Integer getNumberOfRows() throws SQLException {
        cs.getMoreResults();
        ResultSet rs = cs.getResultSet();
        int numberOfRows = 0;
        while (rs != null && rs.next()) {
            numberOfRows += rs.getInt(1);
        }
        rs.close();
        return numberOfRows;
    }

    @Override
    public void close() throws Exception {
        if (rbc != null) {
            rbc.close();
        }
        if (cs != null) {
            cs.close();
        }
        if (connection != null) {
            connection.close();
        }

        logConnectionClosed();
    }

    private void logConnectionClosed() {
        var activeConnections = pool.getActiveConnectionCount();
        var availableConnections = pool.getAvailableConnectionCount();

        log.info("[AS400JDBCConnectionPool] - Connections Report: " + activeConnections + " Active | " + availableConnections + " Available");
    }
}
