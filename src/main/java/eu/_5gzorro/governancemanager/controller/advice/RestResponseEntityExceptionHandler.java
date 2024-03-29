package eu._5gzorro.governancemanager.controller.advice;


import eu._5gzorro.governancemanager.dto.ApiErrorResponse;
import eu._5gzorro.governancemanager.model.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value= { IllegalArgumentException.class, IllegalStateException.class })
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {

        ApiErrorResponse responseBody = new ApiErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());

        return handleExceptionInternal(ex, responseBody,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    protected ApiErrorResponse onConstraintValidationException(ConstraintViolationException e) {
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(), "The request contained invalid parameters");

        for (ConstraintViolation violation : e.getConstraintViolations()) {
            response.getErrors().put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return response;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(), "The request contained invalid parameters");

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            response.getErrors().put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return super.handleExceptionInternal(ex, response, headers, status, request);
    }

    @ExceptionHandler({ MemberNotFoundException.class, GovernanceProposalNotFoundException.class })
    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    @ResponseBody
    protected ApiErrorResponse handleEntityNofFoundException(HttpServletRequest req, Exception ex) {
        return new ApiErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    @ExceptionHandler({
            MemberStatusException.class,
            GovernanceProposalStatusException.class,
            InvalidGovernanceActionException.class
    })
    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    @ResponseBody
    protected ApiErrorResponse handleInvalidRequests(HttpServletRequest req, Exception ex) {
        return new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler({
            DIDCreationException.class
    })
    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    protected ApiErrorResponse handleErroredRequests(HttpServletRequest req, Exception ex) {
        return new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }
}