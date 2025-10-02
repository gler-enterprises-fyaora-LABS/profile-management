package com.fyaora.profilemanagement.profileservice.advice;

import com.fyaora.profilemanagement.profileservice.model.response.FieldErrorResponse;
import com.fyaora.profilemanagement.profileservice.model.response.MessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @Autowired
    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(UserTypeNotFoundException.class)
    public ResponseEntity<MessageDTO> handleUserTypeNotFoundException(UserTypeNotFoundException ex, WebRequest webRequest) {
        MessageDTO messageDTO = getMessageDTO(HttpStatus.NOT_FOUND, ex.getMessage(), webRequest);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageDTO);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<MessageDTO> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest webRequest) {
        MessageDTO messageDTO = getMessageDTO(HttpStatus.NOT_FOUND, ex.getMessage(), webRequest);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageDTO);
    }

    @ExceptionHandler(DuplicateWaitlistRequestException.class)
    public ResponseEntity<MessageDTO>  handleDuplicateWaitlistRequestException(DuplicateWaitlistRequestException ex, WebRequest webRequest) {
        MessageDTO messageDTO = getMessageDTO(HttpStatus.BAD_REQUEST, ex.getMessage(), webRequest);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageDTO);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<MessageDTO> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest webRequest) {
        MessageDTO messageDTO = getMessageDTO(HttpStatus.BAD_REQUEST, ex.getMessage(), webRequest);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageDTO);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<MessageDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest webRequest) {
        String message = null != ex.getMostSpecificCause() ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
        MessageDTO messageDTO = getMessageDTO(HttpStatus.BAD_REQUEST, message, webRequest);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageDTO);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageDTO> handleException(Exception ex, WebRequest webRequest) {
        log.error(ex.getMessage());
        String message = messageSource.getMessage("system.admin.message", null, LocaleContextHolder.getLocale());
        MessageDTO messageDTO = getMessageDTO(HttpStatus.INTERNAL_SERVER_ERROR, message, webRequest);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageDTO);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest webRequest) {
        List<FieldErrorResponse> errorMessages =
                ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(err ->
                                new FieldErrorResponse(err.getField(), messageSource.getMessage(err, LocaleContextHolder.getLocale())))
                        .collect(Collectors.toList());
        MessageDTO messageDTO = getMessageDTO(HttpStatus.BAD_REQUEST, errorMessages, webRequest);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageDTO);
    }

    private MessageDTO getMessageDTO(HttpStatus status, String message, WebRequest webRequest) {
        return new MessageDTO(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                null,
                webRequest.getDescription(false).replace("uri=", ""));
    }

    private MessageDTO getMessageDTO(HttpStatus status, List<FieldErrorResponse> messages, WebRequest webRequest) {
        return new MessageDTO(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(), null,
                messages,
                webRequest.getDescription(false).replace("uri=", ""));
    }
}