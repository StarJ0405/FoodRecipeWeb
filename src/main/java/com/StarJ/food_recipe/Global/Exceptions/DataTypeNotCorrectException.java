package com.StarJ.food_recipe.Global.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "data type not correct")
public class DataTypeNotCorrectException extends RuntimeException {
    public DataTypeNotCorrectException(String message) {
        super(message);
    }
}