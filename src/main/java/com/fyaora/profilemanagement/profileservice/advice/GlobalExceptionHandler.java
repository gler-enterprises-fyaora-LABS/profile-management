package com.fyaora.profilemanagement.profileservice.advice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fyaora.profilemanagement.profileservice.dto.MessageDTO;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserTypeNotFoundException.class)
    public ResponseEntity<MessageDTO> handleUserTypeNotFoundException(UserTypeNotFoundException ex, WebRequest webRequest) {
        MessageDTO messageDTO = getMessageDTO(HttpStatus.NOT_FOUND, ex.getMessage(), webRequest);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageDTO);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<MessageDTO> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, WebRequest request) {
        String message = "Bad request";

        // If the root cause is enum conversion failure
        if (ex.getCause() instanceof InvalidFormatException invalidEx &&
                invalidEx.getTargetType() != null &&
                invalidEx.getTargetType().isEnum()) {
            message = "Invalid type. It should be CUSTOMER, SERVICE_PROVIDER, INDIVIDUAL or BUSINESS";
        }

        MessageDTO messageDTO = getMessageDTO(HttpStatus.BAD_REQUEST, message, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageDTO);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<MessageDTO> handleEnumConversionException(
            MethodArgumentTypeMismatchException ex, WebRequest webRequest) {

        if (ex.getRequiredType() != null && ex.getRequiredType().isEnum()) {
            String msg = "Invalid type. It should be CUSTOMER, SERVICE_PROVIDER, INDIVIDUAL or BUSINESS";
            MessageDTO messageDTO = getMessageDTO(HttpStatus.NOT_FOUND, msg, webRequest);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageDTO);
        }

        MessageDTO messageDTO = getMessageDTO(HttpStatus.BAD_REQUEST, "Bad request", webRequest);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageDTO);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<MessageDTO> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest webRequest) {
        MessageDTO messageDTO = getMessageDTO(HttpStatus.BAD_REQUEST, ex.getMessage(), webRequest);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageDTO);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<MessageDTO> handleConstraintViolationException(ConstraintViolationException ex, WebRequest webRequest) {
        String msg = "Invalid type. It should be CUSTOMER, SERVICE_PROVIDER, INDIVIDUAL or BUSINESS";
        MessageDTO messageDTO = getMessageDTO(HttpStatus.BAD_REQUEST, msg, webRequest);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageDTO);
    }

    private MessageDTO getMessageDTO(HttpStatus status, String message, WebRequest webRequest) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setTimestamp(LocalDateTime.now());
        messageDTO.setStatus(status.value());
        messageDTO.setError(status.getReasonPhrase());
        messageDTO.setMessage(message);
        messageDTO.setPath(webRequest.getDescription(false).replace("uri=", ""));
        return messageDTO;
    }
}