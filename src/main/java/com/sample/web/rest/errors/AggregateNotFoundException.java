package com.sample.web.rest.errors;

/**
 * Created by neerajsi on 18/09/15.
 */
public class AggregateNotFoundException extends RuntimeException {

    public AggregateNotFoundException(String message) {
        super(message);
    }

    public AggregateNotFoundException() {
        super();
    }

}
