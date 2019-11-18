package com.project.mainapp.Activities;

public class TypeAlreadyExistsException extends RuntimeException {

    public TypeAlreadyExistsException(String error){
        super(error);
    }
}
