package com.example.bankapi.ExceptionHandle;

public class InvalidAccountTypeException extends RuntimeException{
    public InvalidAccountTypeException(String message){
        super(message);
    }
}
