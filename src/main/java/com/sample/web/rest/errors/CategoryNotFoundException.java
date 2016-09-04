package com.sample.web.rest.errors;

/**
 * Created by neerajsi on 18/09/15.
 */
public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(String message) {
        super(message);
    }

    public CategoryNotFoundException() {
        super();
    }

}
