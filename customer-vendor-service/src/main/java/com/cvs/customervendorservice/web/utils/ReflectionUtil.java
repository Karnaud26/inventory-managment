package com.cvs.customervendorservice.web.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Component
public class ReflectionUtil {

    private static final Logger log = LoggerFactory.getLogger(ReflectionUtil.class);

    /**
     * Utility method to debug available methods in a class
     * You can call this from your application startup to verify method signatures
     */
    public static void logMethodSignatures(Class<?> clazz) {
        log.info("==== Method signatures for {} ====", clazz.getName());
        for (Method method : clazz.getDeclaredMethods()) {
            log.info("Method: {}", method.getName());
            log.info("Return type: {}", method.getReturnType().getName());
            log.info("Parameter types: {}", Arrays.toString(
                    Arrays.stream(method.getParameterTypes())
                            .map(Class::getName)
                            .toArray()));
            log.info("----");
        }
    }
}
