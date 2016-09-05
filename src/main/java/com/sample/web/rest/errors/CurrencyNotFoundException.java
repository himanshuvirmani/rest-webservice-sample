package com.sample.web.rest.errors;

/**
 * Created by neerajsi on 18/09/15.
 */
public class CurrencyNotFoundException extends RuntimeException {

    public CurrencyNotFoundException(String message) {
        super(message);
    }

    public CurrencyNotFoundException() {
        super();
    }

}
