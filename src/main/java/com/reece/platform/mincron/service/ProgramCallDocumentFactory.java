package com.reece.platform.mincron.service;

import com.ibm.as400.access.AS400ConnectionPool;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProgramCallDocumentFactory {
    private final AS400ConnectionPool connectionPool;

    @Value("${library}")
    @Getter
    private String library;

    @Value("${mincron_host_program_call}")
    private String mincronUrl;

    @Value("${username_program_call}")
    private String username;

    @Value("${password_program_call}")
    private String password;

    public PooledMincronConnection getPooledMincronConnection() {
        return new PooledMincronConnection(connectionPool, mincronUrl, library, username, password);
    }
}
