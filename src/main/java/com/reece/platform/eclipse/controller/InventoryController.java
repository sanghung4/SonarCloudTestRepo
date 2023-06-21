package com.reece.platform.eclipse.controller;

import com.reece.platform.eclipse.exceptions.*;
import com.reece.platform.eclipse.external.ec.*;
import com.reece.platform.eclipse.model.DTO.EclipseLoadCountDto;
import com.reece.platform.eclipse.model.DTO.ErrorDTO;
import com.reece.platform.eclipse.model.DTO.UpdateCountDTO;
import com.reece.platform.eclipse.service.EclipseService.FileTransferService;
import com.reece.platform.eclipse.util.TokenUtils;
import io.jaegertracing.internal.utils.Http;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final EclipseCredentialsStore eclipseCredentialsStore;
    private final EclipseConnectService eclipseConnectService;
    private final FileTransferService fileTransferService;

    @GetMapping("/branches/{branchId}/batches/{batchId}")
    public @ResponseBody
    EclipseBatchDTO validateBatch(@RequestHeader(value = "Authorization", required = false) String authHeader, @RequestHeader(value = "X-Eclipse-Credentials", required = false) String eclipseCredentialsHeader, @RequestHeader(value = "X-Session-Id", required = false) String sessionIdHeader, @PathVariable String branchId, @PathVariable String batchId) {
        EclipseCredentials credentials;
        if (authHeader != null) {
            val userId = TokenUtils.extractUserId(authHeader);
            val sessionId = TokenUtils.extractSessionId(authHeader, sessionIdHeader);
            credentials = eclipseCredentialsStore.getCredentials(userId, sessionId);
        } else if (eclipseCredentialsHeader != null) {
            credentials = TokenUtils.extractEclipseCredentials(eclipseCredentialsHeader, sessionIdHeader);
        } else {
            throw new InvalidEclipseCredentialsException();
        }

        return eclipseConnectService.validateBatch(credentials, branchId, batchId);
    }

    @PutMapping("/count/_load")
    public @ResponseBody
    List<EclipseLoadCountDto> getInventoryCounts() throws EclipseLoadCountsException{
        return fileTransferService.downloadLatestEclipseCountsFile();
    }

    @GetMapping("/branches/{branchId}/batches/{batchId}/locations")
    public @ResponseBody
    List<String> getAllLocations(@RequestHeader("Authorization") String authHeader, @RequestHeader(value = "X-Session-Id", required = false) String sessionIdHeader, @PathVariable String branchId, @PathVariable String batchId) {
        val userId = TokenUtils.extractUserId(authHeader);
        val sessionId = TokenUtils.extractSessionId(authHeader, sessionIdHeader);
        val credentials = eclipseCredentialsStore.getCredentials(userId, sessionId);

        return eclipseConnectService.getAllLocations(credentials, branchId, batchId);
    }

    @GetMapping("/branches/{branchId}/batches/{batchId}/locations/_fullLoad")
    public @ResponseBody
    List<EclipseLocationDTO> getAllProducts(@RequestHeader(value = "Authorization", required = false) String authHeader, @RequestHeader(value = "X-Eclipse-Credentials", required = false) String eclipseCredentialsHeader, @RequestHeader(value = "X-Session-Id", required = false) String sessionIdHeader, @PathVariable String branchId, @PathVariable String batchId) {
        EclipseCredentials credentials;
        if (authHeader != null) {
            val userId = TokenUtils.extractUserId(authHeader);
            val sessionId = TokenUtils.extractSessionId(authHeader, sessionIdHeader);
            credentials = eclipseCredentialsStore.getCredentials(userId, sessionId);
        } else if (eclipseCredentialsHeader != null) {
            credentials = TokenUtils.extractEclipseCredentials(eclipseCredentialsHeader, sessionIdHeader);
        } else {
            throw new InvalidEclipseCredentialsException();
        }

        return eclipseConnectService.getAllProducts(credentials, branchId, batchId);
    }

    @GetMapping("/batches/{batchId}/locations/{locationId}")
    public @ResponseBody
    List<EclipseLocationItemDTO> getLocation(@RequestHeader("Authorization") String authHeader, @RequestHeader(value = "X-Session-Id", required = false) String sessionIdHeader, @PathVariable String batchId, @PathVariable String locationId) {
        val userId = TokenUtils.extractUserId(authHeader);
        val sessionId = TokenUtils.extractSessionId(authHeader, sessionIdHeader);
        val credentials = eclipseCredentialsStore.getCredentials(userId, sessionId);

        return eclipseConnectService.getLocationItems(credentials, batchId, locationId);
    }

    @GetMapping("/batches/{batchId}/locations/{locationId}/_next")
    public @ResponseBody
    EclipseNextLocationDTO getNextLocation(@RequestHeader("Authorization") String authHeader, @RequestHeader(value = "X-Session-Id", required = false) String sessionIdHeader, @PathVariable String batchId, @PathVariable String locationId) {
        val userId = TokenUtils.extractUserId(authHeader);
        val sessionId = TokenUtils.extractSessionId(authHeader, sessionIdHeader);
        val credentials = eclipseCredentialsStore.getCredentials(userId, sessionId);

        return eclipseConnectService.getNextLocation(credentials, batchId, locationId);
    }

    @PostMapping("/batches/{batchId}/locations/{locationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCount(@RequestHeader("Authorization") String authHeader, @RequestHeader(value = "X-Session-Id", required = false) String sessionIdHeader, @PathVariable String batchId, @PathVariable String locationId, @RequestBody UpdateCountDTO updateCountDTO) {
        val userId = TokenUtils.extractUserId(authHeader);
        val sessionId = TokenUtils.extractSessionId(authHeader, sessionIdHeader);
        val credentials = eclipseCredentialsStore.getCredentials(userId, sessionId);

        eclipseConnectService.updateCount(credentials, new EclipseUpdateCountDTO(batchId, locationId, updateCountDTO.getProductId(), updateCountDTO.getQuantity()));
    }

    @PostMapping("/batches/{batchId}/locations/{locationId}/_new")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    EclipseLocationItemDTO addToCount(@RequestHeader("Authorization") String authHeader, @RequestHeader(value = "X-Session-Id", required = false) String sessionIdHeader, @PathVariable String batchId, @PathVariable String locationId, @RequestBody UpdateCountDTO updateCountDTO) {
        val userId = TokenUtils.extractUserId(authHeader);
        val sessionId = TokenUtils.extractSessionId(authHeader, sessionIdHeader);
        val credentials = eclipseCredentialsStore.getCredentials(userId, sessionId);

        return eclipseConnectService.addToCount(credentials, new EclipseUpdateCountDTO(batchId, locationId, updateCountDTO.getProductId(), updateCountDTO.getQuantity()));
    }

}
