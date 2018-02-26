package com.bestarchitect.persistence.exception;

public class BestArchitectPersistenceException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 8967504350117304098L;

    public BestArchitectPersistenceException(String message) {
        super(message);
    }

    public BestArchitectPersistenceException(String message, Throwable t) {
        super(message, t);
    }
}
