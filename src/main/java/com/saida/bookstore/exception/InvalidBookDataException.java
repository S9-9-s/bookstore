package com.saida.bookstore.exception;

public class InvalidBookDataException extends RuntimeException {
    public InvalidBookDataException(String message) {
        super(message);
    }
}