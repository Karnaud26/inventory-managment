package com.as.addressservice.web;

import com.as.addressservice.entities.Address;
import com.as.addressservice.repository.ReadOnlyAddressRepository;
import com.as.addressservice.repository.WriteAddressRepository;
import com.as.addressservice.web.dto.AddressRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.as.addressservice.web.utils.Constants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
@Transactional
class AddressRestControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WriteAddressRepository writeAddressRepository;

    @Autowired
    private ReadOnlyAddressRepository readOnlyAddressRepository;

    private Address existingAddress;

    @BeforeEach
    void setUp() {
        existingAddress = Address.builder()
                .street("123 Main St")
                .address1("3456 Ave Norvège")
                .city("Springfield")
                .state("IL")
                .zipCode("62704")
                .country("USA")
                .build();

        existingAddress = writeAddressRepository.saveAndFlush(existingAddress);
    }

    // ---- GET /addresses/{id} ----

    @Test
    void findAddressById_ShouldReturnAddress() throws Exception {
        mockMvc.perform(get(FIND_ADDRESS_BY_CUSTOMER_ID_ENDPOINT , existingAddress.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(existingAddress.getId().toString()))
                .andExpect(jsonPath("$.street").value("123 Main St"))
                .andExpect(jsonPath("$.city").value("Springfield"));
    }

    @Test
    void findAddressById_WithNonExistentId_ShouldReturnNotFound() throws Exception {
        String randomId = UUID.randomUUID().toString();

        mockMvc.perform(get(FIND_ADDRESS_BY_CUSTOMER_ID_ENDPOINT, randomId))
                .andExpect(status().isNotFound());
    }

    // ---- POST /addresses ----

    @Test
    void saveAddress_WithValidRequest_ShouldReturnCreated() throws Exception {
        AddressRequest request = AddressRequest.builder()
                .street("999 New Street")
                .address1("Apt 4B")
                .city("Chicago")
                .state("IL")
                .zipCode("60601")
                .country("USA")
                .build();

        mockMvc.perform(post(CREATE_ADDRESS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.city").value("Chicago"))
                .andExpect(jsonPath("$.address1").value("Apt 4B"));
    }

    @Test
    void saveAddress_MissingAddress1_ShouldReturnBadRequest() throws Exception {
        AddressRequest request = AddressRequest.builder()
                .street("999 New Street")
                .city("Chicago")
                .state("IL")
                .zipCode("60601")
                .country("USA")
                // address1 manquant
                .build();

        mockMvc.perform(post(CREATE_ADDRESS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveAddress_MissingZipCode_ShouldReturnBadRequest() throws Exception {
        AddressRequest request = AddressRequest.builder()
                .street("999 New Street")
                .address1("Apt 4B")
                .city("Chicago")
                .state("IL")
                .country("USA")
                // zipCode manquant
                .build();

        mockMvc.perform(post(CREATE_ADDRESS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveAddress_WithEmptyBody_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post(CREATE_ADDRESS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // ---- DELETE /addresses/{id} ----

    @Test
    void deleteAddressById_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete(DELETE_ADDRESS_BY_CUSTOMER_ID_ENDPOINT, existingAddress.getId()))
                .andExpect(status().isNoContent());

        // vérifie que l'adresse a bien été supprimée en base
        org.assertj.core.api.Assertions.assertThat(
                readOnlyAddressRepository.findById(existingAddress.getId())
        ).isEmpty();
    }

    @Test
    void deleteAddressById_WithNonExistentId_ShouldReturnNotFound() throws Exception {
        String randomId = UUID.randomUUID().toString();

        mockMvc.perform(delete(DELETE_ADDRESS_BY_CUSTOMER_ID_ENDPOINT, randomId))
                .andExpect(status().isNotFound());
    }
}