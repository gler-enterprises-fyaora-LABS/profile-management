package com.fyaora.profilemanagement.profileservice.advice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fyaora.profilemanagement.profileservice.dto.MessageDTO;
import com.fyaora.profilemanagement.profileservice.dto.MessageListDTO;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserTypeNotFoundException.class)
    public ResponseEntity<?> handleUserTypeNotFoundException(Exception ex, WebRequest webRequest) {
        return getMessageDTO(HttpStatus.NOT_FOUND, ex.getMessage(), webRequest);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(Exception ex, WebRequest webRequest) {
        return getMessageDTO(HttpStatus.BAD_REQUEST, ex.getMessage(), webRequest);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentsException(MethodArgumentNotValidException ex, WebRequest webRequest) {
        return getMessageListDTO(HttpStatus.BAD_REQUEST, ex, webRequest);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<?> handleInvalidFormatException(Exception ex, WebRequest webRequest) {
        return getMessageDTO(HttpStatus.BAD_REQUEST, ex.getMessage(), webRequest);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadable(Exception ex, WebRequest webRequest) {
        return getMessageDTO(HttpStatus.BAD_REQUEST, ex.getMessage(), webRequest);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMethodArgumentTypeMismatch(Exception ex, WebRequest webRequest) {
        return getMessageDTO(HttpStatus.BAD_REQUEST, ex.getMessage(), webRequest);
    }

    @ExceptionHandler(InvalidEnumValueException.class)
    public ResponseEntity<?> handleInvalidEnumValueException(Exception ex, WebRequest webRequest) {
        return getMessageDTO(HttpStatus.BAD_REQUEST, ex.getMessage(), webRequest);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(Exception ex, WebRequest webRequest) {
        return getMessageDTO(HttpStatus.BAD_REQUEST, ex.getMessage(), webRequest);
    }

    private ResponseEntity<MessageDTO> getMessageDTO(HttpStatus status, String message, WebRequest webRequest) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setTimestamp(LocalDateTime.now());
        messageDTO.setStatus(status.value());
        messageDTO.setError(status.getReasonPhrase());
        messageDTO.setMessage(message);
        messageDTO.setPath(webRequest.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(status).body(messageDTO);
    }

    private ResponseEntity<MessageListDTO> getMessageListDTO(HttpStatus status, MethodArgumentNotValidException ex, WebRequest webRequest) {
        MessageListDTO messageListDTO = new MessageListDTO();
        messageListDTO.setTimestamp(LocalDateTime.now());
        messageListDTO.setStatus(status.value());
        messageListDTO.setError(status.getReasonPhrase());
        messageListDTO.setPath(webRequest.getDescription(false).replace("uri=", ""));

        Map<String, String> messages = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(er -> messages.put(er.getField(), er.getDefaultMessage()));
        messageListDTO.setMessages(messages);

        return ResponseEntity.status(status).body(messageListDTO);
    }
}