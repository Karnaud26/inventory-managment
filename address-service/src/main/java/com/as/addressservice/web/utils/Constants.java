package com.as.addressservice.web.utils;

public interface Constants extends ApiVersion{
    String APP_ROOT = V1;
    String ADDRESS_ENDPOINT = APP_ROOT ;
    String CREATE_ADDRESS_ENDPOINT = ADDRESS_ENDPOINT + "/create";
    String FIND_ADDRESS_BY_CUSTOMER_ID_ENDPOINT = ADDRESS_ENDPOINT + "/search/{id}";
    String DELETE_ADDRESS_BY_CUSTOMER_ID_ENDPOINT = ADDRESS_ENDPOINT + "/delete/{id}";
}
