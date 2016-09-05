package com.sample.web.rest.errors;

/**
 * Created by neerajsi on 18/09/15.
 */
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException() {
        super();
    }

}
