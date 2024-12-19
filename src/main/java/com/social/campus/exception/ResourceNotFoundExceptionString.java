package com.social.campus.exception;

public class ResourceNotFoundExceptionString extends RuntimeException{
    String resourceName;
    String fieldName;
    String fieldValue;
    public ResourceNotFoundExceptionString(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s not found with %s : %s",resourceName,fieldName,fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
