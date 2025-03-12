package com.as.addressservice.web.controller.api;

import com.as.addressservice.web.dto.AddressRequest;
import com.as.addressservice.web.dto.AddressResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.as.addressservice.web.utils.Constants.*;

@Tag(name = "Address Api", description = "the Address API with documentation annotations")
@Api(value = APP_ROOT , protocols = "http, https", produces =  MediaType.APPLICATION_JSON_VALUE, tags = "Address Api")
public interface AddressApi {

    @Operation(summary = "Get address details", description = "Get the address details. The operation returns the details of the address by customer Id.")
    @GetMapping(value = FIND_ADDRESS_BY_CUSTOMER_ID_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The address has been found"),
            @ApiResponse(responseCode = "204", description = "The address was not found in the database")
    })
    ResponseEntity<AddressResponse> findAddressById(@PathVariable final String id);

    @DeleteMapping(value = DELETE_ADDRESS_BY_CUSTOMER_ID_ENDPOINT)
    ResponseEntity<Void> deleteAddressById(@PathVariable final String id);

    @Operation(summary = "Save Address infos", description = "Save the address. The operation returns the current address saved")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created Address"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Server Error")})
    @PostMapping(value = CREATE_ADDRESS_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AddressResponse> saveAddress(@Valid @RequestBody final AddressRequest  addressRequest);
}
