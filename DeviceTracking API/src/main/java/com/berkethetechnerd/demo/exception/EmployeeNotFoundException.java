package com.berkethetechnerd.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Throw this exception when the employee is not found
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Employee not found")
public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(Integer id) {
        super("Employee with id '" + id + "' does not exist.");
    }
}