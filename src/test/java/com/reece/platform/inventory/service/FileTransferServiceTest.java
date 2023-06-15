package com.reece.platform.inventory.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.reece.platform.inventory.exception.EclipseLoadCountsException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class FileTransferServiceTest {

    @Mock
    private JSch mockJSch;

    @Mock
    private LoadCountService loadCountService;

    @InjectMocks
    private FileTransferService fileTransferService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        fileTransferService = new FileTransferService(loadCountService, mockJSch, "Testing");
    }

    @Test
    void downloadLatestEclipseCountsFile() throws JSchException, EclipseLoadCountsException {
        var result = fileTransferService.downloadLatestEclipseCountsFile();

        assertEquals(List.of(), result);
        verify(mockJSch, times(1)).addIdentity(anyString());
    }
}
