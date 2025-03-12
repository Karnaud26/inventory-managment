package com.cvs.customervendorservice.web.utils;

public interface Constants extends ApiVersion{
    String APP_ROOT = V1;

    // CUSTOMER API ENDPOINT
    String CUSTOMER_ENDPOINT = APP_ROOT ;
    String FIND_ALL_CUSTOMERS = CUSTOMER_ENDPOINT + "customers/all";
    String CREATE_CUSTOMER_ENDPOINT = CUSTOMER_ENDPOINT + "customers/create";
    String FIND_CUSTOMER_BY_ID_ENDPOINT = CUSTOMER_ENDPOINT + "customers/search/{customerId}";
    String DELETE_CUSTOMER_BY_ID_ENDPOINT = CUSTOMER_ENDPOINT + "customers/delete/{customerId}";

     // VENDOR API ENDPOINT
    String VENDOR_ENDPOINT = APP_ROOT ;
    String FIND_ALL_VENDORS = VENDOR_ENDPOINT + "vendors/all";
    String CREATE_VENDOR_ENDPOINT = VENDOR_ENDPOINT + "vendors/create";
    String FIND_VENDOR_BY_ID_ENDPOINT = VENDOR_ENDPOINT + "vendors/search/{customerId}";
    String DELETE_VENDOR_BY_ID_ENDPOINT = VENDOR_ENDPOINT + "vendors/delete/{customerId}";
}
