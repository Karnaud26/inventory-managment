package com.as.addressservice.service;

import com.as.addressservice.entities.Address;
import com.as.addressservice.logger.AuditLogger;
import com.as.addressservice.repository.WriteAddressRepository;
import com.as.addressservice.web.dto.AddressRequest;
import com.as.addressservice.web.dto.AddressResponse;
import com.as.addressservice.exceptions.EntityNotFoundException;
import com.as.addressservice.exceptions.ErrorCodes;
import com.as.addressservice.exceptions.InvalidEntityException;
import com.as.addressservice.web.mappers.AddressMapper;
import com.as.addressservice.repository.ReadOnlyAddressRepository;
import com.as.addressservice.web.validator.AddressValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private static final Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);

    private static final AuditLogger  auditLogger = new AuditLogger();

    private final ReadOnlyAddressRepository readOnlyAddressRepository;
    private final WriteAddressRepository writeAddressRepository;
    private final AddressMapper addressMapper;

    @Override
    @Transactional(readOnly = true)
    public AddressResponse findAddressById(final String id) {
        if(id == null){
            logger.error("ID is null");
            return null;
        }
        return readOnlyAddressRepository.findById(UUID.fromString(id))
                .map(addressMapper::toAddressResponse)
                .orElseThrow(
                        () -> new EntityNotFoundException("The address provided with the identifier Id=" + id + " does not exist",
                                ErrorCodes.ADDRESS_NOT_FOUND)
                );
    }

    @Override
    public void deleteAddress(final String id) {
        if(id == null){
            logger.error("ID is null");
            return;
        }
        AddressResponse addressResponse = readOnlyAddressRepository.findById(UUID.fromString(id))
                .stream().map(addressMapper::toAddressResponse)
                .findFirst()
                .orElseThrow(
                        () -> new EntityNotFoundException("The address provided with the identifier Id=" + id + " does not exist",
                ErrorCodes.ADDRESS_NOT_FOUND)
                );
        if (addressResponse != null){
            writeAddressRepository.deleteById(UUID.fromString(id));
        }
    }

    @Override
    public AddressResponse saveAddress(final AddressRequest addressRequest) {
        validateAddressRequest(addressRequest);

        Address address = (addressRequest.getId() != null)
                ? updateExistingAddress(addressRequest)
                : createNewAddress(addressRequest);

        Address savedAddress = writeAddressRepository.saveAndFlush(address);
        auditLogger.log("Address saved", savedAddress.getId(), savedAddress.getCreateBy(), savedAddress.getModifiedBy()); // optional custom logger
        return addressMapper.toAddressResponse(savedAddress);
    }

    private  void validateAddressRequest(final AddressRequest addressRequest) {
        List<String> errorMessages = AddressValidator.validAddress(addressRequest);
        if (!errorMessages.isEmpty()) {
            logger.error("Address is not valid: {}", addressRequest);
            throw new InvalidEntityException(
                    "The Address is not valid",
                    ErrorCodes.ADDRESS_NOT_VALID,
                    errorMessages
            );
        }
    }

    private Address updateExistingAddress(AddressRequest addressRequest) {
        Address existingAddress = readOnlyAddressRepository.findById(UUID.fromString(addressRequest.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Address not found with ID: " + addressRequest.getId(), ErrorCodes.ADDRESS_NOT_FOUND));

        addressMapper.updateAddressFromAddressRequest(addressRequest, existingAddress);
        return existingAddress;
    }

    private Address createNewAddress(AddressRequest addressRequest) {
        return addressMapper.toAddress(addressRequest);
    }
}
