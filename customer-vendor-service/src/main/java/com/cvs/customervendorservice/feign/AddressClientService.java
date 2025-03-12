package com.cvs.customervendorservice.feign;

import com.cvs.customervendorservice.exceptions.AddressServiceUnavailableException;
import com.cvs.customervendorservice.exceptions.ErrorCodes;
import com.cvs.customervendorservice.service.CustomerServiceImpl;
import com.cvs.customervendorservice.web.model.AddressRequest;
import com.cvs.customervendorservice.web.model.AddressResponse;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressClientService  {

    private static final Logger logger = LoggerFactory.getLogger(AddressClientService.class);

    private final AddressClient addressClient;
    private static final String ADDRESS_SERVICE = "addressService";

    @CircuitBreaker(name = ADDRESS_SERVICE, fallbackMethod = "saveAddressFallback")
    @Retry(name = ADDRESS_SERVICE, fallbackMethod = "saveAddressFallback")
    public AddressResponse saveAddress(final AddressRequest addressRequest) {
        logger.info("Attempting to save address: {}", addressRequest);
        try {
            return addressClient.saveAddress(addressRequest).getBody();
        } catch (FeignException.ServiceUnavailable ex) {
            // Re-throw for controller advice to handle, or return fallback
            throw ex;
        } catch (Exception e) {
            logger.error("Error calling address service: {}", e.getMessage(), e);
            throw e;
        }
    }

    @CircuitBreaker(name = ADDRESS_SERVICE, fallbackMethod = "getDefaultAddressResponse")
    @Retry(name = ADDRESS_SERVICE, fallbackMethod = "getDefaultAddressResponse")
    public AddressResponse findAddressById(final String id) {
        return addressClient.findAddressById(id).getBody();
    }

    @CircuitBreaker(name = ADDRESS_SERVICE, fallbackMethod = "getDefaultAddressResponse")
    @Retry(name = ADDRESS_SERVICE, fallbackMethod = "getDefaultAddressResponse")
    public void deleteAddressById(final String id) {
        addressClient.deleteAddressById(id).getBody();
    }

    // Fallback methods
    public AddressResponse saveAddressFallback(AddressRequest addressRequest, Exception e) {
        logger.error("Circuit breaker fallback: Failed to save address. Error: {}", e.getMessage());
        return createFallbackAddress(e);
    }

    // Fallback method must have the same return type and parameters plus an additional Exception parameter
    public AddressResponse createFallbackAddress(Exception e) {
        logger.error("Failed to save address after retries. Error: {}", e.getMessage());
        // Return default or cached data, or throw a custom exception
        throw new AddressServiceUnavailableException("Downstream Address service error " ,  ErrorCodes.SERVICE_UNAVAILABLE ,e.getCause());
    }

    public AddressResponse getDefaultAddressResponse(final String id, Exception e) {
        return AddressResponse.builder()
                .id(id)
                .state("Fallback State")
                .street("Fallback Street")
                .address1("Fallback Address 1")
                .city("Fallback City")
                .country("Fallback Country")
                .build();
    }
}
