package com.reece.platform.mincron.callBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResponseBuilderConfig implements AutoCloseable {
    private final ResultSet rs;
    private int resultNumber;
    private final int startingRowIndex;

    public ResponseBuilderConfig(ResultSet rs, Integer startingRowIndex) {
        this.rs = rs;
        this.resultNumber = startingRowIndex;
        this.startingRowIndex = startingRowIndex;
    }

    public String getResultString() {
        try {
            return rs.getString(resultNumber++).trim();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return "";
        }
    }

    public String getMincronDate() {
        try {
            String century = pad0(rs.getString(resultNumber++),2);
            String year = pad0(rs.getString(resultNumber++),2);
            String month = pad0(rs.getString(resultNumber++),2);
            String day = pad0(rs.getString(resultNumber++),2);
            return century + year + month + day;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return "";
        }
    }

    public Boolean hasMoreData() throws SQLException {
        resultNumber = startingRowIndex;
        return rs != null && rs.next();
    }

    @Override
    public void close() throws Exception {
        if (rs != null) {
            rs.close();
        }
    }

    private String pad0(String text, int length) {
        if (text.length() > length) {
            return text.substring(0,length);
        }
        while (text.length() < length ) {
            text = "0" + text;
        }
        return text;
    }

}
