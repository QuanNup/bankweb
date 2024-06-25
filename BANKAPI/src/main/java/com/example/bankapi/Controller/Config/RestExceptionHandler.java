package com.example.bankapi.Controller.Config;

import com.example.bankapi.Config.GlobalConfig.StaticVar;
import com.example.bankapi.ExceptionHandle.InsufficientBalanceException;
import com.example.bankapi.ExceptionHandle.InvalidAccountTypeException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;


@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler({BadCredentialsException.class, AccessDeniedException.class,
            SignatureException.class, ExpiredJwtException.class})
    public ResponseEntity<ProblemDetail> handleSecurityException(Exception ex){
        ProblemDetail errorDetail;
        HttpStatus status;
        String accessDeniedReason;

        if (ex instanceof BadCredentialsException) {
            status = HttpStatus.UNAUTHORIZED;
            accessDeniedReason = StaticVar.UNAUTHORIZED_EXCEPTION_MESSAGE;
        } else if (ex instanceof AccessDeniedException) {
            status = HttpStatus.FORBIDDEN;
            accessDeniedReason = ex.getMessage();
        } else if (ex instanceof SignatureException) {
            status = HttpStatus.FORBIDDEN;
            accessDeniedReason = StaticVar.SIGNATURE_EXCEPTION_MESSAGE;
        } else if (ex instanceof ExpiredJwtException) {
            status = HttpStatus.FORBIDDEN;
            accessDeniedReason = StaticVar.EXPIRED_JWT_EXCEPTION_MESSAGE;
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            accessDeniedReason = StaticVar.INTERNAL_SERVER_ERROR_MESSAGE;
        }

        errorDetail = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        errorDetail.setProperty(StaticVar.ACCESS_DENIED_PROPERTY_REASON, accessDeniedReason);

        return ResponseEntity.status(status).body(errorDetail);
    }
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<String> handleInsufficientBalanceException(InsufficientBalanceException ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InvalidAccountTypeException.class)
    public ResponseEntity<String> handleInvalidAccountTypeException(InvalidAccountTypeException ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }
}
