package com.fyaora.profilemanagement.profileservice.controller;

import com.fyaora.profilemanagement.profileservice.dto.InquiryDTO;
import com.fyaora.profilemanagement.profileservice.dto.MessageDTO;
import com.fyaora.profilemanagement.profileservice.dto.ResponseDTO;
import com.fyaora.profilemanagement.profileservice.service.InquiryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/inquiry")
public class InquiryController {

    private final MessageSource messageSource;
    private final InquiryService inquiryService;

    @Autowired
    public InquiryController(MessageSource messageSource, InquiryService inquiryService) {
        this.messageSource = messageSource;
        this.inquiryService = inquiryService;
    }

    @Operation(
            summary = "Add inquiry",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully added iquiry", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTO.class)))
            }
    )
    @PostMapping
    public ResponseEntity<?> addInquiry(@Valid @RequestBody InquiryDTO inquiryDTO) {
        inquiryService.addInquiry(inquiryDTO);
        String message = messageSource.getMessage("inquiry.add.success", null, LocaleContextHolder.getLocale());
        ResponseDTO response = new ResponseDTO(message);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
