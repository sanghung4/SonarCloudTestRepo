package com.reece.platform.mincron.utilities;

import com.ibm.as400.access.ConnectionPoolEvent;
import com.ibm.as400.access.ConnectionPoolListener;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class JDBCConnectionPoolListener implements ConnectionPoolListener {

    private String connectionPoolName;

    @Override
    public void connectionCreated(ConnectionPoolEvent connectionPoolEvent) {
        log.info("[{}] - Created Connection", connectionPoolName);
    }

    @Override
    public void connectionExpired(ConnectionPoolEvent connectionPoolEvent) {
        log.warn("[{}] - Connection Expired", connectionPoolName);
    }

    @Override
    public void connectionPoolClosed(ConnectionPoolEvent connectionPoolEvent) {
        log.warn("[{}] - Connection Closed", connectionPoolName);
    }

    @Override
    public void connectionReleased(ConnectionPoolEvent connectionPoolEvent) {
        log.warn("[{}] - Connection Released", connectionPoolName);
    }

    @Override
    public void connectionReturned(ConnectionPoolEvent connectionPoolEvent) {
        log.warn("[{}] - Connection Returned", connectionPoolName);
    }

    @Override
    public void maintenanceThreadRun(ConnectionPoolEvent connectionPoolEvent) {
        log.warn("[{}] - Maintenance Thread Run", connectionPoolName);
    }
}
