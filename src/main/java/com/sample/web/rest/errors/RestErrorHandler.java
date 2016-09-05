package com.sample.web.rest.errors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RestErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestErrorHandler.class);

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Category not found")
    public void handleCategoryNotFoundException(CategoryNotFoundException ex) {
        LOGGER.debug(ex.getMessage());
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Product not found")
    public void handleProductNotFoundException(ProductNotFoundException ex) {
        LOGGER.debug(ex.getMessage());
    }

    @ExceptionHandler(CurrencyNotFoundException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Currency not found")
    public void handleCurrencyNotFoundException(CurrencyNotFoundException ex) {
        LOGGER.debug(ex.getMessage());
    }
}


