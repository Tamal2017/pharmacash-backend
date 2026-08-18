package com.jo.dev.pharmacash.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.security.core.AuthenticationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<CustomError> handleException(Exception ex) {
        CustomError error = new CustomError();
        error.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setPath(ex.getMessage());
        error.setDetail(ex.getLocalizedMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

    @ExceptionHandler({UserException.class})
    public ResponseEntity<CustomError> handleUserException(UserException ex) {
        CustomError error = new CustomError();
        error.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setPath(ex.getMessage());
        error.setDetail(ex.getLocalizedMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
    // Handles 401 Unauthorized exceptions thrown within the controller layer
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<CustomError> handleAuthenticationException(AuthenticationException ex) {
        CustomError error = new CustomError();
        error.setCode(HttpStatus.UNAUTHORIZED.value());
        error.setPath(ex.getMessage());
        error.setDetail(ex.getLocalizedMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(error);
    }

    // Handles 403 Forbidden exceptions (e.g., failed @PreAuthorize checks)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CustomError> handleAccessDeniedException(AccessDeniedException ex) {
        CustomError error = new CustomError();
        error.setCode(HttpStatus.FORBIDDEN.value());
        error.setPath(ex.getMessage());
        error.setDetail("You do not have permission to access this resource.");

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(error);
    }
}
