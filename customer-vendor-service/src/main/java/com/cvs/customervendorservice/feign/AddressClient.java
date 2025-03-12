package com.cvs.customervendorservice.feign;

import com.cvs.customervendorservice.config.FeignClientAdvancedConfig;
import com.cvs.customervendorservice.config.FeignClientConfig;
import com.cvs.customervendorservice.config.RetryConfiguration;
import com.cvs.customervendorservice.web.model.AddressRequest;
import com.cvs.customervendorservice.web.model.AddressResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "address-service",
        url = "${spring.cloud.openfeign.client.config.address-service.url}",
        configuration = {FeignClientConfig.class, RetryConfiguration.class},
        fallback = AddressFallbackFactory.class
)
public interface AddressClient {

    @PostMapping(value = "/create")
    ResponseEntity<AddressResponse> saveAddress(@RequestBody final AddressRequest addressRequest);

    @GetMapping(value = "/search/{id}")
    ResponseEntity<AddressResponse> findAddressById(@PathVariable final String id);

    @DeleteMapping(value = "/delete/{id}")
    ResponseEntity<Void> deleteAddressById(@PathVariable final String id);
}
