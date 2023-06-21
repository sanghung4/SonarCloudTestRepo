package com.reece.platform.eclipse.model.DTO;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class GetOrderResponseDTOTest {

    @Test
    public void shouldDefaultToSubmitted() throws IOException {
        GetOrderResponseDTO response = new GetOrderResponseDTO();
        response.setWebStatus(null, null, null);
        assertEquals(response.getWebStatus(), WebStatuses.SUBMITTED);
    }


    @Test
    public void shouldShowInvoicedRegardlessOfShipVia() throws IOException {
        GetOrderResponseDTO response = new GetOrderResponseDTO();
        response.setWebStatus("I", "BLAH BLAH", "B");
        assertEquals(response.getWebStatus(), WebStatuses.INVOICED);
        response.setWebStatus("I", "BLAH BLAH", "N");
        assertEquals(response.getWebStatus(), WebStatuses.INVOICED);
        response.setWebStatus("I", "FS", "N");
        assertEquals(response.getWebStatus(), WebStatuses.INVOICED);
        response.setWebStatus("I", "JPI", "N");
        assertEquals(response.getWebStatus(), WebStatuses.INVOICED);
    }

    @Test
    public void shouldShowDirect() throws IOException {
        GetOrderResponseDTO response = new GetOrderResponseDTO();
        response.setWebStatus("D", "BLAH BLAH", "B");
        assertEquals(response.getWebStatus(), WebStatuses.DIRECT);
    }

    @Test
    public void shouldShowShippedForThirdParty() throws IOException {
        GetOrderResponseDTO response = new GetOrderResponseDTO();
        response.setWebStatus("I", "SEFL", "S");
        assertEquals(response.getWebStatus(), WebStatuses.SHIPPED);
    }

    @Test
    public void shouldShowReadyForPickup() throws IOException {
        GetOrderResponseDTO response = new GetOrderResponseDTO();
        response.setWebStatus("I", "WC", "Q");
        assertEquals(response.getWebStatus(), WebStatuses.READY_FOR_PICKUP);
    }

    @Test
    public void shouldShowPickedUp() throws IOException {
        GetOrderResponseDTO response = new GetOrderResponseDTO();
        response.setWebStatus("I", "WC", "P");
        assertEquals(response.getWebStatus(), WebStatuses.PICKED_UP);
    }

    @Test
    public void shouldShowInProcess() throws IOException {
        GetOrderResponseDTO response = new GetOrderResponseDTO();
        response.setWebStatus("I", "ST", "Q");
        assertEquals(response.getWebStatus(), WebStatuses.IN_PROCESS);
    }

    @Test
    public void shouldShowManifested() throws IOException {
        GetOrderResponseDTO response = new GetOrderResponseDTO();
        response.setWebStatus("I", "ST", "M");
        assertEquals(response.getWebStatus(), WebStatuses.MANIFESTED);

    }

    @Test
    public void shouldShowDelivered() throws IOException {
        GetOrderResponseDTO response = new GetOrderResponseDTO();
        response.setWebStatus("I", "ST", "P");
        assertEquals(response.getWebStatus(), WebStatuses.DELIVERED);
    }
}
