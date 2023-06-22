package com.reece.platform.mincron.service;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400ConnectionPool;
import com.ibm.as400.access.ConnectionPoolException;
import com.ibm.as400.data.PcmlException;
import com.ibm.as400.data.ProgramCallDocument;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class PooledMincronConnection implements AutoCloseable {
    public static final String PROGRAM_CALL_DOCUMENT = "programCallDocument";

    private final AS400ConnectionPool connectionPool;
    private final String systemName;
    private final String libraryName;
    private final String userId;
    private final String password;

    private AS400 as400;
    private boolean closed = false;

    /**
     * Calls a program on the IBM iSeries. A corresponding pcml file must exist for
     * the program in the <code>src/main/resources</code> directory.
     *
     * @param programName      name of the program to call
     * @param parameters       The names and values of the input parameters as defined in
     *                         <code>programName.pcml</code>
     * @param returnValueNames the names of the output parameters as defined in <code>programName.pcml</code>
     * @return a <code>Map</code> of returnValueNames to values
     */
    @SneakyThrows
    public Map<String, Object> callProgram(String programName, PCMLParameters parameters, String... returnValueNames) {
        if (closed) {
            throw new IllegalStateException("closed == true");
        }

        val pcd = new ProgramCallDocument(getConnection(), programName.toLowerCase());
        pcd.setPath(programName, "/QSYS.LIB/" + libraryName + ".LIB/" + programName + ".PGM");

        for (val parameter : parameters) {
            pcd.setValue(programName + "." + parameter.getName(), parameter.getValue());
        }

        pcd.callProgram(programName);

        final Map<String, Object> returnValues = new HashMap<>();
        for (val name : returnValueNames) {
            val value = getProgramDocumentValue(pcd, name, programName);
            returnValues.put(name, value);
        }

        returnValues.put(PROGRAM_CALL_DOCUMENT, pcd);

        return returnValues;
    }

    /**
     * Retrieves the given param name from the given program name through the PCD
     * given
     *
     * @param programCallDocument call document to pull params from
     * @param paramName           name of param to return
     * @param programName         name of program to get param on
     * @return string value of param
     * @throws PcmlException thrown if there is an error in retrieving the param
     *                       value
     */
    private String getProgramDocumentValue(ProgramCallDocument programCallDocument, String paramName,
                                           String programName) throws PcmlException {
        return programCallDocument.getStringValue(String.format("%s.%s", programName, paramName));
    }

    private AS400 getConnection() throws ConnectionPoolException {
        if (as400 == null) {
            as400 = connectionPool.getConnection(systemName, userId, password);
        }

        logConnectionOpened();

        return as400;
    }

    @Override
    public void close() {
        if (as400 != null) {
            connectionPool.returnConnectionToPool(as400);
            as400 = null;
        }

        logConnectionClosed();

        closed = true;
    }

    private void logConnectionOpened() {
        var activeConnections = connectionPool.getActiveConnectionCount(systemName, userId);
        var availableConnections = connectionPool.getAvailableConnectionCount(systemName, userId);

        log.info("[AS400ConnectionPool] - Connections Report: " + activeConnections + " Active | " + availableConnections + " Available");
    }

    private void logConnectionClosed() {
        var activeConnections = connectionPool.getActiveConnectionCount(systemName, userId);
        var availableConnections = connectionPool.getAvailableConnectionCount(systemName, userId);

        log.info("[AS400ConnectionPool] - Connections Report: " + activeConnections + " Active | " + availableConnections + " Available");
    }
}
