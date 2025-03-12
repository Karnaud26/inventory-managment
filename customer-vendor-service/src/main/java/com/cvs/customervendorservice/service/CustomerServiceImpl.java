package com.cvs.customervendorservice.service;

import com.cvs.customervendorservice.entities.Customer;
import com.cvs.customervendorservice.exceptions.*;
import com.cvs.customervendorservice.feign.AddressClient;
import com.cvs.customervendorservice.feign.AddressClientService;
import com.cvs.customervendorservice.repository.ReadOnlyCustomerRepository;
import com.cvs.customervendorservice.repository.WriteCustomerRepository;
import com.cvs.customervendorservice.web.dto.CustomerRequest;
import com.cvs.customervendorservice.web.dto.CustomerResponse;
import com.cvs.customervendorservice.web.dto.ImageDetails;
import com.cvs.customervendorservice.web.mapper.CustomerMapper;
import com.cvs.customervendorservice.web.model.AddressResponse;
import com.cvs.customervendorservice.web.validator.CustomerValidator;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final WriteCustomerRepository writeCustomerRepository;
    private final ReadOnlyCustomerRepository readOnlyCustomerRepository;
    private final CustomerMapper customerMapper;
    private final AddressClientService addressClientService;
    private final ImageStorageService imageStorageService;

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> getAllCustomers() {
        return readOnlyCustomerRepository.findAll()
                .stream()
                .map(this::getCustomerResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(final String customerId) {
        if(customerId == null){
            logger.error("Customer ID is null");
            return null;
        }

        return readOnlyCustomerRepository.findById(UUID.fromString(customerId))
                .map(this::getCustomerResponse)
                .orElseThrow(
                        () -> new EntityNotFoundException("The customer provided with the identifier customerId=" + customerId + " does not exist",
                                ErrorCodes.CUSTOMER_NOT_FOUND)
                );
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerByCode(final String code) {
        if(code == null){
            logger.error("Code is null");
            return null;
        }
        return readOnlyCustomerRepository.findCustomerByCode(code)
                .map(this::getCustomerResponse)
                .orElseThrow(
                        () -> new EntityNotFoundException("The customer provided with the code=" + code + " does not exist",
                                ErrorCodes.CUSTOMER_NOT_FOUND)
                );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomerResponse save(final CustomerRequest customerRequest, final MultipartFile imageFile) throws IOException{
        List<String> errors = CustomerValidator.validate(customerRequest);
        if(!errors.isEmpty()){
            logger.error("Customer is not valid {}", customerRequest);
            throw new InvalidEntityException("Customer object is not valid ", ErrorCodes.CUSTOMER_NOT_VALID, errors);
        }
        if (customerRequest.getId() == null) {
            if(readOnlyCustomerRepository.existsCustomerByCode(customerRequest.getCode())){
                logger.error("Customer already exist with code = {}", customerRequest.getCode());
                throw new EntityAlreadyExistException("Customer already exist into the database with code="+customerRequest.getCode(), ErrorCodes.CUSTOMER_ALREADY_EXIST);
            }
            if(readOnlyCustomerRepository.existsCustomerByEmail(customerRequest.getEmail())){
                logger.error("Customer already exist with email = {}", customerRequest.getEmail());
                throw new EntityAlreadyExistException("Customer already exist into the database with email="+ customerRequest.getEmail() ,ErrorCodes.CUSTOMER_ALREADY_EXIST);
            }
        }

        if (customerRequest.getAddressRequest() != null){
            try {
                AddressResponse addressResponse = addressClientService.saveAddress(customerRequest.getAddressRequest());
                customerRequest.setAddress_id(addressResponse.getId());
            } catch (AddressServiceUnavailableException e) {
                logger.error("Rolling back customer creation due to address service unavailability: {}", e.getMessage());
                throw e;
            } catch (FeignException.BadRequest ex) {
                logger.error("Address validation failed in Address Service: {}", ex.getMessage());
                throw new InvalidEntityException("Invalid address data provided.", ErrorCodes.valueOf(ex.getMessage()));
            } catch (FeignException.ServiceUnavailable e) {
                logger.error("Address Service is down: {}", e.getMessage());
                throw e;
            }catch (Exception e) {
                logger.error("Unexpected error while saving address: {}", e.getMessage());
                throw new RuntimeException("Failed to create Customer with address. ", e);
            }
        }
        // Handle image upload if provided
        if (imageFile != null && !imageFile.isEmpty()) {
            handleImageUpload(customerRequest, imageFile);
        }
        Customer savedCustomer = writeCustomerRepository.save(customerMapper.toCustomer(customerRequest));
        return this.getCustomerResponse(savedCustomer);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteCustomerById(final String customerId) {
        // Validate input
        if (customerId == null || customerId.trim().isEmpty()) {
            logger.error("Customer ID is null or empty");
            throw new IllegalArgumentException("Customer ID cannot be null or empty");
        }

        UUID customerUUID;
        try {
            customerUUID = UUID.fromString(customerId);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid Customer ID format: {}", customerId);
            throw new IllegalArgumentException("Invalid Customer ID format: " + customerId);
        }

        // Fetch Customer Details
        CustomerResponse customerResponse = readOnlyCustomerRepository.findById(customerUUID)
                .map(customerMapper::toCustomerResponse)
                .orElseThrow(() -> {
                    logger.error("Customer with ID {} does not exist.", customerId);
                    return new EntityNotFoundException("Customer with ID " + customerId + " does not exist.");
                });

        // Delete Address from Address Service
        try {
            if (customerResponse.getAddress_id() != null) {
                addressClientService.deleteAddressById(customerResponse.getAddress_id());
                logger.info("Successfully deleted address with ID: {}", customerResponse.getAddress_id());
            }
        } catch (FeignException.NotFound e) {
            logger.warn("Address with ID {} not found in Address Service. Skipping deletion.", customerResponse.getAddress_id());
        } catch (FeignException.ServiceUnavailable e) {
            logger.error("Address Service is unavailable: {}", e.getMessage());
            throw new RuntimeException("Address Service is currently unavailable. Please try again later.");
        } catch (Exception e) {
            logger.error("Failed to delete associated address: {}", e.getMessage());
            throw new RuntimeException("Failed to delete associated address: " + e.getMessage());
        }

        // Delete Customer
        try {
            writeCustomerRepository.deleteById(customerUUID);
            logger.info("Successfully deleted customer with ID: {}", customerUUID);
        } catch (Exception e) {
            logger.error("Failed to delete customer: {}", e.getMessage());
            throw new RuntimeException("Failed to delete customer: " + e.getMessage());
        }
    }

    private CustomerResponse getCustomerResponse(final Customer customer) {
        CustomerResponse customerResponse = customerMapper.toCustomerResponse(customer);

        try {
            if (customerResponse.getAddress_id() != null) {
                AddressResponse addressResponse = addressClientService.findAddressById(customerResponse.getAddress_id());
                customerResponse.setAddressResponse(addressResponse);
            }
        } catch (FeignException.NotFound e) {
            logger.warn("Address with ID {} not found. Returning customer without address.", customerResponse.getAddress_id());
            customerResponse.setAddressResponse(null); // Handle gracefully by returning a null address
        } catch (FeignException.ServiceUnavailable e) {
            logger.error("Address Service is currently unavailable. Customer ID: {}. Error: {}", customer.getId(), e.getMessage());
            throw new RuntimeException("Address Service is currently unavailable. Please try again later.");
        } catch (Exception e) {
            logger.error("Unexpected error occurred while fetching address for Customer ID: {}. Error: {}", customer.getId(), e.getMessage());
            throw new RuntimeException("Failed to retrieve customer address: " + e.getMessage());
        }
        return customerResponse;
    }

    /**
     * Handle image upload
     */
    private void handleImageUpload(final CustomerRequest customerRequest, MultipartFile imageFile) throws IOException {
        // Delete existing image if present
        if (customerRequest.getImagePath() != null) {
            try {
                imageStorageService.deleteImage(customerRequest.getImagePath());
            } catch (IOException e) {
                // Log error but continue
                System.err.println("Failed to delete existing image: " + e.getMessage());
            }
        }

        // Store new image
        ImageDetails imageDetails = imageStorageService.storeImage(imageFile);
        customerRequest.setImagePath(imageDetails.getPath());
        customerRequest.setImageFilename(imageDetails.getFilename());
        customerRequest.setImageContentType(imageDetails.getContentType());

        // Store small images in DB for quicker access
        if (imageFile.getSize() <= 500 * 1024) { // 500KB limit
            customerRequest.setImageData(imageDetails.getData());
        } else {
            customerRequest.setImageData(null); // Clear any existing image data
        }
    }
}
