package com.reece.punchoutcustomerbff.rest;

import com.reece.punchoutcustomerbff.dto.CustomerDto;
import com.reece.punchoutcustomerbff.dto.ListCustomersDto;
import com.reece.punchoutcustomerbff.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Represents the endpoint for dealing wth customers.
 * @author john.valentino
 */
@RestController
@Tag(name = "customer-rest", description = "the customer controller which handles operations to customers")
@Slf4j
public class CustomerRest {

    @Autowired
    private CustomerService customerService;

    @Operation(summary = "Get a list of all customers")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Customer list successfully retrieved",
                content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ListCustomersDto.class)),
                }
            ),
            @ApiResponse(responseCode = "403", description = "Invalid token", content = @Content),
        }
    )
    @GetMapping("/customer/list")
    public ListCustomersDto listCustomers() {
        return customerService.retrieveAllCustomers();
    }

    @Operation(summary = "View a customer in detail by their id")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Customer successfully retrieved",
                content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerDto.class)),
                }
            ),
            @ApiResponse(responseCode = "403", description = "Invalid token", content = @Content),
        }
    )
    @GetMapping("/customer/detail/{customerId}")
    public CustomerDto retrieveCustomerDetails(@PathVariable(value = "customerId") String customerId) {
        return customerService.retrieveCustomerDetails(customerId);
    }

    @Operation(summary = "Save a customer in detail")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Customer successfully saved",
                content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerDto.class)),
                }
            ),
            @ApiResponse(responseCode = "403", description = "Invalid token", content = @Content),
        }
    )
    @PostMapping("/customer/new")
    public CustomerDto save(@RequestBody CustomerDto customer) {
        return customerService.save(customer);
    }
}
