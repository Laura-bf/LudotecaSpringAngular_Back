package com.ccsw.tutorial.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "This field cannot be empty")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EmptyMandatoryFieldException extends Exception {

    private static final long serialVersionUID = 7414029436714750147L;

    private String message;
}
