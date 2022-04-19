package com.example.connectorsecond.exceptions;

public class DropBoxException extends RuntimeException{

    public DropBoxException(String message) {
        super(message);
    }

    public DropBoxException(String message, Exception cause) {
        super(message, cause);
    }

}
