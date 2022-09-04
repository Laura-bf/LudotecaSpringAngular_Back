package com.ccsw.tutorial.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Resource not available - already in use")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NotAvailableForUseException extends Exception {

    private static final long serialVersionUID = -2943337727910581279L;

    private String message;

}
