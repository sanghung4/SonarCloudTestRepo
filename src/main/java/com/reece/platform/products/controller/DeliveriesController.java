package com.reece.platform.products.controller;

import com.reece.platform.products.model.DTO.DeliveriesDeleteResponseDTO;
import com.reece.platform.products.service.DeliveriesService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/deliveries")
public class DeliveriesController {

    @Autowired
    public DeliveriesService deliveriesService;

    /**
     * Deletes deliveries, currently only used by the account service's delete user
     *
     * @param shipToAccountId to delete from
     */
    @DeleteMapping("{shipToAccountId}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody DeliveriesDeleteResponseDTO deleteDeliveries(@PathVariable UUID shipToAccountId) {
        return deliveriesService.deleteDeliveries(shipToAccountId);
    }
}
