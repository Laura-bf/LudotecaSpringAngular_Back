package com.ccsw.tutorial.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Resource Not Found")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ResourceNotFoundException extends Exception {

    private static final long serialVersionUID = -4108433206504294900L;

    String message;

}
